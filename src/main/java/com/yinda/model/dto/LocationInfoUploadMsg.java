package com.yinda.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 9:28
 * @UpdateUser:
 * @UpdateDate:2019/5/31 9:28
 * @UpdateRemark:
 * @Version:
 */
@Data
@NoArgsConstructor
public class LocationInfoUploadMsg extends PackageData {
    /**
     * byte[0-3] 告警信息
     */
    private int warningFlagField;
    /**
     * byte[4-7] 状态(DWORD(32))
     */
    private int statusField;
    /**
     * byte[8-11] 纬度(DWORD(32))
     */
    private float latitude;
    /**
     * byte[12-15] 经度(DWORD(32))
     */
    private float longitude;
    /**
     * byte[16-17] 高程(WORD(16)) 海拔高度，单位为米（ m）
     */
    private int elevation;
    /**
     * byte[18-19] 速度(WORD) 1/10km/h
     */
    private int speed;
    /**
     * byte[20-21] 方向(WORD) 0-359,正北为 0，顺时针
     */
    private int direction;
    /**
     * byte[22-x] 时间(BCD[6]) YY-MM-DD-hh-mm-ss
     * GMT+8 时间，本标准中之后涉及的时间均采用此时区
     */
    private Date time;

    public LocationInfoUploadMsg(PackageData packageData) {
        this();
        this.channel = packageData.getChannel();
        this.checkSum = packageData.getCheckSum();
        this.msgBodyBytes = packageData.getMsgBodyBytes();
        this.msgHeader = packageData.getMsgHeader();
    }
}