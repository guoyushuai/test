package com.gys.util;

import java.util.List;

/**
 * 对分页结果的封装
 */
public class Page<T> {

    //总页数
    private int totalPage;
    //当前页码
    private int pageNo;
    //当前页数据!!!!!将页面数据封装到page中
    private List<T> items;

    //总条数
    private int totals;
    //每页显示的数据量
    private int pageSize = 5;
    //当前页的起始行号
    private int start;

    public Page(int totals,int pageNo) {

        this.totals = totals;

        //获取总页数
        totalPage = totals / pageSize;
        //如果有余数，最大页数加1
        if(totals % pageSize != 0) {
            totalPage++;
        }

        //页码大于最大页数，定位到最后一页
        if(pageNo > totalPage) {
            pageNo = totalPage;
        }

        //页码小于1，定位到第一页
        if(pageNo < 1) {
            pageNo = 1;
        }

        this.pageNo = pageNo;

        //计算当前页的起始行数
        start = (pageNo - 1) * pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
