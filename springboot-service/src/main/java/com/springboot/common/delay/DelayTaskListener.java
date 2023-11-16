package com.springboot.common.delay;

/**
 * 延时任务监听接口
 */
public interface DelayTaskListener<T extends BaseTask> {

    /**
     * 延时调用方法
     *
     * @param t 任务元素
     */
    void invoke(T t);

}
