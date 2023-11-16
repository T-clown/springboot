package com.springboot.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Greeting {
    private final long id;
    private final String content;
}
