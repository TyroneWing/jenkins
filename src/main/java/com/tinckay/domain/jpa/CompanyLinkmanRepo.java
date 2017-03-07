package com.tinckay.domain.jpa;

import com.tinckay.domain.base.CompanyLinkman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by root on 2/22/17.
 */
public interface CompanyLinkmanRepo extends JpaRepository<CompanyLinkman,Integer> {


    List<CompanyLinkman> findByCompanyKey(String key);
}
