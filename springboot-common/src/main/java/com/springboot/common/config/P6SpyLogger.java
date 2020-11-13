package com.springboot.common.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class P6SpyLogger implements MessageFormattingStrategy {

    private String message = "\n执行时间: %s, \n耗时: %s ms, \nSQL语句: %s , \n连接地址: %s";

    @Override
    public String formatMessage(int connectionId, String execTime, long elapsed, String category, String preparedSql,
                                String sql, String url) {
        return String.format(message, execTime, elapsed, sql, url);
    }
}
