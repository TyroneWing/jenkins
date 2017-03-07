package com.tinckay.domain.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tinckay.common.GlobalVar;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_loginfo")
public class UserLoginfo {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private byte flag;


    private int companyId;


    private int pjtId;


    private long taskId;
    private String title;
    private String resume;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtShortFmt, timezone = "Asia/Shanghai")
    private Date time;


    private String planResume;


    private int linkmanId;
    private String creator;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtLongFmt, timezone = "Asia/Shanghai")
    @Column(insertable = false)
    private Date createTime;



    private String userLoginfoKey;

    @Transient
    private String companyName;

    @Transient
    private String pjtName;

    @Transient
    private String linkman;

}
