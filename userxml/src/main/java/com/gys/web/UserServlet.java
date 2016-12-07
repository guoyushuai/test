package com.gys.web;

import com.gys.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet("/users.xml")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(101,"tom","北京");
        User user2 = new User(102,"jack","上海");
        User user3 =new User(103,"rose","杭州");

        List<User> userList = Arrays.asList(user,user2,user3);

        resp.setCharacterEncoding("UTF-8");//设置响应的字符编码
        resp.setContentType("text/xml;charset=UTF-8");//设置响应头MEMI type
        //resp.setContentType("text/plain;charset=UTF-8");

        PrintWriter out = resp.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        out.print("<users>");
        for(User u : userList) {
            out.print("<user id=\"" + u.getId() + "\">");//!!!!!!!注意转义，否则不识别
            out.print("<name>" + u.getUsername() + "</name>");
            out.print("<address>" + u.getAddress() + "</address>");
            out.print("</user>");
        }
        out.print("</users>");

        out.flush();
        out.close();

    }
}
