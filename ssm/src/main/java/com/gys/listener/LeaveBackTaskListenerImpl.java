package com.gys.listener;

import com.gys.mapper.process.LeaveMapper;
import com.gys.pojo.process.Leave;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
@Transactional
public class LeaveBackTaskListenerImpl implements TaskListener {

    @Autowired
    private LeaveMapper leaveMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateTask delegateTask) {

        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        leaveMapper = (LeaveMapper) wac.getBean("leaveMapper");

        //入库请假的真实开始和结束时间

        //获取完成任务时设置的流程变量
        Object startTime = delegateTask.getVariable("realityStartTime");
        Object endTime = delegateTask.getVariable("realityEndTime");

        /*Object startTimes = runtimeService.getVariable(delegateTask.getExecutionId(),"realityStartTime");
        Object endTimes = runtimeService.getVariable(delegateTask.getExecutionId(),"realityEndTime");*/

        //DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        String leaveId = delegateTask.getExecution()//获取流程实例
                .getProcessBusinessKey();//获取流程实例对应的请假业务的id
        Leave leave = leaveMapper.findById(leaveId);//查找对应请假业务对象
        //设置相应的值并更新数据库
        leave.setRealityStartTime(String.valueOf(startTime));
        leave.setRealityEndTime(String.valueOf(endTime));

        //配置监听器的时候用的不是表达式，不能直接用Spring容器中的Bean
        /*//手动加载springBean
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        leaveManager = (LeaveManager)wac.getBean("leaveManager");*/

        leaveMapper.update(leave);

    }
}
