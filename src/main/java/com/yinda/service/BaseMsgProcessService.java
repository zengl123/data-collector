package com.yinda.service;

import com.yinda.model.dto.Session;
import com.yinda.server.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 10:12
 * @UpdateUser:
 * @UpdateDate:2019/5/31 10:12
 * @UpdateRemark:
 * @Version:
 */
@Slf4j
public class BaseMsgProcessService {
    protected SessionManager sessionManager;

    public BaseMsgProcessService() {
        this.sessionManager = SessionManager.getInstance();
    }

    protected ByteBuf getByteBuf(byte[] arr) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(arr.length);
        byteBuf.writeBytes(arr);
        return byteBuf;
    }

    public void send2Client(Channel channel, byte[] arr) throws InterruptedException {
        ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(arr)).sync();
        if (!future.isSuccess()) {
            log.error("发送数据出错:{}", future.cause());
        }
    }

    protected int getFlowId(Channel channel, int defaultValue) {
        Session session = this.sessionManager.findBySessionId(Session.buildId(channel));
        if (session == null) {
            return defaultValue;
        }
        return session.currentFlowId();
    }

    protected int getFlowId(Channel channel) {
        return this.getFlowId(channel, 0);
    }
}
