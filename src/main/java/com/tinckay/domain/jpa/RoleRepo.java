package com.tinckay.domain.jpa;

import com.tinckay.domain.base.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 1/9/17.
 */
public interface RoleRepo extends JpaRepository<Role,Integer> {

}
