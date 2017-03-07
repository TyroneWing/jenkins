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
@Table(name = "company_linkman")
public class CompanyLinkman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String companyKey;

    private String name;
    private boolean sex;
    private String email;
    private String mobile;
    private String position;
    private String resume;
    private String creator;

    private boolean main;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtLongFmt, timezone = "Asia/Shanghai")
    @Column(insertable = false)
    private Date createTime;




}
