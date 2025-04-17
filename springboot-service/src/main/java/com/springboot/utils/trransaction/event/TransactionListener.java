package com.springboot.utils.trransaction.event;


import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author macbookpro
 */
@Slf4j
@Component
public class TransactionListener {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommit(UserTransactionEvent event) {
        log.info("事务提交之前执行TransactionEvent：{}", JSON.toJSONString(event));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit(UserTransactionEvent event) {
        log.info("事务提交之后执行TransactionEvent：{}", JSON.toJSONString(event));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void afterRollback(UserTransactionEvent event) {
        log.info("事务回滚之后执行TransactionEvent：{}", JSON.toJSONString(event));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletion(UserTransactionEvent event) {
        log.info("事务完成之后执行TransactionEvent：{}", JSON.toJSONString(event));
    }
}
