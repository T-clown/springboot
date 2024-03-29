
package com.springboot.common.spi;

import com.springboot.common.spi.annotation.SPI;

/**
 * ExtensionFactory
 */
@SPI
public interface ExtensionFactory {

    /**
     * Get extension.
     *
     * @param type object type.
     * @param name object name.
     * @return object instance.
     */
    <T> T getExtension(Class<T> type, String name);

}
