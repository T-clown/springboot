package com.springboot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.springboot.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class ParserDataUtil {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T parseData(String jsonData, Class<T> clazz) {
        if (StringUtils.isBlank(jsonData)) {
            return null;
        }
        T result = null;
        try {
            result = OBJECT_MAPPER.readValue(jsonData, clazz);
        } catch (IOException e) {
            log.error("com.springboot.util.ParserDataUtil.parseData error,errorMessage:{}", e.getMessage(), e);
        }
        return result;
    }

    public static <T> List<T> parseDataArray(String jsonData, Class<T> clazz) {
        if (StringUtils.isBlank(jsonData)) {
            return null;
        }
        List<T> result = null;
        try {
            CollectionType type = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
            result = OBJECT_MAPPER.readValue(jsonData, type);
        } catch (IOException e) {
            log.error("com.springboot.util.ParserDataUtil.parseData error,errorMessage:{}",e.getMessage(),e);
        }
        return result;
    }

    public static <T> List<T> parseDataArray2(String jsonData) {
        if (StringUtils.isBlank(jsonData)) {
            return null;
        }
        List<T> result = null;
        try {
           result= OBJECT_MAPPER.readValue(jsonData, new TypeReference<>() {});
           return result;
        } catch (IOException e) {
            log.error("com.springboot.util.ParserDataUtil.parseData error,errorMessage:{}",e.getMessage(),e);
        }
        return result;
    }

    public static void main(String[] args) throws JsonProcessingException {
        User user=new User();
        user.setId(1);
        user.setUsername("aa");
        String s = OBJECT_MAPPER.writeValueAsString(Collections.singletonList(user));

        List<User> users = parseDataArray(s, User.class);
        List<User> users2 = parseDataArray2(s);
        List<User> users1 = OBJECT_MAPPER.readValue(s, new TypeReference<>() {});
        Map<String, Object> map = OBJECT_MAPPER.readValue(s, new TypeReference<Map<String,Object>>(){});
        System.out.println();
    }

}

