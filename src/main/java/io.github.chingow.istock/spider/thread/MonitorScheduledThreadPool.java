package io.github.chingow.istock.spider.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * 线程池监控
 *
 * @author chingow
 * @create 2019-10-24 22:26
 **/
@Slf4j
public class MonitorScheduledThreadPool extends ScheduledThreadPoolExecutor {

    public MonitorScheduledThreadPool(int corePoolSize) {
        super(corePoolSize);
    }

    public MonitorScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory) {
        //指定核心线程数，创建线程工厂类
        super(corePoolSize, threadFactory);
    }

    public MonitorScheduledThreadPool(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public MonitorScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
//        log.info("beforeExecute...");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        log.info("{}", String.format("线程工厂:%s,总线程数:%s,活动线程数:%s,执行完成线程数:%s,排队线程数:%s",
                getThreadFactory() instanceof MyThreadFactory ? ((MyThreadFactory) getThreadFactory()).name
                        : getThreadFactory().hashCode(),
                getTaskCount(),
                getActiveCount(),
                getCompletedTaskCount(),
                getQueue().size()
        ));
    }

    @Override
    protected void terminated() {
        super.terminated();
        log.error("terminated...");
    }
}
