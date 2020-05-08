package com.springboot.entity;

import lombok.Data;

@Data
public class DataSource {
    private String driverName;
    private String url;
    private String username;
    private String password;
}
