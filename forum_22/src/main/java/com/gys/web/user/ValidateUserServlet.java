package com.gys.web.user;

import com.gys.entity.User;
import com.gys.service.UserService;
import com.gys.util.StringUtil;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/validate/user")
public class ValidateUserServlet extends BaseServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");

        /*//通过url进行传值，处理中文
        username = new String(str.getBytes("ISO8859-1"),"UTF-8");
        //可写到父类中简化操作
        username = ISOtoUTF(username);*/

        //字符转换与servlet没有太大关系，可写成通用的工具类
        username = StringUtil.ISOtoUTF8(username);

        //User user = userService.validateUsername(username);
        boolean result = userService.validateUsername(username);

        //输出写到父类中进行渲染简化
        //romote要求服务端返回字符串类型的（true|false）
        if(result) {
            renderText("true",resp);
        } else {
            renderText("false",resp);
        }
    }
}
