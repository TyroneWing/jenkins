package com.tinckay.domain.controller;

import com.tinckay.common.ErrorHandlerController;
import com.tinckay.domain.base.Dept;
import com.tinckay.domain.base.User;
import com.tinckay.domain.jpa.DeptRepo;
import com.tinckay.domain.jpa.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by root on 1/10/17.
 */
@Controller
@RequestMapping("/capi/Dept")
public class DeptController extends ErrorHandlerController {
    @Autowired
    DeptRepo deptRepo;

    @Autowired
    UserRepo userRepo;

    /**
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/listDeptUsers")
    public @ResponseBody List<Dept> listDeptUsers(){
        List<Dept> deptList = deptRepo.findAll();
        if(null != deptList){
            List<User> userList = userRepo.findAll();
            if(null != userList)
                for(Dept dept:deptList){
                    List<User> users = dept.getUserList();
                    for(User user:userList) {
                        if(dept.getId() == user.getDepartmentId()){
                            if(null == users)
                                users = new ArrayList<>();
                            users.add(user);
                            //userList.remove(user);
                        }
                    }
                    if(null != users)
                        dept.setUserList(users);
                }
        }
        return deptList;
    }

    /**
     * 查询所有部门列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/listAll")
    @ResponseBody
    public  List<Dept> listAll(){
        List<Dept> deptList = deptRepo.findAll();
        if(deptList.size() > 0) {
            return deptList;
        } else {
            return Collections.emptyList();
        }
    }
}
