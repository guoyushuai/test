package com.gys.mapper;

import com.gys.pojo.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdminMapper {

    Admin findById(Integer id);
    List<Admin> findAll();

    void save(Admin admin);
    void update(Admin admin);
    void delete(Integer id);


    //多个参数
    Admin findByUsernameAndPassword1(String param1,String param2);
    Admin findByUsernameAndPassword2(@Param("name")String param1,@Param("pwd")String param2);
    Admin findByUsernameAndPassword3(Admin admin);
    Admin findByUsernameAndPassword4(Map<String,Object> param);


    //动态SQL
    Admin findByParam1(Map<String,Object> param);
    Admin findByParam2(Map<String,Object> param);
    Admin findByParam3(Map<String,Object> param);

    List<Admin> findByIds(List<Integer> idList);
}
