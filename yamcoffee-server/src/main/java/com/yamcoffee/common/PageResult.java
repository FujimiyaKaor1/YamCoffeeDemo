package com.yamcoffee.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {
    
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pages;
    private List<T> list;
    
    public PageResult() {
    }
    
    public PageResult(Long total, Integer pageNum, Integer pageSize, List<T> list) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = (int) Math.ceil((double) total / pageSize);
        this.list = list;
    }
}
