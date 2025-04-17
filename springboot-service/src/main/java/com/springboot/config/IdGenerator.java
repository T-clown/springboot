package com.springboot.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.springboot.common.utils.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author macbookpro
 */
@Slf4j
@Component
public class IdGenerator implements IdentifierGenerator {

    @Override
    public Long nextId(Object entity) {
        String bizKey = entity.getClass().getName();
        log.info("bizKey:{}", bizKey);
        MetaObject metaObject = SystemMetaObject.forObject(entity);
        String name = (String) metaObject.getValue("username");
        long id = IdUtil.generateId();
        log.info("为{}生成主键值->:{}", name, id);
        return id;
    }
}
