
package com.springboot.common.spring;

import org.apache.poi.ss.formula.functions.T;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

import static org.springframework.util.Assert.notNull;

/**
 * @see MapperScannerConfigurer
 */
public class ProxyScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {

  private String basePackage;

  private String lazyInitialization;

  private Class<? extends Annotation> annotationClass;

  private Class<?> markerInterface;

  private Class<? extends ProxyFactoryBean<T>> mapperFactoryBeanClass;

  private ApplicationContext applicationContext;

  private String beanName;

  private BeanNameGenerator nameGenerator;

  private String defaultScope;


  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }


  public void setLazyInitialization(String lazyInitialization) {
    this.lazyInitialization = lazyInitialization;
  }

  /**
   * 有此注解的会被扫描
   * @param annotationClass
   *          annotation class
   */
  public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
    this.annotationClass = annotationClass;
  }

  /**
   * 父类为此类的会被扫描
   * @param superClass
   *          parent class
   */
  public void setMarkerInterface(Class<?> superClass) {
    this.markerInterface = superClass;
  }



  /**
   * The class of the {@link MapperFactoryBean} to return a mybatis proxy as spring bean.
   *
   * @param mapperFactoryBeanClass
   *          The class of the MapperFactoryBean
   *
   * @since 2.0.1
   */
  public void setMapperFactoryBeanClass(Class<? extends ProxyFactoryBean<T>> mapperFactoryBeanClass) {
    this.mapperFactoryBeanClass = mapperFactoryBeanClass;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBeanName(String name) {
    this.beanName = name;
  }

  /**
   * Gets beanNameGenerator to be used while running the scanner.
   *
   * @return the beanNameGenerator BeanNameGenerator that has been configured
   *
   * @since 1.2.0
   */
  public BeanNameGenerator getNameGenerator() {
    return nameGenerator;
  }

  /**
   * Sets beanNameGenerator to be used while running the scanner.
   *
   * @param nameGenerator
   *          the beanNameGenerator to set
   *
   * @since 1.2.0
   */
  public void setNameGenerator(BeanNameGenerator nameGenerator) {
    this.nameGenerator = nameGenerator;
  }

  /**
   * Sets the default scope of scanned mappers.
   * <p>
   * Default is {@code null} (equiv to singleton).
   * </p>
   *
   * @param defaultScope
   *          the default scope
   *
   * @since 2.0.6
   */
  public void setDefaultScope(String defaultScope) {
    this.defaultScope = defaultScope;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    notNull(this.basePackage, "Property 'basePackage' is required");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    // left intentionally blank
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.0.2
   */
  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {

    ClassPathProxyScanner scanner = new ClassPathProxyScanner(registry);
    scanner.setAnnotationClass(this.annotationClass);
    scanner.setMarkerInterface(this.markerInterface);
    scanner.setResourceLoader(this.applicationContext);
    scanner.setBeanNameGenerator(this.nameGenerator);
    scanner.setMapperFactoryBeanClass(this.mapperFactoryBeanClass);
    if (StringUtils.hasText(lazyInitialization)) {
      scanner.setLazyInitialization(Boolean.parseBoolean(lazyInitialization));
    }
    if (StringUtils.hasText(defaultScope)) {
      scanner.setDefaultScope(defaultScope);
    }
    scanner.registerFilters();
    scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
  }

}
