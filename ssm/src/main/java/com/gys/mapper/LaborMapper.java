package com.gys.mapper;

import com.gys.pojo.Labor;

import java.util.List;
import java.util.Map;

public interface LaborMapper {
    void save(Labor labor);

    List<Labor> findBySearchParam(Map<String, Object> searchParam);

    Long count();

    Long countBySearchParam(Map<String, Object> searchParam);

    void del(Integer id);

    List<Labor> findAll();

    Labor findById(Integer id);

    void updateCurrentNum(Labor labor);

    Labor findByName(String laborName);

    void update(Labor labor);
}
