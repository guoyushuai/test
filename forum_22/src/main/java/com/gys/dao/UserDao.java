package com.gys.dao;

import com.gys.entity.User;
import com.gys.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;

public class UserDao {

    public User findByUsername(String username) {
        String sql = "select * from t_user where username = ?";
        return DbHelp.query(sql,new BeanHandler<>(User.class),username);
    }

    public User findByEmail(String email) {
        String sql = "select * from t_user where email = ?";
        return DbHelp.query(sql,new BeanHandler<>(User.class),email);
    }

    public void save(User user) {
        String sql = "insert into t_user(username,password,email,phone,state,avatar) values(?,?,?,?,?,?)";
        DbHelp.update(sql,user.getUsername(),user.getPassword(),user.getEmail(),user.getPhone(),user.getState(),user.getAvatar());
    }
}
