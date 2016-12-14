package com.gys.service;

import com.gys.dao.UserDao;
import com.gys.entity.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceWithGuavaTestCase {

    //视频演示错误 此处应为UserServiceWithGuava而不是UserService
    private UserServiceWithGuava userServiceWithGuava;

    //针对创建对象比较复杂的类
    @Before
    public void setUp() throws Exception {
        userServiceWithGuava = new UserServiceWithGuava();
    }

    @Test
    public void findById() throws Exception {

        User user = userServiceWithGuava.findById(7);

        //以下方式二选一
        System.out.println(user);

        assertNotNull(user);

    }

}