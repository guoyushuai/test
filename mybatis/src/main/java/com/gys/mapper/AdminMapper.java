package com.gys.mapper;

import com.gys.pojo.Admin;

import java.util.List;

public interface AdminMapper {

    Admin findById(Integer id);
    List<Admin> findAll();

    void save(Admin admin);
    void update(Admin admin);
    void delete(Integer id);


}
