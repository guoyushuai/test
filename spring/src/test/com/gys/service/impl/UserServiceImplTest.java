package com.gys.service.impl;

import com.gys.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserServiceImplTest {
    //自动注入UserService的实现类
    @Autowired
    private UserService userService;

    /*private UserServiceImpl userService;*/

    @Test
    public void save() throws Exception {
        userService.save();
    }

    @Test
    public void update() throws Exception {
        userService.update();
    }

}