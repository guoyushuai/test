package com.gys.hello;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class HelloWord {

    private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 第一步 部署流程
     */
    @Test
    public void deployment() {

        Deployment deploy = engine.getRepositoryService()
                .createDeployment()//创建一个部署对象
                .name("HelloWord")//添加部署名称
                .addClasspathResource("diagrams/HelloWorld.bpmn")//从classpath中加载资源文件，每次一个
                .addClasspathResource("diagrams/HelloWorld.png")
                .deploy();//完成部署

        System.out.println("deployId:" + deploy.getId());
        System.out.println("deployName:" + deploy.getName());
        System.out.println("deployTime:" + deploy.getDeploymentTime());

    }

    /**
     * 第二部 启动流程
     */
    @Test
    public void startProcess() {

        ProcessInstance instance = engine.getRuntimeService()
                .startProcessInstanceByKey("myProcessTest");//使用流程定义的key启动流程实例property:id的value:myProcessTest
        //流程图中的id属性等价于数据库表格中的key字段

        System.out.println("instanceId:" + instance.getId());
        System.out.println("instanceName:" + instance.getName());
        System.out.println("instanceDeploymentId:" + instance.getDeploymentId());
        System.out.println("instanceProcessId:" + instance.getProcessDefinitionId());
        System.out.println("instanceProcessKey:" + instance.getProcessDefinitionKey());
        System.out.println("instanceProcessName:" + instance.getProcessDefinitionName());

    }

    /**
     * 第三步 获取任务列表
     */
    @Test
    public void getTaskList() {

        List<Task> taskList = engine.getTaskService()
                .createTaskQuery()//创建查询对象
                .list();

        System.out.println("taskListSize:" + taskList.size());

        for(Task task:taskList) {
            System.out.println("流程定义id:" + task.getProcessDefinitionId());
            System.out.println("任务内容名称:" + task.getName());
            System.out.println("办理人:" + task.getAssignee());
            System.out.println("任务id:" + task.getId());
        }

    }

    /**
     * 第四步 完成任务
     */
    @Test
    public void completeTask() {

        String taskId = "2504";

        engine.getTaskService().complete(taskId);

        System.out.println("处理成功");


    }

}
