package com.springboot.common.spi.test;


import com.springboot.common.spi.URL;

public class BananaFruitImpl implements Fruit {
    @Override
    public void printName(URL url) {
        System.out.println("banana");
    }
}
