package com.gys.web.user;

import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        session.invalidate();

        //servlet->servlet重定向
        resp.sendRedirect("/login?state=logout");

        //servlet->jsp请求转发地址栏不变
        //req.setAttribute("message","您已安全退出");
        //forward("user/login",req,resp);
    }
}
