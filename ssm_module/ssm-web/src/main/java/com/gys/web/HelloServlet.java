package com.gys.web;

import com.gys.pojo.User;
import com.gys.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();

        User user =  new User();
        user.setId(520);
        user.setUsername("1314");

        userService.saveUser(user);

        /*resp.sendRedirect("hello.jsp");//重定向*/

        req.setAttribute("user",user);
        req.getRequestDispatcher("/WEB-INF/home.jsp").forward(req,resp);//请求转发
    }
}
