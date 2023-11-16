package com.springboot.utils.excel.easyexcel;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 有个很重要的点 ExcelUploadListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @param <T>
 * @param <R>
 */
public class ExcelListener<T, R> extends AnalysisEventListener<T> {
    private static final Logger logger = LoggerFactory.getLogger(ExcelListener.class);

    private static final int BATCH_COUNT = 1000;

    /**
     * 当然如果不用存储这个对象没用。
     */
    private final List<R> result = Lists.newArrayList();
    /**
     * 数据转换
     */
    private final Function<T, R> converter;
    /**
     * 数据业务操作，如持久化
     */
    private final Consumer<List<R>> consumer;


    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     */
    public ExcelListener(Function<T, R> converter, Consumer<List<R>> consumer) {
        this.converter = converter;
        this.consumer = consumer;
    }

    /**
     * 每一条数据解析都会来调用
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        logger.debug("解析到一条数据:{}", JSON.toJSONString(data));
        //
        this.result.add(converter.apply(data));

        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (this.result.size() >= BATCH_COUNT) {
            //持久化
            consumer.accept(result);
            // 存储完成清理 data
            this.result.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        //持久化
        consumer.accept(result);
        logger.info("所有数据解析完成！");
    }

}
