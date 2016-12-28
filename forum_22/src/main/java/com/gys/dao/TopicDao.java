package com.gys.dao;

import com.google.common.collect.Lists;
import com.gys.entity.Topic;
import com.gys.entity.User;
import com.gys.util.DbHelp;
import com.gys.util.Page;
import com.gys.util.StringUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
    /*//未设置分页时如下
    public List<Topic> findAllTopics() {
        String sql = "select tu.avatar,tu.username,tt.* from t_topic tt,t_user tu where tt.userid = tu.id order by tt.lastreplytime desc";
        return DbHelp.query(sql, new AbstractListHandler<Topic>() {
            @Override
            protected Topic handleRow(ResultSet resultSet) throws SQLException {
                Topic topic = new BasicRowProcessor().toBean(resultSet,Topic.class);
                User user = new User();
                user.setAvatar(resultSet.getString("avatar"));
                user.setUsername(resultSet.getString("username"));
                *//*user.setId(resultSet.getInt("userid"));*//*
                topic.setUser(user);
                return topic;
            }
        });
    }
    //不设user.id，否则导致查找结果中有两个id列，将user封装到了topic中后，在jsp页面用topic.id取值时造成混乱，会取到user的id
    public List<Topic> findAllTopicsByNodeid(Integer nodeid) {
        String sql = "select * from t_topic tt,t_user tu where tt.userid = tu.id and nodeid = ? order by tt.lastreplytime desc";
        return DbHelp.query(sql, new AbstractListHandler<Topic>() {
            @Override
            protected Topic handleRow(ResultSet resultSet) throws SQLException {
                Topic topic = new BasicRowProcessor().toBean(resultSet,Topic.class);
                User user = new User();
                user.setAvatar(resultSet.getString("avatar"));
                user.setUsername(resultSet.getString("username"));
                *//*user.setId(resultSet.getInt("userid"));*//*
                topic.setUser(user);
                return topic;
            }
        },nodeid);
    }*/

    public int count() {
        String sql = "select count(*) from t_topic";
        return DbHelp.query(sql,new ScalarHandler<Long>()).intValue();
    }

    public int countByNodeid(Integer nodeid) {
        String sql = "select count(*) from t_topic where nodeid = ?";
        return DbHelp.query(sql,new ScalarHandler<Long>(),nodeid).intValue();
    }

    public List<Topic> findAllTopics(Map<String, Object> map) {
        /*SELECT * FROM t_topic tt,t_user tu WHERE tt.userid = tu.id AND nodeid = 1 LIMIT 5,5*/
        String sql = "select tt.*,tu.avatar,tu.username from t_topic tt,t_user tu where tt.userid = tu.id";
        String other = "";

        //传参，dbhelp中最后要求传入不定项参数，本质上是数组，所以用数组传参
        List<Object> list = Lists.newArrayList();

        String nodeid = map.get("nodeid") == null ? null : String.valueOf(map.get("nodeid"));
        /*//不能直接写进if,转换后，null也转换成字符串了，变成了非空
        System.out.println(String.valueOf(map.get("nodeid")));*/

        if (StringUtil.isNotEmpty(nodeid)) {
            //注意问号后空格，容易导致null
            sql += " and nodeid = ? ";
            list.add(nodeid);
        }

        sql += " ORDER BY tt.lastreplytime DESC limit ?,?";
        list.add(map.get("start"));
        list.add(map.get("pagesize"));

        return DbHelp.query(sql, new AbstractListHandler<Topic>() {
            @Override
            protected Topic handleRow(ResultSet resultSet) throws SQLException {
                Topic topic = new BasicRowProcessor().toBean(resultSet,Topic.class);
                User user = new User();
                user.setAvatar(resultSet.getString("avatar"));
                user.setUsername(resultSet.getString("username"));
                //将每条帖子对应的账户封装进topic中
                topic.setUser(user);
                return topic;
            }
        },list.toArray());

    }

    public List<Topic> findAllTopicsByPage(Page<Topic> topicPage) {
        String sql = "select tt.*,tu.avatar,tu.username from t_topic tt,t_user tu where tt.userid = tu.id ORDER BY tt.lastreplytime DESC limit ?,?";
        return DbHelp.query(sql, new AbstractListHandler<Topic>() {
            @Override
            protected Topic handleRow(ResultSet resultSet) throws SQLException {
                Topic topic = new BasicRowProcessor().toBean(resultSet,Topic.class);
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                topic.setUser(user);
                return topic;
            }
        },topicPage.getStart(),topicPage.getPageSize());
    }

    public void deleteTopicById(Integer topicid) {
        String sql = "delete from t_topic where id = ?";
        DbHelp.update(sql,topicid);
    }
}
