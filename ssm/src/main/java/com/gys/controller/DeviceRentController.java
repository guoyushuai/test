package com.gys.controller;

import com.google.common.collect.Maps;
import com.gys.dto.AjaxResult;
import com.gys.dto.DataTablesResult;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

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

    //list只做跳转用，客户端进入页面后ajax异步向服务器load发起数据请求返回结果进行显示

    @GetMapping("/load")
    @ResponseBody
    public DataTablesResult load(HttpServletRequest request) {
        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");

        //往Mybatis传多个参数时使用map集合
        Map<String,Object> queryParam = Maps.newHashMap();
        queryParam.put("start",start);
        queryParam.put("length",length);

        //不做页面搜索查询，只分页,并根据id进行倒序显示
        List<DeviceRent> deviceRentList = deviceService.findDeviceRentByQueryParam(queryParam);

        //datatables插件需要的参数draw,recordsTotal,recoredsFilter,data四个参数
        Long count = deviceService.deviceRentCount();//与deviceMapper.count,deviceRentMapper.count

        //Datatables插件对响应结果的格式有要求，JSON格式(对象{}/数组[])要求为{}对象，或者用Map集合进行传值
        // ！！！@ResponseBody springMVC默认将结果自动转换为JSON格式，
        return new DataTablesResult(draw,count,count,deviceRentList);
    }

    /**
     * 修改合同的状态
     * @param id
     * @return
     */
    @PostMapping("/state/change")
    @ResponseBody
    public String changeRentState(Integer id) {
        deviceService.changeRentState(id);
        //尾款付清
        return "success";
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
            return new AjaxResult(AjaxResult.ERROR,e.getMessage());
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
