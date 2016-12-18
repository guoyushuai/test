package com.gys.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.gys.dao.LoginLogDao;
import com.gys.dao.UserDao;
import com.gys.entity.LoginLog;
import com.gys.entity.User;
import com.gys.exception.ServiceException;
import com.gys.util.Config;
import com.gys.util.EmailUtil;
import com.gys.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserService {

    private UserDao userDao = new UserDao();

    Logger logger = LoggerFactory.getLogger(UserService.class);

    //发送激活邮件的缓存
    private static Cache<String,String> activeCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .build();

    //发送改密邮件的缓存
    private static Cache<String,String> resetCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(30,TimeUnit.MINUTES)
            .build();

    //限制用户发送频率的缓存
    private static Cache<String,String> limitCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(60,TimeUnit.SECONDS)
            .build();

    /**
     * 查询数据库，判断用户输入的账号名称是否可用
     */
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
        //业务需要，某些数据库中没有被占用的名字在现实实际中是不可使用的，俗称保留字
        //先查找保留表，再读取用户信息表,使用validateUsername(String username)
    }

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
            //User user = userDao.findByUsername(username);

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
     * 新用户注册，并将其基本资料存入数据库中,并向其注册邮箱发送激活邮件
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

                //用户点击链接后跳转到的页面地址
                String url = "http://bbs.kaishengit.com/user/active?_=" + uuid;

                //邮件服务崩溃，手动获取连接
                System.out.println(url);

                String html = "<h3>亲爱的:"+ username +"<h3>请点击<a href="+ url +">该链接</a>激活您的账号。" ;

                EmailUtil.sendHtmlEmail("账号激活邮件",html,email);

            }
        });
        th.start();

        //发送邮件可能失败，需要给提示没收到？点击选择重新发送
        //或者在用户登录时显示账号未激活，需要激活，重新发送邮件?
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

    /**
     * 用户登录
     */
    public User login(String username, String password, String ip) {
        User user = userDao.findByUsername(username);

        String psd = DigestUtils.md5Hex(password + Config.get("user.password.salt"));

        if(user != null && user.getPassword().equals(psd)) {
            //账号密码正确，查看用户状态
            if(user.getState().equals(User.STATE_ACTIVE)) {
                //正常，将登陆信息记录到数据库中
                LoginLog login = new LoginLog();
                login.setIp(ip);
                login.setUserid(user.getId());

                LoginLogDao loginLogDao = new LoginLogDao();
                loginLogDao.save(login);

                //同样要将登录信息记录到系统日志中
                logger.info("{}在{}登录了系统",username,ip);

                //用户登录后，需要将用户信息放在session中，用以确保用户进行操作时，服务端能辨识是同一个用户
                return user;
            } else if(user.getState().equals(User.STATE_UNACTIVE)) {
                throw new ServiceException("账号还未激活");
            } else {
                throw new ServiceException("账号已被禁用");
            }
        } else {
            throw new ServiceException("账号或密码错误");
        }

    }

    /**
     *向忘记密码的用户发送找回密码的邮件
     */
    //public void foundPassword(String type, String value) {
    public void foundPassword(String sessionId,String type, String value) {
        //对用户点击提交发送服务进行限制，防止发送频率过快，普通的按钮倒计时low
        //若同一用户用不同邮箱注册多个账号，用同一浏览器申请重置不同账号密码，依然会提示频率过快
        //同一用户用不同浏览器重置同一账号密码，这个限制请求频率的缓存没用
        if(limitCache.getIfPresent(sessionId) == null) {
            if("email".equals(type)) {
                User user = userDao.findByEmail(value);
                if(user != null) {
                    //向已注册用户发送邮件
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //一一对应
                            String uuid = UUID.randomUUID().toString();

                            //缓存设置时的键值对都是字符串String类型
                            resetCache.put(uuid,user.getUsername());

                            //用户点击链接后跳转到的界面地址，参数token=uuid
                            String url = "http://bbs.kaishengit.com/resetPassword?token=" + uuid;

                            //邮件服务崩溃，手动获取连接
                            System.out.println(url);

                            String html = "<h3>亲爱的:"+ user.getUsername() +"</h3>请点击<a href="+ url +">该链接</a>重置您的密码，链接有效期30分钟。";
                            //30分钟内多次点击该连接都有效，即使重置过密码，依然有效，可以再次重置

                            EmailUtil.sendHtmlEmail("密码重置邮件",html,value);
                        }
                    });
                    th.start();

                }
                /*//非本站注册账户，不发送邮件
                else {
                    throw new ServiceException("无对应邮箱");
                    //不安全，用户会试哪个邮箱注册了，什么都不做比较好
                }*/
            } else if("phone".equals(type)) {
                // TODO: 2016/12/18
            }
            //将客户端的sessionid放入相应的缓存中
            limitCache.put(sessionId,"xxx");
        } else {
            throw new ServiceException("发送频率过快，请稍后再试！");
        }

    }

    /**
     *根据连接找到需要重置密码的用户
     */
    public User findUserByToken(String token) {

        //token-username-user放入缓存的时候键值都是String类型
        //token就是url中的参数，等于uuid
        String username = resetCache.getIfPresent(token);

        if(StringUtil.isEmpty(username)) {
            throw new ServiceException("token无效或已过期");
        } else {
            User user = userDao.findByUsername(username);
            if(user == null) {
                throw new ServiceException("未找到对应账号");
            } else {
                //暂时不能删除token
                return user;
            }
        }
    }

    /**
     *重置密码
     */
    public void resetPassword(String id, String password, String token) {

        //再次判断token,防止用户拿到url,for循环{post请求,id从头到尾把password都修改掉}
        //if(resetCache.getIfPresent(token) == null)
        String username = resetCache.getIfPresent(token);
        if(StringUtil.isEmpty(username)) {
            throw new ServiceException("token无效或已过期");
        } else {
            //根据id找到对应账户，未找到对应账号的抛异常在上一步已经解决了
            User user = userDao.findById(Integer.valueOf(id));

            //将密码加盐加密更新数据库中对应账户的信息
            user.setPassword(DigestUtils.md5Hex(password+Config.get("user.password.salt")));
            userDao.update(user);
            //记录日志
            logger.info("{}修改了密码",user.getUsername());
            resetCache.invalidate(token);
        }
    }
}
