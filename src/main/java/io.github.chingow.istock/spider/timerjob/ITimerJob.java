package io.github.chingow.istock.spider.timerjob;

/**
 * 1天只需要执行一次的定时任务
 * timer job interface
 *
 * @author chingow
 * @create 2019-10-25 15:25
 **/
public interface ITimerJob {


    enum COMMAND {
        START, STOP
    }

    enum STATUS {
        STOP, RUN, UNKNOW, ERROR
    }

    /**
     * 执行
     *
     * @param command 命令
     * @throws Exception
     */
    void execute(COMMAND command) throws Exception;

}
