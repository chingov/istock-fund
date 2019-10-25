package io.github.chingow.istock.spider.timerjob;

/**
 * @author chingow
 * @create 2019-10-25 15:43
 **/
public abstract class AbstractTimerJob implements ITimerJob {

    protected String name;
    protected STATUS status = STATUS.STOP;
}
