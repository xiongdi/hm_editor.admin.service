package com.hm.editor.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页属性
 * Created by dingyuanyuan on 2018/11/8.
 */
@Data
public class Page implements Serializable{

    private static final long serialVersionUID = -4629775364070595382L;

    /**
     * 总记录数
     */
    private long totalRecords;

    /**
     * 分页大小
     */
    private long pageSize=10;

    /**
     * 当前页码
     */
    private long currentPage=1;

    Page(){}

    public Page(long totalRecords, long pageSize, long currentPage){
        this.totalRecords = totalRecords;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }
}
