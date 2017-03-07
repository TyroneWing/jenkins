package com.tinckay.domain.controller;

import com.tinckay.common.BeanRefUtil;
import com.tinckay.common.ErrorHandlerController;
import com.tinckay.common.PageObject;
import com.tinckay.common.ResponseObj;
import com.tinckay.domain.base.*;
import com.tinckay.domain.jpa.*;
import com.tinckay.domain.vo.UserLoginfoVo;
import com.tinckay.view.base.ViewUserLoginfo;
import com.tinckay.view.service.ViewUserLoginfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by root on 2/23/17.
 */
@Controller
@RequestMapping("/capi/UserLoginfo")
public class UserLoginfoController extends ErrorHandlerController {

    @Autowired
    CompanyRepo companyRepo;

    @Autowired
    CompanyLinkmanRepo companyLinkmanRepo;


    @Autowired
    CompanyLogInfoRepo companyLogInfoRepo;

    @Autowired
    UpFileRepo upFileRepo;

    @Autowired
    UserLoginfoRepo userLoginfoRepo;

    @Autowired
    ViewUserLoginfoService viewUserLoginfoService;
    /**
     * 新增工作记录
     * @param userLoginfoVo
     * @return

     */
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    @ResponseBody
    @Transactional
    public ResponseObj add(@RequestBody UserLoginfoVo userLoginfoVo,
                           HttpServletRequest request) {
        if(null == userLoginfoVo){
            return new ResponseObj(-1,"接收信息为空，操作失败。",null);
        }

        UserLoginfo userLoginfo = userLoginfoVo.getUserLoginfo();
        if(null == userLoginfo){
            return new ResponseObj(-1,"接收【工作记录】信息为空，操作失败。",null);
        }

        String creator = BeanRefUtil.getRemoteUserName(request);
        String strKey = BeanRefUtil.getUUID();
        userLoginfo.setUserLoginfoKey(strKey);
        userLoginfo.setCreator(creator);



        //记录日志对应的文件信息

        List<UpFile> upFileList = userLoginfoVo.getUpFileList();
        if(null != upFileList){
            for(UpFile upFile:upFileList){
                upFile.setUserLoginfoKey(strKey);
                //需从Session中获取最新的用户对应的id号
                upFile.setCreator(creator);
                upFile.setFlag((byte) 4);
            }
        }

        //将最新的日志信息更新至供应商日志表
        CompanyLoginfo companyLoginfo = null;
        int companyId = userLoginfo.getCompanyId();
        if(0 != companyId) {
            Company company = companyRepo.findOne(companyId);
            if (null != company) {
                companyLoginfo = companyLogInfoRepo.findTop1ByCompanyKey(company.getCompanyKey());
                if (null != companyLoginfo) {
                    companyLoginfo.setFirstLog(companyLoginfo.getSecondLog());
                    companyLoginfo.setFirstLogKey(companyLoginfo.getSecondLogKey());
                    companyLoginfo.setSecondLog(companyLoginfo.getThirdLog());
                    companyLoginfo.setSecondLog(companyLoginfo.getThirdLogKey());
                    companyLoginfo.setThirdLog(userLoginfo.getTime() + "(" + userLoginfo.getCreator() + ")\n" + userLoginfo.getResume());
                    companyLoginfo.setThirdLogKey(strKey);
                }
            }
        }



        try{
            if(null != companyLoginfo) {
                companyLogInfoRepo.save(companyLoginfo);
            }
            if(null != upFileList) {
                upFileRepo.save(upFileList);
            }
            userLoginfoRepo.save(userLoginfo);
            return new ResponseObj(0,"工作记录【" + userLoginfo.getTitle() + "】新增成功。",userLoginfo.getId());
        }catch (Exception e){
            return new ResponseObj(-1,"工作记录【" + userLoginfo.getTitle() + "】新增失败。" + e.getMessage(),null);

        }
    }

    /**
     * 删除工作记录
     * @param id
     * @return
     *
     */
    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    @Transactional
    public ResponseObj delete(@PathVariable long id){
        UserLoginfo userLoginfo = userLoginfoRepo.findOne(id);
        if(null == userLoginfo){
            return new ResponseObj(-1,"未找到ID为【" + id + "】的工作记录，操作失败。",null);
        }

        String title = userLoginfo.getTitle();
        try{
            userLoginfoRepo.delete(userLoginfo);
            return new ResponseObj(0,"工作记录【" + title + "】已删除。",id);
        }catch (Exception e) {
            return new ResponseObj(-1,"工作记录【" + title + "】删除失败。" + e.getMessage(),null);
        }

    }

    /**
     * 编辑工作记录信息
     * @param userLoginfoVo
     * @return
     *
     *
     */
    @RequestMapping(method = RequestMethod.POST, value = "/modify")
    @ResponseBody
    @Transactional
    public ResponseObj modify(@RequestBody UserLoginfoVo userLoginfoVo,
                                HttpServletRequest request)  throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{

        if(null == userLoginfoVo){
            return new ResponseObj(-1,"接收信息为空，操作失败。",null);
        }

        UserLoginfo userLoginfo = userLoginfoVo.getUserLoginfo();
        if(null == userLoginfo){
            return new ResponseObj(-1,"接收【工作记录】信息为空，操作失败。",null);
        }
        String title = userLoginfo.getTitle();
        long id = userLoginfo.getId();
        UserLoginfo userLoginfoEdit = userLoginfoRepo.findOne(id);
        if(null == userLoginfoEdit){
            return new ResponseObj(-1,"未找到标题为【" + title + "】的工作记录，操作失败。",null);
        }

        String strKey = userLoginfo.getUserLoginfoKey();
        String creator = BeanRefUtil.getRemoteUserName(request);

        //更新工作记录的附件
        List<UpFile> upFileList = userLoginfoVo.getUpFileList();
        List<UpFile> upFileListEdit = upFileRepo.findByUserLoginfoKey(strKey);
        int editSize = 0;
        if(null != upFileListEdit){
            editSize = upFileListEdit.size();
        }

        if((null == upFileList || 0 == upFileList.size()) &&  0 < editSize ){
            upFileListEdit.clear();
        }else{
            Map<String,UpFile> upFileMap = new HashMap<>();
            for(UpFile upFile:upFileList){
                long fileId = upFile.getId();
                if(0 == fileId){
                    upFile.setUserLoginfoKey(strKey);
                    upFile.setCreator(creator);
                    upFile.setFlag((byte) 4);
                    upFileListEdit.add(upFile);
                }else {
                    upFileMap.put(String.valueOf(fileId), upFile);
                }
            }

            if(0 < editSize) {
                for (int i = 0; i < upFileListEdit.size(); i++) {
                    UpFile upFile = upFileListEdit.get(i);
                    String strId = String.valueOf(upFile.getId());
                    if (!"0".equals(strId)) {
                        if (upFileMap.containsKey(strId)) {
                            //编辑
                            BeanRefUtil.copyProperty(upFile, upFileMap.get(strId));
                        } else {
                            //删除
                            upFileListEdit.remove(upFile);
                        }
                    }
                }
            }
            upFileMap.clear();
        }




        //将最新的日志信息更新至供应商日志表
        CompanyLoginfo companyLoginfo = null;

        int companyId = userLoginfo.getCompanyId();
        if(0 != companyId) {
            Company company = companyRepo.findOne(companyId);
            if (null == company) {
                return new ResponseObj(-1, "未找到ID为【" + companyId + "】的客户信息，操作失败。", null);
            }else{
                companyLoginfo = companyLogInfoRepo.findTop1ByCompanyKey(company.getCompanyKey());
                if (null != companyLoginfo) {
                    String firstLogKey = companyLoginfo.getFirstLogKey();
                    String secondLogKey = companyLoginfo.getSecondLogKey();
                    String thirdLogKey = companyLoginfo.getThirdLogKey();
                    if(strKey.equals(firstLogKey)){
                        companyLoginfo.setFirstLog(userLoginfo.getTime() + "(" + userLoginfo.getCreator() + ")\n" + userLoginfo.getResume());
                    }else if (strKey.equals(secondLogKey)){
                        companyLoginfo.setSecondLog(userLoginfo.getTime() + "(" + userLoginfo.getCreator() + ")\n" + userLoginfo.getResume());
                    }else{
                        companyLoginfo.setThirdLog(userLoginfo.getTime() + "(" + userLoginfo.getCreator() + ")\n" + userLoginfo.getResume());
                        if(!strKey.equals(thirdLogKey)) {
                            companyLoginfo.setFirstLog(companyLoginfo.getSecondLog());
                            companyLoginfo.setFirstLogKey(companyLoginfo.getSecondLogKey());
                            companyLoginfo.setSecondLog(companyLoginfo.getThirdLog());
                            companyLoginfo.setSecondLog(companyLoginfo.getThirdLogKey());
                            companyLoginfo.setThirdLogKey(strKey);
                        }
                    }
                }
            }
        }

        BeanRefUtil.copyProperty(userLoginfoEdit,userLoginfo);
        try{

            if(null != upFileListEdit){
                upFileRepo.save(upFileListEdit);
            }
            if(null != companyLoginfo){
                companyLogInfoRepo.save(companyLoginfo);
            }
            userLoginfoRepo.save(userLoginfoEdit);
            return new ResponseObj(0,"工作记录【" + title + "】信息已修改。",id);
        }catch (Exception e){
            return new ResponseObj(-1,"工作记录【" + title + "】信息修改失败。" + e.getMessage(),0);
        }

    }


    @RequestMapping(method = RequestMethod.GET, value = "/listFromFlag/{flag}")
    @ResponseBody
    public ResponseObj listFromFlag(@PathVariable Byte flag) {
//    public List<UserLoginfo> list(@RequestParam("flag") Byte flag) {
        List<UserLoginfo> userLoginfoList = null;
        List<Byte> flagList = new ArrayList<>();
        flagList.add(flag);
        flagList.add((byte) 100);
        try {
            userLoginfoList = userLoginfoRepo.findByFlagIn(flagList);
            if (null == userLoginfoList) {
                return new ResponseObj(-1, "没有满足条件的工作日志记录。", null);
            } else {
                return new ResponseObj(0, "获取工作日志成功。", userLoginfoList);
            }
        } catch (Exception e) {
            return new ResponseObj(-1, "获取记录信息失败。" + e.getMessage(), null);
        } finally {
            flagList.clear();

        }
    }


    @RequestMapping(method = RequestMethod.GET, value = "/listFromParam")
    @ResponseBody
    public ResponseObj listFromParam(   @RequestParam("companyId") int companyId,
                                        @RequestParam("pjtId") int pjtId,
                                        @RequestParam("taskId") long taskId,
                                        @RequestParam("creator") String creator,
                                        @RequestParam("flag") String flagList) {
//    public List<UserLoginfo> list(@RequestParam("flag") Byte flag) {
        List<ViewUserLoginfo> viewUserLoginfoList = null;
        try {
            List<String> paramList = new ArrayList<>();
            if(0 < companyId) {
                paramList.add(" companyId = " + companyId);
            }
            if(0 < pjtId) {
                paramList.add(" pjtId = " + pjtId);
            }
            if(0 < taskId) {
                paramList.add(" taskId = " + taskId);
            }
            if(null != creator && "" != creator) {
                paramList.add(" creator = '" + creator + "'");
            }
            if(null != flagList && "" != flagList) {
                paramList.add(" flag in " + flagList);
            }

            viewUserLoginfoList = viewUserLoginfoService.findByParamList(paramList);
            if (null == viewUserLoginfoList) {
                return new ResponseObj(-1, "没有满足条件的工作日志记录。", null);
            } else {
                return new ResponseObj(0, "获取工作日志成功。", viewUserLoginfoList);
            }
        } catch (Exception e) {
            return new ResponseObj(-1, "获取记录信息失败。" + e.getMessage(), null);
        }
    }

    /**
     * 分页展示工作记录信息包含
     * @param page
     * @param size
     * @return PageObject<UserLoginfo>
     *
     */
    @RequestMapping(method = RequestMethod.GET, value = "/listPage")
    @ResponseBody
    public PageObject<UserLoginfo> list(@RequestParam("page") int page,
                                        @RequestParam("size") int size,
                                        @RequestParam("flag") String flag) {

        Pageable pageable = new PageRequest(page, size);
        //Page<Object[]> objectList = (Page<Object[]>)userLoginfoRepo.listAllPage(pageable);
//        /*
//        COALESCE(b.pjtNum,0) as pjtNum, " +
//            " COALESCE(b.linkman,'') as linkman, " +
//            " COALESCE(b.salesman,'') as salesman, " +
//            " COALESCE(b.firstLog,'') as firstLog, " +
//            " COALESCE(b.secondLog,'') as secondLog, " +
//            " COALESCE(b.thirdLog,'') as thirdLog " +
//         */
//
        List<UserLoginfo> result = null;
//
//        if (null != objectList) {
//            result = new ArrayList<>();
//
//            for (Object[] object : objectList) {
//                Company company = (Company) object[0];
//                company.setPjtNum(Integer.valueOf(object[1].toString()));
//                company.setLinkman(object[2].toString());
//                company.setSalesman(object[3].toString());
//
//                String strloginfo = "";
//                String str = object[4].toString().trim();
//                strloginfo = (!"".equals(str)) ? str : "";
//                str = object[5].toString().trim();
//                if(!"".equals(str)){
//                    strloginfo = str +  "\n" + strloginfo;
//                }
//                str = object[6].toString().trim();
//                if(!"".equals(str)){
//                    strloginfo = str +  "\n" + strloginfo;
//                }
//                company.setLogInfo(strloginfo);
//
//                result.add(company);
//            }
            PageObject<UserLoginfo> po = new PageObject<>();
            po.setPage(page);
            po.setDataList(result);
//            po.setTotalPages(objectList.getTotalPages());
//            po.setTotalElements(objectList.getTotalElements());
            return po;
//        }
//        else
//            return result;
    }



    /**
     * 通过Id获取单个工作记录
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/get/{id}")
    @ResponseBody
    public ResponseObj get(@PathVariable long id) {
        UserLoginfo userLoginfo = userLoginfoRepo.findOne(id);
        if(null == userLoginfo){
            return new ResponseObj(-1,"未找到ID为【" + id + "】的工作记录，操作失败。",null);
        }
        List<UpFile> upFileList = upFileRepo.findByUserLoginfoKey(userLoginfo.getUserLoginfoKey());

        UserLoginfoVo userLoginfoVo = new UserLoginfoVo();
        userLoginfoVo.setUserLoginfo(userLoginfo);
        userLoginfoVo.setUpFileList(upFileList);

        return new ResponseObj(0,"信息获取完成。",userLoginfoVo);
    }

//
//    /**
//     * 通过Id获取单个公司的所有相关信息（公司，公司联系人，公司跟进人，最新跟进记录，公司项目列表）
//     * @param id
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.GET,value = "/getInfo/{id}")
//    @ResponseBody
//    public ResponseObj getInfo(@PathVariable int id) {
//        Company company = companyRepo.findOne(id);
//        if(null == company){
//            return new ResponseObj(-1,"未找到ID为【" + id + "】的客户，操作失败。",null);
//        }
//
//        List<Project> projectList = projectRepo.findByCompanyId(id);
//        List<UserLoginfo> userLoginfoList = userLoginfoRepo.findByCompanyId(id);
//        List<CompanySalesman> companySalesmanList = companySalesmanRepo.findByCompanyKey(company.getCompanyKey());
//        List<CompanyLinkman> companyLinkmanList = companyLinkmanRepo.findByCompanyKey(company.getCompanyKey());
//
//
//        CompanyInfoVo companyInfoVo = new CompanyInfoVo();
//        companyInfoVo.setCompany(company);
//        companyInfoVo.setCompanyLinkmanList(companyLinkmanList);
//        companyInfoVo.setCompanySalesmanList(companySalesmanList);
//        companyInfoVo.setProjectList(projectList);
//        companyInfoVo.setUserLoginfoList(userLoginfoList);
//        return new ResponseObj(0,"信息获取完成。",companyInfoVo);
//    }

}
