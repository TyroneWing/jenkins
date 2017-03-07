package com.tinckay.domain.jpa;

import com.tinckay.domain.base.PjtUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by root on 12/26/16.
 */
public interface PjtUsersRepo extends JpaRepository<PjtUsers,Integer> {

    List<PjtUsers> findByPjtKey(String pjtKey);

    @RestResource(exported = false)
    @Query( " SELECT a , " +
            " b.name as name " +
            " FROM PjtUsers a ,User b" +
            " where a.pjtKey = ?1 and a.userId = b.id")
    List<Object[]> listByPjtKey(String pjtKey);
}
