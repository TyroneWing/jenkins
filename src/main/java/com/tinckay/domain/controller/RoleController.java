package com.tinckay.domain.controller;

import com.tinckay.common.ErrorHandlerController;
import com.tinckay.domain.base.Role;
import com.tinckay.domain.jpa.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by root on 1/11/17.
 */

@Controller
@RequestMapping("/capi/Role")
public class RoleController extends ErrorHandlerController {
    @Autowired
    RoleRepo roleRepo;

    /**
     * 查询用户列表

     * @return 成功：所有用户列表信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/listAll")
    public @ResponseBody
    List<Role> listAll() {
        return roleRepo.findAll();
    }

}
