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
@Table(name="project")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnCnf(description = "项目编号")
    private String code;

    @ColumnCnf(description = "项目名称")
    private String name;
    private byte level;

    private byte state;

    @ColumnCnf(description = "项目负责人")
    @Column(name = "manager_id")
    private int managerId;

    @ColumnCnf(description = "项目联系人")
    @Column(name = "linkman_id")
    private int linkmanId;

    private String creator;


    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = GlobalVar.dtShortFmt, timezone = "Asia/Shanghai")
    @Column(name = "plan_start")
    @ColumnCnf(description = "计划开始时间")
    private Date planStart;

    @JsonFormat(pattern = GlobalVar.dtShortFmt, timezone = "Asia/Shanghai")
    @Column(name = "plan_end")
    @ColumnCnf(description = "计划结束时间")
    private Date planEnd;

    @JsonFormat(pattern = GlobalVar.dtShortFmt, timezone = "Asia/Shanghai")
    @Column(name = "real_start")
    @ColumnCnf(description = "实际开始时间")
    private Date realStart;

    @JsonFormat(pattern = GlobalVar.dtShortFmt, timezone = "Asia/Shanghai")
    @Column(name = "real_end")
    @ColumnCnf(description = "实际结束时间")
    private Date realEnd;

    private String resume;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtLongFmt, timezone = "Asia/Shanghai")
    @Column(name = "create_time",insertable = false)
    private Date createTime;

    @Column(name = "pjt_key")
    private String pjtKey;

    @Column(name = "company_id")
    @ColumnCnf(description = "所属客户")
    private int companyId;




    private byte percent;

    @Transient
    private Long consumingTime;

    @Transient
    private String companyName;

    @Transient
    private String managerName;

    @Transient
    private String linkman;

}
