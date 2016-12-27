package com.gys.web.user;

import com.gys.dto.JsonResult;
import com.gys.service.UserService;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/notifyRead")
public class NotifyReadServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ids =req.getParameter("ids");
        UserService userService = new UserService();
        userService.updateNotifyState(ids);
        /*JsonResult jsonResult = new JsonResult();
        jsonResult.setState(JsonResult.SUCCESS);*/
        //jsp中不需要返回参数返回一个状态就好，
        //（表单验证中的remote同样不需要返回参数,但限制使用rendertext时返回字符串类型的"true/false"代表成功/失败）
        renderText("success",resp);
    }
}
