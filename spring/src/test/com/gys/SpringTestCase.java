package com.gys;

import com.gys.dao.UserDao;
import com.gys.dao.impl.UserDaoImpl;
import com.gys.service.UserService;
import com.gys.service.impl.UserServiceImpl;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTestCase {

    @Test
    public void load() {
        //1、获取Spring容器(BeanFactory)(创建容器 容器启动)
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //2、从Spring容器中获得Bean(根据Bean节点的id属性值)

        UserDao userDao = applicationContext.getBean("userDao", UserDaoImpl.class);
        userDao.save();

        UserService userService = applicationContext.getBean("userService", UserServiceImpl.class);
        userService.save();

    }

}
