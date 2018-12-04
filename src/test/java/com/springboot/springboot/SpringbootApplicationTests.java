package com.springboot.springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MockServletContext.class)
public class SpringbootApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println("00000000");
	}

}
