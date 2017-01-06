package com.gys.pojo;

public class Student {

    private Integer id;
    private String stuname;
    private Integer gradeid;

    private Grade grade;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", stuname='" + stuname + '\'' +
                ", gradeid=" + gradeid +
                '}';
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public Integer getGradeid() {
        return gradeid;
    }

    public void setGradeid(Integer gradeid) {
        this.gradeid = gradeid;
    }
}
