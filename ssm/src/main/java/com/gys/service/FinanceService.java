package com.gys.service;

import com.gys.pojo.Finance;

import java.util.List;
import java.util.Map;

public interface FinanceService {

    List<Finance> findByQueryParam(Map<String, Object> queryParam);

    Long count();

    Long countByParam(Map<String, Object> queryParam);

    void confirmFinanceById(Integer id);

    List<Finance> findByCreateDate(String createDate);
}
