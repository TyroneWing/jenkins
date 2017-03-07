package com.tinckay.common;

/**
 * Created by root on 1/3/17.
 */
public enum StateValue {
    IS_NEW((byte)0),
    IS_END((byte)1),
    IS_CANCEL((byte)2),
    IS_PAUSE((byte)3),
    IS_RUN((byte)10),
    END_ASK((byte)11),
    END_NOT_PASS((byte)12),
    CANCEL_ASK((byte)21),
    CANCEL_NOT_PASS((byte)22),
    PAUSE_ASK((byte)31),
    PAUSE_NOT_PASS((byte)32);

    private byte value = 0;

    private StateValue(byte value){
        this.value = value;
    }

    public static StateValue valueOf(int value) {    //    手写的从int到enum的转换函数
        switch (value) {
            case 0:
                return IS_NEW;
            case 1:
                return IS_END;
            case 2:
                return IS_CANCEL;
            case 3:
                return IS_PAUSE;
            case 10:
                return IS_RUN;
            case 11:
                return END_ASK;
            case 12:
                return END_NOT_PASS;
            case 21:
                return CANCEL_ASK;
            case 22:
                return CANCEL_NOT_PASS;
            case 31:
                return PAUSE_ASK;
            case 32:
                return PAUSE_NOT_PASS;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
