package com.gys.web.user;

import com.google.common.collect.Maps;
import com.gys.entity.User;
import com.gys.exception.ServiceException;
import com.gys.service.UserService;
import com.gys.util.StringUtil;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/resetPassword")
public class ResetPasswordServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*//重置谁的密码！！
        forward("user/resetPassword",req,resp);*/

        //token就是url中的uuid
        String token = req.getParameter("token");

        if(StringUtil.isEmpty(token)) {
            resp.sendError(404);
        } else {

            UserService userService = new UserService();
            try {
                User user = userService.findUserByToken(token);
                //找到对应账户才能去相应页面改密码

                //往jsp页面传值,不用map
                req.setAttribute("user",user);
                req.setAttribute("token",token);
                forward("user/resetPassword",req,resp);
            } catch (ServiceException e) {
                req.setAttribute("message",e.getMessage());
                forward("user/reset_error",req,resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取重置密码页面提交的数据
        String id = req.getParameter("id");
        String token = req.getParameter("token");
        String password = req.getParameter("password");

        Map<String,String> result = Maps.newHashMap();
        UserService userService = new UserService();
        try {
            userService.resetPassword(id,password,token);
            result.put("state","success");
        } catch (ServiceException e) {
            result.put("state","error");
            result.put("message",e.getMessage());
        }
        renderJson(result,resp);
    }
}

