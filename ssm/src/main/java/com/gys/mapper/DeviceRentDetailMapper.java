package com.gys.mapper;

import com.gys.pojo.DeviceRentDetail;

import java.util.List;

public interface DeviceRentDetailMapper {
    void batchSave(List<DeviceRentDetail> rentDetailList);

    List<DeviceRentDetail> findByRentId(Integer id);
}
