package com.gys.service.impl;

import com.gys.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class UserServiceImplTestCase {

    @Test
    public void load() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserService userService = applicationContext.getBean("userService",UserServiceImpl.class);

        userService.save();
        userService.update();
    }

}