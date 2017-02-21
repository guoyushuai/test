package com.gys.service.impl;

import com.google.common.collect.Lists;
import com.gys.dto.DeviceRentDto;
import com.gys.exception.ServiceException;
import com.gys.mapper.DeviceMapper;
import com.gys.mapper.DeviceRentDetailMapper;
import com.gys.mapper.DeviceRentDocMapper;
import com.gys.mapper.DeviceRentMapper;
import com.gys.pojo.Device;
import com.gys.pojo.DeviceRent;
import com.gys.pojo.DeviceRentDetail;
import com.gys.pojo.DeviceRentDoc;
import com.gys.service.DeviceService;
import com.gys.shiro.ShiroUtil;
import com.gys.util.SerialNumberUtil;
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

        //获取合同流水号
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

        //返回序列号
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

        //2财务模块处理


    }
}
