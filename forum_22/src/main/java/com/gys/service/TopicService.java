package com.gys.service;

import com.gys.dao.NodeDao;
import com.gys.dao.TopicDao;
import com.gys.entity.Node;
import com.gys.entity.Topic;

import java.util.List;

public class TopicService {

    private NodeDao nodeDao = new NodeDao();
    private TopicDao topicDao = new TopicDao();

    /**
     * 查找所有节点
     */
    public List<Node> findAllNodes() {

        return nodeDao.findAllNodes();

    }

    public Topic saveNewTopic(String title, String content, Integer nodeid, Integer userid) {
        //将客户端数据封装到topic对象中
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setUserid(userid);
        topic.setNodeid(nodeid);

        //将封装好的数据保存到数据库并获得该帖在数据库中的id
        Integer topicid = topicDao.save(topic);

        //再次封装（将从数据库中获得的id封装到topic对象中）
        topic.setId(topicid);

        //传回到serclet中，进而返回客户端在页面上现实
        return topic;
    }
}
