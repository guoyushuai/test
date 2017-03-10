package com.gys.controller;

import com.gys.dto.AjaxResult;
import com.gys.exception.NoFoundException;
import com.gys.pojo.DeviceRent;
import com.gys.pojo.DeviceRentDoc;
import com.gys.pojo.LaborDispatch;
import com.gys.pojo.LaborDispatchDoc;
import com.gys.service.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;


    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult ajaxFileUpload(MultipartFile file) {

        try {
            String newFileName = fileService.uploadFile(file.getOriginalFilename(),file.getContentType(),file.getInputStream());

            Map<String,String> result = new HashMap<>();
            result.put("newFileName",newFileName);
            result.put("originalFilename",file.getOriginalFilename());

            return new AjaxResult(result);

            //不仅有IOException,还有自定义的RuntimeException
        } catch (Exception e) {
            //捕获异常后不能没有任何提示或处理
            e.printStackTrace();
            return new AjaxResult(AjaxResult.ERROR,e.getMessage());
        }
    }

    /**
     * 同一份合同的文件打包下载
     * @param id rent_id,合同的id,不是每一份合同文件的id
     */
    @GetMapping("/download/rent/doc/zip")
    public void downloadRentZipFile(Integer id,HttpServletResponse response) throws IOException {
        //根据合同id查找相应的合同
        DeviceRent rent = fileService.findDeviceRentById(id);

        if(rent != null) {

            //将文件下载标记为二进制（设置相应的文件头）//spring framework框架定义的常量
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());

            //设置弹出的对话框中的文件名称//注意中文乱码问题
            String filename = rent.getCompanyName() + ".zip";
            filename = new String(filename.getBytes("UTF-8"),"ISO8859-1");
            response.setHeader("Content-Disposition","attachment;filename=\"" + filename + "\"");//注意格式 转义引号

            OutputStream outputStream = response.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            fileService.downloadRentZipFile(rent,zipOutputStream);

        } else {
            throw new NoFoundException();
        }
    }


    /*spring方式的下载*/
    @GetMapping("/download/rent/doc")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadRentFile(Integer id) throws IOException {
        InputStream inputStream = fileService.downloadRentFile(id);
        if(inputStream != null) {
            DeviceRentDoc rentDoc = fileService.findDeviceRentDocById(id);
            String fileName = rentDoc.getSourceName();

            //响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment",fileName, Charset.forName("UTF-8"));
            return new ResponseEntity<InputStreamResource>(new InputStreamResource(inputStream),headers, HttpStatus.OK);


        } else {
            throw new NoFoundException();
        }

    }

    /**
     * 同一份合同的文件打包下载
     * @param id dispatch_id,合同的id,不是每一份合同文件的id
     */
    @GetMapping("/download/dispatch/doc/zip")
    public void downloadDispatchZipFile(Integer id,HttpServletResponse response) throws IOException {
        //根据合同id查找相应的合同
        LaborDispatch dispatch = fileService.findLaborDispatchById(id);

        if(dispatch != null) {

            //将文件下载标记为二进制（设置相应的文件头）//spring framework框架定义的常量
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());

            //设置弹出的对话框中的文件名称//注意中文乱码问题
            String filename = dispatch.getCompanyName() + ".zip";
            filename = new String(filename.getBytes("UTF-8"),"ISO8859-1");
            response.setHeader("Content-Disposition","attachment;filename=\"" + filename + "\"");//注意格式 转义引号

            OutputStream outputStream = response.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            fileService.downloadDispatchZipFile(dispatch,zipOutputStream);

        } else {
            throw new NoFoundException();
        }
    }


    /*spring方式的下载*/
    @GetMapping("/download/dispatch/doc")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadDispatchFile(Integer id) throws IOException {
        InputStream inputStream = fileService.downloadDispatchFile(id);
        if(inputStream != null) {
            LaborDispatchDoc dispatchDoc = fileService.findLaborDispatchDocById(id);
            String fileName = dispatchDoc.getSourceName();

            //响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment",fileName, Charset.forName("UTF-8"));
            return new ResponseEntity<InputStreamResource>(new InputStreamResource(inputStream),headers, HttpStatus.OK);


        } else {
            throw new NoFoundException();
        }

    }


    /*@GetMapping("/download/rent/doc")
    public void downloadfile(Integer id,HttpServletResponse response) throws IOException {
        InputStream inputStream = fileService.downloadRentFile(id);//DeviceRentDoc的id
        //拿到文件输入流进行下一步操作
        if(inputStream != null) {
            //这里又查了一遍，无奈方法不能返回两个参数
            DeviceRentDoc rentDoc  = fileService.findDeviceRentDocById(id);

            //将文件下载标记为二进制（设置相应的文件头）
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());//spring framework框架定义的常量
            //设置弹出的对话框中的文件名称
            String fileName = rentDoc.getSourceName();
            fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");//注意中文乱码问题
            response.setHeader("Content-Disposition","attachment;filename=\"" + fileName + "\"");//注意格式 转义引号 ,弹出对话框中的自动补上文件原始名字

            //响应输出流
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } else {
            throw new NoFoundException();
        }
    }*/


    /*//合同详情显示页面，点击文件名连接下载相应文件;异常的处理不太合适
    @GetMapping("/download/rent/doc")
    @ResponseBody
    public void downloadFile(Integer id, HttpServletResponse response) throws IOException {
        try {
            fileService.downloadFile(id, response);
        } catch (NoFoundException e) {
            todo
            //下载文件没有返回信息选项，浏览器设置的锅
            捕捉了NoFound又要抛NoFound，不用捕捉了，service层抛过会直接进入404界面
        }
    }*/
}
