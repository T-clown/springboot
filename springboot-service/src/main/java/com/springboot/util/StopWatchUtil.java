package com.springboot.util;

import org.springframework.util.StopWatch;

public class StopWatchUtil {
    private static ThreadLocal<StopWatch> threadLocal = ThreadLocal.withInitial(() -> new StopWatch("stopwatch"));

    public static StopWatch getCurrentStopWatch() {
        StopWatch stopWatch = threadLocal.get();
        if (stopWatch == null) {
            stopWatch = new StopWatch("stopwatch");
            threadLocal.set(stopWatch);
        }
        return stopWatch;
    }

    public static void start(String taskname) {
        if (!getCurrentStopWatch().isRunning()) {
            getCurrentStopWatch().start(taskname);
        } else {
            getCurrentStopWatch().stop();
            getCurrentStopWatch().start(taskname);
        }
    }

    public static void stop() {
        if (getCurrentStopWatch().isRunning()) {
            getCurrentStopWatch().stop();
        }
    }

    /**
     * 输出任务执行情况，当前watch实例生命周期结束
     *
     * @return
     */
    public static String prettyPrint() {
        if (getCurrentStopWatch().isRunning()) {
            stop();
        }
        String outputString = getCurrentStopWatch().prettyPrint();
        removeStopWatch();
        return outputString;
    }

    public static void removeStopWatch() {
        threadLocal.remove();
    }
}
