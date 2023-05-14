package com.scut.es.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagination<T> {

    /**
     * 当前分页的数据集
     */
    private List<T> list;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 当前页
     */
    private long pageNum;

    /**
     * 每页记录数
     */
    private long pageSize;
}
