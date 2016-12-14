package com.gys.service;

import com.gys.dao.UserDao;
import com.gys.entity.User;
import com.gys.util.EhCacheUtil;

import java.util.List;

public class UserService {

    //常量
    private static final String USER_EHCACHE_NAME = "user";

    private UserDao userDao = new UserDao();

    private EhCacheUtil ehCacheUtil = new EhCacheUtil();

    public User findById(Integer id) {
        //User类需要实现可序列化接口
        User user = (User) ehCacheUtil.get(USER_EHCACHE_NAME,"user:" + id);
        if(user == null) {
            user = userDao.findById(id);
            ehCacheUtil.set(USER_EHCACHE_NAME,"user:" + id,user);
        }
        return user;
    }

    public List<User> findAll() {
        List<User> userList = (List<User>) ehCacheUtil.get(USER_EHCACHE_NAME,"userList");
        if(userList == null) {
            userList =userDao.findAll();
            ehCacheUtil.set(USER_EHCACHE_NAME,"userList",userList);
        }
        return userList;
    }

    public void save(User user) {
        userDao.save(user);
        //添加数据，为了保持数据的一致性，需要清缓存.（增加了数据，数据集合肯定发生了变化需要清掉）
        ehCacheUtil.remove(USER_EHCACHE_NAME,"userList");
    }

    public void del(Integer id) {
        userDao.del(id);
        //删除数据，为了保持数据的一致性，需要清缓存。（删除了数据，数据集合肯定发生了变化，需要清掉缓存，该条数据的缓存也需要清掉）
        ehCacheUtil.remove(USER_EHCACHE_NAME,"user:" + id);
        ehCacheUtil.remove(USER_EHCACHE_NAME,"userList");
    }

}
