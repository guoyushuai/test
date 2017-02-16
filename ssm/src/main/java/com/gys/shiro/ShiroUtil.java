package com.gys.shiro;

import com.gys.pojo.User;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil {

    /**
     * 获取当前登录用户
     * @return
     */
    public static User getCurrentUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 获取当前登录用户的账户名称
     * @return
     */
    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    /**
     * 获取当前登录用户的id
     * @return
     */
    public static Integer getCurrentUserId() {
        return getCurrentUser().getId();
    }


}
