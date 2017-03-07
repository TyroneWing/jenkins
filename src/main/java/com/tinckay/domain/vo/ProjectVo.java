package com.tinckay.domain.vo;

import com.tinckay.domain.base.PjtUsers;
import com.tinckay.domain.base.Project;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by root on 12/29/16.
 */
@Getter
@Setter
public class ProjectVo {
    private Project project;
    private List<PjtUsers> pjtUsersList;
}
