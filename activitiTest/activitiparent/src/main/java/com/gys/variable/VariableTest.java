package com.gys.variable;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VariableTest {

    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 启动实例流程变量使用runtimeService
     */
    @Test
    public void startProcessVariable(){
        Map<String,Object> variables = new HashMap<>();
        variables.put("days", 5);
        variables.put("startTime", new Date());
        ProcessInstance pi = engine.getRuntimeService().startProcessInstanceByKey("myProcess",variables);

        System.out.println("id: " +pi.getId());
        System.out.println("name: " +pi.getName());
        System.out.println("key: " + pi.getProcessDefinitionKey() );


    }

    /**
     * 实例流程变量使用runtimeService
     */
    @Test
    public void setRuntimeVariables(){
        Map<String,Object> variables = new HashMap<>();
        variables.put("days1", 5);
        variables.put("startTime1", new Date());
        engine.getRuntimeService().setVariables("17501",variables);


    }

    /**
     * 任务流程变量使用taskService
     */
    @Test
    public void setTaskVariables(){
        Map<String,Object> variables = new HashMap<>();
        variables.put("days2", 5);
        variables.put("startTime2", new Date());
        engine.getTaskService().setVariables("17506",variables);

    }

}
