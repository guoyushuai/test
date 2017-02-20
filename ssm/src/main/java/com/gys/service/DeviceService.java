package com.gys.service;

import com.gys.dto.DeviceRentDto;
import com.gys.pojo.Device;
import com.gys.pojo.DeviceRent;
import com.gys.pojo.DeviceRentDetail;
import com.gys.pojo.DeviceRentDoc;

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

    Device findDeviceById(Integer id);

    String saveDeviceRent(DeviceRentDto deviceRentDto);

    DeviceRent findDeviceRentBySerialNumber(String serialNumber);

    List<DeviceRentDetail> findDeviceRentDetailListByRentId(Integer id);

    List<DeviceRentDoc> findDeviceRentDocListByRentId(Integer id);
}
