package com.gys.entity;

public class Node {

    private Integer id;
    private String nodename;
    private Integer topicnum;

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", nodename='" + nodename + '\'' +
                ", topicnum=" + topicnum +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNodename() {
        return nodename;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename;
    }

    public Integer getTopicnum() {
        return topicnum;
    }

    public void setTopicnum(Integer topicnum) {
        this.topicnum = topicnum;
    }
}
