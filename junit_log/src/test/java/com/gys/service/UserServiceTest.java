package com.gys.service;

import com.gys.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.jws.soap.SOAPBinding;
import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest {

    private UserService userService;

    //在每个对象运行前运行,创建对象
    @Before
    public void setUp() throws Exception {
        userService = new UserService();
    }

    @Test
    public void findById() throws Exception {
        User user = userService.findById(5);
        //断言
        Assert.assertNotNull(user);
        //若要打印输出user,需重写User类中的toString方法
        System.out.println(user);
    }

    @Test
    public void findAll() throws Exception {
        List<User> userList = userService.findAll();

        //Assert.assertEquals(5,userList.size());

        System.out.println(userList);
    }

    @Test
    public void save() throws Exception {
        List<User> userList = userService.findAll();
        int beforeSize = userList.size();

        User user = new User();
        user.setUsername("张三");
        user.setAge(23);
        user.setAddress("郑州");
        user.setTel("410");

        userService.save(user);

        userList = userService.findAll();
        int afterSize = userList.size();

        Assert.assertEquals(beforeSize + 1,afterSize);
    }

    @Test
    public void del() throws Exception {
        userService.del(5);
    }

}