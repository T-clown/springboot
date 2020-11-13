package com.springboot.service;

import com.springboot.annotation.ImportSelector;
import com.springboot.common.enums.CommonYN;
import com.springboot.service.impl.AppleServiceImpl;

public class PayConfigurationSelector extends PayModeImportSelector<ImportSelector> {
    @Override
    public String[] selectImports(CommonYN commonYN) {
        return commonYN.equals(CommonYN.YES) ? new String[] {AppleServiceImpl.class.getName()}
            : new String[] {OrangeServiceImpl.class.getName()};
    }
}
