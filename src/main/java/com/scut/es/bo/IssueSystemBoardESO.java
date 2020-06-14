package com.scut.es.bo;

import lombok.Data;

@Data
public class IssueSystemBoardESO {

    private Long id;

    private Long issueId;

    private Long systemBoardId;

    private String version;
}
