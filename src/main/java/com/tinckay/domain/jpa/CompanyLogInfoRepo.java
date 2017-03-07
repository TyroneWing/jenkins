package com.tinckay.domain.jpa;

import com.tinckay.domain.base.CompanyLoginfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 2/22/17.
 */
public interface CompanyLogInfoRepo extends JpaRepository<CompanyLoginfo,Integer> {

    CompanyLoginfo findTop1ByCompanyKey(String key);

}
