package com.gys.web.user;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.gys.dto.JsonResult;
import com.gys.entity.Notify;
import com.gys.entity.User;
import com.gys.service.UserService;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/notify")
public class NotifyServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUser(req);

        UserService userService = new UserService();
        List<Notify> notifyList = userService.findNotifyListByUser(user);

        req.setAttribute("notifyList",notifyList);
        forward("user/notify",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理登录后，导航条上的未读通知条数显示的轮询请求
        User user =getCurrentUser(req);
        UserService userService = new UserService();

        /*//1.1、根据用户id和通知状态state查询未读列表
        List<Notify> notifyList = userService.findUnreadNotifyListByUserAndState(user);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(notifyList.size());
        jsonResult.setState(JsonResult.SUCCESS);
        renderJson(jsonResult,resp);*/
        /*//1.2、根据用户id和通知状态state查询未读总数
        int unreadnum = userService.countUnreadnumFromNotifyByUserAndState(user);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(unreadnum);
        jsonResult.setState(JsonResult.SUCCESS);
        renderJson(jsonResult,resp);*/

        //2、根据guava的collections2.filter过滤未读数据
        List<Notify> notifyList = userService.findNotifyListByUser(user);
        List<Notify> unreadList = Lists.newArrayList(Collections2.filter(notifyList, new Predicate<Notify>() {
            @Override
            public boolean apply(Notify notify) {
                return notify.getState() == 0;
            }
        }));
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(unreadList.size());
        jsonResult.setState(JsonResult.SUCCESS);
        renderJson(jsonResult,resp);
    }
}
