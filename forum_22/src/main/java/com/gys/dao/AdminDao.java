package com.gys.dao;

import com.gys.entity.Admin;
import com.gys.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;

public class AdminDao {


    public Admin findByAdminname(String adminname) {
        String sql = "select * from t_admin where adminname = ?";
        return DbHelp.query(sql,new BeanHandler<>(Admin.class),adminname);
    }
}
