package com.tinckay.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by root on 1/16/17.
 */
@Getter
@Setter
public class UserLoginVo {
    private String userName;
    private String userPwd;
    private String verifyCode;
}
