package io.github.chingow.istock.spider.schedule;

import io.github.chingow.istock.common.util.spring.SpringContextUtil;
import io.github.chingow.istock.spider.timerjob.ITimerJob;
import io.github.chingow.istock.spider.timerjob.ITimerJobFactory;
import io.github.chingow.istock.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 调度作业线程
 *
 * @author chingow
 * @date 2019-10-25 14:52
 */
@Slf4j
public class ScheduleThread implements Runnable {

    void jobProcess() throws Exception {

        TradingDateUtil tradingDateUtil = SpringContextUtil.getBean(TradingDateUtil.class);
        boolean tradeDay = tradingDateUtil.isTradingDay();
        if (!tradeDay) {
            log.info("not trade day . don't work ~ ");
            return;
        }
        LocalDateTime dateTime = LocalDateTime.now();

        // 交易时间内，指数更新程序启动
        if (tradingDateUtil.isTradingTimeNow()) {
            ITimerJobFactory.getJob(ITimerJobFactory.TimerJob.INDEX).execute(ITimerJob.COMMAND.START);
        } else {
            // 非交易时间内，指数更新程序关闭
            ITimerJobFactory.getJob(ITimerJobFactory.TimerJob.INDEX).execute(ITimerJob.COMMAND.STOP);
            // 下午3点闭市后，爬取其它信息
            if (dateTime.getHour() >= 15) {
                log.info("not trade time and after 15 hours . crawl other info ~ ");
            }
        }

        switch (dateTime.getHour()) {
            case 0:
                //晚上12点
                if(dateTime.getMinute()==02){
                    // 清理
                    ITimerJobFactory.getJob(ITimerJobFactory.TimerJob.CLEAR).execute(null);
                    }
                break;
            case 1:
                break;
            case 9:
                //早上9点
                break;
            case 11:
                //上午11点
                break;
            case 13:
                //下午1点
                break;
            case 15:
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        try {
            jobProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
