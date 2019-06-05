package com.yinda.exception;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 17:25
 * @UpdateUser:
 * @UpdateDate:2019/5/31 17:25
 * @UpdateRemark:
 * @Version:
 */
public class MyException extends RuntimeException {

    public MyException(String message) {
        super(message);
    }

    public MyException(String msgTemplate, Object... args) {
        super(String.format(msgTemplate, args));
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }
}
