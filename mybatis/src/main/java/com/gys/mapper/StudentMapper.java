package com.gys.mapper;

import com.gys.pojo.Student;

import java.util.List;

public interface StudentMapper {

    Student findById(Integer id);





    List<Student> findAll();


    Student findByGradeId();

}
