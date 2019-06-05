package com.yinda.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 9:42
 * @UpdateUser:
 * @UpdateDate:2019/5/31 9:42
 * @UpdateRemark:
 * @Version:
 */
@Data
@NoArgsConstructor
public class TerminalRegisterMsgRespBody {
    public static final byte SUCCESS = 0;
    public static final byte CAR_ALREADY_REGISTERED = 1;
    public static final byte CAR_NOT_FOUND = 2;
    public static final byte TERMINAL_ALREADY_REGISTERED = 3;
    public static final byte TERMINAL_NOT_FOUND = 4;
    /**
     * byte[0-1] 应答流水号(WORD) 对应的终端注册消息的流水号
     */
    private int replyFlowId;
    /***
     * byte[2] 结果(BYTE) <br>
     * 0：成功<br>
     * 1：车辆已被注册<br>
     * 2：数据库中无该车辆<br>
     **/
    private byte replyCode;
    /**
     * byte[3-x] 鉴权码(STRING) 只有在成功后才有该字段
     */
    private String replyToken;
}
