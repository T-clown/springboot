package com.springboot.service.impl;

import com.springboot.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Override
    public void add() {
        System.out.println(1);
    }
}
