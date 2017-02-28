package com.gys.dao;

import com.gys.pojo.User;

public class UserDao {

    public void saveUser(User user) {
        System.out.println(user.getUsername() + "saved!");
    }

}
