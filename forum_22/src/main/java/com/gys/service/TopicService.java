package com.gys.service;

import com.google.common.collect.Maps;
import com.gys.dao.*;
import com.gys.entity.*;
import com.gys.exception.ServiceException;
import com.gys.util.Page;
import com.gys.util.StringUtil;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class TopicService {

    private NodeDao nodeDao = new NodeDao();
    private TopicDao topicDao = new TopicDao();
    private UserDao userDao = new UserDao();
    private ReplyDao replyDao = new ReplyDao();
    private NotifyDao notifyDao = new NotifyDao();

    private FavDao favDao = new FavDao();

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

        //数据库中一个表中只能有一个timestamp的默认值可以设置成current_timestamp
        //将最后回复时间暂时设置为当前时间（发帖时间）
        topic.setLastreplytime(new Timestamp(new DateTime().getMillis()));

        //将封装好的数据保存到数据库并获得该帖在数据库中topic表的id
        Integer topicid = topicDao.save(topic);
        //再次封装（将从数据库返回的id封装到topic对象中）
        topic.setId(topicid);

        //根据nodeid查找node表中对应的node对象，
        Node node = nodeDao.findNodeById(nodeid);
        //更新表中相应节点的topicnum
        /*Integer a = null;
        int b = a;*/
        //数据库node表中，topicnum列默认值为null，从数据库中查找出来后附给对象相应属性topicnum值为null,
        // public Integer getTopicnum(){return topicnum}时将null值附给了Integer包装类,
        //将Integer类型的null值自动拆箱赋值给int时引起NullPointException
        if(node != null) {
            int oldtopicnum = node.getTopicnum();
            node.setTopicnum(oldtopicnum + 1);
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

    /**
     * 保存回复
     * 发送通知
     */
    public void saveNewReply(String content, Integer topicid, Integer userid) {

        //将获得的数据封装到reply对象中
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setTopicid(topicid);
        reply.setUserid(userid);

        //将封装的数据保存到数据库对应的表中(dbhelp.update),并获得该回复在数据库中reply表的id(dbhelp,insert)
        Integer replyid = replyDao.save(reply);

        //根据topicid查找topic表中对应的topic对象，并更新表中的回复数量replynum和最后回复时间lastreplytime
        Topic topic = topicDao.findTopicById(topicid);
        //回复时根据id查找对应帖子，触发帖子点击数+1的事件，转移到TopicDetailServlet

        if(topic != null) {
            Reply getReply = replyDao.findReplyById(replyid);
            if(getReply != null) {
                topic.setReplynum(topic.getReplynum() + 1);
                topic.setLastreplytime(getReply.getCreatetime());
                topicDao.update(topic);
            } else {
                throw new ServiceException("回复不存在或已被删除");
            }

            //新增回复的同时，判断是否要加入消息通知(自己回复自己不发通知不加入通知表)
            if(!topic.getUserid().equals(userid)) {
                Notify notify = new Notify();
                notify.setUserid(topic.getUserid());//发帖人
                notify.setContent("您发布的主题<a href=\"/topicDetail?topicid=" + topicid + "\"><" + topic.getTitle() + "></a>有了新的回复");
                //notify.setState(Notify.STATE_UNREAD);//数据库中默认为0 未读
                notifyDao.save(notify);
            }

        } else {
            throw new ServiceException("帖子不存在或已被删除");
        }

    }

    /**
     * 根据topicid查找该帖对应的所有回复
     */
    public List<Reply> findAllReplys(Integer topicid) {

        List<Reply> replyList = replyDao.findReplyListByTopicId(topicid);
        //每条回复中包含回复表reply中的content，userid,topicid,
        //(根据userid查找用户表user中的username，useravatar)*回复数
        //需要两表联查，不同于显示帖子详情的三表单查
        return replyList;
    }

    /**
     * 修改帖子后根据topicid更新数据库中帖子信息
     */
    public void updateTopicById(Integer topicid, String title, String content, Integer newnodeid) {

        if(StringUtil.isNumeric(topicid.toString())) {
            Topic topic = topicDao.findTopicById(topicid);
            if (topic != null) {
                if(topic.isEdit()) {

                    Integer oldnodeid = topic.getNodeid();
                    if (!newnodeid.equals(oldnodeid)) {
                        Node oldnode = nodeDao.findNodeById(oldnodeid);
                        oldnode.setTopicnum(oldnode.getTopicnum() - 1);
                        nodeDao.update(oldnode);
                        Node newnode = nodeDao.findNodeById(newnodeid);
                        newnode.setTopicnum(newnode.getTopicnum() + 1);
                        nodeDao.update(newnode);
                    }
                    /*Node node = nodeDao.findNodeById(oldnodeid);
                    if (!nodeid.equals(node.getId())) {
                        Integer oldtopicnum = node.getTopicnum();
                        node.setTopicnum(oldtopicnum - 1);
                    }*/

                    topic.setTitle(title);
                    topic.setContent(content);
                    topic.setNodeid(newnodeid);

                    topicDao.update(topic);
                } else {
                    throw new ServiceException("该帖已不可编辑");
                }
            } else {
                throw new ServiceException("帖子不存在或已被删除");
            }
        } else {
            throw new ServiceException("参数异常");
        }
    }

    /**
     *更新帖子信息，（点击 次数）
     */
    public void update(Topic topic) {
        topicDao.update(topic);
    }

    /**
     * 查询用户对帖子的收藏状态
     */
    public Fav findFavById(Integer userid, Integer topicid) {
        return favDao.findByid(userid,topicid);
    }

    /**
     *加入收藏
     */
    public void favTopic(Integer userid, Integer topicid) {
        Fav fav = new Fav();
        fav.setUserid(userid);
        fav.setTopicid(topicid);
        favDao.favTopic(fav);

        //更新数据库中topic表的favnum列的值
        Topic topic = topicDao.findTopicById(topicid);
        topic.setFavnum(topic.getFavnum() + 1);
        topicDao.update(topic);

    }

    /**
     * 取消收藏
     */
    public void unfavTopic(Integer userid, Integer topicid) {
        Fav fav = new Fav();
        fav.setUserid(userid);
        fav.setTopicid(topicid);
        favDao.unfavTopic(fav);

        //更新数据库中topic表的favnum列的值
        Topic topic = topicDao.findTopicById(topicid);
        topic.setFavnum(topic.getFavnum() - 1);
        topicDao.update(topic);
    }

    /*//未设置分页，未指定nodeid时，查找所有的帖子
    public List<Topic> findAllTopics() {
        return topicDao.findAllTopics();
    }
    //未设置分页，指定nodeid时，查找对应节点所有的帖子
    public List<Topic> findAllTopicsByNodeid(Integer nodeid) {
        return topicDao.findAllTopicsByNodeid(nodeid);
    }*/

    /**
     * 根据nodeid,pageno查找对应数据
     */
    public Page<Topic> findAllTopicsByNodeid(String nodeid, Integer pageno) {
        //page中需要totals,pageno连个参数（其余参数page中内置）

        //根据nodeid查找对应节点的帖子总数totals
        int totals = 0;

        //nodeid不为空且不能转换为数字在servlet中已经处理
        //nodeid为空去主页
        if(StringUtil.isEmpty(nodeid)){
            totals = topicDao.count();
            /*count = nodeDao.sum();*/
        } else if (!exitNode(Integer.valueOf(nodeid))){
            //传递来的nodeid不为空，能转换为数字，但是在node表中不存在,让nodeid强制等于null,
            totals = topicDao.count();
            nodeid = null;
        } else {
            totals = topicDao.countByNodeid(Integer.valueOf(nodeid));
        }

        /*参数不够，找不到结果
        Page<Topic> page = topicDao.findAllTopics(totals,pageno);*/

        Page<Topic> topicPage = new Page<>(totals,pageno);
        Map<String,Object> map = Maps.newHashMap();
        map.put("nodeid",nodeid);
        /*select * from t_topic where nodeid = ? limit start,pagesize;*/
        map.put("start",topicPage.getStart());
        map.put("pagesize",topicPage.getPageSize());

        //获取页面对应的所有帖子
        List<Topic> topicList = topicDao.findAllTopics(map);
        //将帖子集合封装进page对象中
        topicPage.setItems(topicList);

        return topicPage;
    }

    //判断url中获取的可转换为数字的nodeid在node表中是否存在
    public boolean exitNode(Integer nodeid) {
        List<Node> nodeList = nodeDao.findAllNodes();
        for(int i = 0;i < nodeList.size();i++) {
            Node node = nodeList.get(i);
            if (nodeid == node.getId()) {
                return true;
            }
        }
        return false;
    }
}
