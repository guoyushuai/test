package com.gys.web.admin;

import com.gys.service.AdminService;
import com.gys.util.Page;
import com.gys.util.StringUtil;
import com.gys.vo.CountTopicAndReplyByDay;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/home")
public class AdminHomeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String p = req.getParameter("p");
        Integer pageNo = StringUtil.isNumeric(p) ? Integer.valueOf(p) : 1 ;
        AdminService adminService = new AdminService();
        Page<CountTopicAndReplyByDay> page = adminService.countTopicAndReplyByDayAndPageNo(pageNo);
        req.setAttribute("page",page);
        forward("admin/home",req,resp);
    }
}
