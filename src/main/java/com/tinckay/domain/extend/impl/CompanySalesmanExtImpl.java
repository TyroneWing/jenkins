package com.tinckay.domain.extend.impl;

import com.tinckay.domain.base.CompanySalesman;
import com.tinckay.domain.extend.service.CompanySalesmanExtService;
import com.tinckay.domain.jpa.CompanySalesmanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 3/6/17.
 */
@Service
public class CompanySalesmanExtImpl implements CompanySalesmanExtService{

    @Autowired
    CompanySalesmanRepo companySalesmanRepo;

    /**
     * 展示关联信息赋值（员工名称）
     * @param key
     * @return
     */

    public List<CompanySalesman> listByCompanyKey(String key){
        List<CompanySalesman> companySalesmanList = null;
        List<Object[]> objectList = companySalesmanRepo.listByCompanyKey(key);
        if(null != objectList){
            companySalesmanList = new ArrayList<>();
            for(Object[] o:objectList){
                CompanySalesman companySalesman = (CompanySalesman) o[0];
                companySalesman.setName(o[1].toString());
                companySalesmanList.add(companySalesman);
            }

        }
        return companySalesmanList;
    }
}
