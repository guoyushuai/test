package com.gys.mapper;

import com.gys.pojo.Grade;

import java.util.List;

public interface GradeMapper {

    Grade findById(Integer id);





    Grade findByGradeId(Integer id);


    List<Grade> findAll();

}
