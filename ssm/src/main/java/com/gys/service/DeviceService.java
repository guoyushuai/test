package com.gys.service;

import com.gys.pojo.Device;

import java.util.List;
import java.util.Map;

public interface DeviceService {
    void saveNewDevice(Device device);

    List<Device> findAllDevices();

    List<Device> findDeviceByPage(String start, String length);

    Long count();

    List<Device> findDeviceBySearchParam(Map<String, Object> searchParam);

    void delDevice(Integer id);

    Long countBySearchParam(Map<String, Object> searchParam);
}
