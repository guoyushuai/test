package com.gys;

import com.gys.dao.UserDao;
import com.gys.dao.impl.UserDaoImpl1;
import com.gys.service.UserService;
import com.gys.service.impl.UserServiceImpl1;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTestCase {

    @Test
    public void load() {
        //1、获取Spring容器(BeanFactory)(创建容器 容器启动)
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext1.xml");
        //2、从Spring容器中获得Bean(根据Bean节点的id属性值)

        /*接口指向实现类对象，降低组件之间的耦合性
        UserDao userDao = (UserDao) applicationContext.getBean("userDaoImpl");
        UserDaoImpl1 userDao = (UserDaoImpl1) applicationContext.getBean("userDaoImpl");*/
        UserDao userDao = applicationContext.getBean("userDao", UserDaoImpl1.class);
        userDao.save();

        /*UserService userService = applicationContext.getBean("userService",UserServiceImpl1.class);*/
        //BeanNotOfRequiredTypeException.加入aop后返回的是代理对象，却要强转为目标对象会引起该异常
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.save();

    }

}
