package com.gys.service;

import com.gys.dao.UserDao;
import com.gys.entity.User;
import com.gys.util.Config;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;
import java.util.List;

public class UserService {

    private UserDao userDao = new UserDao();

    /**
     * 判断用户输入的账号名称是否可用
     * @param username
     * @return
     */
    /*public User validateUsername(String username) {
        return userDao.findByUsername(username);
        //业务需要，某些数据库中没有被占用的名字在现实实际中是不可使用的，俗称保留字
        //先查找保留表，再读取用户信息表
    }*/
    public Boolean validateUsername(String username) {
        //1保留名制成表放在数据库中2保留名写在配置文件中
        //多次读取配置文件性能不高，将读取配置文件写成工具类，只执行一次，其他类从工具类中读取数据
        //获取保留账户名称字符串
        String name = Config.get("reserved.username");
        //以逗号进行截取存入集合中
        List<String> nameList = Arrays.asList(name.split(","));
        //判断注册用户输入的账户名是否在集合中存在
        if(nameList.contains(username)) {
            return false;
        } else {
            //return userDao.findByUsername(username) == null ? true : false;
            return userDao.findByUsername(username) == null;
        }

    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public void save(String username,String password,String email,String phone) {
        User user = new User();
        user.setUsername(username);
        //user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        //需要将默认值也保存进来
        user.setAvatar(User.DEFAULT_AVATAR);
        user.setState(User.STATE_UNACTIVE);
        //对密码加盐并进行加密
        user.setPassword(DigestUtils.md5Hex(password+Config.get("user.password.salt")));

        userDao.save(user);
    }
}
