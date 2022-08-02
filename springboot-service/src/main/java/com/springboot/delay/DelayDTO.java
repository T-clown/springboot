package com.springboot.delay;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DelayDTO extends BaseTask{
    private String id;

    @Override
    public String taskIdentity() {
        return id;
    }

}
