package com.scut.es.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AddIssueESBO extends BaseIssueBO implements Serializable {

    private Long componentPartId;

    private String componentPartStr;//添加该字段，是因为ui需要。 (请参考ui设计的第5页)

    private Long assignedToId;

    private Long issueTypeId;

    private String issueTypeVal;//添加该字段，是因为ui需要。 (请参考ui设计的第5页)

    private String title;

    private String description;

    private Long creator;

    private Integer priority;

    private String priorityStr;

    private Integer status;

    private String statusStr;

    private Integer severity;

    private String severityStr;

    private Boolean isClose;

    private Boolean internalOnly;

    private String createdBy;

    private Date created;

    private String modifiedBy;

    private Date modified;

    private Long projectId;

    private String projectTicketNo;

    private Boolean isDelete;

    private String assignedToUserName;

    private String projectName;

    //不再被使用了.
    private List<IssueSystemBoardESO> systemBoard; //添加该字段，是因为ui需要。 (请参考ui设计的第5页)

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

    private List<Long> systemBoardIdList;

    private Long issueId;

    private Long supplierId;

    private String supplierName;

    private List<CustomField> customFields;
}
