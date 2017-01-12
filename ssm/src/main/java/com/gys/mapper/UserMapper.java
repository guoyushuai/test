package com.gys.mapper;

import com.gys.pojo.User;

import java.util.List;

public interface UserMapper {
    List<User> findAllUsers();

    void save(User user);

    void del(Integer id);

    User findUserById(Integer id);

    void update(User user);
}
