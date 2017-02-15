package com.gys.controller;

import com.google.common.collect.Maps;
import com.gys.pojo.Device;
import com.gys.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统设置中的设备管理控制器
 */
@Controller
@RequestMapping("/setting/device")
public class SettingDeviceController {

    @Autowired
    private DeviceService deviceService;

    /*@GetMapping
    public String list(Model model) {
        List<Device> deviceList = deviceService.findAllDevices();
        model.addAttribute("deviceList",deviceList);
        return "setting/device/list";
    }*/
    //使用dataTable插件后，list只做跳转就行，在通过get请求来到页面时页面显示数据通过插件ajax异步方式查询，

    @GetMapping
    public String list() {
        return "setting/device/list";
    }

    @GetMapping("/load")
    @ResponseBody
    public Map<String,Object> load(HttpServletRequest request) {
        //第几次请求
        String draw = request.getParameter("draw");
        //从第几条数据开始
        String start = request.getParameter("start");
        //每页显示几条内容
        String length = request.getParameter("length");

        List<Device> deviceList = deviceService.findDeviceByPage(start,length);

        Long count = deviceService.count();

        //插件对结果的JSON格式要求{}对象，map转成JSON后就是对象
        Map<String,Object> resultMap = Maps.newHashMap();
        //传过来第几次，返回就是第几次
        resultMap.put("draw",draw);
        //记录总数
        resultMap.put("recordsTotal",count);
        //暂时保持与count一致
        resultMap.put("recordsFiltered",count);
        //该页的数据
        resultMap.put("data",deviceList);

        return resultMap;
    }

    @GetMapping("/new")
    public String newDevice() {
        return "setting/device/new";
    }

    @PostMapping("/new")
    public String newDevice(Device device, RedirectAttributes redirectAttributes) {
        deviceService.saveNewDevice(device);
        redirectAttributes.addFlashAttribute("message","操作成功");
        return "redirect:/setting/device";
    }

}
