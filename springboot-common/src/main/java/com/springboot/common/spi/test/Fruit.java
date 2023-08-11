package com.springboot.common.spi.test;

import com.springboot.common.spi.URL;
import com.springboot.common.spi.annotation.Adaptive;
import com.springboot.common.spi.annotation.SPI;

@SPI(value = "banana")
public interface Fruit {

    @Adaptive(value = "key")
    void printName(URL url);
}
