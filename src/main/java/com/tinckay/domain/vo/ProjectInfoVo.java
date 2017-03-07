package com.tinckay.domain.vo;

import com.tinckay.domain.base.UserLoginfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by root on 3/7/17.
 */
@Getter
@Setter
public class ProjectInfoVo extends ProjectVo {
    List<UserLoginfo> userLoginfoList;
}
