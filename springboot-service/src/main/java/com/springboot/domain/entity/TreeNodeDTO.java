package com.springboot.domain.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreeNodeDTO {

    private String name;

    private String value;

    private String value2;

    private List<TreeNodeDTO> children;
}
