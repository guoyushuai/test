package com.gys.dao.impl;

import com.gys.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserDaoImplTest {
    //自动注入UserDao的实现类
    @Autowired
    private UserDao userDao;

    @Test
    public void save() throws Exception {
        userDao.save();
    }

    @Test
    public void update() throws Exception {
        userDao.update();
    }

}