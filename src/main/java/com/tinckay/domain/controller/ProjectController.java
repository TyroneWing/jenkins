package com.tinckay.domain.controller;

import com.tinckay.common.BeanRefUtil;
import com.tinckay.common.ErrorHandlerController;
import com.tinckay.common.PageObject;
import com.tinckay.common.ResponseObj;
import com.tinckay.domain.base.*;
import com.tinckay.domain.extend.service.PjtUsersExtService;
import com.tinckay.domain.extend.service.ProjectExtService;
import com.tinckay.domain.jpa.CompanyRepo;
import com.tinckay.domain.jpa.PjtUsersRepo;
import com.tinckay.domain.jpa.ProjectRepo;
import com.tinckay.domain.vo.OptResumeVo;
import com.tinckay.domain.vo.ProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by root on 12/26/16.
 */
@Controller
@RequestMapping("/capi/Project")
public class ProjectController extends ErrorHandlerController{

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    ProjectExtService projectExtService;

    @Autowired
    PjtUsersRepo pjtUsersRepo;

    @Autowired
    PjtUsersExtService pjtUsersExtService;

    @Autowired
    CompanyRepo companyRepo;

    /**
     * Created by root on 01/03/17.
     * 新增项目信息（项目基本信息及项目人员构成）
     * @param projectVo(包含项目对象及项目人员构成列表对象)
     * @return
     *
     */
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    @Transactional
    public @ResponseBody ResponseObj add(@RequestBody ProjectVo projectVo,
                                         HttpServletRequest request) {

        if(null == projectVo) {
            return new ResponseObj(-1, "无法获取新增项目信息，操作失败。", null);
        }


        Project project  = projectVo.getProject();
        if(null == project) {
            return new ResponseObj(-1, "新增项目【信息】为空，操作失败。", null);
        }

        int companyId = project.getCompanyId();
        Company company = null;
        if(0 == companyId){
            return new ResponseObj(-1, "新增项目【所属客户】不能为空，操作失败。", null);
        }else {
            company = companyRepo.findOne(companyId);
            if(null == company){
                return new ResponseObj(-1, "未找到ID为【" + companyId + "】的客户信息，操作失败。", null);
            }
        }

        String code = project.getCode();
        if(null == code || code.trim().equals("")) {
            return new ResponseObj(-1, "新增项目【编号】不能为空，操作失败。", null);
        }




        String name = project.getName();
        if(null == name || name.trim().equals("")) {
            return new ResponseObj(-1, "新增项目【名称】不能为空，操作失败。", null);
        }

        Date planStart = project.getPlanStart();
        if(null == planStart) {
            return new ResponseObj(-1, "新增项目【计划开始时间】不能为空，操作失败。", null);
        }

        Date planEnd = project.getPlanEnd();
        if(null == planEnd) {
            return new ResponseObj(-1, "新增项目【计划结束时间】不能为空，操作失败。", null);
        }

        if(planEnd.getTime() <= planStart.getTime()) {
            return new ResponseObj(-1, "新增项目【计划结束时间】不能 <= 【计划开始时间】，操作失败。", null);
        }

        //还需加入规则，结束时间必须大于起始时间3天以上才做项目计划

        String creator = BeanRefUtil.getRemoteUserName(request);
        String strKey = BeanRefUtil.getUUID();
        project.setPjtKey(strKey);
        project.setCreator(creator);
        project.setState((byte) 0);
        project.setLevel((byte) 0);

        List<PjtUsers> pjtUsersList = projectVo.getPjtUsersList();
        if(null != pjtUsersList){
            for(PjtUsers pjtUsers:pjtUsersList){
                pjtUsers.setPjtKey(strKey);
                pjtUsers.setCreator(creator);

            }

        }
        try{
            if(null != pjtUsersList) {
                pjtUsersRepo.save(pjtUsersList);
            }
            company.setPjtNum(company.getPjtNum() + 1);
            companyRepo.save(company);
            projectRepo.save(project);
            return new ResponseObj("项目【" + project.getName() + "】新增成功",project.getId());

        }catch (Exception e){
            return new ResponseObj(-1,"项目【" + project.getName() + "】新增失败。" + e.getMessage(),null);
        }

    }


    /**
     * 编辑项目信息
     * @param projectVo
     * @return 成功：projectVo
     */
    @RequestMapping(method = RequestMethod.POST, value = "/modify")
    public @ResponseBody ResponseObj modify(@RequestBody ProjectVo projectVo,
                                            HttpServletRequest request) throws NoSuchFieldException, IllegalAccessException {
        //ProjectVo target = new ProjectVo();
        if(null == projectVo) {
            return new ResponseObj(-1, "无法获取项目相关信息，操作失败。", null);
        }


        Project project  = projectVo.getProject();
        if(null == project) {
            return new ResponseObj(-1, "项目编辑【信息】为空，操作失败。", null);
        }

        Project editPjt = projectRepo.findOne(project.getId());
        if(null == editPjt) {
            return new ResponseObj(-1, "无法从数据库获取对应的项目信息，操作失败。", null);
        }

        //判断项目状态是否满足编辑条件
        String stateInfo = BeanRefUtil.optPjtInState(editPjt.getState(),(byte)1);
        if(!stateInfo.equals("")) {
            return new ResponseObj(-1, stateInfo, null);
        }


        //项目基本格式检查
        String code = project.getCode();
        if(null == code || code.trim().equals(""))
            return new ResponseObj(-1,"新增项目【编号】不能为空，操作失败。", null);

        String name = project.getName();
        if(null == name || "".equals(name.trim()))
            return new ResponseObj(-1,"项目【名称】不能为空，操作失败。", null);

        Date planStart = project.getPlanStart();
        if(null == planStart)
            return new ResponseObj(-1,"项目【计划开始时间】不能为空，操作失败。", null);

        Date planEnd = project.getPlanEnd();
        if(null == planEnd)
            return new ResponseObj(-1,"项目【计划结束时间】不能为空，操作失败。", null);

        if(planEnd.getTime() <= planStart.getTime())
            return new ResponseObj(-1,"项目【计划结束时间】不能 <= 【计划开始时间】，操作失败。", null);


        Date realStart = project.getRealStart();
        if (null != realStart && realStart.getTime() < planStart.getTime())
                return new ResponseObj(-1,"项目【开始时间】不能 < 【计划开始时间】，操作失败。", null);

        Date realEnd = project.getRealEnd();

        if (null != realStart && null != realEnd && realEnd.getTime() <= realStart.getTime())
                return new ResponseObj(-1,"项目【结束时间】不能 <= 【开始时间】，操作失败。", null);


        //修改机制的确定，项目管理员的变更（编号/名称/管理员/计划时间/实际时间/状态变更）
        BeanRefUtil.copyProperty(editPjt,project);

//        if(!editPjt.getCode().equals(code))
//            editPjt.setCode(code);
//
//        if(!editPjt.getName().equals(name))
//            editPjt.setName(name);
//
//        if(editPjt.getManagerId() != project.getManagerId())
//            editPjt.setManagerId(project.setManagerId());
//
//        if(editPjt.getPlanStart().getTime() != planStart.getTime())
//            editPjt.setPlanStart(planStart);
//
//        if(editPjt.getPlanEnd().getTime() != planEnd.getTime())
//            editPjt.setPlanEnd(planEnd);
//
//        if(null != realStart)
//            editPjt.setRealStart(realStart);
//
//        if(null != realEnd)
//            editPjt.setRealEnd(realEnd);
//
//        if(editPjt.getState() != project.getState())
//            editPjt.setState(project.getState());
//
//        String resume = project.getResume().trim();
//        if(!editPjt.getResume().equals(resume))
//            editPjt.setResume(resume);


        //项目人员信息编辑及检查
        String strKey = editPjt.getPjtKey();
        String creator = BeanRefUtil.getRemoteUserName(request);
        List<PjtUsers> pjtUsersList = projectVo.getPjtUsersList();
        List<PjtUsers> editPjtUserList = pjtUsersRepo.findByPjtKey(strKey);
//        if(null != pjtUsersList){
//
//
//            //添加新的人员列表
//            for (PjtUsers pjtUser : pjtUsersList) {
//                pjtUser.setPjtKey(editPjt.getPjtKey());
//                Integer pjtUserId = pjtUser.getId();
//                if(null == pjtUserId || 0 == pjtUserId){
//                    editPjtUserList.add(pjtUser);
//                }
//                else{
//                    for(PjtUsers editPjtUser :editPjtUserList){
//                        if(pjtUserId == editPjtUser.getUserId()){
//                            editPjtUser.setRoleType(pjtUser.getRoleType());
//                            editPjtUser.setLeave(pjtUser.isLeave());
//                            editPjtUser.setLeaveResume(pjtUser.getLeaveResume());
//                            if(pjtUser.isLeave())
//                                editPjtUser.setLeaveTime(new Date());
//                        }
//                    }
//                }
//            }
//
//
//
//
//            pjtUsersRepo.save(editPjtUserList);
//        }




        //跟进人信息操作
        Map<String,PjtUsers> pjtUsersMap = new HashMap<>();
        for(PjtUsers pjtUsers:pjtUsersList){
            int id = pjtUsers.getId();
            if(0 == id){
                pjtUsers.setPjtKey(strKey);
                pjtUsers.setCreator(creator);
                editPjtUserList.add(pjtUsers);
            }else {
                pjtUsersMap.put(String.valueOf(id), pjtUsers);
            }
        }

        for(int i = 0; i< editPjtUserList.size() ; i++){
            PjtUsers pjtUsers = editPjtUserList.get(i);
            String strId = String.valueOf(pjtUsers.getId());
            if(!"0".equals(strId)) {
                if (pjtUsersMap.containsKey(strId)) {
                    //编辑
                    BeanRefUtil.copyProperty(pjtUsers,pjtUsersMap.get(strId));
                } else {
                    //删除
                    editPjtUserList.remove(pjtUsers);

                }
            }
        }

        pjtUsersMap.clear();
        try{
            if(null != editPjtUserList){
                pjtUsersRepo.save(editPjtUserList);
            }
            projectRepo.save(editPjt);
            return new ResponseObj("项目【" + editPjt.getName() + "】信息修改成功",editPjt.getId());

        }catch (Exception e){
            return new ResponseObj(-1,"项目【" + editPjt.getName() + "】信息修改失败。"+e.getMessage(),editPjt.getId());
        }

    }

    /**
     * 删除项目信息
     * @param id（project 的 key id）
     * @return
     */
    @RequestMapping(value = "/delete/{id}")
    @Transactional
    public @ResponseBody ResponseObj delete(@PathVariable Long id) {

        Project project  = projectRepo.findOne(id);
        if(null != project) {
            //判断项目状态
            String stateInfo = BeanRefUtil.optPjtInState(project.getState(),(byte)2);
            if(!stateInfo.equals("") )
                return new ResponseObj(-1,stateInfo, null);

            String name = project.getName();

            List<PjtUsers> users = pjtUsersRepo.findByPjtKey(project.getPjtKey());
            if(null != users){
                pjtUsersRepo.delete(users);
            }


            int companyId = project.getCompanyId();
            Company company = company = companyRepo.findOne(companyId);
            if(null == company){
                return new ResponseObj(-1, "未找到ID为【" + companyId + "】的客户信息，操作失败。", null);
            }

            try{
                company.setPjtNum(company.getPjtNum() - 1);
                companyRepo.save(company);
                projectRepo.delete(project);
                return new ResponseObj("项目【" + name  + "】删除成功。",id);
            }catch (Exception e){
                return new ResponseObj(-1,"项目【" + name  + "】删除失败。" + e.getMessage(),null);
            }

        }
        else{
            return new ResponseObj(-1,"未找到ID为【" + id + "】对应的项目，无法删除。", null);
        }


    }

    /**
     * 启动项目
     * @param optResumeVo project 的 key id 及 操作原因
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/start")
    @Transactional
    public @ResponseBody ResponseObj start(@RequestBody OptResumeVo optResumeVo) {
        return optPjt(optResumeVo,(byte)10);
    }


    /**
     * 完结项目操作
     * @param optResumeVo project 的 key id 及 操作原因
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/finish")
    @Transactional
    public @ResponseBody ResponseObj finishPjt(@RequestBody OptResumeVo optResumeVo) {
        return optPjt(optResumeVo,(byte)1);
    }


    /**
     * 取消项目操作
     * @param optResumeVo project 的 key id 及 操作原因
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/cancel")
    @Transactional
    public @ResponseBody ResponseObj cancel(@RequestBody OptResumeVo optResumeVo) {
        return optPjt(optResumeVo,(byte)2);

    }


    /**
     * 暂停项目操作
     * @param optResumeVo project 的 key id 及 操作原因
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/pause")
    @Transactional
    public @ResponseBody ResponseObj pause(@RequestBody OptResumeVo optResumeVo) {
        return optPjt(optResumeVo,(byte)3);

    }

    /**
     * 变更项目状态操作
     * @param optResumeVo
     * @param optFlag
     * @return
     */

    private @ResponseBody ResponseObj optPjt(OptResumeVo optResumeVo,byte optFlag) {
        String optInfo = BeanRefUtil.getOptFlagStr(optFlag);
        if (null == optResumeVo)
            return new ResponseObj(-1, "无法获取项目【" + optInfo + "】操作信息，操作失败。", null);

        Project project = projectRepo.findOne(optResumeVo.getId());
        if (null != project) {
            byte state = project.getState();
            if(optFlag == 10){
                //项目只有在状态为新增和取消状态才允许执行启动操作
                if (state != 0 && state != 3)
                    return new ResponseObj(-1, "项目状态为【" +
                            BeanRefUtil.stateMap.get(0) + "，" +
                            BeanRefUtil.stateMap.get(3) + "】才允许执行【" + optInfo + "】操作。", null);

            }
            else {
                //项目只有在新增和进行中才允许执行取消/暂停/完结操作
                if (state != 0 && state != 10)
                    return new ResponseObj(-1, "项目状态为【" +
                            BeanRefUtil.stateMap.get(0) + "，" +
                            BeanRefUtil.stateMap.get(10) + "】才允许执行【" + optInfo + "】操作。", null);
            }
            //取消和暂停操作需要注明原因
            String resume = optResumeVo.getResume();
            if ((optFlag == 2 || optFlag == 3) && (null == resume || resume.trim().equals("")))
                return new ResponseObj(-1, "项目的【" + optInfo + "】原因不能为空，无法执行【" + optInfo + "】操作。", null);

            String name = project.getName();
            project.setState(optFlag);
            projectRepo.save(project);
            return new ResponseObj("项目【" + name + "】执行【" + optInfo + "】操作成功", project);
        } else {
            return new ResponseObj(-1, "未找到对应的项目，无法执行【" + optInfo + "】操作。", null);
        }

    }
    /**
     * 查询项目信息
     * @param page
     * @param size
     * @return 成功：项目列表信息
     */
    /**
     * 获取单个项目信息
     * @param pjtId
     * @return 成功：项目对应的任务列表信息（按orderId排序）
     */
    @RequestMapping(method = RequestMethod.GET, value = "/get/{pjtId}")
    public @ResponseBody ProjectVo listPjtTask(@PathVariable Long pjtId) {
        ProjectVo projectVo = null;
        Project project = projectExtService.getOneById(pjtId);
        if(null != project){
            projectVo = new ProjectVo();
            projectVo.setProject(project);
            List<PjtUsers>  pjtUsersList = pjtUsersExtService.listByPjtKey(project.getPjtKey());
            if(null != pjtUsersList)
                projectVo.setPjtUsersList(pjtUsersList);
        }
        return projectVo;
    }

    /**
     *

     */


    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public @ResponseBody
    PageObject<Project> list(@RequestParam("page") int page, @RequestParam("size") int size) {
        return projectExtService.listFromPageParam(page, size);
    }
}
