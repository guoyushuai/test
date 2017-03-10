package com.gys.controller;

import com.google.common.collect.Maps;
import com.gys.dto.AjaxResult;
import com.gys.dto.DataTablesResult;
import com.gys.dto.DeviceRentDto;
import com.gys.dto.LaborDispatchDto;
import com.gys.exception.NoFoundException;
import com.gys.exception.ServiceException;
import com.gys.pojo.*;
import com.gys.service.LaborService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/labor/dispatch")
public class LaborDispatchController {

    @Autowired
    private LaborService laborService;

    /**
     * 劳务派遣主页面，显示雇佣合同列表
     * @return
     */
    @GetMapping
    public String list() {
        return "labor/dispatch/list";
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
        List<LaborDispatch> laborDispatchList = laborService.findLaborDispatchByQueryParam(queryParam);

        //datatables插件需要的参数draw,recordsTotal,recoredsFilter,data四个参数
        Long count = laborService.laborDispatchCount();//与laborMapper.count,laborDispatchMapper.count

        //Datatables插件对响应结果的格式有要求，JSON格式(对象{}/数组[])要求为{}对象，或者用Map集合进行传值
        // ！！！@ResponseBody springMVC默认将结果自动转换为JSON格式，
        return new DataTablesResult(draw,count,count,laborDispatchList);
    }

    /**
     * 修改合同的状态
     * 同时需要工人归队
     * @param id
     * @return
     */
    @PostMapping("/state/change")
    @ResponseBody
    public String changeDispatchState(Integer id) {
        laborService.changeDispatchState(id);
        return "success";
    }




    /**
     * 新建雇佣合同，需要数据库中所有的工种信息,进入页面时就进行查询所有工种信息
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newDispatch(Model model) {
        //查询所有的工种信息,雇佣工人时依据
        List<Labor> laborList = laborService.findAllLabors();
        //传入到前端JSP中,便于下拉框取值
        model.addAttribute("laborList",laborList);
        return "labor/dispatch/new";
    }

    /**
     * 根据客户端下拉框选择的id查找对应的工种信息
     * @param id
     * @return
     */
    @GetMapping("/labor.json")
    @ResponseBody
    public AjaxResult laborJson(Integer id) {
        Labor labor = laborService.findLaborById(id);
        if(labor == null) {
            return new AjaxResult(AjaxResult.ERROR,"工种不存在");
        } else {
            return new AjaxResult(labor);
        }
    }

    /**
     * 保存客户端发送过来的合同详情数据
     * @RequestBody 将客户端发送的json数据转换成DeviceRentDto对象
     * @return
     */
    @PostMapping("/new")
    @ResponseBody
    public AjaxResult saveDispatch(@RequestBody LaborDispatchDto laborDispatchDto) {

        try {
            String serialNumber = laborService.saveLaborDispatch(laborDispatchDto);

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
        LaborDispatch laborDispatch = laborService.findLaborDispatchBySerialNumber(serialNumber);
        if(laborDispatch == null) {
            throw new NoFoundException();
        } else {
            //2.查询合同详情列表
            List<LaborDispatchDetail> detailList = laborService.findLaborDispatchDetailListByDispatchId(laborDispatch.getId());
            //3.查询合同文件列表
            List<LaborDispatchDoc> docList = laborService.findLaborDispatchtDocListByDispatchtId(laborDispatch.getId());

            model.addAttribute("dispatch",laborDispatch);
            model.addAttribute("detailList",detailList);
            model.addAttribute("docList",docList);

            return "labor/dispatch/show";
        }
    }
}
