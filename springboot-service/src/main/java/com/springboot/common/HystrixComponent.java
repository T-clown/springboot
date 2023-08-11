package com.springboot.common;

import com.springboot.domain.entity.User;
import com.springboot.domain.entity.UserQueryRequest;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * https://mp.weixin.qq.com/s/-s7gndjIH5p2jm8LWvKSQA
 * https://xie.infoq.cn/article/2593d1a3b9e1e06cac6502c4f
 */
@Component
public class HystrixComponent {

    @Autowired
    private UserService userService;

    /**
     * 服务降级处理。
     * 当前方法远程调用application service服务的时候，如果service服务出现了任何错误（超时，异常等）
     * 不会将异常抛到客户端，而是使用本地的一个fallback（错误返回）方法来返回一个兜底数据。避免客户端看到错误页面。
     * 使用注解来描述当前方法的服务降级逻辑。
     * @HystrixCommand - 开启Hystrix命令的注解。代表当前方法如果出现服务调用问题，使用Hystrix逻辑来处理。
     *  重要属性 - fallbackMethod
     *      错误返回方法名。如果当前方法调用服务，远程服务出现问题的时候，调用本地的哪个方法得到托底数据。
     *      Hystrix会调用fallbackMethod指定的方法，获取结果，并返回给客户端。
     * @return
     */
    //@HystrixCommand(fallbackMethod="fallback")
    public List<User> getUsers() {
        //远程调用其他服务
        List<User> result = userService.list(new UserQueryRequest());
        return result;
    }

    /**
     * fallback方法。本地定义的。用来处理远程服务调用错误时，返回的基础数据。
     */
    private List<User> fallback(){
        List<User> result = new ArrayList<>();
        User user=new User();
        user.setUsername("降级处理");
        return result;
    }



}
