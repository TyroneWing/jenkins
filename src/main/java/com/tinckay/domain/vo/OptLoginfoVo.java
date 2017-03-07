package com.tinckay.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tinckay.common.GlobalVar;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by root on 1/20/17.
 */
@Getter
@Setter
public class OptLoginfoVo {
    Long optId;//关联操作记录id
    String optType;//操作类型
    String userName;//操作人
    String resume;//操作内容哦
    String ip;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = GlobalVar.dtLongFmt, timezone = "Asia/Shanghai")
    Date optDate;//操作时间

}
