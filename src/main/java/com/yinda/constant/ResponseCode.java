package com.yinda.constant;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/6/3 8:56
 * @UpdateUser:
 * @UpdateDate:2019/6/3 8:56
 * @UpdateRemark:
 * @Version:
 */
public enum ResponseCode {
    /**
     * 响应成功
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * 响应失败
     */
    ERROR(1, "ERROR"),
    ;

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

