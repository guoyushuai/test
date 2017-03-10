package com.gys.service.impl;

import com.google.common.collect.Lists;
import com.gys.dto.DeviceRentDto;
import com.gys.dto.wx.TextMessage;
import com.gys.exception.ServiceException;
import com.gys.mapper.*;
import com.gys.pojo.*;
import com.gys.service.DeviceService;
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
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private DeviceRentMapper rentMapper;
    @Autowired
    private DeviceRentDetailMapper rentDetailMapper;
    @Autowired
    private DeviceRentDocMapper rentDocMapper;

    @Autowired
    private FinanceMapper financeMapper;

    @Autowired
    private WeiXinService weiXinService;

    @Override
    public void saveNewDevice(Device device) {
        //新增数据时当前库存数量等于总数量
        device.setCurrentNum(device.getTotalNum());
        deviceMapper.save(device);
    }

    @Override
    public List<Device> findAllDevices() {
        return deviceMapper.findAll();
    }

    @Override
    public List<Device> findDeviceByPage(String start, String length) {
        return deviceMapper.findByPage(start,length);
    }

    @Override
    public Long count() {
        return deviceMapper.count();
    }

    @Override
    public List<Device> findDeviceBySearchParam(Map<String, Object> searchParam) {
        return deviceMapper.findBySearchParam(searchParam);
    }

    @Override
    public void delDevice(Integer id) {
        deviceMapper.del(id);
    }

    @Override
    public Long countBySearchParam(Map<String, Object> searchParam) {
        return deviceMapper.countBySearchParam(searchParam);
    }

    @Override
    public Device findDeviceById(Integer id) {
        return deviceMapper.findById(id);
    }

    @Override
    @Transactional
    public String saveDeviceRent(DeviceRentDto deviceRentDto) {
        //1. 保存合同
        DeviceRent deviceRent = new DeviceRent();
        deviceRent.setCompanyName(deviceRentDto.getCompanyName());
        deviceRent.setLinkMan(deviceRentDto.getLinkMan());
        deviceRent.setCardNum(deviceRentDto.getCardNum());
        deviceRent.setTel(deviceRentDto.getTel());
        deviceRent.setAddress(deviceRentDto.getAddress());
        deviceRent.setFax(deviceRentDto.getFax());
        deviceRent.setRentDate(deviceRentDto.getRentDate());
        deviceRent.setBackDate(deviceRentDto.getBackDate());
        deviceRent.setTotalDays(deviceRentDto.getTotalDays());
        deviceRent.setCreateUser(ShiroUtil.getCurrentUsername());
        //必须先存合同获得主键值后，再存合同详情，先设成0，否则在下边存的时候需要重新循环一次
        deviceRent.setLastCost(0F);
        deviceRent.setPreCost(0F);
        deviceRent.setTotalPrice(0F);

        //生成合同流水号
        deviceRent.setSerialNumber(SerialNumberUtil.getSerialNumber());

        //deviceRent的状态默认未完成

        //保存成功后，mybatis中获得的自动增长的主键值就自动赋值给了deviceRent对象的id属性
        rentMapper.save(deviceRent);

        //2. 保存租赁合同中每种设备的租赁详情
        List<DeviceRentDto.DeviceArrayBean> deviceArray  = deviceRentDto.getDeviceArray();
        //一次插入多条
        List<DeviceRentDetail> RentDetailList = Lists.newArrayList();
        float total = 0F;
        for(DeviceRentDto.DeviceArrayBean bean : deviceArray) {

            //查询当前设备的库存
            Device device = deviceMapper.findById(bean.getId());
            //库存大于租赁数量时,更新数据库中库存数量；否则抛出库存不足异常，租赁合同不生效
            if(device.getCurrentNum() > bean.getNum()) {
                device.setCurrentNum(device.getCurrentNum() - bean.getNum());
                deviceMapper.updateCurrentNum(device);
            } else {
                throw new ServiceException(device.getName() + "库存不足");
            }

            //添加每种设备的租赁信息，放入集合中
            DeviceRentDetail rentDetail = new DeviceRentDetail();
            rentDetail.setDeviceName(bean.getName());
            rentDetail.setTotalPrice(bean.getTotal());
            rentDetail.setDevicePrice(bean.getPrice());
            rentDetail.setDeviceUnit(bean.getUnit());
            rentDetail.setNum(bean.getNum());

            rentDetail.setRentId(deviceRent.getId());

            RentDetailList.add(rentDetail);

            //每种设备的每天租赁总价相加
            total += bean.getTotal();
        }
        //严谨，确保集合不为空，避免sql出错
        if(!RentDetailList.isEmpty()) {
            //批量插入
            rentDetailMapper.batchSave(RentDetailList);
        }

        //计算合同总价及预付款、尾款，并更新租赁合同DeviceRent中没有填写的值
        total = total * deviceRent.getTotalDays();
        float preCost = total  * 0.3F;
        float lastCost = total - preCost;
        rentMapper.updateCost(total,preCost,lastCost,deviceRent.getId());

        //3. 保存租赁合同上传的文件
        List<DeviceRentDto.FileArrayBean> fileArray = deviceRentDto.getFileArray();
        List<DeviceRentDoc> rentDocList = Lists.newArrayList();
        for(DeviceRentDto.FileArrayBean bean : fileArray) {
            DeviceRentDoc rentDoc = new DeviceRentDoc();
            rentDoc.setNewName(bean.getNewName());
            rentDoc.setSourceName(bean.getSourceName());

            rentDoc.setRentId(deviceRent.getId());

            rentDocList.add(rentDoc);
        }
        //严谨，确保集合不为空，避免sql出错
        if(!rentDocList.isEmpty()) {
            //批量插入
            rentDocMapper.batchSave(rentDocList);
        }


        //4. 写入财务流水
        Finance finance = new Finance();
        finance.setSerialNumber(SerialNumberUtil.getSerialNumber());//生成财务流水号
        finance.setType(Finance.TYPE_INCOME);//收入
        finance.setMoney(preCost);//预付款
        finance.setState(Finance.STATE_UNFINISHED);//显示未确认
        finance.setModule(Finance.MODULE_DEVICE);//来自设备租赁
        finance.setCreateUser(ShiroUtil.getCurrentUsername());//创建者
        finance.setCreateDate(DateTime.now().toString("yyyy-MM-dd"));//创建时间，注意格式大小写
        finance.setRemark(Finance.REMARK_PRECOST);//备注来自预付款
        finance.setModuleSerialNumber(deviceRent.getSerialNumber());//业务流水号

        financeMapper.save(finance);//保存预付款的财务

        /*//5、用微信给财务部发送消息
        TextMessage message = new TextMessage();
        TextMessage.TextBean textBean = new TextMessage.TextBean();//内部类
        textBean.setContent("设备租赁模块添加一笔财务流水[预付款]，请确认");
        message.setText(textBean);
        message.setToparty("5");//财务部ID

        weiXinService.sendTextMessage(message);*/

        //获取租赁合同流水号
        return deviceRent.getSerialNumber();

    }

    @Override
    public DeviceRent findDeviceRentBySerialNumber(String serialNumber) {
        return rentMapper.findBySerialNumber(serialNumber);
    }

    //根据设备租赁合同ID查询详情列表
    @Override
    public List<DeviceRentDetail> findDeviceRentDetailListByRentId(Integer id) {
        return rentDetailMapper.findByRentId(id);
    }

    //根据设备租赁合同ID查询文件列表
    @Override
    public List<DeviceRentDoc> findDeviceRentDocListByRentId(Integer id) {
        return rentDocMapper.findByRentId(id);
    }




    @Override
    public List<DeviceRent> findDeviceRentByQueryParam(Map<String, Object> queryParam) {
        return rentMapper.findByQueryParam(queryParam);
    }

    @Override
    public Long deviceRentCount() {
        return rentMapper.count();
    }

    @Override
    @Transactional
    public void changeRentState(Integer id) {
        //1、修改合同的状态为已完成
        DeviceRent deviceRent = rentMapper.findById(id);
        deviceRent.setState("已完成");
        rentMapper.updateState(deviceRent);

        //2、合同租用的设备入库
        /*根据租赁合同id查找到合同中租赁的所有设备详情集合*/
        List<DeviceRentDetail> rentDetailList = rentDetailMapper.findByRentId(id);
        /*List<Device> deviceList = Lists.newArrayList();*/
        for (DeviceRentDetail rentDetail : rentDetailList) {
            String deviceName = rentDetail.getDeviceName();
            Integer rentNum = rentDetail.getNum();

            //根据设备名称查找到设备
            Device device = deviceMapper.findByName(deviceName);
            device.setCurrentNum(device.getCurrentNum() + rentNum);

            deviceMapper.update(device);
            /*deviceList.add(device);*/
        }
        /*deviceMapper.batchUpdate(deviceList);*/

        //3、财务模块处理
        //获得合同尾款
        float lastCost = deviceRent.getLastCost();

        //尾款新的财务流水与预付款无关
        Finance finance = new Finance();
        finance.setSerialNumber(SerialNumberUtil.getSerialNumber());//流水号
        finance.setType(Finance.TYPE_INCOME);//收入
        finance.setMoney(lastCost);//尾款
        finance.setState(Finance.STATE_UNFINISHED);//默认未完成
        finance.setModule(Finance.MODULE_DEVICE);//来自设备租赁
        finance.setCreateUser(ShiroUtil.getCurrentUsername());//创建者
        finance.setCreateDate(DateTime.now().toString("yyyy-MM-dd"));//创建时间
        finance.setRemark(Finance.REMARK_LASTCOST);//备注来自尾款
        finance.setModuleSerialNumber(deviceRent.getSerialNumber());//业务流水号

        financeMapper.save(finance);

        /*//4、用微信给财务部发送消息
        TextMessage message = new TextMessage();
        TextMessage.TextBean textBean = new TextMessage.TextBean();//内部类
        textBean.setContent("设备租赁模块添加一笔财务流水[尾款]，请确认");
        message.setText(textBean);
        message.setToparty("5");//财务部ID

        weiXinService.sendTextMessage(message);*/

    }
}
