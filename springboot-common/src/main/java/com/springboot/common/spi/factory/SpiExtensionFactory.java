
package com.springboot.common.spi.factory;

import com.springboot.common.spi.ExtensionFactory;
import com.springboot.common.spi.ExtensionLoader;
import com.springboot.common.spi.annotation.SPI;

/**
 * SpiExtensionFactory
 */
public class SpiExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(type);
            if (!loader.getSupportedExtensions().isEmpty()) {
                return loader.getAdaptiveExtension();
            }
        }
        return null;
    }

}
