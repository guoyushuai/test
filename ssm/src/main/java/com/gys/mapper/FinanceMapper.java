package com.gys.mapper;

import com.gys.pojo.Finance;

import java.util.List;
import java.util.Map;

public interface FinanceMapper {

    void save(Finance finance);

    Finance findBySerialNumber(String serialNumber);

    List<Finance> findByQueryParam(Map<String, Object> queryParam);

    Long count();

    Long countByQueryParam(Map<String, Object> queryParam);

    Finance findById(Integer id);

    void update(Finance finance);

    List<Finance> findByCreateDate(String createDate);
}
