package com.gys.web.topic;

import com.google.common.collect.Maps;
import com.gys.dto.JsonResult;
import com.gys.entity.Node;
import com.gys.entity.Topic;
import com.gys.entity.User;
import com.gys.exception.ServiceException;
import com.gys.service.TopicService;
import com.gys.util.Config;
import com.gys.web.BaseServlet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

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

        String ak = Config.get("qiniu.ak");
        String sk = Config.get("qiniu.sk");
        String bucketName = Config.get("qiniu.bucketname");
        String domain = Config.get("qiniu.domain");
        Auth auth = Auth.create(ak,sk);
        //simditor要求的json格式
        /*{
            "success": true/false,
                "msg": "error message", # optional
            "file_path": "[real file path]"
        }*/
        //自定义响应内容（模板）符合simditor格式要求
        String returnBody = "{\"success\":true,\"file_path\":\""+ domain +"${key}\"}";
        StringMap map = new StringMap();
        map.put("returnBody",returnBody);
        String token = auth.uploadToken(bucketName,null,3600,map);
        req.setAttribute("token",token);

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

        //将信息传递到业务层进行包装保存,并获得该帖在数据库中的id(便于之后根据id跳转到相应的帖子详情页面)
        TopicService topicService = new TopicService();
        JsonResult result = null;
        try{
            Topic topic = topicService.saveNewTopic(title,content,Integer.valueOf(nodeid),userid);
            result = new JsonResult(topic);
        } catch (ServiceException e) {
            result = new JsonResult(e.getMessage());
        }

        //将数据传递给客户端，返回给ajax提交中的success进一步跳转到新页面，不是直接请求转发到新页面
        /*Map<String,Object> result = Maps.newHashMap();
        result.put("state","success");
        result.put("data",topic);
        renderJson(result,resp);*/

        renderJson(result,resp);

        /*req.setAttribute("topic",topic);
        forward("topic/topicDetail",req,resp);*/

    }
}
