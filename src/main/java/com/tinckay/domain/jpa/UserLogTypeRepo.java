package com.tinckay.domain.jpa;

import com.tinckay.domain.base.UserLogType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 2/28/17.
 */
public interface UserLogTypeRepo extends JpaRepository<UserLogType,Byte> {

}
