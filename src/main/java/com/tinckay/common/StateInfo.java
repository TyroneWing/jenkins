package com.tinckay.common;

/**
 * Created by root on 1/3/17.
 * 状态值：
 * 0,新增
 * 1,完结
 * 2,取消
 * 3,暂停
 * 10,进行中
 * 11,完结申请
 * 12,完结未通过
 * 21,取消申请
 * 22,取消未通过
 * 31,暂停申请
 * 32,暂停未通过
 *
 */
public enum StateInfo {
    NEW_INFO("新增"),
    END_INFO("完结"),
    CANCEL_INFO("取消"),
    PAUSE_INFO("暂停"),
    RUN_INFO("进行中"),
    END_ASK_INFO("完结申请"),
    END_NOT_PASS_INFO("完结未通过"),
    CANCEL_ASK_INFO("取消申请"),
    CANCEL_NOT_PASS_INFO("取消未通过"),
    PAUSE_ASK_INFO("暂停请求"),
    PAUSE_NOT_PASS_INFO("暂停未通过");

    private String value = "";

    private StateInfo(String value){
        this.value = value;
    }


    public String value() {
        return this.value;
    }
}
