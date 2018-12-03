package com.springboot.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MockServletContext.class)
public class YellowTest {
    //@Autowired
    //private Properties properties;
    //
    //
    //@Test
    //public void getHello() {
    //    Assert.assertEquals(properties.getName(), "程序猿DD");
    //    Assert.assertEquals(properties.getTitle(), "Spring Boot教程");
    //}


}
