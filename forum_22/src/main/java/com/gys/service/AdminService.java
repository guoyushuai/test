package com.gys.service;

import com.gys.dao.AdminDao;
import com.gys.entity.Admin;
import com.gys.exception.ServiceException;
import com.gys.util.Config;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminService {

    Logger logger = LoggerFactory.getLogger(AdminService.class);

    AdminDao adminDao = new AdminDao();


    /**
     * 管理员登陆
     */
    public Admin login(String adminname, String password, String ip) {
        Admin admin = adminDao.findByAdminname(adminname);
        password = DigestUtils.md5Hex(password + Config.get("user.password.salt"));
        if (admin != null && admin.getPassword().equals(password)) {
            logger.info("管理员{}在{}登录了管理系统",adminname,ip);
            return admin;
        } else {
            throw new ServiceException("账号或密码错误");
        }
    }
}
