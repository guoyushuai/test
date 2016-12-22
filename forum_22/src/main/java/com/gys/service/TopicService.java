package com.gys.service;

import com.gys.dao.NodeDao;
import com.gys.dao.TopicDao;
import com.gys.dao.UserDao;
import com.gys.entity.Node;
import com.gys.entity.Topic;
import com.gys.entity.User;
import com.gys.exception.ServiceException;
import com.gys.util.StringUtil;

import java.util.List;

public class TopicService {

    private NodeDao nodeDao = new NodeDao();
    private TopicDao topicDao = new TopicDao();
    private UserDao userDao = new UserDao();

    /**
     * 查找所有节点
     */
    public List<Node> findAllNodes() {

        return nodeDao.findAllNodes();

    }

    /**
     * 保存帖子
     */
    public Topic saveNewTopic(String title, String content, Integer nodeid, Integer userid) {
        //将客户端数据封装到topic对象中
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setUserid(userid);
        topic.setNodeid(nodeid);

        //将封装好的数据保存到数据库并获得该帖在数据库中topic表的id
        Integer topicid = topicDao.save(topic);
        //再次封装（将从数据库返回的id封装到topic对象中）
        topic.setId(topicid);

        //根据nodeid查找node表中对应的node对象，并更新表中相应节点的topicnum
        Node node = nodeDao.findNodeById(nodeid);

        /*Integer a = null;
        int b = a;*/

        if(node != null) {
            //数据库node表中，topicnum列默认值为null，从数据库中查找出来后附给对象相应属性topicnum值为null,
            // public Integer getTopicnum(){return topicnum}时将null值附给了Integer包装类,
            //将Integer类型的null值自动拆箱赋值给int时引起NullPointException
            int num = node.getTopicnum();
            node.setTopicnum(num + 1);
            nodeDao.update(node);
        } else {
            throw new ServiceException("节点不存在");
        }

        //传回到serclet中，进而返回客户端在页面上现实
        return topic;
    }


    /**
     * 根据id查找topic对象
     * @param topicid
     */
    public Topic findTopicById(Integer topicid) {
        if (StringUtil.isNumeric(topicid.toString())) {
            Topic topic = topicDao.findTopicById(topicid);
            if (topic != null) {
                //根据topic对象中的对应的id查找相应的对象
                User user = userDao.findById(topic.getUserid());
                Node node = nodeDao.findNodeById(topic.getNodeid());
                //同时只能给客户端返回一个对象，需要将获取到的user ，node对象重新封装到topic对象中（数据库中的数据并没有变化，只是将对象丰富了）
                topic.setUser(user);
                topic.setNode(node);

                return topic;
            } else {
                throw new ServiceException("帖子不存在或已被删除");
            }
        } else {
            throw new ServiceException("参数错误");
        }


    }
}
