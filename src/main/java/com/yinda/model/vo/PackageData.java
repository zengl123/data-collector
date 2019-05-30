package com.yinda.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.netty.channel.Channel;

import java.util.Arrays;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/30 16:46
 * @UpdateUser:
 * @UpdateDate:2019/5/30 16:46
 * @UpdateRemark:
 * @Version:
 */
public class PackageData {
    /**
     * 16byte 消息头
     */
    protected MsgHeader msgHeader;
    @JSONField(serialize=false)
    protected byte[] msgBodyBytes;
    /**
     * 校验码 1byte
     */
    protected int checkSum;
    @JSONField(serialize=false)
    protected Channel channel;

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public byte[] getMsgBodyBytes() {
        return msgBodyBytes;
    }

    public void setMsgBodyBytes(byte[] msgBodyBytes) {
        this.msgBodyBytes = msgBodyBytes;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "PackageData [msgHeader=" + msgHeader + ", msgBodyBytes=" + Arrays.toString(msgBodyBytes) + ", checkSum="
                + checkSum + ", address=" + channel + "]";
    }

    public static class MsgHeader {
        protected int msgId;
        protected int msgBodyPropsField;
        protected int msgBodyLength;
        protected int encryptionType;
        protected boolean hasSubPackage;
        protected String reservedBit;
        protected String terminalPhone;
        protected int flowId;
        protected int packageInfoField;
        protected long totalSubPackage;
        protected long subPackageSeq;

        public int getMsgId() {
            return msgId;
        }

        public void setMsgId(int msgId) {
            this.msgId = msgId;
        }

        public int getMsgBodyLength() {
            return msgBodyLength;
        }

        public void setMsgBodyLength(int msgBodyLength) {
            this.msgBodyLength = msgBodyLength;
        }

        public int getEncryptionType() {
            return encryptionType;
        }

        public void setEncryptionType(int encryptionType) {
            this.encryptionType = encryptionType;
        }

        public String getTerminalPhone() {
            return terminalPhone;
        }

        public void setTerminalPhone(String terminalPhone) {
            this.terminalPhone = terminalPhone;
        }

        public int getFlowId() {
            return flowId;
        }

        public void setFlowId(int flowId) {
            this.flowId = flowId;
        }

        public boolean isHasSubPackage() {
            return hasSubPackage;
        }

        public void setHasSubPackage(boolean hasSubPackage) {
            this.hasSubPackage = hasSubPackage;
        }

        public String getReservedBit() {
            return reservedBit;
        }

        public void setReservedBit(String reservedBit) {
            this.reservedBit = reservedBit;
        }

        public long getTotalSubPackage() {
            return totalSubPackage;
        }

        public void setTotalSubPackage(long totalPackage) {
            this.totalSubPackage = totalPackage;
        }

        public long getSubPackageSeq() {
            return subPackageSeq;
        }

        public void setSubPackageSeq(long packageSeq) {
            this.subPackageSeq = packageSeq;
        }

        public int getMsgBodyPropsField() {
            return msgBodyPropsField;
        }

        public void setMsgBodyPropsField(int msgBodyPropsField) {
            this.msgBodyPropsField = msgBodyPropsField;
        }

        public void setPackageInfoField(int packageInfoField) {
            this.packageInfoField = packageInfoField;
        }

        public int getPackageInfoField() {
            return packageInfoField;
        }

        @Override
        public String toString() {
            return "MsgHeader [msgId=" + msgId + ", msgBodyPropsField=" + msgBodyPropsField + ", msgBodyLength="
                    + msgBodyLength + ", encryptionType=" + encryptionType + ", hasSubPackage=" + hasSubPackage
                    + ", reservedBit=" + reservedBit + ", terminalPhone=" + terminalPhone + ", flowId=" + flowId
                    + ", packageInfoField=" + packageInfoField + ", totalSubPackage=" + totalSubPackage
                    + ", subPackageSeq=" + subPackageSeq + "]";
        }
    }
}
