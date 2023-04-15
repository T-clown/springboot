package com.springboot.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Greeting {
    private final long id;
    private final String content;
}
