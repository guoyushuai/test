package com.gys;

import com.gys.dao.UserDao;
import com.gys.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringAnnotationTestCase {

    @Test
    public void load() {
        /*ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");*/
        //替代ApplicationContext.xml
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);

        UserDao userDao = (UserDao) applicationContext.getBean("userDaoImpl");
        userDao.save();
        userDao.update();

        UserService userService = (UserService) applicationContext.getBean("userServiceImpl");
        userService.save();
        userService.update();
    }

}
