package com.gys;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

public class ActivitiEngine {

    /**
     * 通过ProcessEngineConfiguration类加载ProcessEngine，初始化数据库
     */
    @Test
    public void initTable1() {

        ProcessEngineConfiguration pec = ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration();
        //连接数据库配置
        pec.setJdbcDriver("com.mysql.jdbc.Driver");
        pec.setJdbcUrl("jdbc:mysql://localhost:3306/activiti_test");
        pec.setJdbcUsername("root");
        pec.setJdbcPassword("root");

        /*public static final String DB_SCHEMA_UPDATE_FALSE = "false";//不自动创建表
        public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";//先删除表再创建表
        public static final String DB_SCHEMA_UPDATE_TRUE = "true";//如果表不存在自动创建表*/
        pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        //根据自定义配置更新默认配置创建ProcessEngine引擎
        ProcessEngine engine = pec.buildProcessEngine();

        System.out.println("engine:" + engine);

    }

    /**
     * 创建名为activiti.cfg.xml文件，并通过ProcessEngineConfiguration加载ProcessEngine，初始化数据库：
     */
    @Test
    public void initTable2() {

        //从xml配置文件中获取数据库连接配置
        ProcessEngineConfiguration pec = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml");

        ProcessEngine engine = pec.buildProcessEngine();

        System.out.println("engine:" + engine);
    }

    /**
     * 调用ProcessEngines的getDefaultProceeEngine方法时会自动加载classpath下名为activiti.cfg.xml文件
     */
    @Test
    public void createEngine() {

        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        System.out.println("enging:" + engine);
    }

}
