package com.springboot.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Api 标注在Controller类上，tags和value的值会展示在web界面对应接口描述上
 * @ApiOperation 标注在具体接口方法上，用于描述具体方法的特性
 * @ApiImplicitParams 标注在方法上，用于描述对应接口参数的意义
 * @ApiModel 标注于实体类上
 * @ApiModelProperty 标注于实体类属性上，用于说明各属性含义
 * @ApiIgnore 用于标注在不想被Swagger扫描的类或者方法及属性上
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
            //分组名称
            .groupName("2.0.3版本")
            .select()
            //扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外
            .apis(RequestHandlerSelectors.basePackage("com.springboot.controller"))
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("springboot")
            .description("学习springboot")
            .termsOfServiceUrl("http://localhost:8999/")
            .contact(new Contact("Ti93", "https://doc.xiaominfo.com/knife4j/springboot.html#maven%E5%BC%95%E7%94%A8", "414162330@qq.com"))
            .version("1.0.0-SNAPSHOT")
            .build();
    }

}
