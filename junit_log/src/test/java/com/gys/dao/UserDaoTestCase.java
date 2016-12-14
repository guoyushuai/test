package com.gys.dao;

import com.gys.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UserDaoTestCase {

    private UserDao userDao;

    @Before
    public void setUp() throws Exception {

        userDao = new UserDao();

    }

    @Test
    public void save() throws Exception {

        User user = new User();
        user.setUsername("junit");
        user.setAge(14);
        user.setAddress("杭州");
        user.setTel("369");

        userDao.save(user);

    }

    @Test
    public void findById() throws Exception {

        User user = userDao.findById(5);
        Assert.assertNotNull(user);

    }

    @Test
    public void findAll() throws Exception {

        List<User> userList = userDao.findAll();
        Assert.assertEquals(6,userList.size());

    }

    @Test
    public void del() throws Exception {

        userDao.del(6);

    }

}