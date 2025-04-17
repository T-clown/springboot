package com.springboot.common.validator;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.beans.PropertyEditorSupport;

/**
 * @author macbookpro
 */
@RestControllerAdvice
public class TrimParamAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                // 去掉空格
                setValue(text == null ? null : text.trim());
            }
        });
    }
}

