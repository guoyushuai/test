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
        forward("/user/login",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        //获取登录客户端的ip地址
        String ip = req.getRemoteAddr();

        Map<String,Object> result = Maps.newHashMap();
        try {
            UserService userService = new UserService();
            User user = userService.login(username, password, ip);

            //用户登录成功，需要将用户信息放入session中，
            HttpSession session = req.getSession();
            session.setAttribute("current_user",user);

            result.put("state","success");
        } catch(ServiceException e) {
            result.put("state","error");
            result.put("message",e.getMessage());
        }

        renderJson(result,resp);

    }
}
