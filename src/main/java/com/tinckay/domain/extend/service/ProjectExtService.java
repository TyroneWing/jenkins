package com.tinckay.domain.extend.service;

import com.tinckay.common.PageObject;
import com.tinckay.domain.base.Project;
import org.springframework.stereotype.Service;

/**
 * Created by root on 3/7/17.
 */
@Service
public interface ProjectExtService {

    Project getOneById(Long id);

    PageObject<Project> listFromPageParam(int page, int size);
}
