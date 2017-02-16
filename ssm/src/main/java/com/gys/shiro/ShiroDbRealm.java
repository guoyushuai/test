package com.gys.shiro;

import com.gys.mapper.RoleMapper;
import com.gys.mapper.UserMapper;
import com.gys.pojo.Role;
import com.gys.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShiroDbRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;


    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前登录的用户//SecurityUtils.getSubject().getPrincipal();
        User user = (User) principalCollection.getPrimaryPrincipal();

        //查询该用户所拥有的角色（所属部门）
        List<Role> roleList = roleMapper.findByUserId(user.getId());
        if(!roleList.isEmpty()) {
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            for (Role role:roleList) {
                //将用户拥有的角色名称添加到权限认证中
                //与jsp中<shiro:hasRole name={role_name}></shiro>标签相对应
                authorizationInfo.addRole(role.getRoleName());
            }
            return authorizationInfo;
        }
        return null;
    }

    /**
     * 登录认证
     * @param authenticationToken 所属的类是UsernamePasswordToken类的父类
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        //当前登录用户的用户名
        String username = usernamePasswordToken.getUsername();
        //根据用户名到数据库中查找对应账户
        User user = userMapper.findByUsername(username);
        if(user != null) {
            //第一个：(Object principal)往session中放的东西
            //第二个：用户的password
            //第三个：父类的getName,拿着getname获得密码与传入的password进行比较
            return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
        }
        //用户名都不存在，返回null会触发login中的异常，登录失败
        return null;
    }
}
