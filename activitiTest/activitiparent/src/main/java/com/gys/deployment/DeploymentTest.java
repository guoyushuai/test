package com.gys.deployment;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

public class DeploymentTest {

    private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 以zip压缩文件部署流程
     */
    @Test
    public void deployZip() {

        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("diagrams/vacation.zip");//获取zip文件输入流

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        Deployment deploy = engine.getRepositoryService()
                .createDeployment()
                .name("zipDeploy")
                .addZipInputStream(zipInputStream)//从压缩文件输入流中加载资源文件
                .deploy();

        System.out.println("流程id:" + deploy.getId());//部署id
        System.out.println("流程name:" + deploy.getName());
        System.out.println("流程category:" + deploy.getCategory());
        System.out.println("流程tenantId:" + deploy.getTenantId());
        System.out.println("流程deploymentTime:" + deploy.getDeploymentTime());

    }

    /**
     * 获取流程定义
     */
    @Test
    public void getProcessDef() {

        List<ProcessDefinition> processDefinitionList = engine.getRepositoryService()
                .createProcessDefinitionQuery()//创建查询对象
                /*指定查询条件*/
                /*.deploymentId("")//根据流程部署id查询
                .processDefinitionId("")//根据流程定义id查询(含版本及部署id)
                .processDefinitionKey("")//根据流程定义key查询(流程表的key字段，流程图的id属性)*/
                /*指定排序方式*/
                .orderByProcessDefinitionName().desc()
                .list();

        System.out.println("流程部署个数：" + processDefinitionList.size());

        for(ProcessDefinition def : processDefinitionList) {
            System.out.println("定义id：" + def.getId());
            System.out.println("name：" + def.getName());
            System.out.println("key：" + def.getKey());
            System.out.println("部署id：" + def.getDeploymentId());
            System.out.println("版本：" + def.getVersion());
        }

    }

    /**
     * 获取流程图
     */
    @Test
    public void getProcessDefPng() throws IOException {

        String defId = "vacation:1:7505";//根据流程定义id获取相应图片

        ProcessDefinition processDef = engine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionId(defId)
                .singleResult();

        String deployId = processDef.getDeploymentId();
        String resourceName = processDef.getDiagramResourceName();

        InputStream inputStream = engine.getRepositoryService()
                .getResourceAsStream(deployId,resourceName);//获取资源输入流

        FileUtils.copyInputStreamToFile(inputStream,new File("G:/vacation.png"));

    }

}
