package com.gys.pojo;

import lombok.Data;

@Data
public class Finance {

    public static final String TYPE_INCOME = "收入";
    public static final String TYPE_OUTGO = "支出";

    /*财务模块是否确认*/
    public static final String STATE_UNFINISHED = "未确认";
    public static final String STATE_COMPLETE = "已确认";

    public static final String MODULE_DEVICE = "设备租赁";
    public static final String MODULE_LABOR = "劳务派遣";

    public static final String REMARK_PRECOST = "预付款";
    public static final String REMARK_LASTCOST = "尾款";

    private Integer id;
    private String serialNumber;
    private String type;
    private Float money;
    private String state;
    private String module;
    private String createUser;
    private String createDate;
    private String confirmUser;
    private String confirmDate;
    private String remark;
    private String moduleSerialNumber;

}
