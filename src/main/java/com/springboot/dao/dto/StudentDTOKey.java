package com.springboot.dao.dto;

import java.io.Serializable;

public class StudentDTOKey implements Serializable {
    private Integer id;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}