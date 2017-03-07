package com.tinckay.domain.controller;

import com.tinckay.common.ErrorHandlerController;
import com.tinckay.domain.base.UserLogType;
import com.tinckay.domain.jpa.UserLogTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by root on 2/28/17.
 */
@Controller
@RequestMapping("/capi/UserLogType")
public class UserLogTypeController extends ErrorHandlerController {
    @Autowired
    UserLogTypeRepo userLogTypeRepo;

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    @ResponseBody
    public List<UserLogType> get(){
        return userLogTypeRepo.findAll();
    }
}
