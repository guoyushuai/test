package com.gys.dao;

import com.gys.entity.Topic;
import com.gys.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;

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
}
