package com.springboot.auth.controller;

import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Objects;

/**
 * 页面控制器.
 */
@Controller
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class Oauth2Controller {

    /**
     * 授权码模式跳转到登录页面
     *
     * @return view
     */
    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    /**
     * 退出登录
     *
     * @param redirectUrl 退出完成后的回调地址
     * @param principal   用户信息
     * @return 结果
     */
    @GetMapping("/logout")
    public ModelAndView logoutView(@RequestParam("redirect_url") String redirectUrl, Principal principal) {
        if (Objects.isNull(principal)) {
            throw new ResourceAccessException("请求错误，用户尚未登录");
        }
        ModelAndView view = new ModelAndView();
        view.setViewName("logout");
        view.addObject("user", principal.getName());
        view.addObject("redirectUrl", redirectUrl);
        return view;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:application.yml");
        String file1 = ResourceUtil.getResource("classpath:application.yml").getFile();
        Dict dict = YamlUtil.loadByPath("classpath:application.yml");
        Object byPath = dict.getByPath("spring.datasource.username");
        HashMap hashMap = YamlUtil.loadByPath("classpath:application.yml", HashMap.class);
        Object o = dict.get("spring.datasource.username");
        System.out.println(o.toString());
    }

}
