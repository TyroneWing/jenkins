package com.tinckay.domain.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by root on 12/26/16.
 */
@Entity
@Table(name = "task_event")
@Getter
@Setter
public class TaskEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id")
    private Long taskId;

    private byte flag;

    private String resume;

    @Column(name = "user")
    private String userName;


    @Column(name = "create_time")
    private Date createTime;


}
