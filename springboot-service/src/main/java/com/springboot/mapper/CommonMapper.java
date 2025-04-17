package com.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author macbookpro
 */
public interface CommonMapper<T> extends BaseMapper<T> {
    /**
     * 批量插入
     *
     * @param list
     */
    void batchInsert(List<T> list);
}
