package com.wssong.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by WSS on 2017/4/25.
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/*.xml")
public class ExampleTests {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private StudentController mockMvc;

    //@Before
    public void setup() {
       // this.mockMvc = MockMvcBuilders.webAppContextSetup(this.mockMvc).build();
    }

    @Test
    public void getAccount() throws Exception {

        this.mockMvc.hello();
    }

}