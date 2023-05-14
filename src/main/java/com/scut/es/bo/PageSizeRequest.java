package com.scut.es.bo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageSizeRequest {

    protected Integer pageNum = 1; //页数，从1开始

    protected Integer pageSize = 10;

    /**
     * 每页最大值
     */
    private static final int MAX_PAGE_SIZE = 10000;

    public Integer getPageSize() {
        if (pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("每页数据量最多为100");
        }
        return pageSize;
    }
}
