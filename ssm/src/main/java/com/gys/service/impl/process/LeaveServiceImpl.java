package com.gys.service.impl.process;

import com.gys.mapper.process.LeaveMapper;
import com.gys.pojo.User;
import com.gys.pojo.process.Leave;
import com.gys.service.process.LeaveService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class LeaveServiceImpl implements LeaveService {


    @Autowired
    private LeaveMapper leaveMapper;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    @Transactional
    public void startProcess(Leave leave, User user, String processDefKey, Map<String, Object> variables) {

        //给引擎初始化时initiator中设置的占位符{applyUserId}赋值，引擎自动替换
        identityService.setAuthenticatedUserId(user.getId().toString());

        //继续封装完善leave对象，保存进数据库t_leave表中
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        leave.setApplyTime(format.format(new Date()));
        leave.setUserId(user.getId().toString());
        leaveMapper.save(leave);//保存

        //启动流程（三个参数）
        // (流程定义的processDefinitionKey,
        // 获取请假信息leave.id赋值给businessKey与自定义业务产生关联关系,activiti保留字段
        // 流程启动参数map集合作为runtimeservice的参数)
        ProcessInstance instance = runtimeService
                .startProcessInstanceByKey(processDefKey,leave.getId().toString(),variables);

        //建立双向连接，更新t_leave表中的PROCESS_INSTANCE_ID为processInstance的id值
        leave.setProcessInstanceId(instance.getProcessInstanceId());
        leaveMapper.update(leave);
    }

    @Override
    public Leave findLeaveById(String id) {
        return leaveMapper.findById(id);
    }

    @Override
    public void update(Leave leave) {
        leaveMapper.update(leave);
    }
}
