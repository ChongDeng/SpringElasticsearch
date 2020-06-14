package com.scut.es.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchHistoryBO implements Serializable {

    //搜索历史id: 创建的时候，不用带上该参数； 修改的时候需要带上该参数
    private String id;

    //给本次搜索取的名字
    private String name;

    //搜索的关键词
    private String keyword;

    //该搜索在ui显示时，各列的顺序和排序规则
    private List<SearchDisplayColumn> searchDisplayColumns;

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
    private List<Long> systemBoardIds;

    //issue严重性列表
    private List<Integer> severities;

    //issue的创建者id列表
    private List<Long> reportedByUserIds;

    //issue的开闭状态
    private Boolean isClose;

    //issue的创建者id列表
    private List<Long> creatorIds;

    //issue的创建者所属公司id的列表
    private List<Long> creatorsOfCompanyIds;

    //issue的指派者所属公司id的列表
    private List<Long> assignedOfCompanyIds;
}
