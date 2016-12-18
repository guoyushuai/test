package com.gys.web.user;

import com.google.common.collect.Maps;
import com.gys.exception.ServiceException;
import com.gys.service.UserService;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/foundPassword")
public class FoundPasswordServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        forward("user/foundPassword",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取找回方式
        String type = req.getParameter("type");
        //获取输入框的值
        String value = req.getParameter("value");
        
        /*//处理业务逻辑应该放在service层
        if("email".equals(type)) {

        } else if("phone".equals(type)) {

        }*/

        //获取客户端的sessionID,都行
        String sessionId = req.getRequestedSessionId();
        String sessionID = req.getSession().getId();

        /*System.out.println(sessionId);
        System.out.println(sessionID);*/

        Map<String,Object> result = Maps.newHashMap();

        UserService userService = new UserService();
        try {
            //userService.foundPassword(type,value);
            userService.foundPassword(sessionId,type,value);
            result.put("state","success");
        } catch (ServiceException e) {
            result.put("state","error");
            result.put("message",e.getMessage());
        }
        renderJson(result,resp);
    }
}
