
package com.springboot.common.spi;

/**
 * 加载策略
 */
public interface LoadingStrategy extends Prioritized {
    /**
     * 加载目录
     * @return
     */
    String directory();

    default boolean preferExtensionClassLoader() {
        return false;
    }

    default String[] excludedPackages() {
        return null;
    }

    default boolean overridden() {
        return false;
    }
}
