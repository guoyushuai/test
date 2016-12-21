package com.gys.web.topic;

import com.gys.dto.JsonResult;
import com.gys.entity.Topic;
import com.gys.exception.ServiceException;
import com.gys.service.TopicService;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/topicDetail")
public class TopicDetailServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取url中传递过来的的topicid
        String topicid = req.getParameter("topicid");

        //根据topicid查找对应的帖子
        TopicService topicService = new TopicService();
        JsonResult result = null;
        //将封装好的数据请求转发到客户端
        try {
            Topic topic = topicService.findTopicById(Integer.valueOf(topicid));
            req.setAttribute("topic",topic);
            forward("topic/topicDetail",req,resp);
        } catch (ServiceException e) {
            req.setAttribute("message",e.getMessage());
            forward("topic/topic_404",req,resp);
        }

        //给ajaxq请求返回数据用以下方法，将封装好的数据发送到客户端
        /*try {
            Topic topic = topicService.findTopicById(Integer.valueOf(topicid));
            result = new JsonResult(topic);
            renderJson(result,resp);
        } catch (ServiceException e) {
            result = new JsonResult(e.getMessage());
            renderJson(result,resp);
        }*/
    }
}
