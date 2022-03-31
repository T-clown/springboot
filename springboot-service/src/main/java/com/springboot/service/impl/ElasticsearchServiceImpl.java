package com.springboot.service.impl;

import com.springboot.service.ElasticsearchService;
import com.springboot.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public void addData() {

    }
}
