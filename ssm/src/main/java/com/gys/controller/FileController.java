package com.gys.controller;

import com.gys.dto.AjaxResult;
import com.gys.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            return new AjaxResult(AjaxResult.ERROR,e.getMessage());
        }
    }
}
