package com.springboot.service;

import com.springboot.annotation.ImportSelector;
import com.springboot.common.enums.CommonYN;

public class PayConfigurationSelector extends AbstractImportSelector<ImportSelector> {
    @Override
    public String[] selectImports(CommonYN commonYN) {
        return commonYN.equals(CommonYN.YES) ? new String[] {AppleServiceImpl.class.getName()}
            : new String[] {OrangeServiceImpl.class.getName()};
    }
}
