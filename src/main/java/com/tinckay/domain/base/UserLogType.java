package com.tinckay.domain.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by root on 2/28/17.
 */
@Getter
@Setter
@Entity
@Table(name = "user_logtype")
public class UserLogType {
    @Id

    private byte id;
    private String name;

}
