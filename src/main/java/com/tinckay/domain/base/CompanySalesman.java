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
@Table(name = "company_salesman")
public class CompanySalesman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String companyKey;



    private int userId;
    private boolean main;
    private String creator;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtLongFmt, timezone = "Asia/Shanghai")
    @Column(insertable = false)
    private Date createTime;
    private boolean outed;


    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtLongFmt, timezone = "Asia/Shanghai")
    @Column(insertable = false)
    private Date outedTime;


    private String outedResume;

    @Transient
    private String name;


}
