package com.gys.web.user;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.gys.entity.User;
import com.gys.exception.ServiceException;
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
        UserService userService = new UserService();
        userService.save(user);*/

        //返回JSON键值对对象，1Java中的类，2Map集合
        Map<String, Object> result = Maps.newHashMap();

        //!!!可能失败,这个思路怎么来的？？？底层抛出了异常：数据库挂了，sql拼错
        UserService userService = new UserService();
        //    try {
        userService.save(username, password, email, phone);
        result.put("state", "success");
    //  } catch(Exception e) {
        //catch了所有异常(catch掉了DbHelp中抛的DataAccessException异常，却并没有处理)
        //应该只catch业务异常,其他异常是程序员应该做的事，不是用户的错

    //  } catch(ServiceException) {
        //相应的service层并没有throw抛出ServiceException业务异常，不用try catch

    //        e.printStackTrace();
    //        result.put("state","error");
    //        result.put("message","注册失败");
            //不能result.put("message",e.getMessage());jsp中接收后在javaScript中alert弹框出来了，会将一大串错误详细信息打印出来
            //alert只弹业务异常ServiceException
    //    }

        renderJson(result,resp);

        /*//设置相应MIME头，客户端jquery中的$.ajax根据响应头自动将返回数据转换成相应类型
        //text/plain;text/html;text/xml;application/json
        resp.setContentType("application/json;charset=UTF-8");
        //固定格式，写入父类
        String json = new Gson().toJson(result);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
        out.close();*/

    }
}
