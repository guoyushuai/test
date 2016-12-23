package com.gys.entity;

import org.joda.time.DateTime;

import java.sql.Timestamp;

public class Topic {

    private Integer id;
    private String title;
    private String content;
    private Timestamp createtime;
    private Integer clicknum;
    private Integer favnum;
    private Integer thanksnum;
    private Integer replynum;
    private Timestamp lastreplytime;
    private Integer userid;
    private Integer nodeid;

    private User user;
    private Node node;

    //在topic对象中封装了一个是否可以编辑的方法
    public boolean isEdit() {
        //获取帖子当前创建时间
        DateTime dt = new DateTime(getCreatetime());

        //创建时间+10分钟在当前时间之后并且没有回复，可修改
        if (dt.plusMinutes(10).isAfterNow() && getReplynum() == 0) {
            return true;
        } else {
            return false;
        }
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Integer getClicknum() {
        return clicknum;
    }

    public void setClicknum(Integer clicknum) {
        this.clicknum = clicknum;
    }

    public Integer getFavnum() {
        return favnum;
    }

    public void setFavnum(Integer favnum) {
        this.favnum = favnum;
    }

    public Integer getThanksnum() {
        return thanksnum;
    }

    public void setThanksnum(Integer thanksnum) {
        this.thanksnum = thanksnum;
    }

    public Integer getReplynum() {
        return replynum;
    }

    public void setReplynum(Integer replynum) {
        this.replynum = replynum;
    }

    public Timestamp getLastreplytime() {
        return lastreplytime;
    }

    public void setLastreplytime(Timestamp lastreplytime) {
        this.lastreplytime = lastreplytime;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getNodeid() {
        return nodeid;
    }

    public void setNodeid(Integer nodeid) {
        this.nodeid = nodeid;
    }
}
