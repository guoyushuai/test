package com.gys.dao;

import com.gys.entity.Reply;
import com.gys.entity.User;
import com.gys.util.DbHelp;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReplyDao {


    public Integer save(Reply reply) {
        String sql = "insert into t_reply(content,userid,topicid) values(?,?,?)";
        /*DbHelp.update(sql,reply.getContent(),reply.getUserid(),reply.getTopicid());*/
        return DbHelp.insert(sql,reply.getContent(),reply.getUserid(),reply.getTopicid());
    }

    public Reply findReplyById(Integer replyid) {
        String sql = "select * from t_reply where id = ?";
        return DbHelp.query(sql,new BeanHandler<>(Reply.class),replyid);
    }

    public List<Reply> findReplyListByTopicId(Integer topicid) {
        //两表联查，等值连接，内连接，左连接，右连接
        String sql = "SELECT tu.id,tu.username,tu.avatar,tr.* FROM t_reply tr,t_user tu WHERE tr.userid = tu.id AND topicid = ? order by tr.createtime";
        return DbHelp.query(sql, new AbstractListHandler<Reply>() {
            @Override
            protected Reply handleRow(ResultSet resultSet) throws SQLException {
                //将整个结果集中对应Reply对象的信息赋值给reply
                Reply reply = new BasicRowProcessor().toBean(resultSet,Reply.class);
                User user = new User();
                //如果tu.id，查询结果中有两个id列，取值时一旦用到id值会造成混乱
                /*user.setId(resultSet.getInt("userid"));*/
                user.setUsername(resultSet.getString("username"));
                user.setAvatar(resultSet.getString("avatar"));
                //将user对象封装到relpy中（一条回复只对应一个用户）
                reply.setUser(user);
                return reply;
            }
        },topicid);
    }

    public void deleteReplyByTopicid(Integer topicid) {
        String sql = "delete from t_reply where topicid = ?";
        DbHelp.update(sql,topicid);
    }
}
