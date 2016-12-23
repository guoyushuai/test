package com.gys.web.topic;

import com.google.common.collect.Maps;
import com.gys.dto.JsonResult;
import com.gys.entity.User;
import com.gys.exception.ServiceException;
import com.gys.service.TopicService;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.gys.dto.JsonResult.SUCCESS;

@WebServlet("/newReply")
public class NewReplyServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String topicid = req.getParameter("topicid");
        String content = req.getParameter("content");
        User user = getCurrentUser(req);
        Integer userid = user.getId();

        TopicService topicService = new TopicService();

        try{
            topicService.saveNewReply(content,Integer.valueOf(topicid),userid);

            /*Map<String,Object> result = Maps.newHashMap();
            result.put("state", "success");
            *//*result.put("data",topicid);//不行，String类型自动识别成message*/
            JsonResult result = new JsonResult();
            result.setState(SUCCESS);
            //回复成功后，刷新页面，需要根据帖子id重新获取帖子的内容
            result.setData(topicid);
            renderJson(result,resp);
        } catch (ServiceException e) {
            /*result.put("state","error");
            result.put("message",e.getMessage());*/
            JsonResult result = new JsonResult(e.getMessage());
            renderJson(result,resp);
        }

    }
}
