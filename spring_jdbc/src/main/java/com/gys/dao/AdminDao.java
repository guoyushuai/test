package com.gys.dao;

import com.gys.pojo.Admin;

import java.util.List;

public interface AdminDao {

    void save(Admin admin);
    void update(Admin admin);
    void delete(Integer id);

    Admin findById(Integer id);
    List<Admin> findAll();
    Admin findByUsername(String username);
    Long count();

}
