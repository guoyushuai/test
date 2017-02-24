package com.gys.service.impl;

import com.gys.exception.NoFoundException;
import com.gys.mapper.FinanceMapper;
import com.gys.pojo.Finance;
import com.gys.service.FinanceService;
import com.gys.shiro.ShiroUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private FinanceMapper financeMapper;

    @Override
    public List<Finance> findByQueryParam(Map<String, Object> queryParam) {
        return financeMapper.findByQueryParam(queryParam);
    }

    @Override
    public Long count() {
        return financeMapper.count();
    }

    @Override
    public Long countByParam(Map<String, Object> queryParam) {
        return financeMapper.countByQueryParam(queryParam);
    }

    @Override
    @Transactional
    public void confirmFinanceById(Integer id) {
        Finance finance = financeMapper.findById(id);
        if(finance != null) {
            finance.setState(Finance.STATE_COMPLETE);
            finance.setConfirmUser(ShiroUtil.getCurrentUsername());
            finance.setConfirmDate(DateTime.now().toString("yyyy-MM-dd"));
            financeMapper.update(finance);
        } else {
            throw new NoFoundException();
        }
    }

    @Override
    public List<Finance> findByCreateDate(String createDate) {
        return financeMapper.findByCreateDate(createDate);
    }
}
