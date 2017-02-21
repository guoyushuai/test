package com.gys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data//自动生成各种get,set,toString
@AllArgsConstructor//自动生成所有的构造方法
public class DataTablesResult {

    private String draw;
    private Long recordsTotal;
    private Long recordsFiltered;
    private Object data;

}
