package com.zero.drools.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 方法一：
 * 移除 @Autowired 注解，并手动声明 MockMvc。
 * 在 @Before 中使用 MockMvcBuilders.webAppContextSetup 即可手动声明 MockMvc。
 * 这里使用自动注入的形式
 */
@Configuration
public class MockMvcConfig {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Bean
    public MockMvc init() {
        return MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

}
