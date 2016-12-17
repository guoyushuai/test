package com.gys.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.gys.dao.UserDao;
import com.gys.entity.User;
import com.gys.exception.ServiceException;
import com.gys.util.Config;
import com.gys.util.EmailUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserService {

    private UserDao userDao = new UserDao();

    //激活邮件的缓存
    private static Cache<String,String> activeCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .build();

    /**
     * 查询数据库，判断用户输入的账号名称是否可用
     */
    /*public User findByUsername(String username) {
        return userDao.findByUsername(username);
        //业务需要，某些数据库中没有被占用的名字在现实实际中是不可使用的，俗称保留字
        //先查找保留表，再读取用户信息表
    }*/

    /**
     * 判断用户输入的账号名称是否是保留名，并查询数据库该名称是否已被他人注册
     */
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

    /**
     * 查询数据库，判断用户输入的电子邮件是否已被注册
     */
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }


    /**
     * 新用户注册，并将其基本资料存入数据库中
     */
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

        //向注册用户填写的邮箱发送激活邮件

        //发送邮件过程缓慢，不宜让用户过多等待发邮件，注册成功时直接跳转登录等其他操作，让子线程去做发邮件的事
        //创建线程的两种方式之一，1继承Thread类，重写run方法；2实现Runnable接口，并实现run方法
        //匿名局部内部类
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                /*1时效性(缓存)2与用户一一对应(缓存)3防破解(随机数编码)*/
                String uuid = UUID.randomUUID().toString();
                activeCache.put(uuid,username);

                //用户点击链接后跳转到的页面Servlet
                String url = "http://bbs.kaishengit.com/user/active?_=" + uuid;

                //邮件服务崩溃，手动获取连接
                System.out.println(url);

                String html = "<h3>亲爱的:"+ username +"<h3>请点击<a href="+ url +">该链接</a>激活您的账号。" ;

                EmailUtil.sendHtmlEmail("账号激活邮件",html,email);

            }
        });
        th.start();

        //发送邮件可能失败，需要给提示没收到？点击选择重新发送
        //或者在用户登录时显示账号未激活，需要激活，重新发送邮件
        // TODO: 2016/12/17

    }

    /**
     * 根据token激活对应的账户
     */
    public void activeUser(String uuid) {

        //这个就是发送邮件时,添加到链接后面的uuid(token)
        String username = activeCache.getIfPresent(uuid);
        if(username == null) {
            //缓存过期，或者被伪造.不再是数据连接异常，属于业务异常
            throw new ServiceException("链接无效或已过期");
        } else {
            //根据名字找到对应账户
            User user = userDao.findByUsername(username);
            if(user == null) {
                throw new ServiceException("账户不存在，可能已被注销");
            } else {
                //找到对应账号，修改用户状态为激活保存到数据库中
                user.setState(User.STATE_ACTIVE);
                userDao.update(user);
                //激活完毕，需要将链接无效化，删除缓存token,避免用户多次点击
                activeCache.invalidate(uuid);
            }
        }

    }
}