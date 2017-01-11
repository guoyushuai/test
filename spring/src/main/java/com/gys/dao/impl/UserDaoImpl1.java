package com.gys.dao.impl;

import com.gys.dao.UserDao;

public class UserDaoImpl1 implements UserDao {

    public UserDaoImpl1() {
        System.out.println("Create UserDaoImpl1");
    }

    @Override
    public void save() {
        System.out.println("UserDaoImpl1 save");
    }

    @Override
    public void update() {
        System.out.println("UserDaoImpl1 update");
    }

}
