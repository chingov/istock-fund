package io.github.chingow.istock.spider.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工厂
 *
 * @author chingow
 * @create 2019-10-24 22:26
 **/
public class MyThreadFactory implements ThreadFactory {

    private AtomicInteger counter = new AtomicInteger(0);
    private String name;

    public MyThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, name + "-t-" + counter);
        counter.incrementAndGet();
        return t;
    }
}
