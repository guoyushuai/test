package com.gys.web.admin;

import com.gys.dto.JsonResult;
import com.gys.entity.Admin;
import com.gys.entity.Node;
import com.gys.entity.Topic;
import com.gys.exception.ServiceException;
import com.gys.service.AdminService;
import com.gys.util.Page;
import com.gys.util.StringUtil;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/topic")
public class AdminTopicServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String p = req.getParameter("p");
        //p为空或非数字就去第一页
        Integer pageNo = StringUtil.isNumeric(p) ? Integer.valueOf(p) : 1;

        AdminService adminService = new AdminService();
        //topicService中findAllTopics(nodeid,pageNo)
        Page<Topic> page = adminService.findAllTopicsByPageNo(pageNo);

        //page中封装了当页的topic，无法封装每个topic都拥有的nodeList
        List<Node> nodeList = adminService.fianAllNodes();

        req.setAttribute("page",page);
        req.setAttribute("nodeList",nodeList);
        forward("admin/topic",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if(action.equals("delete")) {
            deleteTopic(req,resp);
        } else if(action.equals("update")) {
            updateTopic(req,resp);
        }
    }

    /**
     * 修改帖子所属节点
     */
    private void updateTopic(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String topicid = req.getParameter("topicid");
        String nodeid = req.getParameter("nodeid");

        AdminService adminService = new AdminService();
        JsonResult jsonResult = new JsonResult();

        try {
            adminService.updateTopicById(topicid,nodeid);
            jsonResult.setState(JsonResult.SUCCESS);
        } catch (ServiceException e) {
            jsonResult.setMessage(e.getMessage());
        }
        renderJson(jsonResult,resp);
    }

    /**
     * 删除帖子
     */
    private void deleteTopic(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String topicid = req.getParameter("topicid");
        AdminService adminService = new AdminService();

        JsonResult jsonResult = new JsonResult();
        try {
            adminService.deleteTopicById(topicid);
            jsonResult.setState(JsonResult.SUCCESS);
        } catch (ServiceException e) {
            jsonResult.setMessage(e.getMessage());
        }

        renderJson(jsonResult,resp);
    }
}
