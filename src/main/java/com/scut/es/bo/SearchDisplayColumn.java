package com.scut.es.bo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SearchDisplayColumn {

    //UI上展示的列的名字: 例如 "projectName", "projectTicketNo", "title", "assignedToUserName", "status", "priority", "modified", "severity", "internalOnly", "createdBy", "componentPartStr", "supplierName"
    private String columnName;

    //该列在其他列中的显示顺序： 例如： 值为1表示该列显示在第1列; 值为2表示该列显示在第2列
    private int columnOrder;

    //该列上的显示排序： 如果isAscending为tru: 表示该列按照升序来排列显示
    private Boolean isAscending;
}
