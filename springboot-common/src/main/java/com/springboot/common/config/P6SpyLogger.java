package com.springboot.common.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * https://segmentfault.com/a/1190000038714503
 */
public class P6SpyLogger implements MessageFormattingStrategy {

    private String message = "\n\t执行时间: %s \n\t耗时: %s ms \n\tSQL语句: \n\t\t%s  \n\t连接地址: \n\t\t %s";

    @Override
    public String formatMessage(int connectionId, String execTime, long elapsed, String category, String preparedSql,
                                String sql, String url) {
       // return String.format(message, execTime, elapsed, sql, url);
        return null;
    }
}
