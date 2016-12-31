package com.gys.service;

import com.google.common.collect.Lists;
import com.gys.dao.*;
import com.gys.entity.*;
import com.gys.exception.ServiceException;
import com.gys.util.Config;
import com.gys.util.Page;
import com.gys.util.StringUtil;
import com.gys.vo.AdminHomeVo;
import com.gys.vo.AdminUserVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class AdminService {

    Logger logger = LoggerFactory.getLogger(AdminService.class);

    AdminDao adminDao = new AdminDao();
    TopicDao topicDao = new TopicDao();
    ReplyDao replyDao = new ReplyDao();
    NodeDao nodeDao = new NodeDao();
    UserDao userDao = new UserDao();
    LoginLogDao loginLogDao = new LoginLogDao();

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

    /**
     * 查找所有节点
     */
    public List<Node> fianAllNodes() {
        return nodeDao.findAllNodes();
    }

    /**
     * 修改帖子对应的节点
     */
    public void updateTopicById(String topicid, String nodeid) {
        if(StringUtil.isNumeric(topicid) && StringUtil.isNumeric(nodeid)) {
            Topic topic = topicDao.findTopicById(Integer.valueOf(topicid));
            if(topic != null) {
                if(topic.getNodeid() != Integer.valueOf(nodeid)) {
                    //更新数据库中相应节点的topicnum
                    Node oldnode = nodeDao.findNodeById(topic.getNodeid());
                    oldnode.setTopicnum(oldnode.getTopicnum() - 1);
                    nodeDao.update(oldnode);

                    Node newnode = nodeDao.findNodeById(Integer.valueOf(nodeid));
                    newnode.setTopicnum(newnode.getTopicnum() + 1);
                    nodeDao.update(newnode);

                    System.out.print("newnodeid:"+nodeid);
                    System.out.print("oldnodeid:"+topic.getNodeid());
                    //更新帖子的节点，并修改数据库中topic表的相应内容,应该放在节点更新的下面，否则这里设置了重新topic的nodeid,根据节点nodeid更新节点的topicnum时获取的节点对象是同一个
                    topic.setNodeid(Integer.valueOf(nodeid));
                    topicDao.update(topic);
                }
                //帖子的新旧节点nodeid一致什么都不做
            } else {
                throw new ServiceException("帖子不存在或已被删除");
            }
        } else {
            throw new ServiceException("参数错误");
        }
    }

    /**
     * 根据日期统计当天的帖子和回复的数量 ，并分页返回相应页码的数据
     */
    public Page<AdminHomeVo> countTopicAndReplyByDayAndPageNo(Integer pageNo) {
        //page中需要totals,pageno两个参数（其余参数(eg:start pagesize)page中内置）
        int totals = topicDao.countByDay();
        Page<AdminHomeVo> homeVoPage = new Page<>(totals,pageNo);
        //sql中需要start pagesize两个参数
        List<AdminHomeVo> topicAndReplyByDayList = topicDao.countTopicAndReplyByDay(homeVoPage);

        homeVoPage.setItems(topicAndReplyByDayList);
        return homeVoPage;
    }

    /**
     * 根据页码查找对应页码的用户数据
     */
    public Page<AdminUserVo> findUserList(Integer pageNo) {
        //用户状态为非未激活的用户
        int totals = userDao.countUsersByState();
        Page<AdminUserVo> userVoPage = new Page<>(totals,pageNo);
        List<User> userList = userDao.findAllUsersByPage(userVoPage);

        List<AdminUserVo> userVoList = Lists.newArrayList();
        for (User user : userList) {
            AdminUserVo adminUserVo = userDao.findAdminUserVoByUserid(user.getId());
            userVoList.add(adminUserVo);
        }
        userVoPage.setItems(userVoList);
        return userVoPage;



        //未分页如下：
        /*//loginlog表只能按照userid一个一个查（多对一），取倒序登录时间的第一个
        List<AdminUserVo> userVoList = Lists.newArrayList();
        List<User> userList = userDao.finAllUsers();
        for (User user : userList) {
            LoginLog loginLog = loginLogDao.findByUserid(user.getId());
            AdminUserVo adminUserVo = new AdminUserVo();
            adminUserVo.setXXX();

            userVoList.add(adminUserVo);
        }
        return userVoList;*/
    }

    /**
     * 根据userid修改账户的状态
     */
    public void updateUserStateByUserid(String userid) {
        if(StringUtil.isNotEmpty(userid) && StringUtil.isNumeric(userid)) {
            User user = userDao.findById(Integer.valueOf(userid));
            if(user != null) {
                int state = user.getState() == 1 ? 2 : 1;
                user.setState(state);
                userDao.update(user);

            } else {
                throw new ServiceException("账户不存在");
            }
        } else {
            throw new ServiceException("参数异常");
        }
    }
}
