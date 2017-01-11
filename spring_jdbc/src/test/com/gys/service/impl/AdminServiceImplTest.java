package com.gys.service.impl;

import com.gys.pojo.Admin;
import com.gys.service.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.PrivateKey;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AdminServiceImplTest {

    @Autowired
    private AdminService adminService;

    @Test
    public void save() throws Exception {
        Admin admin = new Admin("wzz","520");
        adminService.save(admin);
    }

    @Test
    public void update() throws Exception {
        adminService.update("zz","201314");
    }

    @Test
    public void findById() throws Exception {
        Admin admin = adminService.findById(1);
        System.out.println(admin);
        assertNotNull(admin);
    }

}