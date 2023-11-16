package com.springboot.service.converter;

import com.springboot.common.entity.PageResult;
import com.springboot.dao.dto.UserDTO;
import com.springboot.domain.entity.User;
import org.springframework.beans.BeanUtils;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserConverter {
    public static <T, R> PageResult<R> convertPageResult(PageResult<T> pageResult, Function<T, R> converter) {
        if (pageResult == null) {
            return null;
        } else {
            PageResult<R> result = new PageResult();
            result.setPageNum(pageResult.getPageNum());
            result.setPageSize(pageResult.getPageSize());
            result.setTotal(pageResult.getTotal());
            result.setList(Optional.ofNullable(pageResult.getList()).map(x -> x.stream().map(converter::apply).collect(Collectors.toList())).orElse(null));
            return result;
        }
    }

    public static User convert(UserDTO source) {
        if (source == null) {
            return null;
        }
        User target = new User();
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
