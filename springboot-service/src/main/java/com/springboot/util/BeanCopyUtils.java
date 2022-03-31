package com.springboot.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;

@Slf4j
public class BeanCopyUtils {

    public static <R, T> R copyProperties(T source, Class<R> clazz) {
        if (Objects.isNull(source)) {
            return null;
        }
        try {
            Constructor<R> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            R target = constructor.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            log.warn("BeanCopyUtils.copyProperties Exception, source:{}, clazz:{}", JSON.toJSONString(source), clazz.getName(), e);
        }
        return null;
    }

    public static <R, T> List<R> copyProperties(List<T> sources, Class<R> clazz) {
        List<R> result = Lists.newArrayList();
        if (CollectionUtils.isEmpty(sources)) {
            return result;
        }
        try {
            Constructor<R> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            for (T source : sources) {
                R target = constructor.newInstance();
                BeanUtils.copyProperties(source, target);
                result.add(target);
            }
        } catch (Exception e) {
            log.warn("BeanCopyUtils.copyProperties Exception, source:{}, clazz:{}", JSON.toJSONString(sources), clazz.getName(), e);
        }
        return result;
    }

}
