package com.gys.web;

import com.gys.util.HttpUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/dict")
public class DictServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //req.setCharacterEncoding("UTF-8");
        String query = req.getParameter("query");
        query = new String(query.getBytes("ISO8859-1"),"UTF-8");

        String url = "http://fanyi.youdao.com/openapi.do?keyfrom=kaishengit&key=1587754017&type=data&doctype=xml&version=1.1&q=" + query;
        String result = HttpUtil.sendHttpGetRequestWithString(url);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/xml;charset=UTF-8");

        PrintWriter out = resp.getWriter();
        //返回结果 一大串xml
        out.print(result);

        out.flush();
        out.close();
    }
}
