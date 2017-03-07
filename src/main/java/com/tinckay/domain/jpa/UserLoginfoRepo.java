package com.tinckay.domain.jpa;

import com.tinckay.domain.base.UserLoginfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by root on 2/23/17.
 */
public interface UserLoginfoRepo extends JpaRepository<UserLoginfo,Long> {




    //List<UserLoginfo> findByCompanyId(int companyId);
    List<UserLoginfo> findTop10ByCompanyIdOrderByTimeDesc(int companyId);
    UserLoginfo findTop1ByCompanyId(int companyId);
    UserLoginfo findTop1ByLinkmanId(int linkmanId);
    //UserLoginfo findTop1ByPjtId(int pjtId);
    //UserLoginfo findTop1ByTaskId(int taskId);




//    @RestResource(exported = false)
//    @Query( " SELECT a , " +
//            " COALESCE(b.name,'') as companyName, " +
//            " COALESCE(c.name,'') as pjtName, " +
//            " COALESCE(d.name,'') as linkman " +
//            " FROM UserLoginfo a " +
//            " left join Company b on a.companyId = b.id " +
//            " left join Project c on a.pjtId = c.id " +
//            " left join CompanyLinkman d on a.linkmanId = d.id " +
//            " ORDER BY a.time DESC")
//    Page<?> listAllPage(Collection<Byte> flag,Pageable pageable);


    List<UserLoginfo> findByFlagIn(List<Byte> flagList);
}
