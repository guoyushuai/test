package com.gys.controller;

import com.google.common.collect.Maps;
import com.gys.pojo.Device;
import com.gys.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    //使用dataTable插件后，list只做跳转就行，在通过get请求来到页面时页面显示数据通过插件的ajax异步方式查询获得
    @GetMapping
    public String list() {
        return "setting/device/list";
    }

    @PostMapping("/load")
    @ResponseBody
    public Map<String,Object> load(HttpServletRequest request) {
        //第几次请求
        String draw = request.getParameter("draw");
        //从第几条数据开始
        String start = request.getParameter("start");
        //每页显示几条内容(可选的，不确定)
        String length = request.getParameter("length");

        /*下列参数名比较复杂而且含有特殊符号，直接写在参数列表中无法获取其对应的值*/

        //排序的列在页面内表格中的序号
        String orderIndex = request.getParameter("order[0][column]");
        //排序的方式（正序/倒序）
        String orderType = request.getParameter("order[0][dir]");
        //排序列的序号对应的列的名字（请求头对应键值对的键名）
        String orderColumn = request.getParameter("columns["+orderIndex+"][name]");

        //自带的搜索的键名为search[value]

        //搜索框内的值，自行扩展的键
        String deviceName = request.getParameter("deviceName");

        /*需要往xml中传入多个参数时，使用对象、集合、或者一个一个写参数序号或者起别名*/
        Map<String,Object> searchParam = Maps.newHashMap();
        searchParam.put("start",start);
        searchParam.put("length",length);
        searchParam.put("orderType",orderType);
        searchParam.put("orderColumn",orderColumn);
        searchParam.put("deviceName",deviceName);


        /*//零配置时的分页查询，服务端每次查的其实是所有数据
        List<Device> deviceList = deviceService.findDeviceByPage(start,length);
        Long count = deviceService.count();*/

        //查找符合查询条件的所有数据
        List<Device> deviceList = deviceService.findDeviceBySearchParam(searchParam);
        //总记录数
        Long count = deviceService.count();

        //加入搜索后，过滤后的总记录数,参数放入Map集合中进行传递，虽然只用其中一个参数但便于以后扩展
        Long filteredCount = deviceService.countBySearchParam(searchParam);

        //插件对结果的JSON格式要求为{}对象，Map集合转成JSON数据后就是对象（键值对-属性值）
        Map<String,Object> resultMap = Maps.newHashMap();
        //传过来第几次，返回就是第几次
        resultMap.put("draw",draw);
        //总记录数
        resultMap.put("recordsTotal",count);
        //过滤后的总记录数
        resultMap.put("recordsFiltered",filteredCount);
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

    @GetMapping("/{id:\\d+}/del")
    @ResponseBody
    public String delDevice(@PathVariable Integer id) {
        deviceService.delDevice(id);
        return "success";
    }
}
