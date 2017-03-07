package com.tinckay.domain.extend.impl;

import com.tinckay.domain.base.PjtUsers;
import com.tinckay.domain.extend.service.PjtUsersExtService;
import com.tinckay.domain.jpa.PjtUsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 3/6/17.
 */
@Service
public class PjtUsersExtImpl implements PjtUsersExtService {
    @Autowired
    PjtUsersRepo pjtUsersRepo;
    

    public List<PjtUsers> listByPjtKey(String key){
        List<PjtUsers> pjtUsersList = null;
        List<Object[]> objectList = pjtUsersRepo.listByPjtKey(key);
        if(null != objectList){
            pjtUsersList = new ArrayList<>();
            for(Object[] o:objectList){
                PjtUsers pjtUsers = (PjtUsers) o[0];
                pjtUsers.setName(o[1].toString());
                pjtUsersList.add(pjtUsers);
            }

        }
        return pjtUsersList;
    };
}
