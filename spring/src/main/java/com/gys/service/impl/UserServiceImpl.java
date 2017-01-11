package com.gys.service.impl;

import com.gys.dao.UserDao;
import com.gys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    //属性自动注入
    @Autowired
    private UserDao userDao;

    /*//set方法自动注入
    private UserDao userDao;
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }*/

    @Override
    public void save() {
        userDao.save();
    }

    @Override
    public void update() {
        userDao.update();
    }
}
