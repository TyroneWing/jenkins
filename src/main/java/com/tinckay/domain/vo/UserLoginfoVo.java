package com.tinckay.domain.vo;

import com.tinckay.domain.base.UpFile;
import com.tinckay.domain.base.UserLoginfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by root on 2/23/17.
 */
@Getter
@Setter
public class UserLoginfoVo {
    private UserLoginfo userLoginfo;
    private List<UpFile> upFileList;
}
