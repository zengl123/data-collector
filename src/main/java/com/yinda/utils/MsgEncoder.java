package com.yinda.utils;

import com.yinda.constant.Jt808Constant;
import com.yinda.model.dto.*;

import java.util.Arrays;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 10:05
 * @UpdateUser:
 * @UpdateDate:2019/5/31 10:05
 * @UpdateRemark:
 * @Version:
 */
public class MsgEncoder {
    private BitOperator bitOperator;
    private Jt808ProtocolUtil jt808ProtocolUtil;

    public MsgEncoder() {
        this.bitOperator = new BitOperator();
        this.jt808ProtocolUtil = new Jt808ProtocolUtil();
    }

    public byte[] encode4TerminalRegisterResp(TerminalRegisterMsg req, TerminalRegisterMsgRespBody respMsgBody, int flowId) throws Exception {
        // 消息体字节数组
        byte[] msgBody;
        // 鉴权码(STRING) 只有在成功后才有该字段
        if (respMsgBody.getReplyCode() == TerminalRegisterMsgRespBody.SUCCESS) {
            msgBody = this.bitOperator.concatAll(Arrays.asList(
                    // 流水号(2)
                    bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()),
                    // 结果
                    new byte[]{respMsgBody.getReplyCode()},
                    // 鉴权码(STRING)
                    respMsgBody.getReplyToken().getBytes(Jt808Constant.STRING_CHARSET)
            ));
        } else {
            msgBody = this.bitOperator.concatAll(
                    // 流水号(2)
                    Arrays.asList(bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()),
                            new byte[]{respMsgBody.getReplyCode()}
                    ));
        }
        // 消息头
        int msgBodyProps = this.jt808ProtocolUtil.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
        byte[] msgHeader = this.jt808ProtocolUtil.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
                Jt808Constant.CMD_TERMINAL_REGISTER_RESP, msgBody, msgBodyProps, flowId);
        byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);
        // 校验码
        int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);
        // 连接并且转义
        return this.doEncode(headerAndBody, checkSum);
    }

    public byte[] encode4ServerCommonRespMsg(PackageData req, ServerCommonRespMsgBody respMsgBody, int flowId)
            throws Exception {
        byte[] msgBody = this.bitOperator.concatAll(Arrays.asList(
                // 应答流水号
                bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()),
                // 应答ID,对应的终端消息的ID
                bitOperator.integerTo2Bytes(respMsgBody.getReplyId()),
                // 结果
                new byte[]{respMsgBody.getReplyCode()}
        ));
        // 消息头
        int msgBodyProps = this.jt808ProtocolUtil.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
        byte[] msgHeader = this.jt808ProtocolUtil.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
                Jt808Constant.CMD_COMMON_RESP, msgBody, msgBodyProps, flowId);
        byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);
        // 校验码
        int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);
        // 连接并且转义
        return this.doEncode(headerAndBody, checkSum);
    }

    public byte[] encode4ParamSetting(byte[] msgBodyBytes, Session session) throws Exception {
        // 消息头
        int msgBodyProps = this.jt808ProtocolUtil.generateMsgBodyProps(msgBodyBytes.length, 0b000, false, 0);
        byte[] msgHeader = this.jt808ProtocolUtil.generateMsgHeader(session.getTerminalPhone(),
                Jt808Constant.CMD_TERMINAL_PARAM_SETTINGS, msgBodyBytes, msgBodyProps, session.currentFlowId());
        // 连接消息头和消息体
        byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBodyBytes);
        // 校验码
        int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);
        // 连接并且转义
        return this.doEncode(headerAndBody, checkSum);
    }

    private byte[] doEncode(byte[] headerAndBody, int checkSum) throws Exception {
        byte[] noEscapedBytes = this.bitOperator.concatAll(Arrays.asList(
                new byte[]{Jt808Constant.PKG_DELIMITER},
                headerAndBody,
                bitOperator.integerTo1Bytes(checkSum),
                new byte[]{Jt808Constant.PKG_DELIMITER}
        ));
        // 转义
        return jt808ProtocolUtil.doEscape4Send(noEscapedBytes, 1, noEscapedBytes.length - 2);
    }
}