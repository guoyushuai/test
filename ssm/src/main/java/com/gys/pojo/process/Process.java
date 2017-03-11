package com.gys.pojo.process;

import lombok.Data;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.Date;

@Data
public class Process {

    private Long id;
    private String processInstanceId;//流程实例id
    private String processDefinitionName;//流程定义名称
    private String userId;
    private String userName;
    private String applyTime;
    private String updateTime;
    private String taskName;
    private String taskAssignee;

    // 流程定义
    private ProcessDefinition processDefinition;

    // 运行中的流程实例
    private ProcessInstance processInstance;

    // 流程任务
    private Task task;

    // 历史流程实例
    private HistoricProcessInstance historicProcessInstance;

}
