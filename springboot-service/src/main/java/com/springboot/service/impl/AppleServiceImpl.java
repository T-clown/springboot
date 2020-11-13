package com.springboot.service.impl;

import com.springboot.service.SelectorService;

public class AppleServiceImpl implements SelectorService {
    @Override
    public String select() {
        return "苹果";
    }
}
