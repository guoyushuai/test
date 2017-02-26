package com.gys.pojo;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class User implements Serializable {

    private Integer id;
    private String username;
    private String password;
    private List<Role> roleList;
    private String mobile;//与微信中保持一致

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }


    public String getRoleNames() {
        //google-guava将角色role中的viewname抽取出来，显示页面只需要viewNames
        List<String> viewNames = Lists.newArrayList(Collections2.transform(getRoleList(), new Function<Role, String>() {
            @Override
            public String apply(Role role) {
                return role.getViewName();
            }
        }));

        //viewNames.toString();list集合的toString()方法含有中括号[]，输出不好看
        //将集合转换成字符串,多部门之间以空格间隔
        StringBuilder sb = new StringBuilder();
        for (String viewName:viewNames) {
            sb.append(viewName).append(" ");
        }
        return sb.toString();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
