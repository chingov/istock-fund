package io.github.chingow.istock.spider.crawl.index;

import io.github.chingow.istock.common.util.spring.SpringContextUtil;
import io.github.chingow.istock.spider.po.Fund;
import io.github.chingow.istock.spider.thread.MonitorScheduledThreadPool;
import io.github.chingow.istock.spider.thread.MyThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    ConcurrentLinkedQueue<Fund> fundQueue = new ConcurrentLinkedQueue<>();

    // scheduleAtFixedRate 也就是规定频率为1h，A任务开始执行一个小时后，不管A是否执行完，都开启B任务
    // scheduleWithFixedDelay 却是需要在A任务执行完后，在经过1小时后再去执行B任务；
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
                , fundQueue.size()
        );
    }

    @Override
    public void run() {

        MongoTemplate template = SpringContextUtil.getBean(MongoTemplate.class);

        List<String> list = new ArrayList<>();
        try {
            SinaIndexSpider sinaIndexSpider = new SinaIndexSpider(list.toArray(new String[]{}), fundQueue);
            scheduledExecutorService.scheduleWithFixedDelay(sinaIndexSpider, 0, 20, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error("{}", ex);
            ex.printStackTrace();
        }

        scheduledExecutorService2.scheduleWithFixedDelay(
                new Thread(() -> {
                    top();
                    int index = fundQueue.size() > 1000 ? 1000 : fundQueue.size();
                    if (index == 0) {
                        return;
                    }
                    //这里的BulkMode.UNORDERED是个枚举，，，collectionName是mongo的集合名
                    BulkOperations ops = template.bulkOps(BulkOperations.BulkMode.UNORDERED, "stock");
                    for (int i = 0; i < index; i++) {
                        Fund fund = fundQueue.poll();
                        ops.upsert(new Query(Criteria.where("_id").is(fund.getCode())), new Update()
                                .set("_id", fund.getCode())
                                .set("type", fund.getType())
                                .set("name", fund.getName())
                                .set("price", fund.getPrice())
                                .set("yesterdayPrice", fund.getYesterdayPrice())
                                .set("fluctuate", fund.getFluctuate())
                                .set("todayMax", fund.getTodayMax())
                                .set("todayMin", fund.getTodayMin())
                                .set("priceDate", fund.getPriceDate()));
                    }

                    //循环插完以后批量执行提交一下ok！
                    ops.execute();
                }), 0, 2, TimeUnit.SECONDS);
    }
}
