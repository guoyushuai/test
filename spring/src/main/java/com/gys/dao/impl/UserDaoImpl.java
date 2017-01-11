package com.gys.dao.impl;

import com.gys.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    public UserDaoImpl() {
        System.out.println("Create UserDaoImpl...");
    }

    @Override
    public void save() {
        System.out.println("UserDaoImpl save");
    }

    @Override
    public void update() {
        System.out.println("UserDaoImpl update");
    }
}
