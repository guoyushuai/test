package com.gys.service;

import com.gys.pojo.Device;

import java.util.List;

public interface DeviceService {
    void saveNewDevice(Device device);

    List<Device> findAllDevices();

    List<Device> findDeviceByPage(String start, String length);

    Long count();
}
