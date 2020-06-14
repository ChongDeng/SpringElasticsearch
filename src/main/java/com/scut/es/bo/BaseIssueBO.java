package com.scut.es.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseIssueBO implements Serializable {

    private Long id;

    /**
     * issue
     */
    private String type; //添加这个字段，是为了区分发送到MQ的数据类型是啥。便于后面功能的扩展

    /**
     * remove
     * update
     * insert
     */
    private String action;//从MQ获取数据后，根据该字段来对ES作相应操作。

    private String curUser; //用来记录数据发送者的用户名

}
