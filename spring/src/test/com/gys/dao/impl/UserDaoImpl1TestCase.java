package com.gys.dao.impl;

import com.gys.dao.UserDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDaoImpl1TestCase {

    @Test
    public void load() {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext1.xml");

        UserDao userDao = applicationContext.getBean("userDao",UserDaoImpl1.class);

        userDao.save();
        userDao.update();
    }

}