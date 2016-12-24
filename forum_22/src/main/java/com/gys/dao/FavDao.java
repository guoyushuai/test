package com.gys.dao;

import com.gys.entity.Fav;
import com.gys.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;

public class FavDao {


    public Fav findByid(Integer userid, Integer topicid) {
        String sql = "select * from t_fav where userid = ? and topicid = ?";
        return DbHelp.query(sql,new BeanHandler<>(Fav.class),userid,topicid);
    }

    public void favTopic(Fav fav) {
        String sql = "insert into t_fav (userid,topicid)values (?,?)";
        DbHelp.update(sql,fav.getUserid(),fav.getTopicid());
    }

    public void unfavTopic(Fav fav) {
        String sql = "delete from t_fav where userid = ? and topicid = ?";
        DbHelp.update(sql,fav.getUserid(),fav.getTopicid());
    }
}
