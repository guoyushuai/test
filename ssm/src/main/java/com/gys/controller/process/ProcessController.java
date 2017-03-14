package com.gys.controller.process;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.gys.pojo.process.Process;
import com.gys.shiro.ShiroUtil;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.Format;
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

    Logger logger = LoggerFactory.getLogger(ProcessController.class);

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

    /**
     * 签收任务
     */
    @GetMapping("/task/claim/{taskId}")
    public String taskClaim(@PathVariable String taskId,
                            RedirectAttributes redirectAttributes) {
        //获取当前登录用户的id
        String userId = String.valueOf(ShiroUtil.getCurrentUserId());

        try {
            //签收任务
            taskService.claim(taskId, userId);
            redirectAttributes.addFlashAttribute("message","任务签收成功");
        } catch (ActivitiException e) {
            redirectAttributes.addFlashAttribute("message","任务签收失败");
        }

        return "redirect:/process/task/list";
    }

    /**
     * 我发起的运行中的工作流
     * @param model
     * @return
     */
    @GetMapping("/running/list")
    public String runningList(Model model) {

        Format format = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        //获取当前用户id，查询该用户申请的正在运行的流程
        String userId = String.valueOf(ShiroUtil.getCurrentUserId());

        //查询结束时间为空的流程实例（查询t_leave表不靠谱，放弃继续申请的流程也没有结束时间）
        //查询hi_procinst表比较靠谱
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId).list();

        List<Process> processList = new ArrayList<>();

        for(HistoricProcessInstance history:historicProcessInstanceList) {

            /*
            <th>流程实例ID</th>
            <th>所属流程</th>
            <th>启动时间</th>
            <th>流程启动人</th>
            <th>当前节点</th>
            <th>办理人</th>
            <th>分配时间</th>
            */

            if(history.getEndTime() != null) {
                continue;//结束时间不为null,流程已结束，跳出该次循环继续
            }

            Process process = new Process();//每一次循环都需要建立一个新的对象，否则后覆盖前

            process.setProcessInstanceId(history.getId());//没有封装获得proc_inst_id的方法，id与执行表（execution）id相同

            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(history.getProcessDefinitionId())//根据流程定义id查找对应流程定义对象
                    .singleResult();
            process.setProcessDefinitionName(definition.getName());

            process.setApplyTime(format.format(history.getStartTime()));

            User applyUser = identityService.createUserQuery()
                    .userId(history.getStartUserId())
                    .singleResult();
            process.setUserName(applyUser.getFirstName());

            //task中含有流程节点、办理人id（或者未分配）、分配时间
            Task task = taskService.createTaskQuery()
                    .processInstanceId(history.getId())
                    /*.executionId(history.getId())*///执行id与实例id区别（并行网关，一个实例多个执行路线)
                    .singleResult();
            /*process.setTask(task);*/

            User assigneeUser = identityService.createUserQuery()
                    .userId(task.getAssignee())
                    .singleResult();

            process.setTaskName(task.getName());
            process.setTaskAssignee(assigneeUser.getFirstName());
            process.setUpdateTime(format.format(task.getCreateTime()));

            processList.add(process);

        }

        model.addAttribute("processList",processList);

        return "activiti/process/myRunningProcess";
    }

    /**
     * 我发起的历史流程和我参与的历史流程（我处理过了但是流程还未结束）
     * @param model
     * @return
     */
    @GetMapping("/finished/list")
    public String finishedList(Model model) {
        Format format = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        //获取当前用户id，查询该用户申请的正在运行的流程
        String userId = String.valueOf(ShiroUtil.getCurrentUserId());
        com.gys.pojo.User user = ShiroUtil.getCurrentUser();

        List<Process> processList = new ArrayList<>();
        List<Process> processTaskList = new ArrayList<>();

        //1.我发起的历史流程
        /*<th>流程实例ID</th>
        <th>所属流程</th>
        <th>启动时间</th>
        <th>流程启动人</th>
        <th>结束时间</th>*/
        List<HistoricProcessInstance> hisProcList = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId)//发起人
                .finished()//已完成
                .list();

        for(HistoricProcessInstance hisProcess:hisProcList) {
            Process process = new Process();
            process.setProcessInstanceId(hisProcess.getId());

            ProcessDefinition proDef = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(hisProcess.getProcessDefinitionId())
                    .singleResult();
            process.setProcessDefinitionName(proDef.getName());

            process.setApplyTime(format.format(hisProcess.getStartTime()));

            process.setUserName(user.getUsername());

            process.setUpdateTime(format.format(hisProcess.getEndTime()));//当结束时间使

            processList.add(process);
        }

        //2.我参与（我的任务已经完成，未完成的在待办页面）的流程（无论完成未完成）
        //todo 同一个流程对应多条任务 根据任务id查出的流程重复
        /*<th>流程实例ID</th>
        <th>所属流程</th>
        <th>启动时间</th>
        <th>流程启动人</th>
        <th>结束时间</th>
        <th>状态</th>*/
        List<HistoricTaskInstance> hisTaskList = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .list();

        for (HistoricTaskInstance hisTask : hisTaskList) {
            Process process = new Process();
            process.setProcessInstanceId(hisTask.getProcessInstanceId());

            ProcessDefinition proDef = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(hisTask.getProcessDefinitionId())
                    .singleResult();
            process.setProcessDefinitionName(proDef.getName());

            HistoricProcessInstance hisProc = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(hisTask.getProcessInstanceId())
                    .singleResult();
            process.setApplyTime(format.format(hisProc.getStartTime()));

            User startUser = identityService.createUserQuery()
                    .userId(hisProc.getStartUserId())
                    .singleResult();
            process.setUserName(startUser.getFirstName());

            if(hisProc.getEndTime() != null) {
                process.setUpdateTime(format.format(hisProc.getEndTime()));//当结束时间使
            }

            //状态根据是否有结束时间确定

            processTaskList.add(process);
        }

        model.addAttribute("processList",processList);
        model.addAttribute("processTaskList",processTaskList);

        return "activiti/process/finishedProcess";
    }


    @GetMapping("/view/{instanceId}")
    public String processView(@PathVariable String instanceId,
                              Model model) {

        Format format = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        /*流程ID
        流程名称
        业务KEY
        流程启动时间
        流程结束时间
        流程状态*/
        Process process = new Process();
        process.setProcessInstanceId(instanceId);

        HistoricProcessInstance hisProc = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId)
                .singleResult();
        process.setApplyTime(format.format(hisProc.getStartTime()));//启动时间
        if(hisProc.getEndTime() != null) {
            process.setUpdateTime(format.format(hisProc.getEndTime()));//结束时间
        }
        process.setHistoricProcessInstance(hisProc);//业务key(business_key)

        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(hisProc.getProcessDefinitionId())
                .singleResult();
        process.setProcessDefinitionName(procDef.getName());//流程名称


        process.setProcessDefinition(procDef);

        /*<th>活动ID</th>hi_actinst表中筛选出assignee不为空的活动记录。。
        <th>活动名称</th>
        <th>活动类型</th>
        <th>任务ID</th>
        <th>办理人</th>assignee 只有ID
        <th>活动开始时间</th>
        <td>活动结束时间</td>*/

        /*private Long id;
        private String processInstanceId;//流程实例id
        private String processDefinitionName;//流程定义名称
        private String userId;//申请人id
        private String userName;//申请人name
        private String applyTime;//申请时间
        private String updateTime;//更新时间
        private String taskName;//任务名称
        private String taskAssignee;//任务办理人*/

        List<HistoricActivityInstance> hisActList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .orderByHistoricActivityInstanceStartTime()
                .desc()
                .list();
        //筛选
        hisActList = Lists.newArrayList(Collections2.filter(hisActList,new Predicate<HistoricActivityInstance>(){

            @Override
            public boolean apply(HistoricActivityInstance historicActivityInstance) {
                return StringUtils.isNotEmpty(historicActivityInstance.getAssignee());//assignee不为空的记录过滤出去
            }
        }));



        for(HistoricActivityInstance hisAct : hisActList) {
            User actUser = identityService.createUserQuery()
                    .userId(hisAct.getAssignee())
                    .singleResult();
            //todo 怎么将任务对应的办理人名称封装到到对应的任务对象中传递到前端在页面中显示出来
        }

        //在process对象中封装对应的集合
        process.setHisActList(hisActList);

        model.addAttribute("process",process);

        return "activiti/process/processView";
    }

    @GetMapping("/resource/{deploymentId}")
    public void getResource(@PathVariable String deploymentId,
                              @RequestParam("resourceName") String resourceName,
                              HttpServletResponse response){
        //通过接口读取资源文件 作为输入流
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId,resourceName);

        try {
            byte[] b = new byte[1024];
            int len = -1;
            while((len = inputStream.read(b,0,1024)) != -1) {
                response.getOutputStream().write(b);//响应输出流write()显示用
            }
        } catch (IOException e) {
           logger.error("流程图读取异常");
        }
    }
}
