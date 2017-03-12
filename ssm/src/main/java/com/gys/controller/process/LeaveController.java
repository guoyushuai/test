package com.gys.controller.process;

import com.gys.pojo.User;
import com.gys.pojo.process.Leave;
import com.gys.service.process.LeaveService;
import com.gys.shiro.ShiroUtil;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/leave")
public class LeaveController {

    Logger logger = LoggerFactory.getLogger(LeaveController.class);

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

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


    /**
     * 显示请假任务详情
     */
    @GetMapping("/task/view/{taskId}")
    public String leaveTaskView(@PathVariable String taskId,Model model) {

        /*
        标题            task.name
                        (taskDefinitionKey)
        申请人          user.firstname
        申请时间        leave.applyTime
        请假类型        leave.type
        开始时间        leave.startTime
        结束时间        leave。endTime
        请假原因        leave.reason
        */

        //根据taskid获取task对象，（ru_task）
        //然后根据task对象的processInstanceId查询processInstance对象，(ru_execution)
        //根据processInstance对象的businessKey查询leave对象。(t_leave)
        //taskId-Task-proc_inst_id-ProcessInstance-business_key-Leave
        Task task = taskService.createTaskQuery()
                .taskId(taskId).singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult();
        Leave leave = leaveService.findLeaveById(processInstance.getBusinessKey());

        //申请人名字t_leave-user_id-id_user-first
        org.activiti.engine.identity.User actUser = identityService.createUserQuery()
                .userId(leave.getUserId()).singleResult();

        //封装Leave对象
        leave.setTask(task);
        leave.setUserName(actUser.getFirstName());

        model.addAttribute("leave",leave);

        return "activiti/leave/taskVerify";

    }

    /**
     * 办理请假任务
     */
    @PostMapping("/task/complete/{taskId}")
    public String leaveTaskComplete(@PathVariable String taskId,
                                    HttpServletRequest req,
                                    RedirectAttributes redirectAttributes) {

        Map<String,Object> variables = new HashMap<>();

        String taskDefKey = req.getParameter("taskDefKey");//deptLeaderVerify或者hrVerify或者leaveBack

        if(StringUtils.isNotEmpty(taskDefKey) && taskDefKey.equals("leaveBack")) {
            //如果是销假，需要将真实开始结束时间入库t_leave
            String realityStartTime = req.getParameter("realityStartTime");
            String realityEndTime = req.getParameter("realityEndTime");

            //真实开始结束时间留给监听器做或者在此直接入库
            //设置的监听器是在任务完成以后触发调用LeaveBackTaskListenerImpl
            variables.put("realityStartTime",realityStartTime);
            variables.put("realityEndTime",realityEndTime);

        } else {
            //如果是部门领导审批和人事审批，传入任务id和审批意见交给activiti完成

            /*1(任务流程变量赋值使用taskService，对应act_ru_variable表)
            taskService.setVariable(taskId,key,value)
            Map<String,Object> variables = new HashMap<>();
            taskService.setVariables(taskId,variables)*/
            /*2在完成任务的同时，给流程定义的xml中sequenceFlow的conditionExpression中的占位符{}条件赋值
            相应占位符{}内的名字（deptLeaderVerify/hrVerify）对应 ru_task表中的task_def_key字段的key
            taskService.complete(taskId, variables);*/
            //map的key值从前台传过来也是个变量，根据业务任务阶段的不同赋值不同//deptLeaderVerify或者hrVerify

            String verify = req.getParameter("verify");
            //审批意见的name属性名verify必须为boolean值，否则抛出异常
            Object value = BooleanUtils.toBoolean(verify);

            variables.put(taskDefKey,value);
        }

        try {
            //完成任务时传入变量
            taskService.complete(taskId, variables);
            redirectAttributes.addFlashAttribute("message","完成任务成功");
        } catch (ActivitiException e) {
            redirectAttributes.addFlashAttribute("message","完成任务失败");
        }
        //重定向
        return "redirect:/process/task/list";
    }

}
