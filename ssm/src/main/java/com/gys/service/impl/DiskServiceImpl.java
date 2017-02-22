package com.gys.service.impl;

import com.gys.exception.ServiceException;
import com.gys.mapper.DiskMapper;
import com.gys.pojo.Disk;
import com.gys.service.DiskService;
import com.gys.shiro.ShiroUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class DiskServiceImpl implements DiskService {

    @Autowired
    private DiskMapper diskMapper;

    @Value("${upload.path}")
    private String savePath;//获得文件保存路径

    @Override
    public List<Disk> findByFid(Integer fid) {
        return diskMapper.findByFid(fid);
    }

    @Override
    public void saveNewFolder(Integer fid, String sourceName) {
        Disk disk = new Disk();
        disk.setFid(fid);
        disk.setSourceName(sourceName);

        disk.setCreateUser(ShiroUtil.getCurrentUsername());
        disk.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        disk.setType(Disk.DIRECTORY_TYPE);

        diskMapper.save(disk);
    }

    @Override
    @Transactional
    public void saveNewFile(Integer fid, MultipartFile file) {

        //1、将文件保存到磁盘

        String sourceName = file.getOriginalFilename();//获得文件的原始名字
        String newName = UUID.randomUUID().toString();//生成文件上传后保存的名字
        int suffix = sourceName.lastIndexOf(".");
        //上传的文件有后缀名时再将后缀名填上
        if(suffix != -1) {
            newName = newName + sourceName.substring(suffix);
        }

        Long size = file.getSize();//获得文件大小

        try {
            //获得文件输入流
            InputStream inputStream = file.getInputStream();
            //生成文件输出流
            FileOutputStream outputStream = new FileOutputStream(new File(new File(savePath),newName));

            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            //开启事务时，不能直接抛出IOException,事务是根据RuntimeException触发的；
            //没有事务时可以直接在方法上抛出IOException
            throw new ServiceException("文件保存异常");
        }

        //2、将文件记录保存到数据库
        Disk disk = new Disk();
        disk.setCreateUser(ShiroUtil.getCurrentUsername());
        disk.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        disk.setType(Disk.FILE_TYPE);
        disk.setSourceName(sourceName);
        disk.setNewName(newName);
        disk.setFid(fid);

        disk.setSize(FileUtils.byteCountToDisplaySize(size));

        diskMapper.save(disk);
    }
}
