package com.springboot.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author macbookpro
 */
@Getter
@Setter
public class PageParam<T> {

    private Integer pageNum;

    private Integer pageSize;

    private T param;
}
