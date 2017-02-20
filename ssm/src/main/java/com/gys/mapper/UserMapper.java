package com.gys.mapper;

import com.gys.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    List<User> findAllUsers();

    void save(User user);

    void del(Integer id);

    User findUserById(Integer id);

    void update(User user);

    Long count();

    List<User> findByPage(@Param("start") int start,
                          @Param("pageSize") int pageSize);

    Long countByParam(@Param("queryName") String queryName,
                      @Param("queryRole") String queryRole);

    List<User> findByPageAndParam(@Param("start") int start,
                                  @Param("pageSize") int pageSize,
                                  @Param("queryName") String queryName,
                                  @Param("queryRole") String queryRole);

    User findByUsername(String username);
}
