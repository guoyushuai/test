package com.gys.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LaborDispatch {

    private Integer id;
    private String companyName;
    private String linkMan;
    private String cardNum;
    private String tel;
    private String address;
    private String fax;
    private String dispatchDate;
    private String backDate;
    private Integer totalDays;
    private Float totalPrice;
    private Float preCost;
    private Float lastCost;
    private Timestamp createTime;
    private String createUser;
    private String serialNumber;
    private String state;
}
