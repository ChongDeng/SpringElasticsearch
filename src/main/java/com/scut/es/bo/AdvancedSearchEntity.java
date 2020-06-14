package com.scut.es.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdvancedSearchEntity implements Serializable {
    //项目id列表
    private List<Long> projectIds;

    //issue状态id列表
    private List<Integer> issueStatusIds;

    //优先级列表
    private List<Integer> priorities;

    //被分配者的id列表
    private List<Long> assignedToIds;

    //多少天之内创建的该issue
    private int createdDaysRange;

    //多少天之内修改的该issue列表
    private int modifiedDaysRange;

    //issue类型id
    private List<Long> issueTypeIds;

    //componentpart id列表
    private List<Long> componentPartIds;

    //system board id列表
    private List<Long> SystemBoardIds;

    //issue严重性列表
    private List<Integer> severities;

    //issue的创建者id列表
    private List<Long> reportedByUserIds;

    //issue的开闭状态
    private Boolean isClose;

    //issue的创建者id列表
    private List<Long> creatorIds;

    //issue的自定义fields
    private List<CustomField> customFields;

    //customFieldItem的id列表: 同一个customfield下的各customFieldItemId组成一个list,并设为customFieldItemIds的元素
    private List<List<Long>> customFieldItemIds;
}
