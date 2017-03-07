package com.tinckay.domain.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "company_loginfo")
public class CompanyLoginfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "company_key")
    private String companyKey;

    @Column(name = "pjt_num")
    private byte pjtNum;
    private String linkman;
    private String salesman;

    @Column(name = "first_log")
    private String firstLog;

    @Column(name = "second_log")
    private String secondLog;

    @Column(name = "third_log")
    private String thirdLog;

    @Column(name = "first_log_Key")
    private String firstLogKey;

    @Column(name = "second_log_Key")
    private String secondLogKey;

    @Column(name = "third_log_Key")
    private String thirdLogKey;



}
