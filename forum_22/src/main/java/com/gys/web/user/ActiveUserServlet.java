package com.gys.web.user;

import com.google.common.collect.Maps;
import com.gys.exception.ServiceException;
import com.gys.service.UserService;
import com.gys.util.StringUtil;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/user/active")
public class ActiveUserServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("_");
        if(StringUtil.isEmpty(uuid)) {
            //token不存在,恶意
            resp.sendError(404);
        } else {
            //servlet中不处理业务逻辑，只负责接收转发

            /*//存储激活结果的键值对
            Map<String,Object> result = Maps.newHashMap();*/
            //！错误 不是往jquery中的$ajax中传递数据，简单的往jsp传

            //业务层各种抛出异常，这里处理下（try catch）以便给客户端合理的响应

            UserService userService = new UserService();
            try {
                userService.activeUser(uuid);

                forward("user/active_success",req,resp);
            } catch (ServiceException e) {
                //token存在，但可能过期(超时或已点击过)或者无效（恶意）

                //往jsp中传值
                req.setAttribute("message",e.getMessage());
                forward("user/active_error",req,resp);
            }
        }
    }
}
