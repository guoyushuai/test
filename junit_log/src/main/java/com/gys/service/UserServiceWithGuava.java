package com.gys.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.gys.dao.UserDao;
import com.gys.entity.User;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class UserServiceWithGuava {

    private static LoadingCache<String,User> loadingCache = CacheBuilder
            .newBuilder()
            .maximumSize(100)//LRU淘汰机制
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .build(new CacheLoader<String, User>() {//若缓存中没有则执行该方法，到数据库中进行查询
                @Override
                public User load(String key) throws Exception {
                    UserDao userDao = new UserDao();
                    return userDao.findById(Integer.valueOf(key));
                }
            });

    /*public User findById(Integer id) {
        User user = loadingCache.getUnchecked(id.toString());//key
        return user;
    }*/

    //分界线`````````````

    private UserDao userDao = new UserDao();

    private static Cache<String,User> cache = CacheBuilder
            .newBuilder()
            .maximumSize(100)
            .expireAfterAccess(10,TimeUnit.MINUTES)
            .build();

    public User findById(Integer id) {
        User user = null;
        try {
            user = cache.get("user:" + id, new Callable<User>() {
                @Override
                public User call() throws Exception {
                    return userDao.findById(id);
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;
    }


}
