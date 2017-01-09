package com.gys.dao.impl;

import com.gys.dao.UserDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDaoImplTestCase {

    @Test
    public void load() {
        //1、获取Spring容器(BeanFactory)(创建容器 容器启动)
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //2、从Spring容器中获得Bean(根据Bean节点的id属性值)
        UserDao userDao = applicationContext.getBean("userDao",UserDaoImpl.class);
        /*接口指向实现类对象，降低组件之间的耦合性
        UserDao userDao = (UserDao) applicationContext.getBean("userDaoImpl");
        UserDaoImpl userDao = (UserDaoImpl) applicationContext.getBean("userDaoImpl");*/
        userDao.save();
        userDao.update();
    }

}