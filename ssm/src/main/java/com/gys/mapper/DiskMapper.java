package com.gys.mapper;

import com.gys.pojo.Disk;

import java.util.List;

public interface DiskMapper {

    List<Disk> findByFid(Integer fid);

    void save(Disk disk);

    Disk findById(Integer id);

    void delById(Integer id);

    List<Disk> findAll();

    void batchDel(List<Integer> delIdList);
}
