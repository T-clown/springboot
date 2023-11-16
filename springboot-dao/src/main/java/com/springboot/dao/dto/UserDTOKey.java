package com.springboot.dao.dto;

import java.io.Serializable;

public class UserDTOKey implements Serializable {
    private Long id;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}