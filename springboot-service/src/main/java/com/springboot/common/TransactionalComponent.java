package com.springboot.common;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionalComponent {

    public interface Cell {
        void run() throws Exception;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void transactional(Cell cell) throws Exception {
        cell.run();
    }
}