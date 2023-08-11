package com.springboot.common.spring;


import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see MapperScan
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
//@Import(ProxyRegister.class)
public @interface EnableProxy {


    @AliasFor("basePackages")
    String[] value() default {};


    @AliasFor("value")
    String[] basePackages() default {};


    Class<?>[] basePackageClasses() default {};


    /**
     * The {@link BeanNameGenerator} class to be used for naming detected components within the Spring container.
     *
     * @return the class of {@link BeanNameGenerator}
     */
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    /**
     * This property specifies the annotation that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have the specified annotation.
     * <p>
     * Note this can be combined with markerInterface.
     *
     * @return the annotation that the scanner will search for
     */
    Class<? extends Annotation> annotationClass() default Annotation.class;

    /**
     * This property specifies the parent that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have the specified interface class as a
     * parent.
     * <p>
     * Note this can be combined with annotationClass.
     *
     * @return the parent that the scanner will search for
     */
    Class<?> markerInterface() default Class.class;

    /**
     * Specifies which {@code SqlSessionTemplate} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     *
     * @return the bean name of {@code SqlSessionTemplate}
     */
    String sqlSessionTemplateRef() default "";

    /**
     * Specifies which {@code SqlSessionFactory} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     *
     * @return the bean name of {@code SqlSessionFactory}
     */
    String sqlSessionFactoryRef() default "";

    /**
     * Specifies a custom MapperFactoryBean to return a mybatis proxy as spring bean.
     *
     * @return the class of {@code MapperFactoryBean}
     */
    Class<? extends MapperFactoryBean> factoryBean() default MapperFactoryBean.class;

    /**
     * Whether enable lazy initialization of mapper bean.
     * <p>
     * Default is {@code false}.
     * </p>
     *
     * @return set {@code true} to enable lazy initialization
     *
     * @since 2.0.2
     */
    String lazyInitialization() default "";

    /**
     * Specifies the default scope of scanned mappers.
     * <p>
     * Default is {@code ""} (equiv to singleton).
     * </p>
     *
     * @return the default scope
     */
    String defaultScope() default AbstractBeanDefinition.SCOPE_DEFAULT;
}
