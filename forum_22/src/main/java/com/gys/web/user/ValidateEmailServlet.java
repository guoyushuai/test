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

@WebServlet("/validate/email")
public class ValidateEmailServlet extends BaseServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        email = StringUtil.ISOtoUTF8(email);

        System.out.println(email);

        User user = userService.findByEmail(email);
        if(user == null) {
            renderText("true",resp);
        } else {
            renderText("false",resp);
        }
    }
}
