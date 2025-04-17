package com.springboot.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.domain.entity.UserDTO;
import com.springboot.service.converter.UserConvert2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author macbookpro
 */
@Service
public class UserSupport extends ServiceImpl<UserMapper, UserPO> {
    @Autowired
    private UserMapper userMapper;

    public void insert(UserDTO user) {
        save(UserConvert2.INSTANCE.sourceToTarget(user));
    }

    public void batchInsert(List<UserDTO> list) {
        userMapper.batchInsert(UserConvert2.INSTANCE.sourceToTarget(list));
    }


    public UserDTO getById(Long id) {
        return UserConvert2.INSTANCE.targetToSource(super.getById(id));
    }

    public List<UserDTO> listByIds(List<Long> ids) {
        List<UserPO> list = lambdaQuery().in(CollectionUtils.isNotEmpty(ids), UserPO::getId, ids).list();
        return UserConvert2.INSTANCE.targetToSource(list);
    }

    public PageInfo<UserDTO> pageQuery(List<Long> ids, Integer pageNum, Integer pageSize) {
//        try (Page<UserPO> page = PageHelper.startPage(pageNum, pageSize)) {
//            PageInfo<UserPO> pageInfo = page.doSelectPageInfo(() ->
//                    lambdaQuery().in(CollectionUtils.isNotEmpty(ids), UserPO::getId, ids).list()
//            );
//            return UserConvert2.INSTANCE.targetToSource(pageInfo);
//        }
        PageHelper.startPage(pageNum, pageSize);
        List<UserPO> list = lambdaQuery().in(CollectionUtils.isNotEmpty(ids), UserPO::getId, ids).list();
        PageInfo<UserPO> pageInfo = new PageInfo<>(list);
        return UserConvert2.INSTANCE.targetToSource(pageInfo);
    }

    public void updateById(UserDTO user) {
        super.updateById(UserConvert2.INSTANCE.sourceToTarget(user));
    }

    public void delete(Long id) {
        removeById(id);
    }
}
