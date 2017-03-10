package com.gys.controller;

import com.google.common.collect.Maps;
import com.gys.dto.AjaxResult;
import com.gys.dto.DataTablesResult;
import com.gys.exception.NoFoundException;
import com.gys.exception.ServiceException;
import com.gys.pojo.Finance;
import com.gys.service.FinanceService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/finance")
public class FinanceReportController {

    @Autowired
    private FinanceService financeService;

    /**
     * 点击进入日报界面，只做跳转用
     * @return
     */
    @GetMapping("/day")
    public String home() {
        return "finance/day";
    }

    /**
     * 通过datatables插件异步加载day.jsp页面的数据
     */
    @GetMapping("/day/load")
    @ResponseBody
    public DataTablesResult loadDay(HttpServletRequest request) {
        //接收datatables插件ajax请求传递的参数
        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");

        //自定义键值对
        String date = request.getParameter("date");

        //将参数封装到Map集合中传递到Mybatis中进行查询
        Map<String,Object> queryParam = Maps.newHashMap();
        queryParam.put("draw",draw);
        queryParam.put("start",start);
        queryParam.put("length",length);

        queryParam.put("date",date);

        List<Finance> financeList = financeService.findByQueryParam(queryParam);
        Long count = financeService.count();

        Long filterCount = financeService.countByParam(queryParam);

        //将结果以封装好的对象的形式传递给dataTables插件（Spring会自动将对象转换成Json格式）
        return new DataTablesResult(draw,count,filterCount,financeList);
    }

    /**
     * 财务确认
     * @param id
     */
    @GetMapping("/confirm/{id:\\d+}")
    @ResponseBody
    public AjaxResult confirmFinance(@PathVariable Integer id) {
        /*try {*/
            financeService.confirmFinanceById(id);
            return new AjaxResult(AjaxResult.SUCCESS);
        /*} catch (NoFoundException e) {//不合适,不用抓也抓不到，service层已经抛了404，service层不抛的话，返回的是AjaxResult
            return new AjaxResult(AjaxResult.ERROR, e.getMessage());
        }*/
    }

    /**
     * 将指定日期的财务日报导出到Excel表中
     * @param date
     * @param response
     */
    @GetMapping("/export/day/{date}/data.xls")//{date}可以与客户端放入的键名不同，保证与下面方法中的参数名相同即可
    public void exportDayReport(@PathVariable String date, HttpServletResponse response) throws IOException {
        List<Finance> financeList = financeService.findByCreateDate(date);

        //xls文件的MIME头
        response.setContentType("application/vnd.ms-excel");
        //设置自定义文件头（文件名称）
        response.setHeader("Content-Disposition","attachment;filename=\""+ date +"\".xls");

        //1、创建工作表WorkBook
        Workbook workbook = new HSSFWorkbook();

        //2、创建sheet页
        String sheetName = date + ".xls";
        Sheet sheet = workbook.createSheet(sheetName);

        //3、创建行（从0开始）
        Row row = sheet.createRow(0);

        //4、创建单元格(从0开始)
        Cell c0 = row.createCell(0);

        //5、写入单元格内容
        c0.setCellValue("财务流水号");

        row.createCell(1).setCellValue("类型");
        row.createCell(2).setCellValue("金额");
        row.createCell(3).setCellValue("状态");
        row.createCell(4).setCellValue("业务模块");
        row.createCell(5).setCellValue("创建人");
        row.createCell(6).setCellValue("创建时间");
        row.createCell(7).setCellValue("确认人");
        row.createCell(8).setCellValue("确认时间");
        row.createCell(9).setCellValue("备注");
        row.createCell(10).setCellValue("业务流水号");

        for(int i = 0;i < financeList.size();i++) {

            Finance finance = financeList.get(i);

            //标题行占一行，数据行从1行开始,依次累加否则下一个对象的数据将覆盖本行数据
            Row dataRow = sheet.createRow(i+1);
            dataRow.createCell(0).setCellValue(finance.getSerialNumber());
            dataRow.createCell(1).setCellValue(finance.getType());
            dataRow.createCell(2).setCellValue(finance.getMoney());
            dataRow.createCell(3).setCellValue(finance.getState());
            dataRow.createCell(4).setCellValue(finance.getModule());
            dataRow.createCell(5).setCellValue(finance.getCreateUser());
            dataRow.createCell(6).setCellValue(finance.getCreateDate());
            dataRow.createCell(7).setCellValue(finance.getConfirmUser());
            dataRow.createCell(8).setCellValue(finance.getConfirmDate());
            dataRow.createCell(9).setCellValue(finance.getRemark());
            dataRow.createCell(10).setCellValue(finance.getModuleSerialNumber());
        }

        //6、根据数据长短自动调整行宽
        sheet.autoSizeColumn(0);
        /*sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);*/
        sheet.autoSizeColumn(6);
        /*sheet.autoSizeColumn(7);*/
        sheet.autoSizeColumn(8);
        /*sheet.autoSizeColumn(9);*/
        sheet.autoSizeColumn(10);

        //7、获得文件响应输出流
        OutputStream outputStream = response.getOutputStream();

        //8、输出
        workbook.write(outputStream);

        outputStream.flush();
        outputStream.close();

    }

    /**
     * 加载当天的饼状图
     * @param type in收入 out支出
     * @param date
     * @return
     */
    @GetMapping("/day/{type}/{date}/pie")
    @ResponseBody
    public AjaxResult dayPieData(@PathVariable String type,@PathVariable String date) {
        //三元表达式判断请求url，将两个方法合并
        type = "in".equals(type) ? "收入" : "支出";
        List<Map<String,Object>> pieData = financeService.findPieDataByDate(date,type);
        //springMVC自动将对象转成JSON,在javascript中对象数组
        return new AjaxResult(pieData);
    }





    /**
     * 点击进入月报界面，只做跳转用
     * @return
     */
    @GetMapping("/month")
    public String monthHome() {
        return "finance/month";
    }

    /**
     * 通过datatables插件异步加载month.jsp页面的数据
     * @return
     */
    @GetMapping("/month/load")
    @ResponseBody
    public AjaxResult loadMonth(HttpServletRequest request) {
        /*select tf.type,sum(tf.money) from t_finance as tf
        where tf.confirm_user is not null and tf.confirm_date = '2017-02-25'
        group by tf.type*/

        return null;
    }


}
