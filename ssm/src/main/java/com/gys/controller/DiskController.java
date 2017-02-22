package com.gys.controller;

import com.gys.dto.AjaxResult;
import com.gys.exception.ServiceException;
import com.gys.pojo.Disk;
import com.gys.service.DiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

}
