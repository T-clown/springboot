package com.springboot.controller;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.config.listener.impl.PropertiesListener;
import com.springboot.common.entity.Result;
import com.springboot.common.enums.ResultCode;
import com.springboot.common.exception.ServiceException;
import com.springboot.common.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Tag(name = "nacos测试")
@Slf4j
@RestController
@RequestMapping("/nacos")
public class NacosController {


    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @Value("${nacos.discovery.server-addr}")
    private String baseUrl;

    private NamingService namingService;

    private ConfigService configService;

    @PostConstruct
    public void init() {
        try {
//            Properties properties = new Properties();
//            // 指定 Nacos 地址
//            properties.put(PropertyKeyConst.SERVER_ADDR, baseUrl);
//
//            // 默认命名空间是空，可以不填写
//            properties.put(PropertyKeyConst.NAMESPACE, "${namespace}");
//            //如果在云上开启鉴权可以传入应用身份
//            properties.put("ramRoleName", "$ramRoleName");
//            properties.put(PropertyKeyConst.ACCESS_KEY, "${accessKey}");
//            properties.put(PropertyKeyConst.SECRET_KEY, "${secretKey}");
//            NamingService naming = NacosFactory.createNamingService(properties);
            namingService = NacosFactory.createNamingService(baseUrl);
            configService = NacosFactory.createConfigService(baseUrl);
        } catch (NacosException e) {
            throw new ServiceException(ResultCode.FAILURE);
        }
    }


    @Operation(summary = "配置中心")
    @GetMapping(value = "/config/get")
    public Result<Boolean> get() {
        return ResultUtil.success(useLocalCache);
    }

    @Operation(summary = "服务发现")
    @GetMapping(value = "/discovery/get")
    public Result<List<Instance>> get(@RequestParam String serviceName) throws NacosException {
        List<Instance> instances = namingService.getAllInstances(serviceName);
        return ResultUtil.success(instances);
    }


    @Operation(summary = "推送配置")
    @GetMapping(value = "/publish/config")
    public Result<Void> publishConfig() throws NacosException {
        // 指定配置的 DataID 和 Group
        String dataId = "testDataId";
        String group = "testGroup";
        String content = "connectTimeoutInMills=5000";

        // 发布配置
        boolean publishConfig = configService.publishConfig(dataId, group, content);
        log.info("publishConfig: {}", publishConfig);
        wait2Sync();
        // 查询配置
        String config = configService.getConfig(dataId, group, 5000);
        log.info("getConfig: {}", config);
        // 监听配置
        configService.addListener(dataId, group, new PropertiesListener() {
            @Override
            public void innerReceive(Properties properties) {
                // 由于properties对象结构，默认反序列化
                log.info("innerReceive: {}", properties);
            }
        });

        configService.addListener(dataId, group, new AbstractListener() {
            @Override
            public void receiveConfigInfo(String s) {
                // 如果是json/yaml/pojo对象，可以根据需要做反序列化
                log.info("innerReceive: {}", s);
            }
        });

        // 更新配置
        boolean updateConfig = configService.publishConfig(dataId, group, "connectTimeoutInMills=3000");
        log.info("updateConfig: {}", updateConfig);
        wait2Sync();
        // 删除配置
        boolean removeConfig = configService.removeConfig(dataId, group);
        log.info("removeConfig: {}", removeConfig);
        wait2Sync();
        config = configService.getConfig(dataId, group, 5000);
        log.info("getConfig: {}", config);
        return ResultUtil.success();
    }

    private static void wait2Sync() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    @Operation(summary = "注册服务")
    @GetMapping(value = "/registry/service")
    public Result<Void> registry() throws NacosException {

        log.info("register service");
        namingService.registerInstance("testService", "11.11.11.11", 8080);

        wait2Sync();

        log.info("subscribe service");
        namingService.subscribe("testService", event -> {
            if (event instanceof NamingEvent) {
                log.info("serviceName:{}", ((NamingEvent) event).getServiceName());
                log.info("Instances:{}", ((NamingEvent) event).getInstances());
            }
        });

        namingService.registerInstance("testService", "11.11.11.12", 8080);

        wait2Sync();
        return ResultUtil.success();
    }


}
