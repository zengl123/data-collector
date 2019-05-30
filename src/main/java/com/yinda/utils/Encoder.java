package com.yinda.utils;

import com.yinda.constant.Jt808Constant;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/30 17:24
 * @UpdateUser:
 * @UpdateDate:2019/5/30 17:24
 * @UpdateRemark:
 * @Version:
 */
@Slf4j
public class Encoder {
    private BitOperator bitOperator;
    private Jt808ProtocolUtil jt808ProtocolUtil;

    public Encoder() {
        this.bitOperator = new BitOperator();
        this.jt808ProtocolUtil = new Jt808ProtocolUtil();
    }

    /**
     * 组装数据
     * 标识符+消息头+消息体+校验码+标识符
     *
     * @param headerAndBody
     * @param checkSum
     * @return
     * @throws Exception
     */
    private byte[] doEncode(byte[] headerAndBody, int checkSum) throws Exception {
        byte[] noEscapedBytes = this.bitOperator.concatAll(Arrays.asList(
                // 0x7e
                new byte[]{Jt808Constant.PKG_DELIMITER},
                // 消息头+ 消息体
                headerAndBody,
                // 校验码
                bitOperator.integerTo1Bytes(checkSum),
                // 0x7e
                new byte[]{Jt808Constant.PKG_DELIMITER}
        ));
        // 转义
        return jt808ProtocolUtil.doEscape4Send(noEscapedBytes, 1, noEscapedBytes.length - 2);
    }
}
