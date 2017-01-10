package com.gys.service.impl;

import com.gys.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class UserServiceImplTestCase {

    @Test
    public void load() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        /*UserService userService = applicationContext.getBean("userService",UserServiceImpl.class);*/
        //BeanNotOfRequiredTypeException.加入aop后返回的是代理对象，却要强转为目标对象会引起该异常
        UserService userService = (UserService) applicationContext.getBean("userService");

        userService.save();
        userService.update();
    }

}