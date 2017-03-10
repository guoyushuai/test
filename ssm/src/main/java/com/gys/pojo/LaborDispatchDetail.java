package com.gys.pojo;

import lombok.Data;

@Data
public class LaborDispatchDetail {

    private Integer id;
    private String laborName;
    private String laborUnit;
    private Float laborPrice;
    private Integer num;
    private Float totalPrice;
    private Integer dispatchId;
}
