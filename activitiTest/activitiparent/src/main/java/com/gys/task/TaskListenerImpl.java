package com.gys.task;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class TaskListenerImpl implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {

        delegateTask.setAssignee("西西");

    }

    private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 第一步 部署流程
     */
    @Test
    public void deployment() {

        Deployment deploy = engine.getRepositoryService()
                .createDeployment()//创建一个部署对象
                .name("taskListener")//添加部署名称
                .addClasspathResource("diagrams/taskListener.bpmn")//从classpath中加载资源文件，每次一个
                .addClasspathResource("diagrams/taskListener.png")
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
                .startProcessInstanceByKey("taskListener");//,maps使用流程定义的key启动流程实例property:id的value:myProcessTest
        //流程图中的id属性等价于数据库表格中的key字段

        System.out.println("instanceId:" + instance.getId());
        System.out.println("instanceName:" + instance.getName());
        System.out.println("instanceDeploymentId:" + instance.getDeploymentId());
        System.out.println("instanceProcessId:" + instance.getProcessDefinitionId());
        System.out.println("instanceProcessKey:" + instance.getProcessDefinitionKey());
        System.out.println("instanceProcessName:" + instance.getProcessDefinitionName());

    }
}
