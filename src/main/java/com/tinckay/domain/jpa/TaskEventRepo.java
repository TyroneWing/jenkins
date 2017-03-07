package com.tinckay.domain.jpa;

import com.tinckay.domain.base.TaskEvent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 12/26/16.
 */
public interface TaskEventRepo extends JpaRepository<TaskEvent,Long> {
}
