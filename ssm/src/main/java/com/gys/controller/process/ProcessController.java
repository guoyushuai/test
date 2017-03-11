package com.gys.controller.process;

import com.gys.pojo.process.Process;
import com.gys.shiro.ShiroUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;

    /**
     * 显示所有流程种类（前端固定静态页面，不需要后端收集数据）
     * @return
     */
    @GetMapping("/apply")
    public String processApply() {
        return "activiti/process/processList";
    }

    /**
     * 显示当前登录用户所有待办任务列表（后端从数据库中查找相应字段传给前端显示）
     * 或者返回夜一个VO对象？
     * @return
     */
    @GetMapping("/task/list")
    public String taskList(Model model) {

        /*
        <th>申请人</th>             User.getFirstName()
        <th>任务名称</th>           task.name
        <th>任务创建时间</th>       task.createTime
        <th>所属流程</th>           processDefinition.name
        <th>流程创建时间</th>       historicProcessInstance.startTime
        <th>操作</th>              task.assignee
        */

        //1、通过当前登录的userId查询当前用户的任务列表
        String userId = ShiroUtil.getCurrentUserId().toString();//当前登录用户的ID
        //指定该用户处理的任务
        List<Task> todoList = taskService.createTaskQuery()//创建查询对象
                .taskAssignee(userId)//指定查询条件，当前登录用户
                .orderByTaskCreateTime().asc().list();
        //被指定为候选人未被认领的任务
        List<Task> unClaimList = taskService.createTaskQuery()
                .taskCandidateUser(userId)
                .orderByTaskCreateTime().asc().list();

        List<Task> taskList = new ArrayList<>();
        taskList.addAll(todoList);
        taskList.addAll(unClaimList);

        List<Process> processList = new ArrayList<>();
        for (Task task : taskList) {
            //根据任务表ru_task中的流程定义id proc_def_id在流程定义表re_procref中查找该任务对应的流程定义名称name（即该任务所属流程）
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();
            /*String definitionName = definition.getName();*/

            ///根据任务表ru_task中的流程实例id proc_inst_id从历史流程实例表hi_procinst中查询流程对应的申请人和创建时间
            HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            /*String startUserId = historicInstance.getStartUserId();*/
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String startTime = format.format(historicInstance.getStartTime());

            //根据历史流程实例表中的启动人id start_user_id从id_user表中查找对应的启动人名称first
            User startUser = identityService.createUserQuery()
                    .userId(historicInstance.getStartUserId())
                    .singleResult();
            /*String startUserName = startUser.getFirstName();*/

            Process process = new Process();
            process.setUserName(startUser.getFirstName());
            process.setTask(task);
            process.setProcessDefinitionName(definition.getName());
            process.setApplyTime(startTime);

            processList.add(process);
        }

        model.addAttribute("processList",processList);

        return "activiti/process/taskList";
    }

}
