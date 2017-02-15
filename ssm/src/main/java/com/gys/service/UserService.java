package com.gys.service;

import com.gys.pojo.Role;
import com.gys.pojo.User;
import com.gys.util.db.Page;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    void save(User user);

    void del(Integer id);

    User findUserById(Integer id);

    void edit(User user, Integer[] roleid);

    List<Role> findAllRoles();

    void saveNewUser(User user, Integer[] roleids);

    Page<User> findUserByPageNo(Integer pageNo);

    Page<User> findUserByPageNoAndSearchParam(Integer p, String queryName, String queryRole);
}
