package com.springboot.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;

public class StopWatchUtil {

    //private static ThreadLocal<StopWatch> threadLocal = ThreadLocal.withInitial(() -> new StopWatch("StopW111atch"));
    private static final ThreadLocal<StopWatch> STOPWATCH = new ThreadLocal<>();

    public static StopWatch getCurrentStopWatch() {
        StopWatch stopWatch = STOPWATCH.get();
        if (stopWatch == null) {
            stopWatch = new StopWatch("StopWatch");
            STOPWATCH.set(stopWatch);
        }
        return stopWatch;
    }
    public static StopWatch getCurrentStopWatch(String id) {
        StopWatch stopWatch = STOPWATCH.get();
        if (stopWatch == null) {
            stopWatch = new StopWatch(id);
            STOPWATCH.set(stopWatch);
        }
        return stopWatch;
    }

    public static void start(String taskname) {
        if (getCurrentStopWatch().isRunning()) {
            getCurrentStopWatch().stop();
        }
        getCurrentStopWatch().start(taskname);
    }

    public static void start(String id,String taskname) {
        if (getCurrentStopWatch(id).isRunning()) {
            getCurrentStopWatch(id).stop();
        }
        getCurrentStopWatch(id).start(taskname);
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
    public static String prettyPrint2() {
        if (getCurrentStopWatch().isRunning()) {
            stop();
        }
        String outputString = getCurrentStopWatch().prettyPrint();
        removeStopWatch();
        return outputString;
    }
    public static String shortSummary(StopWatch stopWatch) {
        return "StopWatch '" + stopWatch.getId() + "': running time = " + stopWatch.getTotalTimeSeconds() + " 秒";
    }

    public static String prettyPrint() {
        stop();
        StopWatch currentStopWatch = getCurrentStopWatch();
        StringBuilder sb = new StringBuilder(shortSummary(currentStopWatch));
        sb.append('\n');
        if (ArrayUtils.isEmpty(currentStopWatch.getTaskInfo())) {
            sb.append("No task info kept");
        }
        else {
            sb.append("---------------------------------------------\n");
            sb.append("Task name\t\t%\t\t\t\t秒\n");
            sb.append("---------------------------------------------\n");
//            NumberFormat nf = NumberFormat.getNumberInstance();
//            nf.setMinimumIntegerDigits(4);
//            nf.setGroupingUsed(false);
            NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(2);
            pf.setMaximumFractionDigits(2);
            pf.setGroupingUsed(false);
            for (StopWatch.TaskInfo task : currentStopWatch.getTaskInfo()) {
                sb.append(task.getTaskName()).append("\t\t\t");
                BigDecimal divide = BigDecimal.valueOf(task.getTimeNanos()).divide(BigDecimal.valueOf(currentStopWatch.getTotalTimeNanos()), MathContext.DECIMAL32);
                sb.append(pf.format(divide)).append("\t\t\t");
                //sb.append(nf.format(task.getTimeSeconds())).append("\n");
                sb.append(task.getTimeSeconds()).append("\n");
            }
        }
        removeStopWatch();
        return sb.toString();
    }

    public static void removeStopWatch() {
        STOPWATCH.remove();
    }
}
