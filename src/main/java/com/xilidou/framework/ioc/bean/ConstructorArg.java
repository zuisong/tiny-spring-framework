package com.xilidou.framework.ioc.bean;


import lombok.Data;
import lombok.ToString;

@Data
public class ConstructorArg {

    private int index;
    private String ref;
    private String type;
    private Object value;
}
