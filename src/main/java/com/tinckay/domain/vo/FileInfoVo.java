package com.tinckay.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by root on 2/23/17.
 */
@Getter
@Setter
public class FileInfoVo {
    private String name;
    private String path;
    private int size;
    private String type;
}
