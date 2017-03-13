package com.gys.service.process;

import com.gys.pojo.User;
import com.gys.pojo.process.Leave;

import java.util.Map;

public interface LeaveService {
    void startProcess(Leave leave, User user, String processDefKey, Map<String, Object> variables);

    Leave findLeaveById(String id);

    void update(Leave leave);
}
