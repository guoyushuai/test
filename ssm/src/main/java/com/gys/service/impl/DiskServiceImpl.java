package com.gys.service.impl;

import com.google.common.collect.Lists;
import com.gys.exception.NoFoundException;
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

import java.io.*;
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

    @Override
    public InputStream downloadFile(Integer id) throws IOException {
        //根据id查找数据库中是否有相应的文件记录
        Disk disk = diskMapper.findById(id);
        String downloadPath = savePath;
        /*if(disk == null || Disk.DIRECTORY_TYPE.equals(disk.getType())) {//文件不存在或者类型为文件夹，不存在输入流
            return null;
        } else {
            FileInputStream inputStream = new FileInputStream(new File(new File(savePath),disk.getName()));
            return inputStream;
        }*/
        if(disk != null) {
            //到文件保存路径根据保存的名字查找对应文件
            File file = new File(new File(downloadPath),disk.getNewName());
            if(file.exists()) {
                //获得下载文件的输入流
                InputStream inputStream = new FileInputStream(file);
                return inputStream;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Disk findById(Integer id) {
        return diskMapper.findById(id);
    }

    @Override
    @Transactional
    public void delById(Integer id) {

        //根据id查找对应文件/文件夹
        Disk disk = diskMapper.findById(id);
        if(disk != null) {
            //根据类型不同操作不同

            if(Disk.FILE_TYPE.equals(disk.getType())) {//文件
                //删除文件(根据保存的路径和文件名
                File file = new File(savePath,disk.getNewName());
                if(file.exists()) {//文件存在才能删
                    file.delete();//删除文件
                    //删除数据库记录
                    diskMapper.delById(id);
                } else {
                    throw new NoFoundException();
                }

            } else {//文件夹（文件夹不用删磁盘）

                List<Disk> diskList = diskMapper.findAll();//查询所有记录
                List<Integer> delIdList = Lists.newArrayList();//需要删除记录的id集合,先找不管类型
                findDelId(diskList,delIdList,id);//获取第三个参数的子，不管孙子。。。
                //在此对delIdList进行操作（下面再使用delIdList时有影响），对象，内存地址

                delIdList.add(id);//加上树根，连根拔出

                //删除数据库记录（无论文件/文件夹统一）批量删除
                diskMapper.batchDel(delIdList);


                /*for(Integer delId : delIdList) {
                    Disk d = findById(delId);
                    if(d.getType().equals(Disk.FILE_TYPE)) {
                        File file = new File(savePath,d.getNewName());
                        file.delete();
                    }
                }*/

            }

        } else {
            throw new NoFoundException();
        }
    }

    //获取第三个参数的子，不管孙子。。。
    private void findDelId(List<Disk> diskList, List<Integer> delIdList, Integer id) {
        for (Disk d : diskList) {
            if(id.equals(d.getFid())) {//注意Integer类型  不能使用==
                delIdList.add(d.getId());//查找到"id"的直接子，添加到待删除集合
                if(Disk.DIRECTORY_TYPE.equals(d.getType())) {//如果该子还是文件夹，继续查找下一层
                    findDelId(diskList,delIdList,d.getId());//查找到"d.id"的直接子
                } else {//如果该子是文件，那么删磁盘
                    File file =  new File(savePath,d.getNewName());//根据路径和文件保存名字进行删除
                    file.delete();
                }
            }
        }
    }
}
