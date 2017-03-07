package com.tinckay.domain.vo;

import com.tinckay.domain.base.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by root on 1/15/17.
 */
@Getter
@Setter
public class PjtTaskBatchVo {
    private Long pjtId;
    private List<Task> taskList;
}
