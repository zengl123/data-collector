package com.yinda.service;

import com.alibaba.fastjson.JSON;
import com.yinda.model.dto.*;
import com.yinda.server.SessionManager;
import com.yinda.utils.MsgEncoder;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 10:14
 * @UpdateUser:
 * @UpdateDate:2019/5/31 10:14
 * @UpdateRemark:
 * @Version:
 */
@Slf4j
public class TerminalMsgProcessService extends BaseMsgProcessService {
    /**
     * 鉴权码
     */
    private final static String AUTHENTICATION_CODE = "123456";

    private MsgEncoder msgEncoder;
    private SessionManager sessionManager;

    public TerminalMsgProcessService() {
        this.msgEncoder = new MsgEncoder();
        this.sessionManager = SessionManager.getInstance();
    }

    /**
     * 终端注册应答
     *
     * @param msg
     * @throws Exception
     */
    public void processRegisterMsg(TerminalRegisterMsg msg) throws Exception {
        log.info("终端注册:{}", JSON.toJSONString(msg, true));
        putSession(msg.getChannel(), msg.getMsgHeader());
        TerminalRegisterMsgRespBody respMsgBody = new TerminalRegisterMsgRespBody();
        //注册应答
        byte res = 0;
        if (TerminalRegisterMsgRespBody.SUCCESS == res) {
            respMsgBody.setReplyCode(TerminalRegisterMsgRespBody.SUCCESS);
            respMsgBody.setReplyFlowId(msg.getMsgHeader().getFlowId());
            //鉴权码
            respMsgBody.setReplyToken(AUTHENTICATION_CODE);
        } else if (TerminalRegisterMsgRespBody.CAR_ALREADY_REGISTERED == res) {
            respMsgBody.setReplyCode(TerminalRegisterMsgRespBody.CAR_ALREADY_REGISTERED);
            respMsgBody.setReplyFlowId(msg.getMsgHeader().getFlowId());
        } else if (TerminalRegisterMsgRespBody.CAR_NOT_FOUND == res) {
            respMsgBody.setReplyCode(TerminalRegisterMsgRespBody.CAR_NOT_FOUND);
            respMsgBody.setReplyFlowId(msg.getMsgHeader().getFlowId());
        } else if (TerminalRegisterMsgRespBody.TERMINAL_ALREADY_REGISTERED == res) {
            respMsgBody.setReplyCode(TerminalRegisterMsgRespBody.TERMINAL_ALREADY_REGISTERED);
            respMsgBody.setReplyFlowId(msg.getMsgHeader().getFlowId());
        } else if (TerminalRegisterMsgRespBody.TERMINAL_NOT_FOUND == res) {
            respMsgBody.setReplyCode(TerminalRegisterMsgRespBody.TERMINAL_NOT_FOUND);
            respMsgBody.setReplyFlowId(msg.getMsgHeader().getFlowId());
        }
        int flowId = super.getFlowId(msg.getChannel());
        byte[] bs = this.msgEncoder.encode4TerminalRegisterResp(msg, respMsgBody, flowId);
        super.send2Client(msg.getChannel(), bs);
    }

    /**
     * 终端鉴权应答
     *
     * @param msg
     * @throws Exception
     */
    public void processAuthMsg(TerminalAuthenticationMsg msg) throws Exception {
        log.info("终端鉴权:{}", JSON.toJSONString(msg, true));
        putSession(msg.getChannel(), msg.getMsgHeader());
        ServerCommonRespMsgBody respMsgBody = new ServerCommonRespMsgBody();
        respMsgBody.setReplyCode(ServerCommonRespMsgBody.SUCCESS);
        respMsgBody.setReplyFlowId(msg.getMsgHeader().getFlowId());
        respMsgBody.setReplyId(msg.getMsgHeader().getMsgId());
        int flowId = super.getFlowId(msg.getChannel());
        byte[] bs = this.msgEncoder.encode4ServerCommonRespMsg(msg, respMsgBody, flowId);
        super.send2Client(msg.getChannel(), bs);
    }

    /**
     * 终端心跳信息应答
     *
     * @param req
     * @throws Exception
     */
    public void processTerminalHeartBeatMsg(PackageData req) throws Exception {
        log.info("心跳信息:{}", JSON.toJSONString(req, true));
        send2Client(req);
    }

    /**
     * 终端注销应答
     *
     * @param req
     * @throws Exception
     */
    public void processTerminalLogoutMsg(PackageData req) throws Exception {
        log.info("终端注销:{}", JSON.toJSONString(req, true));
        send2Client(req);
    }

    /**
     * 位置信息应答
     *
     * @param req
     * @throws Exception
     */
    public void processLocationInfoUploadMsg(LocationInfoUploadMsg req) throws Exception {
        log.info("位置 信息:{}", JSON.toJSONString(req, true));
        send2Client(req);
    }

    private void putSession(Channel channel, PackageData.MsgHeader msgHeader) {
        final String sessionId = Session.buildId(channel);
        Session session = sessionManager.findBySessionId(sessionId);
        if (session == null) {
            session = Session.buildSession(channel, msgHeader.getTerminalPhone());
        }
        session.setAuthenticated(true);
        session.setTerminalPhone(msgHeader.getTerminalPhone());
        sessionManager.put(session.getId(), session);
    }

    private void send2Client(PackageData req) throws Exception {
        final PackageData.MsgHeader reqHeader = req.getMsgHeader();
        ServerCommonRespMsgBody respMsgBody = new ServerCommonRespMsgBody(reqHeader.getFlowId(), reqHeader.getMsgId(), ServerCommonRespMsgBody.SUCCESS);
        int flowId = super.getFlowId(req.getChannel());
        byte[] bs = this.msgEncoder.encode4ServerCommonRespMsg(req, respMsgBody, flowId);
        super.send2Client(req.getChannel(), bs);
    }
}