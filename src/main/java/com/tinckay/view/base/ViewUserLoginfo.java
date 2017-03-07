package com.tinckay.view.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by root on 2/28/17.
 */
@Getter
@Setter
public class ViewUserLoginfo implements Serializable{
    private long id;
    private byte flag;
    private int companyId;
    private int pjtId;
    private long taskId;
    private String title;
    private String resume;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm", timezone = "Asia/Shanghai")
    private Date time;
    private String planResume;
    private int linkmanId;
    private String creator;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm", timezone = "Asia/Shanghai")
    private Date createTime;
    private String userLoginfoKey;
    private String companyName;
    private String pjtName;
    private String linkman;
    private String userLogTypeName;
}
