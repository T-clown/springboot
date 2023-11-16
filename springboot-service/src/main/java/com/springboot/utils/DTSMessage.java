package com.springboot.utils;

import lombok.Data;

import java.util.Set;

@Data
public class DTSMessage {

    private String env;

    private String sql;

    private Set<String> tableNames;
}
