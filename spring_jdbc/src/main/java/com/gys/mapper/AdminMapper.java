package com.gys.mapper;

import com.gys.pojo.Admin;

public interface AdminMapper {

    void save(Admin admin);
    Admin findById(Integer id);


    Admin finByUsername(String username);

    void update(Admin oldAdmin);
}
