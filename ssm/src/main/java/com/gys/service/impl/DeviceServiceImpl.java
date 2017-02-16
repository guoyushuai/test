package com.gys.service.impl;

import com.gys.mapper.DeviceMapper;
import com.gys.pojo.Device;
import com.gys.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public void saveNewDevice(Device device) {
        //新增数据时当前库存数量等于总数量
        device.setCurrentNum(device.getTotalNum());
        deviceMapper.save(device);
    }

    @Override
    public List<Device> findAllDevices() {
        return deviceMapper.findAll();
    }

    @Override
    public List<Device> findDeviceByPage(String start, String length) {
        return deviceMapper.findByPage(start,length);
    }

    @Override
    public Long count() {
        return deviceMapper.count();
    }

    @Override
    public List<Device> findDeviceBySearchParam(Map<String, Object> searchParam) {
        return deviceMapper.findBySearchParam(searchParam);
    }

    @Override
    public void delDevice(Integer id) {
        deviceMapper.del(id);
    }

    @Override
    public Long countBySearchParam(Map<String, Object> searchParam) {
        return deviceMapper.countBySearchParam(searchParam);
    }
}
