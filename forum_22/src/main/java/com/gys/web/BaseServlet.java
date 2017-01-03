package com.gys.web;

import com.google.gson.Gson;
import com.gys.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
/*import java.io.UnsupportedEncodingException;*/

public class BaseServlet extends HttpServlet {

    //通用请求转发 顺便对路径进行转换简化
    public void forward(String path, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + path + ".jsp").forward(req,resp);
    }

    public void renderText(String str, HttpServletResponse resp) throws IOException {
        /*//响应字符编码，可写入过滤器
        resp.setCharacterEncoding("UTF-8");*/
        resp.setContentType("text/plain;charset=UTF-8");

        //romote要求服务端返回字符串类型的（true|false）,也就是str只有这两个值
        PrintWriter out = resp.getWriter();
        out.print(str);
        out.flush();
        out.close();
    }

    public void renderJson(Object obj,HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String json = new Gson().toJson(obj);

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }

    public User getCurrentUser(HttpServletRequest request) {

        //要想获得httpsession对象，需要request对象，所以需要传入参数req
        HttpSession session = request.getSession();
        //session中可能没有值，不能直接强制转换
        if(session.getAttribute("current_user") != null) {
            return (User) session.getAttribute("current_user");
        } else {
            return null;
        }

    }

    public String getIP(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if("0:0:0:0:0:0:0:1".equals(ip)) {
            return "127.0.0.1";
        } else {
            return ip;
        }

    }


}
