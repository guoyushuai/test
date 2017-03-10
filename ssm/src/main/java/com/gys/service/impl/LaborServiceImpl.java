package com.gys.service.impl;

import com.google.common.collect.Lists;
import com.gys.dto.DeviceRentDto;
import com.gys.dto.LaborDispatchDto;
import com.gys.dto.wx.TextMessage;
import com.gys.exception.ServiceException;
import com.gys.mapper.*;
import com.gys.pojo.*;
import com.gys.service.LaborService;
import com.gys.service.WeiXinService;
import com.gys.shiro.ShiroUtil;
import com.gys.util.SerialNumberUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class LaborServiceImpl implements LaborService {

    @Autowired
    private LaborMapper laborMapper;

    @Autowired
    private LaborDispatchMapper dispatchMapper;
    @Autowired
    private LaborDispatchDetailMapper dispatchDetailMapper;
    @Autowired
    private LaborDispatchDocMapper dispatchDocMapper;

    @Autowired
    private FinanceMapper financeMapper;

    @Autowired
    private WeiXinService weiXinService;

    @Override
    public void saveNewLabor(Labor labor) {
        labor.setCurrentNum(labor.getTotalNum());
        laborMapper.save(labor);
    }

    @Override
    public List<Labor> findLaborBySearchParam(Map<String, Object> searchParam) {
        return laborMapper.findBySearchParam(searchParam);
    }

    @Override
    public Long count() {
        return laborMapper.count();
    }

    @Override
    public Long countBySearchParam(Map<String, Object> searchParam) {
        return laborMapper.countBySearchParam(searchParam);
    }

    @Override
    public void delLabor(Integer id) {
        laborMapper.del(id);
    }

    @Override
    public List<Labor> findAllLabors() {
        return laborMapper.findAll();
    }

    @Override
    public Labor findLaborById(Integer id) {
        return laborMapper.findById(id);
    }

    @Override
    @Transactional
    public String saveLaborDispatch(LaborDispatchDto laborDispatchDto) {
        //1. 保存合同
        LaborDispatch laborDispatch = new LaborDispatch();
        laborDispatch.setCompanyName(laborDispatchDto.getCompanyName());
        laborDispatch.setLinkMan(laborDispatchDto.getLinkMan());
        laborDispatch.setCardNum(laborDispatchDto.getCardNum());
        laborDispatch.setTel(laborDispatchDto.getTel());
        laborDispatch.setAddress(laborDispatchDto.getAddress());
        laborDispatch.setFax(laborDispatchDto.getFax());
        laborDispatch.setDispatchDate(laborDispatchDto.getDispatchDate());
        laborDispatch.setBackDate(laborDispatchDto.getBackDate());
        laborDispatch.setTotalDays(laborDispatchDto.getTotalDays());
        laborDispatch.setCreateUser(ShiroUtil.getCurrentUsername());
        //必须先存合同获得主键值后，再存合同详情，先设成0，否则在下边存的时候需要重新循环一次
        laborDispatch.setLastCost(0F);
        laborDispatch.setPreCost(0F);
        laborDispatch.setTotalPrice(0F);

        //生成合同流水号
        laborDispatch.setSerialNumber(SerialNumberUtil.getSerialNumber());

        //laborDispatch的状态默认未完成

        //保存成功后，mybatis中获得的自动增长的主键值就自动赋值给了laborDispatch对象的id属性
        dispatchMapper.save(laborDispatch);

        //2. 保存雇佣合同中每个工种的雇佣详情
        List<LaborDispatchDto.LaborArrayBean> laborArray  = laborDispatchDto.getLaborArray();
        //一次插入多条
        List<LaborDispatchDetail> dispatchDetailList = Lists.newArrayList();
        float total = 0F;
        for(LaborDispatchDto.LaborArrayBean bean : laborArray) {

            //查询当前设备的库存
            Labor labor = laborMapper.findById(bean.getId());
            //库存大于租赁数量时,更新数据库中库存数量；否则抛出库存不足异常，租赁合同不生效
            if(labor.getCurrentNum() > bean.getNum()) {
                labor.setCurrentNum(labor.getCurrentNum() - bean.getNum());
                laborMapper.updateCurrentNum(labor);
            } else {
                throw new ServiceException(labor.getName() + "库存不足");
            }

            //添加每个工种的雇佣信息，放入集合中
            LaborDispatchDetail dispatchDetail = new LaborDispatchDetail();
            dispatchDetail.setLaborName(bean.getName());
            dispatchDetail.setTotalPrice(bean.getTotal());
            dispatchDetail.setLaborPrice(bean.getPrice());
            dispatchDetail.setLaborUnit(bean.getUnit());
            dispatchDetail.setNum(bean.getNum());

            dispatchDetail.setDispatchId(laborDispatch.getId());

            dispatchDetailList.add(dispatchDetail);

            //每个工种的每天雇佣总价相加
            total += bean.getTotal();
        }
        //严谨，确保集合不为空，避免sql出错
        if(!dispatchDetailList.isEmpty()) {
            //批量插入
            dispatchDetailMapper.batchSave(dispatchDetailList);
        }

        //计算合同总价及预付款、尾款，并更新雇佣合同laborDispatch中没有填写的值
        total = total * laborDispatch.getTotalDays();
        float preCost = total  * 0.3F;
        float lastCost = total - preCost;
        dispatchMapper.updateCost(total,preCost,lastCost,laborDispatch.getId());

        //3. 保存租赁合同上传的文件
        List<LaborDispatchDto.FileArrayBean> fileArray = laborDispatchDto.getFileArray();
        List<LaborDispatchDoc> dispatchDocList = Lists.newArrayList();
        for(LaborDispatchDto.FileArrayBean bean : fileArray) {
            LaborDispatchDoc dispatchDoc = new LaborDispatchDoc();
            dispatchDoc.setNewName(bean.getNewName());
            dispatchDoc.setSourceName(bean.getSourceName());

            dispatchDoc.setDispatchId(laborDispatch.getId());

            dispatchDocList.add(dispatchDoc);
        }
        //严谨，确保集合不为空，避免sql出错
        if(!dispatchDocList.isEmpty()) {
            //批量插入
            dispatchDocMapper.batchSave(dispatchDocList);
        }


        //4. 写入财务流水
        Finance finance = new Finance();
        finance.setSerialNumber(SerialNumberUtil.getSerialNumber());//生成财务流水号
        finance.setType(Finance.TYPE_INCOME);//收入
        finance.setMoney(preCost);//预付款
        finance.setState(Finance.STATE_UNFINISHED);//显示未确认
        finance.setModule(Finance.MODULE_LABOR);//来自劳务派遣
        finance.setCreateUser(ShiroUtil.getCurrentUsername());//创建者
        finance.setCreateDate(DateTime.now().toString("yyyy-MM-dd"));//创建时间，注意格式大小写
        finance.setRemark(Finance.REMARK_PRECOST);//备注来自预付款
        finance.setModuleSerialNumber(laborDispatch.getSerialNumber());//业务流水号

        financeMapper.save(finance);//保存预付款的财务

        /*//5、用微信给财务部发送消息
        TextMessage message = new TextMessage();
        TextMessage.TextBean textBean = new TextMessage.TextBean();//内部类
        textBean.setContent("劳务派遣模块添加一笔财务流水[预付款]，请确认");
        message.setText(textBean);
        message.setToparty("5");//财务部ID

        weiXinService.sendTextMessage(message);*/

        //获取租赁合同流水号
        return laborDispatch.getSerialNumber();
    }

    @Override
    public LaborDispatch findLaborDispatchBySerialNumber(String serialNumber) {
        return dispatchMapper.findBySerialNumber(serialNumber);
    }

    @Override
    public List<LaborDispatchDetail> findLaborDispatchDetailListByDispatchId(Integer id) {
        return dispatchDetailMapper.findByDispatchId(id);
    }

    @Override
    public List<LaborDispatchDoc> findLaborDispatchtDocListByDispatchtId(Integer id) {
        return dispatchDocMapper.findByDispatchId(id);
    }



    @Override
    public List<LaborDispatch> findLaborDispatchByQueryParam(Map<String, Object> queryParam) {
        return dispatchMapper.findByQueryParam(queryParam);
    }

    @Override
    public Long laborDispatchCount() {
        return dispatchMapper.count();
    }

    @Override
    @Transactional
    public void changeDispatchState(Integer id) {
        //1、修改合同的状态为已完成
        LaborDispatch laborDispatch = dispatchMapper.findById(id);
        laborDispatch.setState("已完成");
        dispatchMapper.updateState(laborDispatch);

        //2、合同租用的设备入库
        /*根据租赁合同id查找到合同中租赁的所有设备详情集合*/
        List<LaborDispatchDetail> dispatchDetailList = dispatchDetailMapper.findByDispatchId(id);
        /*List<Labor> laborList = Lists.newArrayList();*/
        for (LaborDispatchDetail dispatchDetail : dispatchDetailList) {
            String laborName = dispatchDetail.getLaborName();
            Integer rentNum = dispatchDetail.getNum();

            //根据设备名称查找到设备
            Labor labor = laborMapper.findByName(laborName);
            labor.setCurrentNum(labor.getCurrentNum() + rentNum);

            laborMapper.update(labor);
            /*deviceList.add(device);*/
        }
        /*deviceMapper.batchUpdate(deviceList);*/

        //3、财务模块处理
        //获得合同尾款
        float lastCost = laborDispatch.getLastCost();

        //尾款新的财务流水与预付款无关
        Finance finance = new Finance();
        finance.setSerialNumber(SerialNumberUtil.getSerialNumber());//流水号
        finance.setType(Finance.TYPE_INCOME);//收入
        finance.setMoney(lastCost);//尾款
        finance.setState(Finance.STATE_UNFINISHED);//默认未完成
        finance.setModule(Finance.MODULE_LABOR);//来自劳务派遣
        finance.setCreateUser(ShiroUtil.getCurrentUsername());//创建者
        finance.setCreateDate(DateTime.now().toString("yyyy-MM-dd"));//创建时间
        finance.setRemark(Finance.REMARK_LASTCOST);//备注来自尾款
        finance.setModuleSerialNumber(laborDispatch.getSerialNumber());//业务流水号

        financeMapper.save(finance);

        /*//4、用微信给财务部发送消息
        TextMessage message = new TextMessage();
        TextMessage.TextBean textBean = new TextMessage.TextBean();//内部类
        textBean.setContent("劳务派遣模块添加一笔财务流水[尾款]，请确认");
        message.setText(textBean);
        message.setToparty("5");//财务部ID

        weiXinService.sendTextMessage(message);*/

    }
}
