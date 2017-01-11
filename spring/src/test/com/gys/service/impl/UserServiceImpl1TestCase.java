package com.gys.service.impl;

import com.gys.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class UserServiceImpl1TestCase {

    @Test
    public void load() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext1.xml");

        UserService userService = (UserService) applicationContext.getBean("userService");

        userService.save();
        userService.update();
    }

}