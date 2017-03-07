package com.tinckay.domain.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by root on 12/26/16.
 */
@Entity
@Table(name = "opt")
@Getter
@Setter

public class Opt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pjt_id")
    private Long pjtId = Long.valueOf(0);

    @Column(name = "task_id")
    private Long taskId = Long.valueOf(0);

    @Column(name = "event_id")
    private Long eventId = Long.valueOf(0);

    @Column(name = "user")
    private String userName;

    @Column(name = "create_time")
    private Date createTime;

    private String resume;

    private String ip;

    private byte flag;


}
