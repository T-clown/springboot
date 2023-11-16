package com.springboot.common.spi.test;

import com.springboot.common.spi.ExtensionLoader;
import com.springboot.common.spi.URL;

public class TestSpi {
    public static void main(String[] args) {
        ExtensionLoader<Fruit> extensionLoader = ExtensionLoader.getExtensionLoader(Fruit.class);
        Fruit fruit = extensionLoader.getAdaptiveExtension();
        URL url = URL.valueOf("dubbo://127.0.0.1:20880?key=apple");
        fruit.printName(url);
    }
}
