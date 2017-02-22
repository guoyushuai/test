package com.gys.service;

import com.gys.pojo.Disk;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DiskService {
    List<Disk> findByFid(Integer fid);

    void saveNewFolder(Integer fid, String sourceName);

    void saveNewFile(Integer fid, MultipartFile file);
}
