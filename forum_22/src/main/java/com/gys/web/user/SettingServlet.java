package com.gys.web.user;

import com.google.common.collect.Maps;
import com.gys.entity.User;
import com.gys.exception.ServiceException;
import com.gys.service.UserService;
import com.gys.util.Config;
import com.gys.web.BaseServlet;
import com.qiniu.util.Auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/setting")
public class SettingServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获得配置文件中设置好的账号的ak,sk,bucketname
        String ak = Config.get("qiniu.ak");
        String sk = Config.get("qiniu.sk");
        String bucketname = Config.get("qiniu.bucketname");

        //密钥设置
        Auth auth = Auth.create(ak,sk);
        //计算token
        String token = auth.uploadToken(bucketname);

        //将token传递给客户端用于后续上传
        req.setAttribute("token",token);

        forward("user/setting",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if("profile".equals(action)) {
            updateProfile(req,resp);
        } else if ("password".equals(action)) {
            updatePassword(req,resp);
        } else if ("avatar".equals(action)) {
            updateAvatar(req,resp);
        }
    }

    /**
     * 修改头像
     */
    private void updateAvatar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String avatarname = req.getParameter("avatarname");

        User user = getCurrentUser(req);
        UserService userService = new UserService();
        userService.updateAvatar(user,avatarname);

        Map<String,Object> result = Maps.newHashMap();
        result.put("state","success");
        renderJson(result,resp);

    }

    /**
     * 修改密码
     */
    private void updatePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取表单中输入的密码,input的name属性，注意大小写
        String oldpassword = req.getParameter("oldpassword");
        String newpassword = req.getParameter("newpassword");

        //获取session中登录的当前账户current_user
        User user =getCurrentUser(req);

        //将数据传输到业务层进行处理，并对处理结果进行判断并封装
        Map<String,Object> result = Maps.newHashMap();
        UserService userService = new UserService();
        try {
            userService.updatePassword(user, oldpassword, newpassword);
            result.put("state","success");
        } catch (ServiceException e) {
            result.put("state","error");
            result.put("message",e.getMessage());
        }
        //将封装结果返回给客户端
        renderJson(result,resp);
    }

    /**
     * 修改邮箱
     */
    private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取表单中用户输入的email地址
        String email = req.getParameter("email");
        //获取session中登录的当前账户
        User user = getCurrentUser(req);
        //把数据传递到业务层进行业务逻辑处理，因为业务层没有抛出业务异常，所以这里不用try catch
        UserService userService = new UserService();
        userService.updateEmail(user,email);
        //对结果进行封装，并传到客户端
        Map<String,Object> result = Maps.newHashMap();
        result.put("state","success");
        renderJson(result,resp);
    }
}
