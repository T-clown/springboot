
package com.springboot.common.spi;

/**
 * Services {@link LoadingStrategy}
 *
 */
public class ServicesLoadingStrategy implements LoadingStrategy {

    @Override
    public String directory() {
        return "META-INF/services/";
    }

    @Override
    public boolean overridden() {
        return true;
    }

    @Override
    public int getPriority() {
        return MIN_PRIORITY;
    }

}
