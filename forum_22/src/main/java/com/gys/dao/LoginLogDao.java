package com.gys.dao;

import com.gys.entity.LoginLog;
import com.gys.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;

public class LoginLogDao {

    //将用户的登录日志保存到数据库中相应的表中
    public void save(LoginLog login) {
        String sql = "insert into t_login_log(ip,userid) values(?,?)";
        DbHelp.update(sql,login.getIp(),login.getUserid());
    }

    public LoginLog findByUserid(Integer userId) {
        String sql = "select * from t_login_log where userid = ?";
        return DbHelp.query(sql,new BeanHandler<>(LoginLog.class),userId);
    }
}
