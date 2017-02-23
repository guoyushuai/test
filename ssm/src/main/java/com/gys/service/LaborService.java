package com.gys.service;

import com.gys.pojo.Labor;

import java.util.List;
import java.util.Map;

public interface LaborService {
    void saveNewLabor(Labor labor);

    List<Labor> findLaborBySearchParam(Map<String, Object> searchParam);

    Long count();

    Long countBySearchParam(Map<String, Object> searchParam);

    void delLabor(Integer id);
}
