package com.gys.web.topic;

import com.gys.dto.JsonResult;
import com.gys.entity.Node;
import com.gys.entity.Topic;
import com.gys.entity.User;
import com.gys.service.TopicService;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/newTopic")
public class NewTopicServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TopicService topicService = new TopicService();
        List<Node> nodeList = topicService.findAllNodes();
        req.setAttribute("nodeList",nodeList);
        forward("topic/newTopic",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取jsp客户端的发帖数据
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String nodeid = req.getParameter("nodeid");
        System.out.println(nodeid);
        //获取发帖的用户信息(当前登录的用户)
        User user = getCurrentUser(req);
        Integer userid = user.getId();

        //将信息传递到业务层进行包装保存,并获得该帖在数据库中的id
        TopicService topicService = new TopicService();
        Topic topic = topicService.saveNewTopic(title,content,Integer.valueOf(nodeid),userid);

        //将数据传递给客户端，返回给ajax提交的表单进一步跳转到新页面，不是直接请求转发到新页面
        JsonResult result = new JsonResult(topic);
        renderJson(result,resp);

        /*req.setAttribute("topic",topic);
        forward("topic/topicDetail",req,resp);*/

    }
}
