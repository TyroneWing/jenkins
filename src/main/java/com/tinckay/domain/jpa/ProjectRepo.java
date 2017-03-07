package com.tinckay.domain.jpa;

import com.tinckay.domain.base.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by root on 12/26/16.
 */
public interface ProjectRepo extends JpaRepository<Project,Long> {

    //Project findById(Integer Id);
    Project findOne(Long Id);


    @RestResource(exported = false)
    @Query( " SELECT a , " +
            " b.name as companyName, " +
            " c.name as managerName, " +
            " d.name as linkman " +
            " FROM Project a ,Company b,User c,CompanyLinkman d " +
            " where a.id = ?1 and a.companyId = b.id and a.managerId = c.id and a.linkmanId = d.id " +
            " ORDER BY a.createTime DESC")
    Object[] getOneById(Long id);


    Project findTop1ByCompanyId(int companyId);
    List<Project> findByCompanyId(int companyId);

    Project findTop1ByLinkmanId(int linkmanId);

    @RestResource(exported = false)
    @Query( " SELECT a , " +
            " b.name as companyName, " +
            " c.name as managerName, " +
            " d.name as linkman " +
            " FROM Project a ,Company b,User c,CompanyLinkman d " +
            " where a.companyId = b.id and a.managerId = c.id and a.linkmanId = d.id " +
            " ORDER BY a.createTime DESC")
    Page<?> listAllPage(Pageable pageable);
}
