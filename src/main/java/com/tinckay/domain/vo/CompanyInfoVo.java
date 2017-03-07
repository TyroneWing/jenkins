package com.tinckay.domain.vo;

import com.tinckay.domain.base.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by root on 2/22/17.
 */
@Getter
@Setter
public class CompanyInfoVo extends CompanyVo{
    //private Company company;
    //private List<CompanyLinkman> companyLinkmanList;
    //private List<CompanySalesman> companySalesmanList;
    private List<Project> projectList;
    private List<UserLoginfo> userLoginfoList;
}
