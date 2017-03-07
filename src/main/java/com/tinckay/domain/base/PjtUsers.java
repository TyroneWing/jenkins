package com.tinckay.domain.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tinckay.common.GlobalVar;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by root on 12/26/16.
 */
@Entity
@Table(name = "pjt_users")
@Getter
@Setter
public class PjtUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    private String pjtKey;


    private Integer userId;


    private byte roleType;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtLongFmt, timezone = "Asia/Shanghai")
    @Column(insertable = false)
    private Date outedTime;


    private boolean outed;


    private String outedResume;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtLongFmt, timezone = "Asia/Shanghai")
    @Column(insertable = false)
    private Date createTime;

    private String creator;

    @Transient
    private String name;


}
