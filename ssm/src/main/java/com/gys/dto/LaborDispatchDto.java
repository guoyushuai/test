package com.gys.dto;

import java.util.List;

public class LaborDispatchDto {


    /**
     * laborArray : [{"id":"1","name":"电焊工","unit":"人","price":"100","num":"1","total":100},{"id":"2","name":"司机","unit":"人","price":"120","num":"1","total":120}]
     * fileArray : [{"sourceName":"阿里巴巴Java开发手册.pdf","newName":"2509d56e-ffd9-4ffb-bff1-92023a42b0a9.pdf"},{"sourceName":"阿里java面试题.doc","newName":"f9cdcdfb-1d08-4e4b-baf5-45d02a256bd3.doc"}]
     * companyName : 凯盛
     * tel : 123
     * dispatchDate : 2017-03-09
     * linkMan : 哥哥
     * address : 人民路
     * backDate : 2017-03-10
     * cardNum : 123
     * fax : 123
     * totalDays : 1
     */

    private String companyName;
    private String tel;
    private String dispatchDate;
    private String linkMan;
    private String address;
    private String backDate;
    private String cardNum;
    private String fax;
    private Integer totalDays;
    private List<LaborArrayBean> laborArray;
    private List<FileArrayBean> fileArray;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(String dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBackDate() {
        return backDate;
    }

    public void setBackDate(String backDate) {
        this.backDate = backDate;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public List<LaborArrayBean> getLaborArray() {
        return laborArray;
    }

    public void setLaborArray(List<LaborArrayBean> laborArray) {
        this.laborArray = laborArray;
    }

    public List<FileArrayBean> getFileArray() {
        return fileArray;
    }

    public void setFileArray(List<FileArrayBean> fileArray) {
        this.fileArray = fileArray;
    }

    public static class LaborArrayBean {
        /**
         * id : 1
         * name : 电焊工
         * unit : 人
         * price : 100
         * num : 1
         * total : 100
         */

        private Integer id;
        private String name;
        private String unit;
        private Float price;
        private Integer num;
        private Float total;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public Float getTotal() {
            return total;
        }

        public void setTotal(Float total) {
            this.total = total;
        }
    }

    public static class FileArrayBean {
        /**
         * sourceName : 阿里巴巴Java开发手册.pdf
         * newName : 2509d56e-ffd9-4ffb-bff1-92023a42b0a9.pdf
         */

        private String sourceName;
        private String newName;

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getNewName() {
            return newName;
        }

        public void setNewName(String newName) {
            this.newName = newName;
        }
    }
}
