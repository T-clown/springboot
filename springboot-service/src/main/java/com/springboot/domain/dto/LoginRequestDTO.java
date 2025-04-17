package com.springboot.domain.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author macbookpro
 */
@Getter
@Setter
public class LoginRequestDTO {
    private String username;

    private String password;
}
