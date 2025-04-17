package com.springboot.service.converter;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author macbookpro
 */
public interface BaseMapper<SOURCE, TARGET> {

    TARGET sourceToTarget(SOURCE source);

    List<TARGET> sourceToTarget(List<SOURCE> sources);

    SOURCE targetToSource(TARGET target);

    List<SOURCE> targetToSource(List<TARGET> targets);

    PageInfo<SOURCE> targetToSource(PageInfo<TARGET> pageInfo);
}
