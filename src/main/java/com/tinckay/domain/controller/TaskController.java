package com.tinckay.domain.controller;

import com.tinckay.common.BeanRefUtil;
import com.tinckay.common.ErrorHandlerController;
import com.tinckay.common.PageObject;
import com.tinckay.common.ResponseObj;
import com.tinckay.domain.base.Project;
import com.tinckay.domain.base.Task;
import com.tinckay.domain.base.TaskEvent;
import com.tinckay.domain.jpa.*;
import com.tinckay.domain.vo.OptResumeVo;
import com.tinckay.domain.vo.PjtTaskBatchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 1/5/17.
 */
@Controller
@RequestMapping("/capi/Task")
public class TaskController extends ErrorHandlerController {
    @Autowired
    ProjectRepo projectRepo;


    @Autowired
    TaskEventRepo taskEventRepo;
//    @Autowired
//    OptRepo optRepo;

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UpFileRepo upFileRepo;


    private ResponseObj checkFieldInfo(Task task){
        String name = task.getTilte();
        if(null == name || name.trim().equals(""))
            return new ResponseObj(-1,"任务【标题】不能为空，操作失败。", null);

        Date planStart = task.getPlanStart();
        if(null == planStart)
            return new ResponseObj(-1,"任务【计划开始时间】不能为空，操作失败。", null);

        Date planEnd = task.getPlanEnd();
        if(null == planEnd)
            return new ResponseObj(-1,"任务【计划结束时间】不能为空，操作失败。", null);

        if(planEnd.getTime() <= planStart.getTime())
            return new ResponseObj(-1,"任务【计划结束时间】不能 <= 【计划开始时间】，操作失败。", null);

        Date realStart = task.getRealStart();
        Date realEnd = task.getRealEnd();
        if(null != realStart && null != realEnd && realEnd.getTime() <= realStart.getTime())
            return new ResponseObj(-1,"任务【实际结束时间】不能 <= 【实际开始时间】，操作失败。", null);

        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    @Transactional
    public @ResponseBody ResponseObj add(@RequestBody Task task) {

        if(null == task)
            return new ResponseObj(-1,"无法获取新增任务信息，操作失败。", null);

        Long pjtId = task.getPjtId();
        if(null == pjtId || 0 == pjtId)
            return new ResponseObj(-1,"任务的所属项目不能为空，操作失败。", null);

        Project project = projectRepo.findOne(pjtId);
        if(null == project)
            return new ResponseObj(-1,"未能在数据库中找到任务所属的项目信息，操作失败。", null);

        ResponseObj result = checkFieldInfo(task);
        if(null != result)
            return result;

        //还需加入规则，结束时间必须大于起始时间3天以上才做项目计划

        String uuid = BeanRefUtil.getUUID();
        task.setTaskKey(uuid);
        task.setState((byte) 0);
        task = taskRepo.saveAndFlush(task);
        return new ResponseObj("新增任务【" + task.getTilte() + "】成功。",task.getId());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/modifyBatch")
    @Transactional
    public @ResponseBody ResponseObj modifyBatch(@RequestBody PjtTaskBatchVo pjtTaskBatchVo) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{

        if(null == pjtTaskBatchVo)
            return new ResponseObj(-1,"无法获取项目任务列表信息，操作失败。", null);

        Long pjtId = pjtTaskBatchVo.getPjtId();
        if(null == pjtId || 0 == pjtId)
            return new ResponseObj(-1,"任务的所属项目不能为空，操作失败。", null);

        Project project = projectRepo.findOne(pjtId);
        if(null == project)
            return new ResponseObj(-1,"未能在数据库中找到任务所属的项目信息，操作失败。", null);

        List<Task> taskList = pjtTaskBatchVo.getTaskList();
        if(null == taskList)
            return new ResponseObj(-1,"项目任务列表信息不能为空，操作失败。", null);

        ResponseObj result = null;
        List<Task> editList = new ArrayList<>();

        for(Task task:taskList){
            result = checkFieldInfo(task);
            if(null != result)
                break;
            else {
                Long taskId = task.getId();
                if(0 < task.getId()){
                    Task oldTask = taskRepo.findOne(taskId);
                    if(null == oldTask) {
                        result = new ResponseObj(-1, "未从数据库中找到对应的任务信息，操作失败。", null);
                        break;
                    }
                    else {
                        BeanRefUtil.copyProperty(oldTask, task);
                        editList.add(oldTask);
                    }
                }
                else{
                    String uuid = BeanRefUtil.getUUID();
                    task.setTaskKey(uuid);
                    editList.add(task);
                }
            }
        }

        if(null != result){
            editList.clear();
            return result;
        }
        else {
            taskRepo.save(editList);
            return new ResponseObj("项目任务批量修改成功。");

        }


    }


    /**
     * 编辑任务信息
     * @param task
     * @return 成功：task
     */
    @RequestMapping(method = RequestMethod.POST, value = "/modify")
    public @ResponseBody ResponseObj modify(@RequestBody Task task) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        //ProjectVo target = new ProjectVo();
        if(null == task)
            return new ResponseObj(-1,"无法获取任务相关信息，操作失败。", null);




        Task editTask = taskRepo.findOne(task.getId());
        if(null == editTask)
            return new ResponseObj(-1,"无法从数据库获取对应的任务信息，操作失败。", null);

        //判断项目状态是否满足编辑条件
        byte state = editTask.getState();
        if((state >= 1 && state <= 3 ) || state == 11 || state == 21 || state == 31 )
            return new ResponseObj(-1,"任务处于【" + BeanRefUtil.stateMap.get(state) + "】状态，不允许编辑操作。",null);

        ResponseObj result = checkFieldInfo(task);
        if(null != result)
            return result;

        BeanRefUtil.copyProperty(editTask,task);

        taskRepo.save(editTask);
        return new ResponseObj("任务信息修改成功。",editTask);
    }

    /**
     * 删除任务信息
     * @param id（task 的 key id）
     * @return
     */
    @RequestMapping(value = "/delete/{id}")
    @Transactional
    public @ResponseBody ResponseObj delete(@PathVariable Long id) {

        Task task  = taskRepo.findOne(id);
        if(null != task) {
            /**
             * 满足二个大条件，任务才允许删除
             * 1，项目状态为【新增】，【进行中】
             * 2，任务状态为【新增】，【取消】
             */
            String title = task.getTilte();
            byte state = task.getState();
            if(state != 0 && state != 2)
                return new ResponseObj(-1,"任务【" + title  + "】处于【" + BeanRefUtil.stateMap.get(state) + "】状态，不允许删除操作。",null);


            taskRepo.delete(task);
            return new ResponseObj("任务【" + title  + "】删除成功");
        }
        else{
            return new ResponseObj(-1,"未找到ID【" + id + "】对应的任务，无法删除。", null);
        }


    }

    /**
     * 启动任务
     * @param optResumeVo project 的 key id 及 操作原因
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/start")
    @Transactional
    public @ResponseBody ResponseObj start(@RequestBody OptResumeVo optResumeVo) {
        return optTask(optResumeVo,(byte)10);
    }


    /**
     * 完结任务
     * @param optResumeVo project 的 key id 及 操作原因
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/finish")
    @Transactional
    public @ResponseBody ResponseObj finish(@RequestBody OptResumeVo optResumeVo) {
        return optTask(optResumeVo,(byte)1);
    }


    /**
     * 取消任务操作
     * @param optResumeVo task 的 key id 及 操作原因
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/cancel")
    @Transactional
    public @ResponseBody ResponseObj cancel(@RequestBody OptResumeVo optResumeVo) {
        return optTask(optResumeVo,(byte)2);

    }


    /**
     * 暂停项目操作
     * @param optResumeVo task 的 key id 及 操作原因
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/pause")
    @Transactional
    public @ResponseBody ResponseObj pause(@RequestBody OptResumeVo optResumeVo) {
        return optTask(optResumeVo,(byte)3);

    }

    /**
     * 变更项目状态操作
     * @param optResumeVo
     * @param optFlag
     * @return
     */

    private @ResponseBody ResponseObj optTask(OptResumeVo optResumeVo,byte optFlag) {
        String optInfo = BeanRefUtil.getOptFlagStr(optFlag);

        if (null == optResumeVo)
            return new ResponseObj(-1, "无法获取任务【" + optInfo + "】操作信息，操作失败。", null);

        Task task = taskRepo.findOne(optResumeVo.getId());
        if (null != task) {
            byte state = task.getState();
            if(optFlag == 10){
                //任务只有在状态为新增和取消状态才允许执行启动操作
                if (state != 0 && state != 3)
                    return new ResponseObj(-1, "任务状态为【" +
                            BeanRefUtil.stateMap.get(0) + "，" +
                            BeanRefUtil.stateMap.get(3) + "】才允许执行【" + optInfo + "】操作。", null);

            }
            else {
                //项目只有在新增和进行中才允许执行取消/暂停/完结操作
                if (state != 0 && state != 10)
                    return new ResponseObj(-1, "任务状态为【" +
                            BeanRefUtil.stateMap.get(0) + "，" +
                            BeanRefUtil.stateMap.get(10) + "】才允许执行【" + optInfo + "】操作。", null);
            }
            //取消和暂停操作需要注明原因
            String resume = optResumeVo.getResume();
            if ((optFlag == 2 || optFlag == 3) && (null == resume || resume.trim().equals("")))
                return new ResponseObj(-1, "任务的【" + optInfo + "】原因不能为空，无法执行【" + optInfo + "】操作。", null);

            String title = task.getTilte();
            task.setState(optFlag);
            taskRepo.save(task);
            return new ResponseObj("任务【" + title + "】执行【" + optInfo + "】操作成功", task);
        } else {
            return new ResponseObj(-1, "未找到对应的任务，无法执行【" + optInfo + "】操作。", null);
        }

    }



    /**
     * 获取项目对应的任务任务信息
     * @param pjtId
     * @return 成功：项目对应的任务列表信息（按orderId排序）
     */
    @RequestMapping(method = RequestMethod.GET, value = "/listPjtTask/{pjtId}")
    public @ResponseBody List<Task> listPjtTask(@PathVariable Long pjtId) {
        return taskRepo.findByPjtIdOrderByOrderId(pjtId);
    }

    /**
     * 获取项目对应的任务任务信息
     * @param id
     * @return 成功：项目对应的任务列表信息（按orderId排序）
     */
    @RequestMapping(method = RequestMethod.GET, value = "/get/{id}")
    public @ResponseBody Task get(@PathVariable Long id) {
        return taskRepo.findOne(id);
    }

    /**
     * 查询任务列表
     * @param page
     * @param size
     * @return 成功：任务列表信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public @ResponseBody
    PageObject<Task> list(@RequestParam("page") int page, @RequestParam("size") int size) {

        Pageable pageable = new PageRequest(page, size);
        Page<Object[]> result = (Page<Object[]>)taskRepo.getAllTaskListPage(pageable);

        List<Task> resp = null;

        if (result != null) {
            resp = new ArrayList<>();
            for (Object[] object : result) {
                Task task = (Task) object[0];
                task.setManagerName(object[1].toString());
                task.setCreatorName(object[2].toString());
                resp.add(task);
            }
            PageObject<Task> po = new PageObject<>();
            po.setPage(page);
            po.setDataList(resp);
            po.setTotalPages(result.getTotalPages());
            po.setTotalElements(result.getTotalElements());
            return po;
        }
        else
            return null;
    }

    /**
     * 显示首页任务提醒
     * @param size
     * @param request
     * @return
     */

    @RequestMapping(method = RequestMethod.GET, value = "/listRemind/{size}")
    public @ResponseBody List<Task> listRemindTask(@PathVariable int size, HttpServletRequest request) {
        Pageable pageable = new PageRequest(0, size);
        String userName = ((null == request) ? "" : request.getRemoteUser());
        if(null == userName || userName.equals("") || userName.toLowerCase().equals("admin"))
            userName = "%";
        Page<Object[]> result = (Page<Object[]>)taskRepo.getRemindTaskList(userName,pageable);
        List<Task> resp = null;
        if (result != null) {
            resp = new ArrayList<>();
            for (Object[] object : result) {
                Task task = (Task) object[0];
                task.setRemainDays(Integer.valueOf(object[1].toString()));
                task.setManagerName(object[1].toString());
                task.setCreatorName(object[2].toString());
                resp.add(task);
            }
        }
        return resp;
    }


    /**
     * 修改任务计划时间
     * @param taskId
     * @param startDate
     * @param endDate
     * @param resume
     * @param request
     * @return ResponseObj
     */
    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/modifyPlanDate")
    public @ResponseBody
    ResponseObj modifyPlanDate(@RequestParam("taskId") Long taskId,
                               @RequestParam("startDate") Date startDate,
                               @RequestParam("endDate") Date endDate,
                               @RequestParam("resume") String resume,
                               HttpServletRequest request) {
        Task task = taskRepo.findOne(taskId);
        if(null == task)
            return new ResponseObj(-1,"未找到ID【" + taskId + "】对应的任务。",null);

        if(task.getPlanStart().getTime() == startDate.getTime() &&
                task.getPlanEnd().getTime() == endDate.getTime())
            return new ResponseObj(-1,"任务的计划时间无变化。",null);

        if(startDate.getTime() > endDate.getTime())
            return new ResponseObj(-1,"任务的计划结束时间不能小于开始时间。",null);

        if(null == resume || resume.equals(""))
            return new ResponseObj(-1,"任务的计划时间调整的理由不能为空。",null);
        if(null != startDate)
            task.setPlanStart(startDate);
        if(null != endDate)
            task.setPlanEnd(endDate);


//        Opt opt = new Opt();
//        opt.setResume(resume);
//        opt.setUserName(((null == request) ? "" : request.getRemoteUser()));
//        opt.setTaskId(taskId);
//        opt.setFlag((byte)10);
//        opt.setIp(((null == request) ? "" : request.getRemoteHost()));
        TaskEvent taskEvent = new TaskEvent();
        taskEvent.setTaskId(taskId);
        taskEvent.setResume(resume);
        taskEvent.setFlag((byte)1);
        taskEvent.setUserName(((null == request) ? "" : request.getRemoteUser()));

        taskEvent = taskEventRepo.saveAndFlush(taskEvent);
        taskRepo.saveAndFlush(task);
        return new ResponseObj("任务【" + task.getTilte() + "】的计划修改成功，变更为【" + startDate + "】至【" + endDate + "】。",taskEvent.getId());
    }

}
