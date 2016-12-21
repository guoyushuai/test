package com.gys.dao;

import com.gys.entity.Topic;
import com.gys.util.DbHelp;

public class TopicDao {
    public Integer save(Topic topic) {
        String sql = "insert into t_topic(title,content,userid,nodeid) values(?,?,?,?)";
        return DbHelp.insert(sql,topic.getTitle(),topic.getContent(),topic.getUserid(),topic.getNodeid());
    }
}
