package com.gys.controller;

import com.gys.dto.AjaxResult;
import com.gys.exception.NoFoundException;
import com.gys.exception.ServiceException;
import com.gys.pojo.Disk;
import com.gys.service.DiskService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

@Controller
@RequestMapping("/pan")
public class DiskController {

    @Autowired
    private DiskService diskService;

    /**
     *首页显示根目录文件列表
     * @param path url中的参数 默认值为"0" 必须用引号引起来
     * @param model
     * @return
     */
    @GetMapping
    public String list(
            @RequestParam(required = false,defaultValue = "0") Integer path,
            Model model) {

        List<Disk> diskList = diskService.findByFid(path);

        model.addAttribute("diskList",diskList);

        //将fid重新传回给客户端，便于之后操作需要（新建文件夹时，传入则在该级目录进行创建）
        model.addAttribute("fid",path);

        return "pan/list";
    }

    /**
     * 新建文件夹
     * @return
     */
    @PostMapping("/folder/new")
    @ResponseBody
    public String newFolder(Integer fid,String sourceName) {
        //或者直接在参数中传入Disk对象，该对象中含有fid,sourceName属性，spring会自动将这两个属性封装到对象中
        diskService.saveNewFolder(fid,sourceName);

        return "success";
    }

    /**
     * 文件上传
     * @param fid 文件要上传到的目录
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult newFile(Integer fid, MultipartFile file) {
        //如果是form表单上传(文件上传域)，需要判断file.isEmpty，防止提交时没有附带文件
        try {
            diskService.saveNewFile(fid, file);
            return new AjaxResult(AjaxResult.SUCCESS);
        } catch (ServiceException e) {
            return new AjaxResult(AjaxResult.ERROR,e.getMessage());
        }
    }

    /**
     * 文件下载Spring
     * @param id 要下载的文件
     * @return
     * @throws IOException
     */
    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadFile(Integer id) throws IOException {
        InputStream inputStream = diskService.downloadFile(id);
        if(inputStream != null) {
            Disk disk = diskService.findById(id);
            String sourceName = disk.getSourceName();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment",sourceName, Charset.forName("UTF-8"));

            return new ResponseEntity<InputStreamResource>(new InputStreamResource(inputStream),headers, HttpStatus.OK);
        } else {
            /*return new NoFoundException();*/
            throw new NoFoundException();
        }
    }

    /**
     * 文件下载servlet
     * @return
     */
    /*@GetMapping("/download")
    public void downloadFile(Integer id,HttpServletResponse response) throws IOException {
        InputStream inputStream = diskService.downloadFile(id);

        if(inputStream != null) {
            //查找对应文件记录
            Disk disk = diskService.findById(id);

            String sourceName = disk.getSourceName();
            sourceName = new String(sourceName.getBytes("UTF-8"),"ISO8859-1");

            //设置响应头
            response.setContentType("application/octet-stream");//标记为二进制流（浏览器不支持的格式）
            *//*response.setContentLengthLong(Long.parseLong(disk.getSize()));//表明文件大小,这里是可视化的大小*//*
            response.setHeader("Content-Disposition","attachment;filename=\"" + sourceName + "\"");//弹出的下载对话框自动补全文件原始名字

            //获得响应输出流
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } else {
            throw new NoFoundException();
        }
    }*/

    @GetMapping("/del/{id:\\d+}")
    @ResponseBody
    public AjaxResult del(@PathVariable Integer id) {
        diskService.delById(id);
        return new AjaxResult(AjaxResult.SUCCESS);
    }

}
