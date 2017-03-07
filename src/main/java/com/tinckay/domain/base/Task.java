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
@Table(name = "task")
@Getter
@Setter
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "pjt_id")
    private Long pjtId;

    private short num;
    @Column(name = "front_num")
    private short frontNum;


    private String tilte;
    private String resume;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtShortFmt, timezone = "Asia/Shanghai")
    @Column(name = "plan_start")
    private Date planStart;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtShortFmt, timezone = "Asia/Shanghai")
    @Column(name = "plan_end")
    private Date planEnd;

    @Column(name = "plan_time")
    private Byte planTime;

    private byte state;
    private byte progress;
    private byte level;

    @Column(name = "has_child")
    private boolean hasChild;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtShortFmt, timezone = "Asia/Shanghai")
    @Column(name = "real_start")
    private Date realStart;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtShortFmt, timezone = "Asia/Shanghai")
    @Column(name = "real_end")
    private Date realEnd;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtLongFmt, timezone = "Asia/Shanghai")
    @Column(name = "create_time",insertable = false)
    private Date createTime;

    private int creator;

    private int manager;

    @Column(name = "task_key")
    private String taskKey;

    @Column(name = "order_id")
    private short orderId;

    @Transient
    private String managerName;

    @Transient
    private String creatorName;

    @Transient
    private int remainDays;
}
