package com.springboot.common;

import com.springboot.handler.CallBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionalComponent {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void transactional(CallBack callBack)  {
        callBack.callback();
    }


    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;

    public void execute(CallBack callBack){
        //开启事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try{
            callBack.callback();
            //提交事务
            dataSourceTransactionManager.commit(transactionStatus);
        }catch(Exception e){
            //回滚事务
            dataSourceTransactionManager.rollback(transactionStatus);
        }
    }
}