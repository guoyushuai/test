package com.gys.web.admin;

import com.gys.dto.JsonResult;
import com.gys.entity.Node;
import com.gys.exception.ServiceException;
import com.gys.service.AdminService;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/node")
public class AdminNodeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AdminService adminService = new AdminService();
        List<Node> nodeList = adminService.findAllNodes();
        req.setAttribute("nodeList",nodeList);
        forward("admin/node",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if("new".equals(action)) {
            newnode(req,resp);
        } else if ("edit".equals(action)) {
            editnode(req,resp);
        } else if ("delete".equals(action)) {
            deletenode(req,resp);
        }
    }

    private void deletenode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String nodeid = req.getParameter("nodeid");
        AdminService adminService = new AdminService();
        JsonResult jsonResult = new JsonResult();
        try {
            adminService.deleteNodeByid(nodeid);
            jsonResult.setState(JsonResult.SUCCESS);
        } catch (ServiceException e) {
            jsonResult.setMessage(e.getMessage());
        }
        renderJson(jsonResult,resp);
    }

    private void editnode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String newnodename = req.getParameter("newnodename");
        String nodeid = req.getParameter("nodeid");
        AdminService adminService = new AdminService();
        JsonResult jsonResult = new JsonResult();
        try {
            adminService.editNode(nodeid,newnodename);
            jsonResult.setState(JsonResult.SUCCESS);
        } catch (ServiceException e) {
            jsonResult.setMessage(e.getMessage());
        }
        renderJson(jsonResult,resp);
    }

    private void newnode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String newnodename = req.getParameter("newnodename");
        AdminService adminService = new AdminService();
        JsonResult jsonResult = new JsonResult();
        try {
            adminService.addNewNode(newnodename);
            jsonResult.setState(JsonResult.SUCCESS);
        } catch (ServiceException e) {
            jsonResult.setMessage(e.getMessage());
        }
        renderJson(jsonResult,resp);
    }
}
