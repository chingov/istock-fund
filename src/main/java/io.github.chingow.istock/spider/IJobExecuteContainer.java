package io.github.chingow.istock.spider;

/**
 * 任务执行容器
 * @author chingow
 * @create 2019-10-25 15:09
 **/
public interface IJobExecuteContainer {

    /**
     * 关闭线程（非强制模式）
     */
    void shutDown();

    /**
     * 关闭线程强制模式
     */
    void forceShutDown();
}
