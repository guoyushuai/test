package com.gys.service;

import com.gys.pojo.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    void save(User user);

    void del(Integer id);

    User findUserById(Integer id);

    void edit(User user);
}
