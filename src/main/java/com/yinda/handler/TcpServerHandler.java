package com.yinda.handler;

import com.yinda.constant.Jt808Constant;
import com.yinda.constant.RedisConstant;
import com.yinda.exception.ExceptionHelper;
import com.yinda.model.dto.*;
import com.yinda.model.dto.PackageData.MsgHeader;
import com.yinda.server.ChannelMap;
import com.yinda.server.SessionManager;
import com.yinda.service.TerminalMsgProcessService;
import com.yinda.utils.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/30 16:35
 * @UpdateUser:
 * @UpdateDate:2019/5/30 16:35
 * @UpdateRemark:
 * @Version:
 */
@Slf4j
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
    private final SessionManager sessionManager;
    private final MsgDecoder decoder;
    private TerminalMsgProcessService msgProcessService;
    private Jt808ProtocolUtil protocolUtil = new Jt808ProtocolUtil();

    public TcpServerHandler() {
        this.sessionManager = SessionManager.getInstance();
        this.decoder = new MsgDecoder();
        this.msgProcessService = new TerminalMsgProcessService();
    }

    /**
     * 接收消息
     *
     * @param ctx
     * @param msg
     * @throws InterruptedException
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf buf = (ByteBuf) msg;
            if (buf.readableBytes() <= 0) {
                return;
            }
            byte[] bs = new byte[buf.readableBytes()];
            buf.readBytes(bs);
            bs = this.protocolUtil.doEscape4Receive(bs, 0, bs.length);
            log.info("数据length bs:{},数据 bs:{}", bs.length, bs);
            // 字节数据转换为针对于808消息结构的实体类
            PackageData pkg = this.decoder.bytes2PackageData(bs);
            // 引用channel,以便回送数据给硬件
            pkg.setChannel(ctx.channel());
            String phone = pkg.getMsgHeader().getTerminalPhone();
            //手机号码长度补全统一长度16
            phone = String.format("%016d", Long.parseLong(phone));
            //使用手机号码作为key，将对应的连接通道保存
            ChannelMap.addChannel(phone, ctx.channel());
            this.processPackageData(pkg);
        } finally {
            release(msg);
        }
    }


    /**
     * 处理业务逻辑
     *
     * @param packageData
     */
    private void processPackageData(PackageData packageData) {
        final MsgHeader header = packageData.getMsgHeader();
        // 1.终端心跳应答
        if (Jt808Constant.MSG_ID_TERMINAL_HEART_BEAT == header.getMsgId()) {
            if (log.isDebugEnabled()) {
                log.debug(">>>>>[终端心跳应答],data={}", packageData);
            }
            try {
                this.msgProcessService.processTerminalHeartBeatMsg(packageData);
            } catch (Exception e) {
                log.error("<<<<<[终端心跳]处理错误,phone={},flow_id={},err={}", header.getTerminalPhone(), header.getFlowId(), ExceptionHelper.hand(e));
            }
        }
        // 2.终端鉴权应答
        else if (Jt808Constant.MSG_ID_TERMINAL_AUTHENTICATION == header.getMsgId()) {
            if (log.isDebugEnabled()) {
                log.debug(">>>>>[终端鉴权应答],data={}", packageData);
            }
            try {
                TerminalAuthenticationMsg authenticationMsg = this.decoder.toTerminalAuthenticationMsg(packageData);
                this.msgProcessService.processAuthMsg(authenticationMsg);
            } catch (Exception e) {
                log.error("<<<<<[终端鉴权]处理错误,phone={},flow_id={},err={}", header.getTerminalPhone(), header.getFlowId(), ExceptionHelper.hand(e));
            }
        }
        // 3.终端注册应答
        else if (Jt808Constant.MSG_ID_TERMINAL_REGISTER == header.getMsgId()) {
            if (log.isDebugEnabled()) {
                log.debug(">>>>>[终端注册应答],data={}", packageData);
            }
            try {
                TerminalRegisterMsg msg = this.decoder.toTerminalRegisterMsg(packageData);
                this.msgProcessService.processRegisterMsg(msg);
            } catch (Exception e) {
                log.error("<<<<<[终端注册]处理错误,phone={},flow_id={},err={}", header.getTerminalPhone(), header.getFlowId(), ExceptionHelper.hand(e));
            }
        }
        // 4.终端注销应答(终端注销数据消息体为空)
        else if (Jt808Constant.MSG_ID_TERMINAL_LOG_OUT == header.getMsgId()) {
            if (log.isDebugEnabled()) {
                log.debug(">>>>>[终端注销应答],data={}", packageData);
            }
            try {
                this.msgProcessService.processTerminalLogoutMsg(packageData);
            } catch (Exception e) {
                log.error("<<<<<[终端注销]处理错误,phone={},flow_id={},err={}", header.getTerminalPhone(), header.getFlowId(), ExceptionHelper.hand(e));
            }
        }
        // 5.位置信息汇报应答
        else if (Jt808Constant.MSG_ID_TERMINAL_LOCATION_INFO_UPLOAD == header.getMsgId()) {
            if (log.isDebugEnabled()) {
                log.debug(">>>>>[位置信息汇报应答],data={}", packageData);
            }
            try {
                LocationInfoUploadMsg locationInfoUploadMsg = this.decoder.toLocationInfoUploadMsg(packageData);
                this.msgProcessService.processLocationInfoUploadMsg(locationInfoUploadMsg);
                String time = DateTimeUtil.dateToString(locationInfoUploadMsg.getTime(), DateTimeUtil.FORMAT_STANDARD_DATETIME);
                RedisHelper redisHelper = SpringContextUtil.getBean(RedisHelper.class);
                String terminalPhone = locationInfoUploadMsg.getMsgHeader().getTerminalPhone();
                redisHelper.setT(RedisConstant.GPS_RECORD_PREFIX + terminalPhone, locationInfoUploadMsg);

            } catch (Exception e) {
                log.error("<<<<<[位置信息]处理错误,phone={},flow_id={},err={}", header.getTerminalPhone(), header.getFlowId(), ExceptionHelper.hand(e));
            }
        }
        // 6.位置信息查询应答
        else if (Jt808Constant.QUERY_LOCATION_RESP == header.getMsgId()) {
            if (log.isDebugEnabled()) {
                log.debug(">>>>>[位置信息查询应答],data={}", packageData);
            }
            try {
                LocationInfoUploadMsg locationInfoUploadMsg = this.decoder.toLocationInfoUploadMsg(packageData);
                this.msgProcessService.processLocationInfoUploadMsg(locationInfoUploadMsg);
            } catch (Exception e) {
                log.error("<<<<<[位置信息查询应答]处理错误,phone={},flow_id={},err={}", header.getTerminalPhone(), header.getFlowId(), ExceptionHelper.hand(e));
            }
        }
        // 其他情况
        else {
            log.info(">>>>>>[未知消息类型应答],phone={},msgId={},package={}", header.getTerminalPhone(), header.getMsgId(), packageData);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("发生异常:{}", cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Session session = Session.buildSession(ctx.channel());
        sessionManager.put(session.getId(), session);
        log.info("终端连接:{}", session);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        final String sessionId = ctx.channel().id().asLongText();
        Session session = sessionManager.findBySessionId(sessionId);
        if (null != session) {
            this.sessionManager.removeBySessionId(sessionId);
            log.info("终端断开连接:{}", session);
        }
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Session session = this.sessionManager.removeBySessionId(Session.buildId(ctx.channel()));
                log.error("服务器主动断开连接:{}", session);
                ctx.close();
            }
        }
    }

    /**
     * 释放
     *
     * @param msg
     */
    private void release(Object msg) {
        try {
            ReferenceCountUtil.release(msg);
        } catch (Exception e) {
            log.error("释放异常:[{}]", ExceptionHelper.hand(e));
        }
    }
}
