package com.gys.service.impl;

import com.gys.mapper.LaborMapper;
import com.gys.pojo.Labor;
import com.gys.service.LaborService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LaborServiceImpl implements LaborService {

    @Autowired
    private LaborMapper laborMapper;

    @Override
    public void saveNewLabor(Labor labor) {
        labor.setCurrentNum(labor.getTotalNum());
        laborMapper.save(labor);
    }

    @Override
    public List<Labor> findLaborBySearchParam(Map<String, Object> searchParam) {
        return laborMapper.findBySearchParam(searchParam);
    }

    @Override
    public Long count() {
        return laborMapper.count();
    }

    @Override
    public Long countBySearchParam(Map<String, Object> searchParam) {
        return laborMapper.countBySearchParam(searchParam);
    }

    @Override
    public void delLabor(Integer id) {
        laborMapper.del(id);
    }
}
