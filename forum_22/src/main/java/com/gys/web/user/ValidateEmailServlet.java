package com.gys.web.user;

import com.gys.entity.User;
import com.gys.service.UserService;
import com.gys.util.StringUtil;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/validate/email")
public class ValidateEmailServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        email = StringUtil.ISOtoUTF8(email);

        /*HttpSession session = req.getSession();
        User current_user = (User) session.getAttribute("current_user");*/
        //经常要用到session中的当前用户，写进父类一劳永逸，
        //而且session.getAttribute("current_user")可能为null，不能直接强制转换

        //1进入注册页面（删除session）强制用户退出,不合适。会导致正常注册的用户进入页面session被重置，不合适
        //2给不同的请求加上无意义的参数，以此来判断不同类型的用户（setting的validate的remote:"/validate/email?type=reset"）
        String type = req.getParameter("type");

        if(StringUtil.isNotEmpty(type) && type.equals("reset")) {

            //已登陆成功用户直接请求注册账号页面会导致该用户的邮箱依然可用于再次注册
            User currentUser = getCurrentUser(req);
            if(currentUser != null) {
                if(currentUser.getEmail().equals(email)) {
                    renderText("true",resp);
                    return;//让方法强制结束
                }
            }

        }

        UserService userService = new UserService();

        User user = userService.findByEmail(email);
        if(user == null) {
            renderText("true",resp);
        } else {
            renderText("false",resp);
        }
    }
}
