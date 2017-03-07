package com.tinckay.domain.extend.service;

import com.tinckay.domain.base.PjtUsers;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 3/6/17.
 */
@Service
public interface PjtUsersExtService {
    List<PjtUsers> listByPjtKey(String key);
}
