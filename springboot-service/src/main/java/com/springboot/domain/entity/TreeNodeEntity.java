package com.springboot.domain.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreeNodeEntity {

    private String key;

    private String value;

    private List<TreeNodeEntity> children;
}
