package com.gys.dao;

import com.gys.entity.Topic;
import com.gys.entity.User;
import com.gys.util.DbHelp;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TopicDao {
    public Integer save(Topic topic) {
        String sql = "insert into t_topic(title,content,userid,nodeid) values(?,?,?,?)";
        return DbHelp.insert(sql,topic.getTitle(),topic.getContent(),topic.getUserid(),topic.getNodeid());
    }

    public Topic findTopicById(Integer topicid) {
        String sql = "select * from t_topic where id = ?";
        return DbHelp.query(sql,new BeanHandler<>(Topic.class),topicid);
    }

    public void update(Topic topic) {
        String sql = "update t_topic set title=?,content=?,clicknum=?,favnum=?,thanksnum=?,replynum=?,lastreplytime=? where id = ?";
        DbHelp.update(sql,topic.getTitle(),topic.getContent(),topic.getClicknum(),topic.getFavnum(),topic.getThanksnum(),topic.getReplynum(),topic.getLastreplytime(),topic.getId());
    }

    /*public List<Topic> findAllTopics() {
        String sql = "select * from t_topic";
        return DbHelp.query(sql,new BeanListHandler<>(Topic.class));
    }
    //每篇帖子对应的发帖人信息没有进行匹配反馈
    public List<Topic> findAllTopicsByNodeid(Integer nodeid) {
        String sql = "select * from t_topic where nodeid = ?";
        return DbHelp.query(sql,new BeanListHandler<>(Topic.class),nodeid);
    }*/
    public List<Topic> findAllTopics() {
        String sql = "select * from t_topic tt,t_user tu where tt.userid = tu.id";
        return DbHelp.query(sql, new AbstractListHandler<Topic>() {
            @Override
            protected Topic handleRow(ResultSet resultSet) throws SQLException {
                Topic topic = new BasicRowProcessor().toBean(resultSet,Topic.class);
                User user = new User();
                user.setAvatar(resultSet.getString("avatar"));
                user.setUsername(resultSet.getString("username"));
                user.setId(resultSet.getInt("id"));
                topic.setUser(user);
                return topic;
            }
        });
    }
    public List<Topic> findAllTopicsByNodeid(Integer nodeid) {
        String sql = "select * from t_topic tt,t_user tu where tt.userid = tu.id and nodeid = ?";
        return DbHelp.query(sql, new AbstractListHandler<Topic>() {
            @Override
            protected Topic handleRow(ResultSet resultSet) throws SQLException {
                Topic topic = new BasicRowProcessor().toBean(resultSet,Topic.class);
                User user = new User();
                user.setAvatar(resultSet.getString("avatar"));
                user.setUsername(resultSet.getString("username"));
                user.setId(resultSet.getInt("id"));
                topic.setUser(user);
                return topic;
            }
        },nodeid);
    }
}
