package com.gys.service.impl;

import com.gys.mapper.RoleMapper;
import com.gys.mapper.UserMapper;
import com.gys.mapper.UserRoleMapper;
import com.gys.pojo.Role;
import com.gys.pojo.User;
import com.gys.pojo.UserRole;
import com.gys.service.UserService;
import com.gys.util.db.Page;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    //注入配置文件中定义过的盐值
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
    @Transactional
    public void del(Integer id) {
        //删除用户的角色（严谨:先查询该用户是否有角色）
        userRoleMapper.delRoleByUserId(id);
        //删除用户
        userMapper.del(id);
    }

    @Override
    public User findUserById(Integer id) {
        User user = userMapper.findUserById(id);
        return user;
    }

    @Override
    public void edit(User user, Integer[] roleids) {
        //删除用户原有的角色
        userRoleMapper.delRoleByUserId(user.getId());
        //添加新角色
        addUserRole(user, roleids);
        //更新用户
        if (StringUtils.isNotEmpty(user.getPassword())) {
            String password = DigestUtils.md5Hex(user.getPassword() + salt);
            user.setPassword(password);
        }
        userMapper.update(user);
    }

    //重构
    private void addUserRole(User user, Integer[] roleids) {
        if(roleids != null) {
            for(Integer roleid : roleids) {
                Role role = roleMapper.findRoleById(roleid);
                if(role != null) {
                    //创建关系表记录
                    UserRole userRole = new UserRole();
                    userRole.setUserid(user.getId());
                    userRole.setRoleid(role.getId());
                    //使用对象传值，避免多个参数时需要起别名
                    userRoleMapper.saveNewUserRole(userRole);
                }
            }
        }
    }

    @Override
    public List<Role> findAllRoles() {
        List<Role> roleList = roleMapper.findAllRoles();
        return roleList;
    }

    @Override
    @Transactional
    public void saveNewUser(User user, Integer[] roleids) {
        //1、保存用户(需要保存后自动增长的id，供后边存关系表使用。在xml中配置好，不用专门接收自动就塞到本对象中)
        userMapper.save(user);
        //2、保存用户和角色关系
        addUserRole(user, roleids);
    }

    @Override
    public Page<User> findUserByPageNo(Integer pageNo) {
        int total = userMapper.count().intValue();;
        Page<User> page = new Page<>(total,pageNo);

        List<User> userList = userMapper.findByPage(page.getStart(),page.getPageSize());
        page.setItems(userList);
        return page;

    }

    @Override
    public Page<User> findUserByPageNoAndSearchParam(Integer pageNo, String queryName, String queryRole) {
        int total = userMapper.countByParam(queryName,queryRole).intValue();;
        Page<User> page = new Page<>(total,pageNo);

        /*List<User> userList = userMapper.findByPage(page.getStart(),page.getPageSize());*/
        List<User> userList = userMapper.findByPageAndParam(page.getStart(),page.getPageSize(),queryName,queryRole);

        page.setItems(userList);
        return page;
    }
}
