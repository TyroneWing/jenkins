package com.tinckay.domain.jpa;


import com.tinckay.domain.base.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by root on 12/26/16.
 */
public interface TaskRepo extends JpaRepository<Task,Long> {

    List<Task> findByPjtIdOrderByOrderId(Long pjtId);


    @RestResource(exported = false)
    @Query("SELECT a ,b.name as managerName,c.name as creatorName "
            + "FROM Task a ,User b,User c "
            + "where a.manager=b.id and a.creator=c.id "
            + "ORDER BY a.createTime DESC")
    Page<?> getAllTaskListPage(Pageable pageable);


    //查询新增及进行中状态的任务，超期或以计划结束时间为标准，提前1天提醒用户,用于首页提醒
    @RestResource(exported = false)
    @Query("SELECT a ,(to_days(a.planEnd) - to_days(now())) as remainDays,b.name as managerName,c.name as creatorName "
            + "FROM Task a ,User b,User c "
            + "where a.state in (0,10) and a.planEnd is not null and a.manager=b.id and a.creator=c.id "
            + "and b.name like :un "
            + "and (to_days(a.planEnd) - to_days(now())) <= 3 "
            + "ORDER BY a.planEnd DESC ")
    Page<?> getRemindTaskList(@Param("un") String username, Pageable pageable);
}
