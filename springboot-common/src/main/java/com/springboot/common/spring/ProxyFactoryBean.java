/*
 * Copyright 2010-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springboot.common.spring;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @see MapperFactoryBean
 */
public class ProxyFactoryBean<T>  implements FactoryBean<T> {

  private Class<T> proxyInterface;

  public ProxyFactoryBean() {
  }

  public ProxyFactoryBean(Class<T> proxyInterface) {
    this.proxyInterface = proxyInterface;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T getObject() throws Exception {
    //return getSqlSession().getMapper(this.mapperInterface);
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<T> getObjectType() {
    return this.proxyInterface;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSingleton() {
    return true;
  }

}
