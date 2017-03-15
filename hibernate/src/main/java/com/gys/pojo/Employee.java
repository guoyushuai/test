package com.gys.pojo;

public class Employee {

    private Integer id;
    private String empname;
    private Dept dept;//在多的一端存一的外键。以面向对象的方式体现


    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }
}
