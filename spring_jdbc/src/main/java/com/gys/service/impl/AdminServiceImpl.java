package com.gys.service.impl;

import com.gys.dao.AdminDao;
import com.gys.mapper.AdminMapper;
import com.gys.pojo.Admin;
import com.gys.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    @Transactional(readOnly = true)
    public void save(Admin admin) throws Exception {
        adminMapper.save(admin);
    }

    @Override
    @Transactional
    public void update(String username, String password) throws Exception {
        Admin oldAdmin = adminMapper.finByUsername(username);
        oldAdmin.setPassword(password);
        adminMapper.update(oldAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public Admin findById(Integer id) {
        return adminMapper.findById(id);
    }









/*
    @Autowired
    private AdminDao adminDao;

    @Override
    @Transactional
    public void save(Admin admin) throws Exception {
        adminDao.save(admin);
        if (true) {
            throw new RuntimeException("故意的");
        }
        adminDao.save(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public void update(String username,String password) throws Exception {
        Admin oldadmin = adminDao.findByUsername(username);
        oldadmin.setPassword(password);
        adminDao.update(oldadmin);
    }

    @Override
    @Transactional(readOnly = true)
    public Admin findById(Integer id) {
        return adminDao.findById(id);
    }
*/
}
