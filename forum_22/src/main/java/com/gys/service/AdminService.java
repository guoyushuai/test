package com.gys.service;

import com.google.common.collect.Maps;
import com.gys.dao.AdminDao;
import com.gys.dao.NodeDao;
import com.gys.dao.ReplyDao;
import com.gys.dao.TopicDao;
import com.gys.entity.Admin;
import com.gys.entity.Node;
import com.gys.entity.Reply;
import com.gys.entity.Topic;
import com.gys.exception.ServiceException;
import com.gys.util.Config;
import com.gys.util.Page;
import com.gys.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class AdminService {

    Logger logger = LoggerFactory.getLogger(AdminService.class);

    AdminDao adminDao = new AdminDao();
    TopicDao topicDao = new TopicDao();
    ReplyDao replyDao = new ReplyDao();
    NodeDao nodeDao = new NodeDao();



    /**
     * 管理员登陆
     */
    public Admin login(String adminname, String password, String ip) {
        Admin admin = adminDao.findByAdminname(adminname);
        password = DigestUtils.md5Hex(password + Config.get("user.password.salt"));
        if (admin != null && admin.getPassword().equals(password)) {
            logger.info("管理员{}在{}登录了管理系统",adminname,ip);
            return admin;
        } else {
            throw new ServiceException("账号或密码错误");
        }
    }

    /**
     * 根据页码查找该页应该显示的帖子
     */
    public Page findAllTopicsByPageNo(Integer pageNo) {
        /*page对象需要总条数totals,当前页码pageNo来计算该页应该显示的数据*/
        /*sql中需要起始行号start,每夜显示的数据量pageSize来进行查询*/

        int totals = topicDao.count();

        //page的构造方法，传入totals,pageNo,自动计算start,并获得设置好的pageSize
        Page<Topic> topicPage = new Page<>(totals,pageNo);

        List<Topic> topicList = topicDao.findAllTopicsByPage(topicPage);
        //或者使用topicService中的findAllTopics(map),map中可以传多个参数，page中没有的参数

        topicPage.setItems(topicList);
        return topicPage;
    }

    /**
     * 根据id删除对应帖子
     */
    public void deleteTopicById(String topicid) {
        if (StringUtil.isNotEmpty(topicid) && StringUtil.isNumeric(topicid)) {
            Topic topic = topicDao.findTopicById(Integer.valueOf(topicid));
            if (topic != null) {
                //有外键约束，需要先删回复
                replyDao.deleteReplyByTopicid(Integer.valueOf(topicid));
                //更新节点下主题数量
                Node node = nodeDao.findNodeById(topic.getNodeid());
                node.setTopicnum(node.getTopicnum() - 1);
                nodeDao.update(node);
                //删除帖子
                topicDao.deleteTopicById(Integer.valueOf(topicid));
            } else {
                throw new ServiceException("该帖子不存在");
            }
        } else {
            throw new ServiceException("参数异常");
        }
    }
}
