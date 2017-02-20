package com.gys.mapper;

import com.gys.pojo.DeviceRentDoc;

import java.util.List;

public interface DeviceRentDocMapper {
    void batchSave(List<DeviceRentDoc> rentDocList);

    List<DeviceRentDoc> findByRentId(Integer id);
}
