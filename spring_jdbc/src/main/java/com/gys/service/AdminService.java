package com.gys.service;

import com.gys.pojo.Admin;

public interface AdminService {

    void save(Admin admin) throws Exception;
    void update(String username,String password) throws Exception;
    Admin findById(Integer id);

}
