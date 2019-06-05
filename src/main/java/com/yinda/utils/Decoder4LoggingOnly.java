package com.yinda.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/30 17:18
 * @UpdateUser:
 * @UpdateDate:2019/5/30 17:18
 * @UpdateRemark:
 * @Version:
 */
@Slf4j
public class Decoder4LoggingOnly extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        String hex = buf2Str(in);
        log.info("ip={},hex = {}", ctx.channel().remoteAddress(), hex);
        ByteBuf buf = Unpooled.buffer();
        while (in.isReadable()) {
            buf.writeByte(in.readByte());
        }
        out.add(buf);
    }

    private String buf2Str(ByteBuf in) {
        byte[] dst = new byte[in.readableBytes()];
        in.getBytes(0, dst);
        return DigitUtil.bytesToHexString(dst);
    }
}
