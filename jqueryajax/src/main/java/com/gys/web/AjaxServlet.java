package com.gys.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ajax")
public class AjaxServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain;charset=UTF-8");

        String name = req.getParameter("name");

        PrintWriter out = resp.getWriter();

        if("tom".equals(name)) {
            out.print("不可用");
            System.out.println("账号已被注册");
        } else {
            out.print("可用");
            System.out.println("账号可以使用");
        }

        System.out.print("hello,Ajax doPost" + name);

        out.flush();
        out.close();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("name");
        name = new String(name.getBytes("ISO8859-1"),"UTF-8");

        resp.setHeader("pragma","no-cache");
        resp.setHeader("cache-control","no-cache");
        resp.setHeader("expries","0");

        System.out.println("hello,Ajax doGet" + name);

        PrintWriter out = resp.getWriter();
        out.print("hello");
        out.flush();
        out.close();

    }
}
