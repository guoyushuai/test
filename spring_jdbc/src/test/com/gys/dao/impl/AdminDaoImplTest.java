package com.gys.dao.impl;

import com.gys.dao.AdminDao;
import com.gys.pojo.Admin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AdminDaoImplTest {

    @Autowired
    private AdminDao adminDao;

    @Test
    public void save() throws Exception {
        Admin admin = new Admin("Spring4.x","233");
        adminDao.save(admin);
    }

    @Test
    public void update() throws Exception {
        Admin admin = adminDao.findById(3);
        admin.setPassword("789");
        adminDao.update(admin);
    }

    @Test
    public void delete() throws Exception {
        adminDao.delete(7);
    }

    @Test
    public void findById() throws Exception {
        Admin admin = adminDao.findById(1);
        System.out.println(admin);
        assertNotNull(admin);
    }

    @Test
    public void findAll() throws Exception {
        List<Admin> adminList = adminDao.findAll();
        for (Admin admin:adminList) {
            System.out.println(admin);
        }
    }

    @Test
    public void findByUsername() throws Exception {
        Admin admin = adminDao.findByUsername("admin");
        System.out.println(admin);
        assertNotNull(admin);
    }

    @Test
    public void count() throws Exception {
        Long result = adminDao.count();
        System.out.println(result);
    }

}