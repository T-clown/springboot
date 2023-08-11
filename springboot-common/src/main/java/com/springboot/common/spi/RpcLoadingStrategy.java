
package com.springboot.common.spi;

/**
 * Rpc {@link LoadingStrategy}
 *
 */
public class RpcLoadingStrategy implements LoadingStrategy {

    @Override
    public String directory() {
        return "META-INF/rpc/";
    }

    @Override
    public boolean overridden() {
        return true;
    }

    @Override
    public int getPriority() {
        return NORMAL_PRIORITY;
    }


}
