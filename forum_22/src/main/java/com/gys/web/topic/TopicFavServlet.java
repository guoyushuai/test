package com.gys.web.topic;

import com.gys.dto.JsonResult;
import com.gys.entity.Topic;
import com.gys.entity.User;
import com.gys.service.TopicService;
import com.gys.util.StringUtil;
import com.gys.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/topicFav")
public class TopicFavServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String topicid = req.getParameter("topicid");
        String action = req.getParameter("action");
        User user = getCurrentUser(req);

        TopicService topicService = new TopicService();
        //servlet处理业务逻辑
        JsonResult result = new JsonResult();
        if (StringUtil.isNotEmpty(action) && StringUtil.isNumeric(topicid)) {

            //前端有action可以判断是收藏还是取消收藏，不需要再靠这里返回数据来判断了
            if(action.equals("fav")) {
                topicService.favTopic(user.getId(),Integer.valueOf(topicid));
                result.setState(JsonResult.SUCCESS);
            } else if (action.equals("unfav")) {
                topicService.unfavTopic(user.getId(),Integer.valueOf(topicid));
                result.setState(JsonResult.SUCCESS);
            }

            //查询数据库中的收藏数量，反馈给客户端
            Topic topic = topicService.findTopicById(Integer.valueOf(topicid));
            Integer favnum = topic.getFavnum();
            result.setData(favnum);

        } else {
            result = new JsonResult("参数异常");
        }

        renderJson(result,resp);
    }
}
