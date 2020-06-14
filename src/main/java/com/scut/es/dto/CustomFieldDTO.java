package com.scut.es.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CustomFieldDTO implements Serializable {

    private Long fieldId;

    private String fieldName;

    private Integer fieldType; //类型: 1List 2TextField 3TextBox 4CheckBox

    private List<String> value1;

    private String value2;

    private String value3;

    private List<String> value4;

}
