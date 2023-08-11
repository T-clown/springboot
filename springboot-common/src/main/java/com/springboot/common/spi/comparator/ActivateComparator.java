
package com.springboot.common.spi.comparator;

import com.springboot.common.spi.annotation.Activate;

import java.util.Comparator;

/**
 * OrderComparator
 */
public class ActivateComparator implements Comparator<Class> {

    public static final Comparator<Class> COMPARATOR = new ActivateComparator();

    @Override
    public int compare(Class o1, Class o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (o1.equals(o2)) {
            return 0;
        }

        ActivateInfo a1 = parseActivate(o1);
        ActivateInfo a2 = parseActivate(o2);
        return a1.order > a2.order ? 1 : -1;

    }

    private ActivateInfo parseActivate(Class<?> clazz) {
        ActivateInfo info = new ActivateInfo();
        if (clazz.isAnnotationPresent(Activate.class)) {
            Activate activate = clazz.getAnnotation(Activate.class);
            info.order = activate.order();
        }
        return info;
    }

    private static class ActivateInfo {
        private int order;
    }
}
