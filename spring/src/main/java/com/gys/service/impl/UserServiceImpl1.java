package com.gys.service.impl;

import com.gys.dao.UserDao;
import com.gys.service.UserService;

public class UserServiceImpl1 implements UserService {

    private UserDao userDao;


    /*//构造方法注入（类之间最高级别的依赖）
    public UserServiceImpl1(UserDao userDao) {
        this.userDao = userDao;
    }*/


    //set方法注入（基本数据类型及集合的注入，写相应的set方法）
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    public void save() {
        userDao.save();
    }

    public void update() {
        userDao.update();
    }
}
