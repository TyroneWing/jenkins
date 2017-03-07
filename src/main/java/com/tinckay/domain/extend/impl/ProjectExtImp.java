package com.tinckay.domain.extend.impl;

import com.tinckay.common.PageObject;
import com.tinckay.domain.base.Project;
import com.tinckay.domain.extend.service.ProjectExtService;
import com.tinckay.domain.jpa.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 3/7/17.
 */
@Service
public class ProjectExtImp implements ProjectExtService{

    @Autowired
    ProjectRepo projectRepo;

    private Project setProjectInfo(Object[] objects){
        if(null != objects && 0 < objects.length) {
            Project project = (Project) objects[0];
            project.setCompanyName(objects[1].toString());
            project.setManagerName(objects[2].toString());
            project.setLinkman(objects[3].toString());
            return project;
        }else{
            return null;
        }
    }

    public Project getOneById(Long id){
        Object[] objects = projectRepo.getOneById(id);
        return  setProjectInfo(objects);
    }

    public PageObject<Project> listFromPageParam(int page,int size){
        PageObject<Project> po = null;
        Pageable pageable = new PageRequest(page, size);
        Page<Object[]> objectList = (Page<Object[]>)projectRepo.listAllPage(pageable);
        List<Project> result = null;
        if (objectList != null) {
            result = new ArrayList<>();
            for (Object[] objects : objectList) {
                Project project = setProjectInfo(objects);
                if(null != project.getPlanStart() && null != project.getPlanEnd()){
                    Long consumingTime = project.getPlanEnd().getTime() - project.getPlanStart().getTime();
                    consumingTime = consumingTime / (1000 * 60 * 60 * 24);
                    project.setConsumingTime(consumingTime);
                }
                else
                    project.setConsumingTime(Long.valueOf(0));
                result.add(project);
            }
            po = new PageObject<>();
            po.setPage(page);
            po.setDataList(result);
            po.setTotalPages(objectList.getTotalPages());
            po.setTotalElements(objectList.getTotalElements());
        }
        return po;
    }


}
