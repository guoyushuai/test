package com.gys.web.user;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.gys.entity.User;
import com.gys.service.UserService;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Map;

@WebServlet("/reg")
public class RegServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forward("/user/reg",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        /*//写入过滤器
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");*/

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");


        /*User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        userService.save(user);*/

        /*UserService userService = new UserService();
        userService.save(user);
        //返回JSON键值对对象，1Java中的类，2Map集合
        Map<String,Object> result = Maps.newHashMap();
        result.put("state","success");*/

        //有可能失败
        Map<String,Object> result = Maps.newHashMap();
        try {
            UserService userService = new UserService();
            userService.save(username,password,email,phone);
            result.put("state","success");
        } catch(Exception e) {
            e.printStackTrace();
            result.put("state","error");
            result.put("message","注册失败");
        }


        /*//设置相应MIME头，客户端jquery中的$.ajax根据响应头自动将返回数据转换成相应类型
        //text/plain;text/html;text/xml;application/json
        resp.setContentType("application/json;charset=UTF-8");
        //固定格式，写入父类
        String json = new Gson().toJson(result);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
        out.close();*/

        renderJson(result,resp);
    }
}
