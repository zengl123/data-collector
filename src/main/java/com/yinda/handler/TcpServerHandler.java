package com.yinda.handler;

import com.yinda.model.vo.PackageData;
import com.yinda.server.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class TcpServerHandler  extends ChannelInboundHandlerAdapter {
    private final SessionManager sessionManager;
    private final MsgDecoder decoder;
    private TerminalMsgProcessService msgProcessService;
    private LoopBuffer loopBuffer;
    private Map<Long, byte[]> map;

    public TcpServerHandler() {
        this.sessionManager = SessionManager.getInstance();
        this.decoder = new MsgDecoder();
        this.msgProcessService = new TerminalMsgProcessService();
        this.loopBuffer = LoopBuffer.getInstance();
        this.map = new ConcurrentHashMap<>();
    }

    /**
     * 接收消息
     *
     * @param ctx
     * @param msg
     * @throws InterruptedException
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        try {
            ByteBuf buf = (ByteBuf) msg;
            if (buf.readableBytes() <= 0) {
                return;
            }
            byte[] bs = new byte[buf.readableBytes()];
            buf.readBytes(bs);
            bs = DigitalUtils.meanTransfer(bs);
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
        // 1. 终端心跳-消息体为空 ==> 平台通用应答
        if (TPMSConsts.MSG_ID_TERMINAL_HEART_BEAT == header.getMsgId()) {
            logger.info(">>>>>[终端心跳],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            try {
                this.msgProcessService.processTerminalHeartBeatMsg(packageData);
            } catch (Exception e) {
                logger.error("<<<<<[终端心跳]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(),
                        e.getMessage());
                e.printStackTrace();
            }
        }
        // 2. 终端鉴权 ==> 平台通用应答
        else if (TPMSConsts.MSG_ID_TERMINAL_AUTHENTICATION == header.getMsgId()) {
            logger.info(">>>>>[终端鉴权],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            try {
                TerminalAuthenticationMsg authenticationMsg = this.decoder.toTerminalAuthenticationMsg(packageData);
                this.msgProcessService.processAuthMsg(authenticationMsg);
                logger.info("<<<<<[终端鉴权],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            } catch (Exception e) {
                logger.error("<<<<<[终端鉴权]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(),
                        e.getMessage());
                e.printStackTrace();
            }
        }
        // 3. 终端注册 ==> 终端注册应答
        else if (TPMSConsts.MSG_ID_TERMINAL_REGISTER == header.getMsgId()) {
            logger.info(">>>>>[终端注册],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            try {
                TerminalRegisterMsg msg = this.decoder.toTerminalRegisterMsg(packageData);
                this.msgProcessService.processRegisterMsg(msg);
                logger.info("<<<<<[终端注册],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            } catch (Exception e) {
                logger.error("<<<<<[终端注册]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(),
                        e.getMessage());
                e.printStackTrace();
            }
        }
        // 4. 终端注销(终端注销数据消息体为空) ==> 平台通用应答
        else if (TPMSConsts.MSG_ID_TERMINAL_LOG_OUT == header.getMsgId()) {
            logger.info(">>>>>[终端注销],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            try {
                this.msgProcessService.processTerminalLogoutMsg(packageData);
                logger.info("<<<<<[终端注销],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            } catch (Exception e) {
                logger.error("<<<<<[终端注销]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(),
                        e.getMessage());
                e.printStackTrace();
            }
        }
        // 5. 位置信息汇报 ==> 平台通用应答
        else if (TPMSConsts.MSG_ID_TERMINAL_LOCATION_INFO_UPLOAD == header.getMsgId()) {
            logger.info(">>>>>[位置信息],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            try {
                LocationInfoUploadMsg locationInfoUploadMsg = this.decoder.toLocationInfoUploadMsg(packageData);
                this.msgProcessService.processLocationInfoUploadMsg(locationInfoUploadMsg);
            } catch (Exception e) {
                logger.error("<<<<<[位置信息]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(),
                        e.getMessage());
                e.printStackTrace();
            }
        }
        // 6. 位置信息查询应答
        else if (TPMSConsts.QUERY_LOCATION_RESP == header.getMsgId()) {
            logger.info(">>>>>[位置信息查询应答],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            logger.info(">>>>>[位置信息查询应答],data={}", packageData);
            try {
                LocationInfoUploadMsg locationInfoUploadMsg = this.decoder.toLocationInfoUploadMsg(packageData);
                this.msgProcessService.processLocationInfoUploadMsg(locationInfoUploadMsg);
                logger.info("<<<<<[位置信息查询应答],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            } catch (Exception e) {
                logger.error("<<<<<[位置信息查询应答]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(),
                        e.getMessage());
                e.printStackTrace();
            }
        }
        // 7. 扩展计时培训消息
        else if (TPMSConsts.DATA_UP == header.getMsgId()) {
            //logger.info(">>>>>[数据上行],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            try {
                ExtendedTimekeepingTrainingMsg msg = this.decoder.toExtendedTimekeepingTrainingMsg(packageData);
                processExtendedMsg(msg);
                // logger.info("<<<<<[数据上行],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            } catch (Exception e) {
                logger.error("<<<<<[数据上行]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(),
                        e.getMessage());
                e.printStackTrace();
            }
        }
        // 8. 查询终端参数应答
        else if (TPMSConsts.MSG_ID_TERMINAL_PARAM_QUERY_RESP == header.getMsgId()) {
            logger.info(">>>>>[查询终端参数应答],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            QueryTerminalArgRespMsgBody msg = null;
            byte[] msgBodyByte = packageData.getMsgBodyBytes();
            byte[] tmp = new byte[msgBodyByte.length - 4];
            System.arraycopy(msgBodyByte, 4, tmp, 0, tmp.length);
            byte[] msg4 = new byte[4];
            System.arraycopy(msgBodyByte, 0, msg4, 0, msg4.length);
            try {
                if (header.isHasSubPackage()) {
                    loopBuffer.write(tmp);
                    if (header.getSubPackageSeq() == header.getTotalSubPackage()) {
                        msg = this.decoder.toQueryTerminalArgRespMsg(msg4, loopBuffer.read(loopBuffer.count()));
                        loopBuffer.remove(loopBuffer.count());
                        logger.info("查询终端参数应答:{}", msg.getTerminalArgList());
                    }
                } else {
                    msg = this.decoder.toQueryTerminalArgRespMsg(msg4, tmp);
                    logger.info("查询终端参数应答:{}", msg.getTerminalArgList());
                }
            } catch (Exception e) {
                logger.error("<<<<<[查询终端参数应答]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(),
                        e.getMessage());
                e.printStackTrace();
            }
        }
        // 其他情况
        else {
            logger.error(">>>>>>[未知消息类型],phone={},msgId={},package={}", header.getTerminalPhone(), header.getMsgId(),
                    packageData);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        logger.error("发生异常:{}", cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Session session = Session.buildSession(ctx.channel());
        sessionManager.put(session.getId(), session);
        logger.debug("终端连接:{}", session);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final String sessionId = ctx.channel().id().asLongText();
        Session session = sessionManager.findBySessionId(sessionId);
        this.sessionManager.removeBySessionId(sessionId);
        logger.debug("终端断开连接:{}", session);
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Session session = this.sessionManager.removeBySessionId(Session.buildId(ctx.channel()));
                logger.error("服务器主动断开连接:{}", session);
                ctx.close();
            }
        }
    }

    private void release(Object msg) {
        try {
            ReferenceCountUtil.release(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
