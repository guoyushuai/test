package com.gys.service;

import com.gys.pojo.DeviceRent;
import com.gys.pojo.DeviceRentDoc;
import com.gys.pojo.LaborDispatch;
import com.gys.pojo.LaborDispatchDoc;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;

public interface FileService {

    String uploadFile(String originalFilename, String contentType, InputStream inputStream);

    /*void downloadFile(Integer id, HttpServletResponse response) throws IOException;*/

    InputStream downloadRentFile(Integer id) throws IOException;

    DeviceRentDoc findDeviceRentDocById(Integer id);

    DeviceRent findDeviceRentById(Integer rentId);

    void downloadRentZipFile(DeviceRent rent, ZipOutputStream zipOutputStream) throws IOException;



    InputStream downloadDispatchFile(Integer id) throws IOException;

    LaborDispatchDoc findLaborDispatchDocById(Integer id);

    LaborDispatch findLaborDispatchById(Integer id);

    void downloadDispatchZipFile(LaborDispatch dispatch, ZipOutputStream zipOutputStream) throws IOException;


}
