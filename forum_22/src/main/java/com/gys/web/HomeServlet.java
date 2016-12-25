package com.gys.web;

import com.gys.entity.Node;
import com.gys.entity.Topic;
import com.gys.service.TopicService;
import com.gys.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TopicService topicService = new TopicService();

        //查找数据库中的节点列表，传递到客户端
        List<Node> nodeList = topicService.findAllNodes();
        req.setAttribute("nodeList",nodeList);

        //获取客户端传递的nodeid，如果有的话
        String nodeid = req.getParameter("nodeid");
        if (StringUtil.isEmpty(nodeid) || !StringUtil.isNumeric(nodeid)) {
            //如果客户端传回的nodeid为空或者为不能转换为数字，直接跳转到主页，并且不再执行服务端后面的程序
            List<Topic> topicList = topicService.findAllTopics();
            req.setAttribute("topicList",topicList);
            forward("index",req,resp);
            return;
        }
        //根据nodeid查找对应节点的所有帖子返回给客户端,还需要将发帖人信息一起反馈
        List<Topic> topicList = topicService.findAllTopicsByNodeid(Integer.valueOf(nodeid));
        req.setAttribute("topicList",topicList);

        //调用父类BaseServlet中定义好的方法forward,简化请求转发操作
        forward("index",req,resp);
    }
}
