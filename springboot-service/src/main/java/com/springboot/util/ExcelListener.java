package com.springboot.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ExcelListener<T> extends AnalysisEventListener<T> {
    private List<T> datas = new ArrayList<>();

    public ExcelListener() {

    }

    public ExcelListener(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public void invoke(T data, AnalysisContext analysisContext) {
        log.info( "解析得到的参数:" + JSON.toJSONString ( data ) );
        datas.add ( data );
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info( "。。。数据导入结束。。。" );
    }
}
