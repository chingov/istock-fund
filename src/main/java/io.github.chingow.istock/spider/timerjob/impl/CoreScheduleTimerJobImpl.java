package io.github.chingow.istock.spider.timerjob.impl;

import io.github.chingow.istock.spider.SimpleTimerJobContainer;
import io.github.chingow.istock.spider.schedule.ScheduleThread;
import io.github.chingow.istock.spider.timerjob.AbstractTimerJob;
import io.github.chingow.istock.spider.timerjob.ITimerJob;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 核心调度任务
 *
 * @author chingow
 * @create 2019-10-25 15:21
 **/
@Slf4j
public class CoreScheduleTimerJobImpl extends AbstractTimerJob {

    private SimpleTimerJobContainer scheduleJob;

    public CoreScheduleTimerJobImpl() {
        name = "核心调度任务负责管理所有任务调度";
    }

    @Override
    public void execute(ITimerJob.COMMAND command) throws Exception {
        switch (command) {
            case START:
                if (null == scheduleJob) {
                    log.info("开启核心调度任务!");
                    ScheduleThread scheduleThread = new ScheduleThread();
                    // 核心调度任务，每一秒执行一次
                    scheduleJob = new SimpleTimerJobContainer(scheduleThread, 0, 1, TimeUnit.MINUTES, "coreSchedule", 1);
                    Thread thread = new Thread(scheduleJob);
                    // 设置线程为守护线程
                    thread.setDaemon(true);
                    thread.start();
                    status = STATUS.RUN;
                }
                break;
            case STOP:
                if (null != scheduleJob) {
                    log.info("关闭核心调度任务!");
                    scheduleJob.shutDown();
                    scheduleJob = null;
                    status = STATUS.STOP;
                }
                break;
        }
    }
}
