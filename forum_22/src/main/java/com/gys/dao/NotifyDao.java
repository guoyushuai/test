package com.gys.dao;

import com.gys.entity.Notify;
import com.gys.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.List;

public class NotifyDao {
    public List<Notify> findNotifyListByUserid(Integer id) {
        String sql = "select * from t_notify where userid = ?";
        return DbHelp.query(sql,new BeanListHandler<>(Notify.class),id);
    }

    public void save(Notify notify) {
        String sql = "insert into t_notify(userid,content) values(?,?)";
        DbHelp.update(sql,notify.getUserid(),notify.getContent());
    }

    public List<Notify> findUnreadNotifyListByUserid(Integer id) {
        String sql = "select * from t_notify where userid = ? and state = 0";
        return DbHelp.query(sql,new BeanListHandler<>(Notify.class),id);
    }

    public int countUnreadnumFromNotifyByUserid(Integer id) {
        String sql = "select count(*) from t_notify where userid = ? and state = 0";
        return  DbHelp.query(sql,new ScalarHandler<Long>(),id).intValue();
    }
}
