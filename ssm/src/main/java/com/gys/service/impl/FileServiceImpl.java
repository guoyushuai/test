package com.gys.service.impl;

import com.gys.exception.NoFoundException;
import com.gys.exception.ServiceException;
import com.gys.mapper.DeviceRentDocMapper;
import com.gys.mapper.DeviceRentMapper;
import com.gys.mapper.LaborDispatchDocMapper;
import com.gys.mapper.LaborDispatchMapper;
import com.gys.pojo.DeviceRent;
import com.gys.pojo.DeviceRentDoc;
import com.gys.pojo.LaborDispatch;
import com.gys.pojo.LaborDispatchDoc;
import com.gys.service.FileService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${upload.path}")//获取配置文件中设置的值
    private String uploadPath;

    @Autowired
    private DeviceRentDocMapper rentDocMapper;
    @Autowired
    private DeviceRentMapper rentMapper;

    @Autowired
    private LaborDispatchDocMapper dispatchDocMapper;
    @Autowired
    private LaborDispatchMapper dispatchMapper;

    @Override
    public String uploadFile(String originalFilename, String contentType, InputStream inputStream) {
        //上传后保存在服务器内的文件名字，保证上传后的文件名唯一
        String newFileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));


        File file = new File(new File(uploadPath),newFileName);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            return newFileName;
        } catch (IOException e) {
            //上传失败需要给用户做提示，不能直接抛IOException(没有提示)
            logger.error("文件上传异常",e);
            throw new ServiceException("文件上传异常",e);
        }

    }



    /**
     * 对异常的处理有点乱，暂不使用
     */
    /*@Override
    public void downloadFile(Integer id, HttpServletResponse response) throws IOException {

        DeviceRentDoc rentDoc = rentDocMapper.findById(id);
        //上传路径即为下载的目标源路径
        String downloadPath = uploadPath;

        if(rentDoc != null) {

            File file = new File(downloadPath,rentDoc.getNewName());
            if(file.exists()) {

                //获得文件输入流
                FileInputStream inputStream = new FileInputStream(file);

                //将文件下载标记为二进制(响应的MIME头)
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
                    //设置文件的总大小
                    //response.setContentLengthLong(file.length());
                //更改文件的下载名称(即弹出的对话框中文件的名称),注意中文乱码问题
                String fileName = rentDoc.getSourceName();
                fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName + "\"");

                //生成响应输出流
                OutputStream outputStream = response.getOutputStream();

                IOUtils.copy(inputStream,outputStream);
                outputStream.flush();
                outputStream.close();
                inputStream.close();

            } else {
                throw new NoFoundException();
            }
        } else {
            throw new NoFoundException();
        }
    }*/

    @Override
    public InputStream downloadRentFile(Integer id) throws IOException {
        //根据id在数据库中查找到相应的文件
        DeviceRentDoc rentDoc = rentDocMapper.findById(id);

        //下载的目标源路径与上传的路径相同
        String downloadPath = uploadPath;

        if(rentDoc != null) {
            //根据路径查找要下载的文件是否存在
            File file = new File(new File(downloadPath),rentDoc.getNewName());
            if(file.exists()) {
                //获得文件输入流
                return new FileInputStream(file);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public DeviceRentDoc findDeviceRentDocById(Integer id) {
        return rentDocMapper.findById(id);
    }

    @Override
    public DeviceRent findDeviceRentById(Integer id) {
        return rentMapper.findById(id);
    }

    @Override
    public void downloadRentZipFile(DeviceRent rent, ZipOutputStream zipOutputStream) throws IOException {
        //根据rent_id，查找该份租赁合同所有的文件
        List<DeviceRentDoc> deviceRentDocList = rentDocMapper.findByRentId(rent.getId());//findDeviceRentDocListByRentId

        for(DeviceRentDoc rentDoc : deviceRentDocList) {
            ZipEntry entry = new ZipEntry(rentDoc.getSourceName());
            zipOutputStream.putNextEntry(entry);

            //找到对应文件，生成输入流
            InputStream inputStream = downloadRentFile(rentDoc.getId());//下载单个文件是定义的方法

            IOUtils.copy(inputStream,zipOutputStream);
            inputStream.close();
            /*循环内不能关闭输出流*/
        }

        zipOutputStream.closeEntry();
        zipOutputStream.flush();
        zipOutputStream.close();
    }





    @Override
    public InputStream downloadDispatchFile(Integer id) throws IOException {
        //根据id在数据库中查找到相应的文件
        LaborDispatchDoc dispatchDoc = dispatchDocMapper.findById(id);

        //下载的目标源路径与上传的路径相同
        String downloadPath = uploadPath;

        if(dispatchDoc != null) {
            //根据路径查找要下载的文件是否存在
            File file = new File(new File(downloadPath),dispatchDoc.getNewName());
            if(file.exists()) {
                //获得文件输入流
                return new FileInputStream(file);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public LaborDispatchDoc findLaborDispatchDocById(Integer id) {
        return dispatchDocMapper.findById(id);
    }

    @Override
    public LaborDispatch findLaborDispatchById(Integer id) {
        return dispatchMapper.findById(id);
    }

    @Override
    public void downloadDispatchZipFile(LaborDispatch dispatch, ZipOutputStream zipOutputStream) throws IOException {
        //根据dispatch_id，查找该份雇佣合同所有的文件
        List<LaborDispatchDoc> laborDispatchDocList = dispatchDocMapper.findByDispatchId(dispatch.getId());//findDeviceRentDocListByRentId

        for(LaborDispatchDoc dispatchDoc : laborDispatchDocList) {
            ZipEntry entry = new ZipEntry(dispatchDoc.getSourceName());
            zipOutputStream.putNextEntry(entry);

            //找到对应文件，生成输入流
            InputStream inputStream = downloadDispatchFile(dispatchDoc.getId());//下载单个文件是定义的方法

            IOUtils.copy(inputStream,zipOutputStream);
            inputStream.close();
            /*循环内不能关闭输出流*/
        }

        zipOutputStream.closeEntry();
        zipOutputStream.flush();
        zipOutputStream.close();
    }

}
