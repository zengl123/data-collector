package com.yinda.model.dto;

import com.yinda.constant.Jt808Constant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:终端鉴权消息
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 9:32
 * @UpdateUser:
 * @UpdateDate:2019/5/31 9:32
 * @UpdateRemark:
 * @Version:
 */
@Data
@NoArgsConstructor
public class TerminalAuthenticationMsg extends PackageData {
    private int timestamp;
    private String authCode;

    public TerminalAuthenticationMsg(PackageData packageData) {
        this();
        this.authCode = new String(packageData.getMsgBodyBytes(), Jt808Constant.STRING_CHARSET);
        this.msgHeader = packageData.getMsgHeader();
        this.msgBodyBytes = packageData.getMsgBodyBytes();
        this.checkSum = packageData.getCheckSum();
        this.channel = packageData.getChannel();
    }

    @Override
    public String toString() {
        return "TerminalAuthenticationMsg [authCode=" + authCode + ",timestamp=" + timestamp + ",msgHeader=" + msgHeader + ", msgBodyBytes="
                + Arrays.toString(msgBodyBytes) + ", checkSum=" + checkSum + ", channel=" + channel + "]";
    }

}