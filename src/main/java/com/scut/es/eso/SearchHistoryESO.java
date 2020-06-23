package com.scut.es.eso;

import com.scut.es.bo.SearchDisplayColumn;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@Document(indexName = "search_history")
@Mapping(mappingPath="es/searchhistory.json")
public class SearchHistoryESO {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    //哪个用户发起的搜索
    @Field(type = FieldType.Long)
    private long userId;

    //保存或修改搜索历史的时间
    @Field(type = FieldType.Date)
    private Date modified;

    //给本次搜索取的名字
    @Field(type = FieldType.Keyword)
    private String name;

    //给本次搜索取的名字 V2
    @Field(type = FieldType.Text)
    private String searchHistoryName;

    //搜索的关键词
    @Field(type = FieldType.Text)
    private String keyword;

    //该搜索在ui显示时，各列的顺序和排序规则
    @Field(type = FieldType.Object)
    private List<SearchDisplayColumn> searchDisplayColumns;

    //项目id列表
    @Field(type = FieldType.Long)
    private List<Long> projectIds;

    //issue状态id列表
    @Field(type = FieldType.Integer)
    private List<Integer> issueStatusIds;

    //优先级列表
    @Field(type = FieldType.Integer)
    private List<Integer> priorities;

    //被分配者的id列表
    @Field(type = FieldType.Long)
    private List<Long> assignedToIds;

    //多少天之内创建的该issue
    @Field(type = FieldType.Integer)
    private int createdDaysRange;

    //多少天之内修改的该issue列表
    @Field(type = FieldType.Integer)
    private int modifiedDaysRange;

    //issue类型id
    @Field(type = FieldType.Long)
    private List<Long> issueTypeIds;

    //componentpart id列表
    @Field(type = FieldType.Long)
    private List<Long> componentPartIds;

    //system board id列表
    @Field(type = FieldType.Long)
    private List<Long> systemBoardIds;

    //issue严重性列表
    @Field(type = FieldType.Integer)
    private List<Integer> severities;

    //issue的创建者id列表
    @Field(type = FieldType.Long)
    private List<Long> reportedByUserIds;

    //issue的开闭状态
    @Field(type = FieldType.Boolean)
    private Boolean isClose;

    //issue的创建者id列表
    @Field(type = FieldType.Long)
    private List<Long> creatorIds;

    //issue的创建者所属公司id的列表
    @Field(type = FieldType.Long)
    private List<Long> creatorsOfCompanyIds;

    //issue的指派者所属公司id的列表
    @Field(type = FieldType.Long)
    private List<Long> assignedOfCompanyIds;
}
