package com.gys.mapper;

import com.gys.pojo.LaborDispatchDetail;

import java.util.List;

public interface LaborDispatchDetailMapper {
    void batchSave(List<LaborDispatchDetail> dispatchDetailList);

    List<LaborDispatchDetail> findByDispatchId(Integer id);
}
