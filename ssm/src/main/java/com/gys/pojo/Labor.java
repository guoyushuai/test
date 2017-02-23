package com.gys.pojo;

import lombok.Data;

@Data
public class Labor {

    private Integer id;
    private String name;
    private String unit;
    private Integer totalNum;
    private Integer currentNum;
    private Float price;
}
