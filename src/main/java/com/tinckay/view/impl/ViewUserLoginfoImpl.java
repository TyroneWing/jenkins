package com.tinckay.view.impl;

import com.tinckay.common.BeanRefUtil;
import com.tinckay.view.base.ViewUserLoginfo;
import com.tinckay.view.common.ObjectDaoService;
import com.tinckay.view.service.ViewUserLoginfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 2/28/17.
 */
@Service
public class ViewUserLoginfoImpl implements ViewUserLoginfoService {
    @Resource
    ObjectDaoService objectDaoService;

    String viewName = "view_user_loginfo";
    String sortField = "time";

    private List<ViewUserLoginfo> convertMapListToRecordList(List<Map<String,Object>> retVal){
        List<ViewUserLoginfo> viewList = null;
        if(null != retVal && 0 < retVal.size()){
            viewList = new ArrayList<>();
            for(Map<String,Object> objectMap:retVal){
                ViewUserLoginfo viewUserLoginfo = new ViewUserLoginfo();
                BeanRefUtil.setFieldObject(viewUserLoginfo,objectMap);
                viewList.add(viewUserLoginfo);
            }
        }
        return viewList;
    }

    public List<ViewUserLoginfo> findByParamList(List<String> paramList){
        List<Map<String,Object>> retVal = objectDaoService.findByParamList(viewName,paramList,sortField);
        return convertMapListToRecordList(retVal);
    }
}
