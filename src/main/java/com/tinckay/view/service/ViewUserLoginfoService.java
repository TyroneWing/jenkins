package com.tinckay.view.service;

import com.tinckay.view.base.ViewUserLoginfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2/28/17.
 */
@Service
public interface ViewUserLoginfoService {
    List<ViewUserLoginfo> findByParamList(List<String> paramList);
}
