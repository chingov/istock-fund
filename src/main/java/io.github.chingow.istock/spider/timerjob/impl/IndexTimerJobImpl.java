package io.github.chingow.istock.spider.timerjob.impl;

import io.github.chingow.istock.spider.crawl.index.IndexCrawlJob;
import io.github.chingow.istock.spider.timerjob.AbstractTimerJob;
import io.github.chingow.istock.spider.timerjob.ITimerJob;
import lombok.extern.slf4j.Slf4j;

/**
 * 指数更新任务
 *
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class IndexTimerJobImpl extends AbstractTimerJob {

    private IndexCrawlJob indexCrawlJob;

    public IndexTimerJobImpl() {
        name = "开盘价格涨幅抓取任务";
    }

    @Override
    public void execute(ITimerJob.COMMAND command) throws Exception {
        switch (command) {
            case START:
                if (null == indexCrawlJob) {
                    log.info("开市时间开启更新线程!");
                    indexCrawlJob = new IndexCrawlJob();
                    Thread thread = new Thread(indexCrawlJob);
                    thread.start();
                    status = ITimerJob.STATUS.RUN;
                }
                break;
            case STOP:
                if (null != indexCrawlJob) {
                    log.info("休市时间关闭更新线程!");
                    indexCrawlJob.stopTask();
                    indexCrawlJob = null;
                    status = STATUS.STOP;
                }
                break;
        }
    }
}
