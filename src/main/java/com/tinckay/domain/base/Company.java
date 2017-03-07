package com.tinckay.domain.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tinckay.common.GlobalVar;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by root on 2/22/17.
 */
@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private byte flag;//0本公司，1客户，2供应商

    @Column(name = "area_code")
    @ColumnCnf(description = "地区编码")
    private String areaCode;

    @Column(name = "industry_id")
    private int industryId;

    @Column(name = "company_key")
    private String companyKey;

    private String code;

    @Column(name = "short_name")
    private String shortName;

    @ColumnCnf(description = "名称")
    private String name;
    private String www;
    private String email;
    private String fax;
    private String tel;
    private String address;
    private String resume;
    private String creator;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtLongFmt, timezone = "Asia/Shanghai")
    @Column(name = "create_time",insertable = false)
    private Date createTime;


    @Transient
    private int pjtNum;

    @Transient
    private String linkman;

    @Transient
    private String salesman;

    @Transient
    private String logInfo;


}
