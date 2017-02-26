package com.gys.dto.wx;

import java.util.List;

public class User {


    /**
     * userid : zhangsan
     * name : 张三
     * department : [1,2]//通讯录-组织架构-修改部门-部门ID
     * mobile : 15913215421
     */

    private String userid;
    private String name;
    private String mobile;//手机。邮箱。微信号不能同时为零
    private List<Integer> department;//与微信中部门名称部门ID保持一致

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<Integer> getDepartment() {
        return department;
    }

    public void setDepartment(List<Integer> department) {
        this.department = department;
    }
}
