package com.gys.web.admin;

import com.gys.dto.JsonResult;
import com.gys.entity.User;
import com.gys.exception.ServiceException;
import com.gys.service.AdminService;
import com.gys.util.Page;
import com.gys.util.StringUtil;
import com.gys.vo.AdminUserVo;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/user")
public class AdminUserServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String p = req.getParameter("p");
        Integer pageNo = StringUtil.isNumeric(p) ? Integer.valueOf(p) : 1 ;

        AdminService adminService = new AdminService();
        Page<AdminUserVo> page = adminService.findUserList(pageNo);

        req.setAttribute("page",page);
        forward("admin/user",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = req.getParameter("userid");
        AdminService adminService = new AdminService();

        JsonResult jsonResult = new JsonResult();
        try {
            adminService.updateUserStateByUserid(userid);

            //将用户账户禁用后，怎么让用户立马掉线
            /*req.getSession().removeAttribute("current_user");*/

            jsonResult.setState(JsonResult.SUCCESS);
        } catch (ServiceException e) {
            jsonResult.setMessage(e.getMessage());
        }
        renderJson(jsonResult,resp);
    }
}
