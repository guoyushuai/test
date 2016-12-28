package com.gys.web.admin;

import com.gys.dto.JsonResult;
import com.gys.entity.Admin;
import com.gys.exception.ServiceException;
import com.gys.service.AdminService;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/login")
public class AdminLoginServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //同user/loginservlet一样,先清除当前用户的session
        req.getSession().removeAttribute("current_admin");
        forward("admin/login",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String adminname = req.getParameter("adminname");
        String password = req.getParameter("password");

        //判断管理员登录地址
        String ip = req.getRemoteAddr();

        AdminService adminService = new AdminService();
        JsonResult jsonResult = new JsonResult();
        try{
            Admin admin = adminService.login(adminname,password,ip);

            //登录成功，放入session中
            HttpSession session = req.getSession();
            session.setAttribute("current_admin",admin);

            jsonResult.setState(JsonResult.SUCCESS);
        } catch (ServiceException e) {
            jsonResult = new JsonResult(e.getMessage());
        }
        renderJson(jsonResult,resp);
    }
}
