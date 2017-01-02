package com.gys.web.user;

import com.google.common.collect.Maps;
import com.gys.entity.User;
import com.gys.exception.ServiceException;
import com.gys.service.UserService;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //用户登录成功后将当前用户存到了session中，用户修改密码后，重定向到登录页面，需要将当前用户从session中删除，否则导航条的状态依然是有有用户登录的状态
        //req.getSession().invalidate();杀伤力太大，session无效化，服务器需要重新给客户端一个sessionId
        req.getSession().removeAttribute("current_user");

        forward("/user/login",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        //获取登录客户端的ip地址
        String ip = req.getRemoteAddr();

        Map<String,Object> result = Maps.newHashMap();

        UserService userService = new UserService();
        try {
            User user = userService.login(username, password, ip);

            //用户登录成功，需要将用户信息放入session中，
            HttpSession session = req.getSession();
            session.setAttribute("current_user",user);

            result.put("state","success");
        } catch(ServiceException e) {
            if("账号还未激活".equals(e.getMessage())) {
                User activeUser = userService.findByUsername(username);
                result.put("email",activeUser.getEmail());
            }
            result.put("state","error");
            result.put("message",e.getMessage());

        }

        renderJson(result,resp);

    }
}
