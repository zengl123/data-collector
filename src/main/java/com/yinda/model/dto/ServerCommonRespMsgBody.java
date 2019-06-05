package com.yinda.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 9:40
 * @UpdateUser:
 * @UpdateDate:2019/5/31 9:40
 * @UpdateRemark:
 * @Version:
 */
@Data
@NoArgsConstructor
public class ServerCommonRespMsgBody {
    public static final byte SUCCESS = 0;
    public static final byte FAILURE = 1;
    public static final byte MSG_ERROR = 2;
    public static final byte UN_SUPPORTED = 3;
    public static final byte WARN_MSG_ACK = 4;

    /**
     * byte[0-1] 应答流水号 对应的终端消息的流水号
     */
    private int replyFlowId;
    /**
     * byte[2-3] 应答ID 对应的终端消息的ID
     */
    private int replyId;
    /**
     * 0：成功∕确认<br>
     * 1：失败<br>
     * 2：消息有误<br>
     * 3：不支持<br>
     * 4：报警处理确认<br>
     */
    private byte replyCode;

    public ServerCommonRespMsgBody(int replyFlowId, int replyId, byte replyCode) {
        super();
        this.replyFlowId = replyFlowId;
        this.replyId = replyId;
        this.replyCode = replyCode;
    }
}
