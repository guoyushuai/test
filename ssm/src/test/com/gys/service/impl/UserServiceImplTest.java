package com.gys.service.impl;

import com.gys.pojo.User;
import com.gys.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:applicationContext-mybatis.xml"})
public class UserServiceImplTest {


    @Autowired
    private UserService userService;

    @Test
    public void load() {
        System.out.println(userService);
    }

    @Test
    public void findAllUsers() throws Exception {
        List<User> userList = userService.findAllUsers();
        for (User user:userList) {
            System.out.println(user);
        }
    }

    @Test
    public void findUserById() throws Exception {
        User user = userService.findUserById(9);
        System.out.println(user);
    }

    @Test
    public void saveNewUser() throws Exception {
        User user = new User("aa","bb");
        Integer[] roleids = {1,2,3};
        userService.saveNewUser(user,roleids);
    }


    @Test
    public void test() {
        System.out.println(11%2);
    }

}