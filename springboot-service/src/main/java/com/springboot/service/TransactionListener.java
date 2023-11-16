package com.springboot.service;


import com.alibaba.fastjson.JSON;
import com.springboot.dao.dto.UserDTO;
import com.springboot.domain.entity.UserQueryRequest;
import com.springboot.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
public class TransactionListener {

    @Autowired
    private UserRepository userRepository;

    public void getUser(String username) {
        UserQueryRequest request = new UserQueryRequest();
        request.setUsername(username);
        List<UserDTO> list = userRepository.list(request);
        log.info("添加用户:{}", JSON.toJSONString(list));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAddUserEvent(UserTransactionEvent event) {
        String username = event.getUserName();
        getUser(username);
    }
}
