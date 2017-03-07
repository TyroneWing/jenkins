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
@Table(name = "up_file")
@Getter
@Setter
public class UpFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private int pjtId;

    private long taskId;

    private long eventId;



    private String userLoginfoKey;

    private String name;
    private String type;
    private Integer size;
    private String path;

    private String resume;
    private byte flag;

    private String creator;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtLongFmt, timezone = "Asia/Shanghai")
    @Column(insertable = false)
    private Date createTime;

}
