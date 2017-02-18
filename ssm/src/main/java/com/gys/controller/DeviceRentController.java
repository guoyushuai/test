package com.gys.controller;

import com.gys.dto.AjaxResult;
import com.gys.pojo.Device;
import com.gys.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/device/rent")
public class DeviceRentController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 设备租赁主页面，显示租赁合同列表
     * @return
     */
    @GetMapping
    public String list() {
        return "device/rent/list";
    }

    /**
     * 新建租赁合同，需要数据库中所有的设备信息
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newRent(Model model) {
        //查询所有的设备信息,租赁设备时依据
        List<Device> deviceList = deviceService.findAllDevices();
        //传入到前端JSP中
        model.addAttribute("deviceList",deviceList);
        return "device/rent/new";
    }

    /**
     * 根据客户端下拉框选择的id查找对应的设备信息
     * @param id
     * @return
     */
    @GetMapping("/device.json")
    @ResponseBody
    public AjaxResult deviceJson(Integer id) {
        Device device = deviceService.findDeviceById(id);
        if(device == null) {
            return new AjaxResult(AjaxResult.ERROR,"设备不存在");
        } else {
            return new AjaxResult(device);
        }
    }

}
