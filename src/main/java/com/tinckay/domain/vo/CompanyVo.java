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
public class CompanyVo {
    private Company company;
    private List<CompanyLinkman> companyLinkmanList;
    private List<CompanySalesman> companySalesmanList;
}
