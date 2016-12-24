package com.gys.web.topic;

import com.gys.dto.JsonResult;
import com.gys.entity.Fav;
import com.gys.entity.Reply;
import com.gys.entity.Topic;
import com.gys.entity.User;
import com.gys.exception.ServiceException;
import com.gys.service.TopicService;
import com.gys.util.StringUtil;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/topicDetail")
public class TopicDetailServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取url中传递过来的的topicid
        String topicid = req.getParameter("topicid");

        TopicService topicService = new TopicService();
        JsonResult result = null;
        //将封装好的数据请求转发到客户端
        try {
            //根据topicid查找对应的帖子
            Topic topic = topicService.findTopicById(Integer.valueOf(topicid));

            //更新数据库topic表中clicknum字段，如果写在service层，每调用一次findTopicById就会刷新一次点击次数不科学
            //回复时刷新了帖子页面，get请求同样也会触发该事件//
            topic.setClicknum(topic.getClicknum() + 1);
            topicService.update(topic);
            req.setAttribute("topic",topic);

            //获得用户是否收藏该帖子
            User user = getCurrentUser(req);
            if(user != null && StringUtil.isNumeric(topicid)) {
                Fav fav = topicService.findFavById(user.getId(),Integer.valueOf(topicid));
                //查找有结果，代表收藏，数据库中没有查找到，结果为空代表没有收藏
                req.setAttribute("fav",fav);
            }

            //根据topicid获取帖子对应的回复列表
            List<Reply> replyList = topicService.findAllReplys(Integer.valueOf(topicid));
            req.setAttribute("replyList",replyList);

            forward("topic/topicDetail",req,resp);
        } catch (ServiceException e) {
            System.out.println(e);
            //直接刷新页面时如果帖子不存在直接显示404页面
            req.setAttribute("message",e.getMessage());
            forward("topic/topic_404",req,resp);

            //Ajax中post请求时，如果帖子不存在，弹框并回到主页
            JsonResult jsonResult = new JsonResult(e.getMessage());
            renderJson(jsonResult,resp);

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
