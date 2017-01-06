package com.gys.pojo;

import java.util.List;

public class Grade {

    private Integer id;
    private String gradename;

    private List<Student> studentList;

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", gradename='" + gradename + '\'' +
                '}';
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }
}
