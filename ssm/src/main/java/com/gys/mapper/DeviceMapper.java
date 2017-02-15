package com.gys.mapper;

import com.gys.pojo.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DeviceMapper {
    void save(Device device);

    List<Device> findAll();

    List<Device> findByPage(@Param("start") String start, @Param("length") String length);

    Long count();

    List<Device> findBySearchParam(Map<String, Object> searchParam);

    void del(Integer id);

    Long countBySearchParam(Map<String, Object> searchParam);
}
