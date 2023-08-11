package com.springboot.common.spi.test;

import com.springboot.common.spi.URL;

public class AppleFruitImpl implements Fruit {
    @Override
    public void printName(URL url) {
        System.out.println("apple");
    }
}
