package com.springboot.common.extension;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.function.Predicate;

/**
 * @Import注解用法 https://juejin.cn/post/6844904035212853255
 */
@Slf4j
public class ImportAnnotation {
    /**
     * 全类名
     */
    public static class ImportOne {
        public void importOne() {
            log.info("import注解用法一");
        }
    }

    /**
     * 全类名
     */
    public static class ImportTwoSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{ImportAnnotation.ImportTwo.class.getName()};
        }

        @Override
        public Predicate<String> getExclusionFilter() {
            return ImportSelector.super.getExclusionFilter();
        }
    }

    public static class ImportTwo {
        public void importOne() {
            log.info("import注解用法二");
        }
    }

    /**
     * 自定义名称
     */
    public static class ImportThreeRegistrar implements ImportBeanDefinitionRegistrar {

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
            ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry, importBeanNameGenerator);
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            //指定bean定义信息（包括bean的类型、作用域...）
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(ImportThree.class);
            //注册一个bean指定bean名字（id）
            registry.registerBeanDefinition("importThree", rootBeanDefinition);
            ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
        }
    }

    public static class ImportThree {
        public void importOne() {
            log.info("import注解用法三");
        }
    }
}
