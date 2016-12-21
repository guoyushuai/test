package com.gys.dao;

import com.gys.entity.Node;
import com.gys.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

public class NodeDao {

    public List<Node> findAllNodes() {
        String sql = "select * from t_node";
        return DbHelp.query(sql,new BeanListHandler<>(Node.class));
    }

    public Node findById(Integer nodeid) {
        String sql = "select * from t_node where id = ?";
        return DbHelp.query(sql,new BeanHandler<>(Node.class),nodeid);

    }
}
