package com.gys.mapper;

import com.gys.pojo.LaborDispatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LaborDispatchMapper {


    void save(LaborDispatch laborDispatch);

    void updateCost(@Param("total") float total,
                    @Param("preCost") float preCost,
                    @Param("lastCost") float lastCost,
                    @Param("id") Integer id);

    LaborDispatch findBySerialNumber(String serialNumber);

    LaborDispatch findById(Integer id);

    List<LaborDispatch> findByQueryParam(Map<String, Object> queryParam);

    Long count();

    void updateState(LaborDispatch laborDispatch);
}
