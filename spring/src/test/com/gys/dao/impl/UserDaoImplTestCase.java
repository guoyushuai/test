package com.gys.dao.impl;

import com.gys.dao.UserDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDaoImplTestCase {

    @Test
    public void load() {
        //1、获取Spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //2、从Spring容器中获得Bean
        UserDao userDao = applicationContext.getBean("userDaoImpl",UserDaoImpl.class);
        /*UserDao userDao = (UserDao) applicationContext.getBean("userDaoImpl");
        UserDaoImpl userDao = (UserDaoImpl) applicationContext.getBean("userDaoImpl");*/
        userDao.save();
        userDao.update();
    }

}