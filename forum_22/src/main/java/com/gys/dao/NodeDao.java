package com.gys.dao;

import com.gys.entity.Node;
import com.gys.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.List;

public class NodeDao {

    public List<Node> findAllNodes() {
        String sql = "select * from t_node";
        return DbHelp.query(sql,new BeanListHandler<>(Node.class));
    }

    public Node findNodeById(Integer nodeid) {
        String sql = "select * from t_node where id = ?";
        return DbHelp.query(sql,new BeanHandler<>(Node.class),nodeid);
    }

    public void update(Node node) {
        String sql = "update t_node set nodename=?,topicnum=? where id = ?";
        DbHelp.update(sql,node.getNodename(),node.getTopicnum(),node.getId());
    }

    public int sum() {
        String sql = "select sum(topicnum) from t_node";
        return DbHelp.query(sql,new ScalarHandler<>());
    }

    public void addNewNode(String newnodename) {
        String sql = "insert into t_node(nodename) values(?)";
        DbHelp.update(sql,newnodename);
    }

    public void deleteNodeById(Integer nodeid) {
        String sql = "delete from t_node where id = ?";
        DbHelp.update(sql,nodeid);
    }

    public Node findNodeByNodename(String newnodename) {
        String sql = "select * from t_node where nodename = ?";
        return DbHelp.query(sql,new BeanHandler<>(Node.class),newnodename);
    }
}
