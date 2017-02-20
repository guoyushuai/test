package com.gys.service;

import com.gys.pojo.DeviceRent;
import com.gys.pojo.DeviceRentDoc;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;

public interface FileService {

    String uploadFile(String originalFilename, String contentType, InputStream inputStream);

    void downloadFile(Integer id, HttpServletResponse response) throws IOException;

    InputStream downloadFile(Integer id) throws IOException;

    DeviceRentDoc findDeviceRentDocById(Integer id);

    DeviceRent findDeviceRentById(Integer rentId);

    void downloadZipFile(DeviceRent rent, ZipOutputStream zipOutputStream) throws IOException;
}
