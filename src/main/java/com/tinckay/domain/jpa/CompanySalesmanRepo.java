package com.tinckay.domain.jpa;

import com.tinckay.domain.base.CompanySalesman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by root on 2/22/17.
 */
public interface CompanySalesmanRepo extends JpaRepository<CompanySalesman,Integer>{
    List<CompanySalesman> findByCompanyKey(String key);


    @RestResource(exported = false)
    @Query( " SELECT a , " +
            " b.name as name " +
            " FROM CompanySalesman a ,User b" +
            " where a.companyKey = ?1 and a.userId = b.id")
    List<Object[]> listByCompanyKey(String key);

}
