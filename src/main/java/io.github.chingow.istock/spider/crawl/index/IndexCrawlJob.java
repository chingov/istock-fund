package io.github.chingow.istock.spider.crawl.index;

import io.github.chingow.istock.common.util.spring.SpringContextUtil;
import io.github.chingow.istock.spider.thread.MonitorScheduledThreadPool;
import io.github.chingow.istock.spider.thread.MyThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * IndexCrawlJob
 *
 * @author chingow
 * @date 2019-10-25 16:32
 */
@Slf4j
public class IndexCrawlJob implements Runnable {

    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledExecutorService scheduledExecutorService2;
//    ConcurrentLinkedQueue<Stock> stockQueue = new ConcurrentLinkedQueue<>();

    //scheduleAtFixedRate 也就是规定频率为1h，那么好，A任务开始执行，过来一个小时后，不管A是否执行完，都开启B任务
    //scheduleWithFixedDealy却是需要在A任务执行完后，在经过1小时后再去执行B任务；
    public IndexCrawlJob() {
        scheduledExecutorService = new MonitorScheduledThreadPool(12, new MyThreadFactory("crawlerJob-index"));
        scheduledExecutorService2 = new MonitorScheduledThreadPool(5, new MyThreadFactory("outJob-index"));
    }

    public void stopTask() {
        scheduledExecutorService.shutdown();
        scheduledExecutorService2.shutdown();
        Thread.currentThread().interrupt();
    }

    private void top() {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) scheduledExecutorService;
        log.info("总线程数{},活动线程数{},执行完成线程数{},排队线程数{},数据队列数{}",
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getQueue().size()
                //               ,stockQueue.size()
        );
    }

    @Override
    public void run() {

    }
}
