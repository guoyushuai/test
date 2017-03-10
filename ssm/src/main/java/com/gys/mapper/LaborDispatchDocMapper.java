package com.gys.mapper;

import com.gys.pojo.LaborDispatchDoc;

import java.util.List;

public interface LaborDispatchDocMapper {
    void batchSave(List<LaborDispatchDoc> dispatchDocList);

    List<LaborDispatchDoc> findByDispatchId(Integer id);

    LaborDispatchDoc findById(Integer id);
}
