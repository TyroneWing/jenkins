package com.tinckay.domain.controller;

import com.tinckay.common.ErrorHandlerController;
import com.tinckay.domain.base.Opt;
import com.tinckay.domain.jpa.OptRepo;
import com.tinckay.domain.vo.OptLoginfoVo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 1/20/17.
 */
@Controller
@RequestMapping("/capi/Opt")
public class OptController extends ErrorHandlerController {
    @Autowired
    private OptRepo optRepo;
    private Logger logger = Logger.getLogger(this.getClass());




    private List<OptLoginfoVo> getOptResp(Page<Opt> optPage){
        List<OptLoginfoVo> resp = null;
        if (optPage != null) {
            resp = new ArrayList<>();
            for (Opt opt : optPage) {
                OptLoginfoVo optLoginfoVo = new OptLoginfoVo();
                optLoginfoVo.setOptId(opt.getId());
                optLoginfoVo.setResume(opt.getResume());
                optLoginfoVo.setOptDate(opt.getCreateTime());
                optLoginfoVo.setIp(opt.getIp());
                optLoginfoVo.setUserName(opt.getUserName());
                resp.add(optLoginfoVo);
            }
        }
        return resp;
    }
    @RequestMapping(method = RequestMethod.GET, value = "/listHome/{size}")
    public @ResponseBody
    List<OptLoginfoVo> listRemindOpt(@PathVariable int size,HttpServletRequest request) {
        Pageable pageable = new PageRequest(0, size, Sort.Direction.DESC,"createTime");
        Page<Opt> result = null;
        String userName = ((null == request) ? "" : request.getRemoteUser());
        if(null != userName && !userName.equals("") && !userName.toLowerCase().equals("admin"))
            result = optRepo.findByUserName(userName,pageable);
        else
            result = optRepo.findAll(pageable);
        return getOptResp(result);

    }

//    @RequestMapping(method = RequestMethod.GET, value = "/listPjt/{size}")
//    public @ResponseBody
//    List<OptLoginfoVo> listPjtOpt(@RequestParam("pjtId") int pjtId,
//                                  @RequestParam("size") int size,
//                                  HttpServletRequest request) {
//        Pageable pageable = new PageRequest(0, size, Sort.Direction.DESC,"createTime");
//        Page<Opt> result = null;
//        String userName = ((null == request) ? "" : request.getRemoteUser());
//
//        if(null != userName && !userName.equals("") && !userName.toLowerCase().equals("admin"))
//            result = optRepo.findByUserName(userName,pageable);
//        else
//            result = optRepo.findAll(pageable);
//
//        return getOptResp(result);
//    }
}
