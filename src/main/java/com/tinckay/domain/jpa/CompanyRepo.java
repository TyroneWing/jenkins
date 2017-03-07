package com.tinckay.domain.jpa;

import com.tinckay.domain.base.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by root on 2/22/17.
 */
public interface CompanyRepo extends JpaRepository<Company,Integer>{
    @RestResource(exported = false)
    @Query( " SELECT a ,COALESCE(b.pjtNum,0) as pjtNum, " +
            " COALESCE(b.linkman,'') as linkman, " +
            " COALESCE(b.salesman,'') as salesman, " +
            " COALESCE(b.firstLog,'') as firstLog, " +
            " COALESCE(b.secondLog,'') as secondLog, " +
            " COALESCE(b.thirdLog,'') as thirdLog " +
            " FROM Company a ,CompanyLoginfo b " +
            " where a.companyKey=b.companyKey " +
            " ORDER BY a.createTime DESC ")
    Page<?> listAllPage(Pageable pageable);
}
