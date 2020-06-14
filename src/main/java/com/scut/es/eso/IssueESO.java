package com.scut.es.eso;

import com.scut.es.bo.CustomField;
import com.scut.es.bo.IssueSystemBoardESO;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@Document(indexName = "issue_eso")
@Mapping(mappingPath="es/issue_mappings.json")
public class IssueESO {

    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Long)
    private Long componentPartId;

    @Field(type = FieldType.Text)
    private String componentPartStr;

    @Field(type = FieldType.Long)
    private Long assignedToId;

    @Field(type = FieldType.Long)
    private Long issueTypeId;

    @Field(type = FieldType.Text)
    private String issueTypeVal;//添加该字段，是因为ui需要。 (请参考ui设计的第5页)

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word"),
            otherFields = {
                    @InnerField(type = FieldType.Text, suffix = "jianpin", analyzer = "jianpin"),
                    @InnerField(type = FieldType.Text, suffix = "quanpin", analyzer = "quanpin"),
                    @InnerField(type = FieldType.Text, suffix = "standard", analyzer = "standard")
            })
    private String title;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word"),
            otherFields = {
                    @InnerField(type = FieldType.Text, suffix = "jianpin", analyzer = "jianpin"),
                    @InnerField(type = FieldType.Text, suffix = "quanpin", analyzer = "quanpin"),
                    @InnerField(type = FieldType.Text, suffix = "standard", analyzer = "standard")
            })
    private String description;

    @Field(type = FieldType.Long)
    private Long creator;

    @Field(type = FieldType.Integer)
    private Integer priority;

    @Field(type = FieldType.Text)
    private String priorityStr;

    @Field(type = FieldType.Integer)
    private Integer status;

    @Field(type = FieldType.Text)
    private String statusStr;

    @Field(type = FieldType.Integer)
    private Integer severity;

    @Field(type = FieldType.Text)
    private String severityStr;

    @Field(type = FieldType.Boolean)
    private Boolean isClose = false;

    @Field(type = FieldType.Boolean)
    private Boolean internalOnly = false;

    @Field(type = FieldType.Text)
    private String createdBy;

    @Field(type = FieldType.Date)
    private Date created;

    @Field(type = FieldType.Text)
    private String modifiedBy;

    @Field(type = FieldType.Date)
    private Date modified;

    @Field(type = FieldType.Long)
    private Long projectId;

    @Field(type = FieldType.Text)
    private String projectTicketNo;

    @Field(type = FieldType.Boolean)
    private Boolean isDelete = false;

    @Field(type = FieldType.Text)
    private String assignedToUserName;

    @Field(type = FieldType.Text)
    private String projectName;

    //不再被使用: 没法从AddSIssueESBO类转过来
    @Field(type = FieldType.Text)
    private String componentPart;//添加该字段，是因为ui需要。 (请参考ui设计的第5页)

    //不再被使用了.
    @Field(type = FieldType.Object)
    private List<IssueSystemBoardESO> systemBoard;//添加该字段，是因为ui需要。 (请参考ui设计的第5页)

    @Field(type = FieldType.Long)
    private Long issueId;

    @Field(type = FieldType.Long)
    private List<Long> systemBoardIdList;

    @Field(type = FieldType.Long)
    private Long supplierId;

    @Field(type = FieldType.Text)
    private String supplierName;

    @Field(type = FieldType.Nested)
    private List<CustomField> customFields;
}


