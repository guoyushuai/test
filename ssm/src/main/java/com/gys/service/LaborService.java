package com.gys.service;

import com.gys.dto.LaborDispatchDto;
import com.gys.pojo.Labor;
import com.gys.pojo.LaborDispatch;
import com.gys.pojo.LaborDispatchDetail;
import com.gys.pojo.LaborDispatchDoc;

import java.util.List;
import java.util.Map;

public interface LaborService {
    void saveNewLabor(Labor labor);

    List<Labor> findLaborBySearchParam(Map<String, Object> searchParam);

    Long count();

    Long countBySearchParam(Map<String, Object> searchParam);

    void delLabor(Integer id);

    List<Labor> findAllLabors();

    Labor findLaborById(Integer id);

    String saveLaborDispatch(LaborDispatchDto laborDispatchDto);

    LaborDispatch findLaborDispatchBySerialNumber(String serialNumber);

    List<LaborDispatchDetail> findLaborDispatchDetailListByDispatchId(Integer id);

    List<LaborDispatchDoc> findLaborDispatchtDocListByDispatchtId(Integer id);

    List<LaborDispatch> findLaborDispatchByQueryParam(Map<String, Object> queryParam);

    Long laborDispatchCount();

    void changeDispatchState(Integer id);
}
