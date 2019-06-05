package com.yinda.constant;

import java.nio.charset.Charset;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/30 16:59
 * @UpdateUser:
 * @UpdateDate:2019/5/30 16:59
 * @UpdateRemark:
 * @Version:
 */
public class Jt808Constant {
    public static final String STRING_ENCODING = "GBK";

    public static final Charset STRING_CHARSET = Charset.forName(STRING_ENCODING);
    /**
     * 平台登录
     */
    public static final int LOGIN = 0x01f0;
    /**
     * 平台登录应答
     */
    public static final int LOGIN_RESP = 0x81f0;
    /**
     * 平台登出
     */
    public static final int OUT = 0x01f1;
    /**
     * 标识位
     */
    public static final int PKG_DELIMITER = 0x7e;
    /**
     * 客户端发呆15分钟后,服务器主动断开连接
     */
    public static int TCP_CLIENT_IDLE_MINUTES = 1;
    /**
     * 终端通用应答
     */
    public static final int MSG_ID_TERMINAL_COMMON_RESP = 0x0001;
    /**
     * 终端心跳
     */
    public static final int MSG_ID_TERMINAL_HEART_BEAT = 0x0002;
    /**
     * 终端注册
     */
    public static final int MSG_ID_TERMINAL_REGISTER = 0x0100;
    /**
     * 终端注销
     */
    public static final int MSG_ID_TERMINAL_LOG_OUT = 0x0003;
    /**
     * 终端鉴权
     */
    public static final int MSG_ID_TERMINAL_AUTHENTICATION = 0x0102;
    /**
     * 位置信息汇报
     */
    public static final int MSG_ID_TERMINAL_LOCATION_INFO_UPLOAD = 0x0200;

    /**
     * 查询终端参数应答
     */
    public static final int MSG_ID_TERMINAL_PARAM_QUERY_RESP = 0x0104;
    /**
     * 平台通用应答
     */
    public static final int CMD_COMMON_RESP = 0x8001;
    /**
     * 终端注册应答
     */
    public static final int CMD_TERMINAL_REGISTER_RESP = 0x8100;
    /**
     * 设置终端参数
     */
    public static final int CMD_TERMINAL_PARAM_SETTINGS = 0X8103;
    /**
     * 查询终端参数
     */
    public static final int CMD_TERMINAL_PARAM_QUERY = 0x8104;
    /**
     * 终端控制
     */
    public static final int CMD_TERMINAL_CONTROL = 0X8105;
    /**
     * 位置信息查询
     */
    public static final int QUERY_LOCATION_MSG = 0X8201;
    /**
     * 位置信息查询应答
     */
    public static final int QUERY_LOCATION_RESP = 0X0201;

}
