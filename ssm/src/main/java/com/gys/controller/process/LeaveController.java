package com.gys.controller.process;

import com.gys.pojo.User;
import com.gys.pojo.process.Leave;
import com.gys.service.process.LeaveService;
import com.gys.shiro.ShiroUtil;
import org.activiti.engine.ActivitiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/leave")
public class LeaveController {

    Logger logger = LoggerFactory.getLogger(LeaveController.class);

    @Autowired
    private LeaveService leaveService;

    /**
     * 进入请假流程申请页面
     * @return
     */
    @GetMapping("/apply")
    public String leaveApply() {
        return "activiti/leave/leaveApply";
    }

    /**
     * 启动请假流程
     * @param leave
     * @param model
     * @return
     */
    @PostMapping("/start")
    public String leaveStart(Leave leave,Model model) {
        //获取当前登录用户
        User user = ShiroUtil.getCurrentUser();

        //该控制器只针对请假流程的控制器
        String processDefKey = "leaveProcess";

        //流程变量（部门经理，人事经理）
        Map<String,Object> variables = new HashMap<>();
        //TODO 查询当前用户的上级ID
        String upperId = "1";

        variables.put("deptLeaderUserId",upperId);

        try {
            //根据参数设置并启动相应流程
            leaveService.startProcess(leave, user, processDefKey, variables);
            model.addAttribute("message","流程启动成功");
        } catch (ActivitiException e) {
            logger.error("KEY为{}的流程启动异常",processDefKey);
            model.addAttribute("message","流程启动失败");
        }

        //启动流程后返回的还是请假申请页面？？
        return "activiti/leave/leaveApply";
    }

}
