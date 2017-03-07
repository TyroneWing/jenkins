package com.tinckay.domain.jpa;

import com.tinckay.domain.base.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by root on 12/26/16.
 */
public interface UserRepo extends JpaRepository<User,Integer> {
    User findByName(String name);

    List<User> findAllByOrderByDepartmentIdAsc();

    @RestResource(exported = false)
    Page<User> findAll(Pageable pageable);

    @RestResource(exported = false)
    @Query("SELECT a,b.name as deptName FROM User a,Dept b "
            + "where a.departmentId = b.id "
            + "ORDER BY a.departmentId asc")
    List<?> getAllUserList();

    User findOneByName(String username);
    User findTopByNumOrCodeOrNameOrMobileOrEmail(String num,String code,String name,String mobile,String Email);
}
