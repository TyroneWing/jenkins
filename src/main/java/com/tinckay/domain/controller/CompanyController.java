package com.tinckay.domain.controller;

import com.tinckay.common.BeanRefUtil;
import com.tinckay.common.ErrorHandlerController;
import com.tinckay.common.PageObject;
import com.tinckay.common.ResponseObj;
import com.tinckay.domain.base.*;
import com.tinckay.domain.extend.service.CompanySalesmanExtService;
import com.tinckay.domain.jpa.*;
import com.tinckay.domain.vo.CompanyInfoVo;
import com.tinckay.domain.vo.CompanyVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 2/22/17.
 */
@Controller
@RequestMapping("/capi/Company")
public class CompanyController extends ErrorHandlerController {

    @Autowired
    CompanyRepo companyRepo;

    @Autowired
    CompanyLinkmanRepo companyLinkmanRepo;

    @Autowired
    CompanySalesmanRepo companySalesmanRepo;

    @Autowired
    CompanySalesmanExtService companySalesmanExtService;

    @Autowired
    CompanyLogInfoRepo companyLogInfoRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    UserLoginfoRepo userLoginfoRepo;

    private static Logger logger = Logger.getLogger(CompanyController.class);


    /**
     *
     * @param companyLinkmanList
     * @param strCompanyKey
     * @param creator
     * @return
     * 未判定如果主要人离开，需调整主要人
     */
    private String getMainLinkman(List<CompanyLinkman> companyLinkmanList,
                                  String strCompanyKey,
                                  String creator){
        boolean existsMain = false;
        String strLinkman = "";
        for(CompanyLinkman companyLinkman : companyLinkmanList){
            companyLinkman.setCompanyKey(strCompanyKey);
            companyLinkman.setCreator(creator);


            if(existsMain && companyLinkman.isMain()) {
                existsMain = false;
                strLinkman = "";
                break;
            }

            if(!existsMain && companyLinkman.isMain()) {
                existsMain = true;
                strLinkman = companyLinkman.getName();
            }
        }
        return strLinkman;
    }

    private String getMainSalesman(List<CompanySalesman> companySalesmanList,
                                   String strCompanyKey,
                                   String creator){
        boolean existsMain = false;
        String strSalesman = "";
        for(CompanySalesman companySalesman : companySalesmanList){

            int id = companySalesman.getUserId();
            User user = userRepo.findOne(id);
            if(null == user){
                strSalesman = "未找到ID为【" + id + "】的主要跟进人信息，操作失败。";
                break;
            }

            companySalesman.setCompanyKey(strCompanyKey);
            companySalesman.setCreator(creator);
            companySalesman.setOuted(false);

            if(existsMain && companySalesman.isMain()) {
                existsMain = false;
                strSalesman = "";
                break;
            }

            if(!existsMain && companySalesman.isMain()) {
                existsMain = true;
                strSalesman = user.getName();
            }
        }
        return strSalesman;
    }

    /**
     * 新增客户或者对方单位信息
     * @param companyVo
     * @return
     * resume：注意新增客户必须设定主联系人/主跟进人,信息冗余记录在公司日志信息中()
     */
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    @ResponseBody
    @Transactional
    public ResponseObj add(@RequestBody CompanyVo companyVo,
                           HttpServletRequest request) {
        if(null == companyVo){
            return new ResponseObj(-1,"接收信息为空，操作失败。",null);
        }

        Company company = companyVo.getCompany();
        if(null == company){
            return new ResponseObj(-1,"接收的【客户】信息为空，操作失败。",null);
        }

        List<CompanyLinkman> companyLinkmanList = companyVo.getCompanyLinkmanList();
        if(null == companyLinkmanList || 0 == companyLinkmanList.size()){
            return new ResponseObj(-1,"接收的【客户联系人】信息为空，操作失败。",null);
        }

        List<CompanySalesman> companySalesmanList = companyVo.getCompanySalesmanList();
        if(null == companySalesmanList || 0 == companySalesmanList.size()){
            return new ResponseObj(-1,"接收的【客户跟进人】信息为空，操作失败。",null);
        }

        String creator = BeanRefUtil.getRemoteUserName(request);
        logger.info("Creator:" + creator);
        String strCompanyKey = BeanRefUtil.getUUID();
        company.setCompanyKey(strCompanyKey);
        company.setCreator(creator);

//        boolean existsMain = false;
//        String strLinkman = "";
//        for(CompanyLinkman companyLinkman : companyLinkmanList){
//            companyLinkman.setCompanyKey(strCompanyKey);
//            companyLinkman.setCreator(creator);
//
//            if(existsMain && companyLinkman.isMain()) {
//                existsMain = false;
//            }
//
//            if(!existsMain && companyLinkman.isMain()) {
//                existsMain = true;
//                strLinkman = companyLinkman.getName();
//            }
//        }
//
//        if(!existsMain){
//            return new ResponseObj(-1,"客户必须且只能设定一个【主要联系人】，操作失败。",null);
//        }
        String strLinkman = getMainLinkman(companyLinkmanList,strCompanyKey,creator);
        if("".equals(strLinkman)){
            return new ResponseObj(-1,"客户必须且只能设定一个【主要联系人】，操作失败。",null);
        }

        String strSalesman = getMainSalesman(companySalesmanList,strCompanyKey,creator);
        logger.info("MainSalesman：" + strSalesman);
        if("".equals(strSalesman) || strSalesman.length() > 20){
            return new ResponseObj(-1,("".equals(strSalesman)) ? "客户必须且只能设定一个【主要跟进人】，操作失败。" : strSalesman,null);
        }
//        existsMain = false;
//        String strSalesman = "";
//        for(CompanySalesman companySalesman : companySalesmanList){
//            companySalesman.setCompanyKey(strCompanyKey);
//            companySalesman.setCreator(creator);
//
//            if(existsMain && companySalesman.isMain()) {
//                existsMain = false;
//            }
//
//            if(!existsMain && companySalesman.isMain()) {
//                existsMain = true;
//                int id = companySalesman.getUserId();
//                User user = userRepo.findOne(id);
//                if(null == user){
//                    return new ResponseObj(-1,"未找到跟进人ID为【" + id + "】的信息，操作失败。",null);
//                }
//                strSalesman = user.getName();
//            }
//        }
//
//        if(!existsMain){
//            return new ResponseObj(-1,"客户必须且只能设定一个【主要跟进人】，操作失败。",null);
//        }

        company.setCompanyKey(strCompanyKey);
        company.setCreator(creator);

        CompanyLoginfo companyLoginfo = new CompanyLoginfo();
        companyLoginfo.setCompanyKey(strCompanyKey);
        companyLoginfo.setPjtNum((byte) 0);
        companyLoginfo.setLinkman(strLinkman);
        companyLoginfo.setSalesman(strSalesman);

        try{
            companyRepo.save(company);
            companyLinkmanRepo.save(companyLinkmanList);
            companySalesmanRepo.save(companySalesmanList);
            companyLogInfoRepo.save(companyLoginfo);
            return new ResponseObj(0,"客户【" + company.getName() + "】新增成功。",company.getId());
        }catch (Exception e){
            return new ResponseObj(-1,"客户【" + company.getName() + "】新增失败。" + e.getMessage(),null);

        }
    }

    /**
     * 删除客户或者对方单位信息
     * @param id
     * @return
     * resume：注意校验客户是否存在项目，客户日志信息，客户跟进人，客户联系人，客户跟进记录，是否满足删除规则
     */
    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    @Transactional
    public ResponseObj delete(@PathVariable int id){
        Company company = companyRepo.findOne(id);
        if(null == company){
            return new ResponseObj(-1,"未找到ID为【" + id + "】的客户，删除操作失败。",null);
        }

        if(0 == company.getFlag()){
            return new ResponseObj(-1,"不允许删除本公司信息，删除操作失败。",null);
        }

        Project project = projectRepo.findTop1ByCompanyId(id);
        if(null != project){
            return new ResponseObj(-1,"客户存在【" + project.getCode() + "：" + project.getName() + "】项目信息，请先删除关联项目。",null);
        }

        //如果存在关联的工作日志也不允许删除
        UserLoginfo userLoginfo = userLoginfoRepo.findTop1ByCompanyId(id);
        if(null != userLoginfo){
            return new ResponseObj(-1,"客户存在【" + userLoginfo.getCreator() + "：" + userLoginfo.getTitle() + "】工作记录，请先删除关联工作记录。",null);
        }

        String strName = company.getName();

        List<CompanySalesman> companySalesmanList = companySalesmanRepo.findByCompanyKey(company.getCompanyKey());
        List<CompanyLinkman> companyLinkmanList = companyLinkmanRepo.findByCompanyKey(company.getCompanyKey());
        //如果在工程项目中存在关联的主要联系人及工作日志中存在关联的联系人，此联系人将不允许删除
        if(null != companyLinkmanList && 0 < companyLinkmanList.size()) {
            for (CompanyLinkman companyLinkman : companyLinkmanList) {
                int linkmanId = companyLinkman.getId();
                String linkmanName = companyLinkman.getName();
                project = projectRepo.findTop1ByLinkmanId(linkmanId);
                if(null != project){
                    return new ResponseObj(-1,"客户联系人【" + linkmanName + "】存在【" + project.getCode() + "：" + project.getName() + "】项目信息，请先删除关联项目。",null);
                }
                userLoginfo = userLoginfoRepo.findTop1ByLinkmanId(linkmanId);
                if(null != userLoginfo){
                    return new ResponseObj(-1,"客户联系人【" + linkmanName + "】存在【" + userLoginfo.getCreator() + "：" + userLoginfo.getTitle() + "】工作记录，请先删除关联工作记录。",null);
                }
            }
        }

        try{
            if(null != companySalesmanList) {
                companySalesmanRepo.delete(companySalesmanList);
            }
            if(null != companyLinkmanList){
                companyLinkmanRepo.delete(companyLinkmanList);
            }
            companyRepo.delete(company);
            return new ResponseObj(0,"客户【" + strName + "】已删除。",id);
        }catch (Exception e) {
            return new ResponseObj(-1,"客户【" + strName + "】删除失败。" + e.getMessage(),null);
        }

    }

    /**
     * 编辑客户或者对方单位信息
     * @param companyVo
     * @return
     *
     * resume：注意客户是否修改客户跟进人，客户联系人，如果有请跟进修改对应的客户日志信息。
     */
    @RequestMapping(method = RequestMethod.POST, value = "/modify")
    @ResponseBody
    @Transactional
    public ResponseObj modify(@RequestBody CompanyVo companyVo,
                              HttpServletRequest request) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{

        if(null == companyVo){
            return new ResponseObj(-1,"接收信息为空，操作失败。",null);
        }

        Company company = companyVo.getCompany();
        if(null == company){
            return new ResponseObj(-1,"接收的【客户】信息为空，操作失败。",null);
        }

        List<CompanyLinkman> companyLinkmanList = companyVo.getCompanyLinkmanList();
        if(null == companyLinkmanList || 0 == companyLinkmanList.size()){
            return new ResponseObj(-1,"接收的【客户联系人】信息为空，操作失败。",null);
        }

        List<CompanySalesman> companySalesmanList = companyVo.getCompanySalesmanList();
        if(null == companySalesmanList || 0 == companySalesmanList.size()){
            return new ResponseObj(-1,"接收的【客户跟进人】信息为空，操作失败。",null);
        }

        int companyId = company.getId();
        String name = company.getName();

        Company companyEdit = companyRepo.findOne(companyId);
        if(null == companyEdit){
            return new ResponseObj(-1,"未找到【" + name + "】的对应信息，操作失败。",null);
        }

        String strCompanyKey = companyEdit.getCompanyKey();
        List<CompanyLinkman> companyEditLinkmanList = companyLinkmanRepo.findByCompanyKey(strCompanyKey);
        if(null == companyEditLinkmanList || 0 == companyEditLinkmanList.size()){
            return new ResponseObj(-1,"未找到【" + name + "】的对应联系人信息，操作失败。",null);
        }

        List<CompanySalesman> companyEditSalesmanList = companySalesmanRepo.findByCompanyKey(strCompanyKey);
        if(null == companyEditSalesmanList || 0 == companyEditSalesmanList.size()){
            return new ResponseObj(-1,"未找到【" + name + "】的对应跟进人信息，操作失败。",null);
        }


        //联系人信息操作
        String creator = BeanRefUtil.getRemoteUserName(request);
        Map<String,CompanyLinkman> companyLinkmanMap = new HashMap<>();
        for(CompanyLinkman companyLinkman:companyLinkmanList){
            int id = companyLinkman.getId();
            if(0 == id){
                companyLinkman.setCompanyKey(strCompanyKey);
                companyLinkman.setCreator(creator);
                companyEditLinkmanList.add(companyLinkman);
            }else {
                companyLinkmanMap.put(String.valueOf(id), companyLinkman);
            }
        }

        for(int i = 0; i< companyEditLinkmanList.size() ; i++){
            CompanyLinkman companyLinkman = companyEditLinkmanList.get(i);
            String strId = String.valueOf(companyLinkman.getId());
            if(!"0".equals(strId)) {
                if (companyLinkmanMap.containsKey(strId)) {
                    //编辑
                    BeanRefUtil.copyProperty(companyLinkman,companyLinkmanMap.get(strId));
                } else {
                    //删除
                    companyEditLinkmanList.remove(companyLinkman);

                }
            }
        }

        companyLinkmanMap.clear();

        //跟进人信息操作
        Map<String,CompanySalesman> companySalesmanMap = new HashMap<>();
        for(CompanySalesman companySalesman:companySalesmanList){
            int id = companySalesman.getId();
            if(0 == id){
                companySalesman.setCompanyKey(strCompanyKey);
                companySalesman.setCreator(creator);
                companyEditSalesmanList.add(companySalesman);
            }else {
                companySalesmanMap.put(String.valueOf(id), companySalesman);
            }
        }

        for(int i = 0; i< companyEditSalesmanList.size() ; i++){
            CompanySalesman companySalesman = companyEditSalesmanList.get(i);
            String strId = String.valueOf(companySalesman.getId());
            if(!"0".equals(strId)) {
                if (companySalesmanMap.containsKey(strId)) {
                    //编辑
                    BeanRefUtil.copyProperty(companySalesman,companySalesmanMap.get(strId));
                } else {
                    //删除
                    companyEditLinkmanList.remove(companySalesman);

                }
            }
        }

        companySalesmanMap.clear();


//        boolean existsMain = false;
//        String strLinkman = "";
//        for(CompanyLinkman companyLinkman : companyEditLinkmanList){
//
//            companyLinkman.setCompanyKey(strCompanyKey);
//            companyLinkman.setCreator(creator);
//
//            if(existsMain && companyLinkman.isMain()) {
//                existsMain = false;
//            }
//
//            if(!existsMain && companyLinkman.isMain()) {
//                existsMain = true;
//                strLinkman = companyLinkman.getName();
//            }
//        }
//
//        if(!existsMain){
//            return new ResponseObj(-1,"客户必须且只能设定一个【主要联系人】，操作失败。",null);
//        }
//
//        existsMain = false;
//        String strSalesman = "";
//        for(CompanySalesman companySalesman : companyEditSalesmanList){
//            companySalesman.setCompanyKey(strCompanyKey);
//            companySalesman.setCreator(creator);
//
//            if(existsMain && companySalesman.isMain()) {
//                existsMain = false;
//            }
//
//            if(!existsMain && companySalesman.isMain()) {
//                existsMain = true;
//                int id = companySalesman.getUserId();
//                User user = userRepo.findOne(id);
//                if(null == user){
//                    return new ResponseObj(-1,"未找到跟进人ID为【" + id + "】的信息，操作失败。",null);
//                }
//                strSalesman = user.getName();
//            }
//        }
//
//        if(!existsMain){
//            return new ResponseObj(-1,"客户必须且只能设定一个【主要跟进人】，操作失败。",null);
//        }


        String strLinkman = getMainLinkman(companyEditLinkmanList,strCompanyKey,creator);
        if("".equals(strLinkman)){
            return new ResponseObj(-1,"客户必须且只能设定一个【主要联系人】，操作失败。",null);
        }

        String strSalesman = getMainSalesman(companyEditSalesmanList,strCompanyKey,creator);
        if("".equals(strSalesman) || strSalesman.length() > 20){
            return new ResponseObj(-1,("".equals(strSalesman)) ? "客户必须且只能设定一个【主要跟进人】，操作失败。" : strSalesman,null);
        }

        BeanRefUtil.copyProperty(companyEdit,company);


        CompanyLoginfo companyLoginfo = companyLogInfoRepo.findTop1ByCompanyKey(strCompanyKey);
        if(null != companyLoginfo) {

            companyLoginfo.setCompanyKey(strCompanyKey);
            companyLoginfo.setLinkman(strLinkman);
            companyLoginfo.setSalesman(strSalesman);
        }
        try{
            companyRepo.save(companyEdit);
            companyLinkmanRepo.save(companyEditLinkmanList);
            companySalesmanRepo.save(companyEditSalesmanList);
            if(null != companyLoginfo) {
                companyLogInfoRepo.save(companyLoginfo);
            }
            return new ResponseObj(0,"客户【" + name + "】修改成功。",company.getId());
        }catch (Exception e){
            return new ResponseObj(-1,"客户【" + name + "】修改失败。" + e.getMessage(),null);

        }


    }


    /**
     * 分页展示客户或供应商信息包含（客户的项目数量汇总/联系人/跟进人/最新的三条跟进记录)
     * @param page
     * @param size
     * @return PageObject<Company>
     *
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    @ResponseBody
    public PageObject<Company> list(@RequestParam("page") int page,
                                    @RequestParam("size") int size,
                                    @RequestParam("showLoginfo") boolean showLoginfo) {

        Pageable pageable = new PageRequest(page, size);
        Page<Object[]> objectList = (Page<Object[]>)companyRepo.listAllPage(pageable);
        /*
        COALESCE(b.pjtNum,0) as pjtNum, " +
            " COALESCE(b.linkman,'') as linkman, " +
            " COALESCE(b.salesman,'') as salesman, " +
            " COALESCE(b.firstLog,'') as firstLog, " +
            " COALESCE(b.secondLog,'') as secondLog, " +
            " COALESCE(b.thirdLog,'') as thirdLog " +
         */

        List<Company> result = null;

        if (null != objectList) {
            result = new ArrayList<>();

            for (Object[] object : objectList) {
                Company company = (Company) object[0];
                company.setPjtNum(Integer.valueOf(object[1].toString()));
                company.setLinkman(object[2].toString());
                company.setSalesman(object[3].toString());
                String strloginfo = "";
                if(showLoginfo) {

                    String str = object[4].toString().trim();
                    strloginfo = (!"".equals(str)) ? str : "";
                    str = object[5].toString().trim();
                    if (!"".equals(str)) {
                        strloginfo = str + "\n" + strloginfo;
                    }
                    str = object[6].toString().trim();
                    if (!"".equals(str)) {
                        strloginfo = str + "\n" + strloginfo;
                    }
                }
                company.setLogInfo(strloginfo);

                result.add(company);
            }
            PageObject<Company> po = new PageObject<>();
            po.setPage(page);
            po.setDataList(result);
            po.setTotalPages(objectList.getTotalPages());
            po.setTotalElements(objectList.getTotalElements());
            return po;
        }
        else
            return null;
    }



    /**
     * 通过Id获取单个公司信息
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/get/{id}")
    @ResponseBody
    public ResponseObj get(@PathVariable int id) {
        Company company = companyRepo.findOne(id);
        if(null == company){
            return new ResponseObj(-1,"未找到ID为【" + id + "】的客户，操作失败。",null);
        }

        List<CompanySalesman> companySalesmanList = companySalesmanExtService.listByCompanyKey(company.getCompanyKey());
        List<CompanyLinkman> companyLinkmanList = companyLinkmanRepo.findByCompanyKey(company.getCompanyKey());
        CompanyVo companyVo = new CompanyVo();
        companyVo.setCompany(company);
        companyVo.setCompanySalesmanList(companySalesmanList);
        companyVo.setCompanyLinkmanList(companyLinkmanList);

        return new ResponseObj(0,"信息获取完成。",companyVo);
    }


    /**
     * 通过Id获取单个公司的所有相关信息（公司，公司联系人，公司跟进人，最新跟进记录，公司项目列表）
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getInfo/{id}")
    @ResponseBody
    public ResponseObj getInfo(@PathVariable int id) {
        Company company = companyRepo.findOne(id);
        if(null == company){
            return new ResponseObj(-1,"未找到ID为【" + id + "】的客户，操作失败。",null);
        }

        List<Project> projectList = projectRepo.findByCompanyId(id);
        List<UserLoginfo> userLoginfoList = userLoginfoRepo.findTop10ByCompanyIdOrderByTimeDesc(id);
        List<CompanySalesman> companySalesmanList = companySalesmanExtService.listByCompanyKey(company.getCompanyKey());
        List<CompanyLinkman> companyLinkmanList = companyLinkmanRepo.findByCompanyKey(company.getCompanyKey());


        CompanyInfoVo companyInfoVo = new CompanyInfoVo();
        companyInfoVo.setCompany(company);
        companyInfoVo.setCompanyLinkmanList(companyLinkmanList);
        companyInfoVo.setCompanySalesmanList(companySalesmanList);
        companyInfoVo.setProjectList(projectList);
        companyInfoVo.setUserLoginfoList(userLoginfoList);
        return new ResponseObj(0,"信息获取完成。",companyInfoVo);
    }

}
