package com.tinckay.domain.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by root on 12/26/16.
 */
@Entity
@Table(name = "user")
@Getter
@Setter
//@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column(name = "department_id")
    private int departmentId;


    private String num;
    private String code;
    private String name;
    private boolean sex;
    private String password;
    private String email;
    private String mobile;

    @Column(name = "photo_address")
    private String photoAddress;

    @Column(name = "enter_time")
    private Date enterTime;

    @Column(name = "is_leave")
    private boolean isLeave;

    @Column(name = "leave_time")
    private Date leaveTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "role_id")
    private int roleId;

    @Transient
    private String deptName;

    @Transient
    private String roleName;

    public User() {
    }

//    public User(User user){
//        this.id = user.getId();
//        this.name = user.getName();
//        this.password = user.getPassword();
//        this.email = user.getEmail();
//        this.mobile = user.getMobile();
//        this.roleId = user.getRoleId();
//    }

    //    @Override
//    public Object clone() throws CloneNotSupportedException
//    {
//        return super.clone();
//    }
}
