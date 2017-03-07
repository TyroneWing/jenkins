package com.tinckay.domain.jpa;

import com.tinckay.domain.base.Opt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 12/26/16.
 */
public interface OptRepo extends JpaRepository<Opt,Long> {


//    @RestResource(exported = false)
//    @Query("SELECT a.id,a.resume,a.createTime,a.flag,a.ip "
//            + "FROM Opt a ,User b "
//            + "where a.userId=b.id "
//            + "ORDER BY a.createTime DESC ")
//    Page<?> getRemindList(Pageable pageable);
    Page<Opt> findByUserName(String userName,Pageable pageable);

    Page<Opt> findByPjtIdAndUserName(int pjtId,String userName,Pageable pageable);

}
