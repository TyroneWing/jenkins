package com.tinckay.domain.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by root on 1/10/17.
 */
@Entity
@Getter
@Setter
@Table(name = "department")

public class Dept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;


    private String name;
    private int pid;


    @Column(name = "company_id")
    private int companyId;

    @Transient
    private List<User> userList;
}
