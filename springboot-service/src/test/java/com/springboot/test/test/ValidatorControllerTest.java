package com.springboot.test.test;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import cn.hutool.json.JSONUtil;
import com.springboot.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ValidatorControllerTest {
    private MockMvc mockMvc;

    //@Before
    //public void setUp() throws Exception {
    //    mockMvc = MockMvcBuilders.standaloneSetup(new Application()).build();
    //}

    @Test
    public void getHello() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect((ResultMatcher)content().string(equalTo("Hello World")));
    }

    @Test
    public void mvc() throws Exception {
        User user = new User();
        user.setUsername("SnailClimb");
        user.setAge(11);
        user.setEmail("82938390@qq.com");
        user.setUsername("www");

        mockMvc.perform(post("/validator")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(JSONUtil.toJsonStr(user)))
            .andExpect(MockMvcResultMatchers.jsonPath("username").value("SnailClimb"))
            .andExpect(MockMvcResultMatchers.jsonPath("classId").value("82938390"))
            .andExpect(MockMvcResultMatchers.jsonPath("sex").value("Man"))
            .andExpect(MockMvcResultMatchers.jsonPath("email").value("Snailclimb@qq.com"));
    }

    @Autowired
    Validator validator;

    /**
     * 手动校验
     */
    @Test
    public void checkManually() {
        //ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //Validator validator = factory.getValidator();
        User user = new User();
        user.setUsername("clown");
        user.setAge(8);
        user.setEmail("82938390@qq.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> constraintViolation : violations) {
            System.out.println(constraintViolation.getMessage());
        }
    }
}
