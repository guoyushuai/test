package com.gys.web.admin;

import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/logout")
public class AdminLogoutServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*HttpSession session = req.getSession();
        session.removeAttribute("current_admin");*/
        //servlet->jsp请求转发地址栏不变
        /*forward("admin/login",req,resp);*/

        //安全退出，就是将session强制销毁（无效）
        HttpSession session = req.getSession();
        session.invalidate();
        //servlet->servlet重定向/login
        resp.sendRedirect("/admin/login?state=logout");
    }
}
