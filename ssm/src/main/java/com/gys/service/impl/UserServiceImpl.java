package com.gys.service.impl;

import com.gys.mapper.UserMapper;
import com.gys.pojo.User;
import com.gys.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Value("${password.salt}")
    private String salt;


    @Override
    public List<User> findAllUsers() {
        List<User> userList = userMapper.findAllUsers();
        return userList;
    }

    @Override
    public void save(User user) {
        String password = DigestUtils.md5Hex(user.getPassword() + salt);
        user.setPassword(password);
        userMapper.save(user);
    }

    @Override
    public void del(Integer id) {
        userMapper.del(id);
    }

    @Override
    public User findUserById(Integer id) {
        User user = userMapper.findUserById(id);
        return user;
    }

    @Override
    public void edit(User user) {
        if (StringUtils.isNotEmpty(user.getPassword())) {
            String password = DigestUtils.md5Hex(user.getPassword() + salt);
            user.setPassword(password);
        }
        userMapper.update(user);
    }
}
