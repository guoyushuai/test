package com.gys.web.topic;

import com.gys.dto.JsonResult;
import com.gys.entity.Node;
import com.gys.entity.Topic;
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
import java.util.ServiceConfigurationError;

@WebServlet("/topicEdit")
public class TopicEditServlet extends BaseServlet {
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

        String topicid = req.getParameter("topicid");
        TopicService topicService = new TopicService();
        Topic topic = topicService.findTopicById(Integer.valueOf(topicid));

        List<Node> nodeList = topicService.findAllNodes();

        req.setAttribute("topic",topic);
        req.setAttribute("nodeList",nodeList);

        /*//请求转发，地址栏不变，带上参数进步到jsp页面
        forward("topic/topicEdit?topicid="+topicid,req,resp);*/


        //不传参数，怎么知道的要修改的是对应的那个帖子？地址栏不变，在a连接get请求跳转到该页面时，地址栏已指定
        forward("topic/topicEdit",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String topicid = req.getParameter("topicid");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String nodeid = req.getParameter("nodeid");

        /*查找node，更新相应的topicnum*/

        TopicService topicService = new TopicService();
        JsonResult result = null;
        try {
            topicService.updateTopicById(Integer.valueOf(topicid),title,content,Integer.valueOf(nodeid));
            result = new JsonResult();
            result.setState(JsonResult.SUCCESS);
            //修改成功后，帖子跳转到详情页需要根据topic重新获取帖子的内容
            result.setData(topicid);
        } catch (ServiceException e) {
            result = new JsonResult(e.getMessage());
        }
        renderJson(result,resp);
    }
}
