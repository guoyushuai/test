package com.gys.controller;

import com.google.common.collect.Maps;
import com.gys.pojo.Labor;
import com.gys.service.LaborService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统设置中的劳务管理控制器
 */
@Controller
@RequestMapping("/setting/labor")
public class SettingLaborController {

    @Autowired
    private LaborService laborService;

    /**
     * 展示工种列表
     * @return
     */
    @GetMapping
    public String list() {
        return "setting/labor/list";
    }//只做跳转用

    /**
     * 进入list界面后datatable插件异步请求显示在表格中的数据
     * @param request
     * @return
     */
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
        String laborName = request.getParameter("laborName");

        System.out.println(draw + "->" + start + "->" + length + orderColumn + orderType + laborName);

        /*需要往mapper.xml中传入多个参数时，使用对象、集合、或者一个一个写参数序号或者起别名*/
        Map<String,Object> searchParam = Maps.newHashMap();
        searchParam.put("start",start);
        searchParam.put("length",length);
        searchParam.put("orderType",orderType);
        searchParam.put("orderColumn",orderColumn);
        searchParam.put("laborName",laborName);


        /*//零配置时的分页查询，服务端每次查的其实是所有数据
        List<Device> deviceList = deviceService.findDeviceByPage(start,length);
        Long count = deviceService.count();*/

        //查找符合查询条件的所有数据
        List<Labor> laborList = laborService.findLaborBySearchParam(searchParam);
        //总记录数
        Long count = laborService.count();

        //加入搜索后，过滤后的总记录数,参数放入Map集合中进行传递，虽然只用其中一个参数但便于以后扩展
        Long filteredCount = laborService.countBySearchParam(searchParam);

        /*插件对响应结果的格式有要求，JSON格式(对象{}/数组[])要求为{}对象，
         ！！！@ResponseBody springMVC默认将结果自动转换为JSON格式，Map集合转成JSON数据后就是对象（键值对-属性值）*/
        Map<String,Object> resultMap = Maps.newHashMap();
        //传过来第几次，返回就是第几次
        resultMap.put("draw",draw);
        //总记录数
        resultMap.put("recordsTotal",count);
        //过滤后的总记录数
        resultMap.put("recordsFiltered",filteredCount);
        //该页的数据
        resultMap.put("data",laborList);

        return resultMap;
    }

    /**
     *
     * @return
     */
    @GetMapping("/new")
    public String newLabor() {
        return "setting/labor/new";
    }

    /**
     * 保存新工种
     * @param labor
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/new")
    public String newLobar(Labor labor,RedirectAttributes redirectAttributes) {
        laborService.saveNewLabor(labor);
        redirectAttributes.addFlashAttribute("message","保存成功");
        return "redirect:/setting/labor";
    }

    /**
     * 删除工种
     * @param id
     * @return
     */
    @GetMapping("/del/{id:\\d+}")
    @ResponseBody
    public String delLabor(@PathVariable Integer id) {
        laborService.delLabor(id);
        return "success";//new AjaxResult(AjaxResult.SUCCESS)
    }
}
