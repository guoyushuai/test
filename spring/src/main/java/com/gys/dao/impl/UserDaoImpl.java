package com.gys.dao.impl;

import com.gys.dao.UserDao;

public class UserDaoImpl implements UserDao {

    public UserDaoImpl() {
        System.out.println("Create UserDaoImpl");
    }
    public void save() {
        System.out.println("UserDaoImpl save");
    }

    public void update() {
        System.out.println("UserDaoImpl update");
    }
}
