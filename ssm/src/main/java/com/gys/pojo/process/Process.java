package com.gys.pojo.process;

import lombok.Data;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.Date;
import java.util.List;

@Data
public class Process {

    private Long id;
    private String processInstanceId;//流程实例id
    private String processDefinitionName;//流程定义名称
    private String userId;//申请人id
    private String userName;//申请人name
    private String applyTime;//申请时间
    private String updateTime;//更新时间
    private String taskName;//任务名称
    private String taskAssignee;//任务办理人

    // 流程定义
    private ProcessDefinition processDefinition;

    // 运行中的流程实例
    private ProcessInstance processInstance;

    // 流程任务
    private Task task;

    // 历史流程实例
    private HistoricProcessInstance historicProcessInstance;

    //历史活动实例
    private HistoricActivityInstance historicActivityInstance;
    private List<HistoricActivityInstance> hisActList;
}
