package com.gys.controller;

import com.gys.dto.AjaxResult;
import com.gys.dto.DeviceRentDto;
import com.gys.exception.NoFoundException;
import com.gys.exception.ServiceException;
import com.gys.pojo.Device;
import com.gys.pojo.DeviceRent;
import com.gys.pojo.DeviceRentDetail;
import com.gys.pojo.DeviceRentDoc;
import com.gys.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 保存客户端发送过来的合同详情数据
     * @RequestBody 将客户端发送的json数据转换成DeviceRentDto对象
     * @return
     */
    @PostMapping("/new")
    @ResponseBody
    public AjaxResult saveRent(@RequestBody DeviceRentDto deviceRentDto) {

        try {
            String serialNumber = deviceService.saveDeviceRent(deviceRentDto);

            AjaxResult result = new AjaxResult();
            //将合同序列号返回给客户端
            result.setData(serialNumber);
            result.setStatus(AjaxResult.SUCCESS);
            return result;
        } catch (ServiceException e) {
            return new AjaxResult(e.getMessage());
        }

    }

    /**
     * 根据流水号显示合同详情
     * @param serialNumber
     * @return
     */
    @GetMapping("/{serialNumber:\\d+}")
    public String showDeviceRent(@PathVariable String serialNumber,Model model) {
        //1.查询合同对象
        DeviceRent deviceRent = deviceService.findDeviceRentBySerialNumber(serialNumber);
        if(deviceRent == null) {
            throw new NoFoundException();
        } else {
            //2.查询合同详情列表
            List<DeviceRentDetail> detailList = deviceService.findDeviceRentDetailListByRentId(deviceRent.getId());
            //3.查询合同文件列表
            List<DeviceRentDoc> docList = deviceService.findDeviceRentDocListByRentId(deviceRent.getId());

            model.addAttribute("rent",deviceRent);
            model.addAttribute("detailList",detailList);
            model.addAttribute("docList",docList);

            return "device/rent/show";
        }
    }

}
