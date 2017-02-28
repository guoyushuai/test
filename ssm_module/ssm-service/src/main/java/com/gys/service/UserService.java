package com.gys.service;

import com.gys.dao.UserDao;
import com.gys.pojo.User;

public class UserService {

    private UserDao userDao = new UserDao();

    public void saveUser(User user) {
        userDao.saveUser(user);
    }

}
