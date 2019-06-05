package com.yinda.utils;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/30 17:15
 * @UpdateUser:
 * @UpdateDate:2019/5/30 17:15
 * @UpdateRemark:
 * @Version:
 */
public class DigitUtil {
    /**
     * 16进制中的字符集
     */
    private static final String HEX_CHAR = "0123456789ABCDEF";

    /**
     * @param src
     * @param start
     * @param end
     * @return byte[]
     * @Description: 截取byte数组，含头含尾
     */
    public static byte[] sliceBytes(byte[] src, Integer start, Integer end) {
        byte[] target = new byte[end - start + 1];
        for (int i = start; i <= end; i++) {
            target[i - start] = src[i];
        }
        return target;
    }

    /**
     * @param b
     * @return String
     * @Description: 将1byte转为2进制字符串
     */
    public static String byteToBinaryStr(byte b) {
        return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    public static int byte2ToInt(byte[] src) {
        int targets = (src[1] & 0xff) | ((src[0] << 8) & 0xff00);
        return targets;
    }

    public static int byte4ToInt(byte[] src) {
        return (src[0] & 0xff) << 24 | (src[1] & 0xff) << 16 | (src[2] & 0xff) << 8 | src[3] & 0xff;
    }

    public static long byte4ToInt(byte[] bs, int pos) {
        int byte1;
        int byte2;
        int byte3;
        int byte4;
        int index = pos;
        byte1 = (0x000000FF & ((int) bs[index]));
        byte2 = (0x000000FF & ((int) bs[index + 1]));
        byte3 = (0x000000FF & ((int) bs[index + 2]));
        byte4 = (0x000000FF & ((int) bs[index + 3]));
        return ((long) (byte1 << 24 | byte2 << 16 | byte3 << 8 | byte4)) & 0xFFFFFFFFL;
    }

    public static String byteToBinaryStr(byte src, Integer high, Integer low) {
        String str = "";
        for (int i = high; i >= low; i--) {
            str += (byte) ((src >> i) & 0x1);
        }
        return str;
    }

    public static byte binaryStrToByte(String str) {
        byte bt = 0;
        for (int i = str.length() - 1, j = 0; i >= 0; i--, j++) {
            bt += (Byte.parseByte(str.charAt(i) + "") * Math.pow(2, j));
        }
        return bt;
    }

    public static byte[] shortTo2Byte(short s) {
        byte[] target = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (target.length - 1 - i) * 8;
            target[i] = (byte) ((s >>> offset) & 0xff);
        }
        return target;
    }

    public static byte[] intTo4Byte(int src) {
        byte[] targets = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((src >>> offset) & 0xff);
        }
        return targets;
    }

    public static byte[] int32To4Byte(int src) {
        byte[] targets = new byte[4];
        // 最低位
        targets[0] = (byte) (src & 0xff);
        //次低位
        targets[1] = (byte) ((src >> 8) & 0xff);
        // 次高位
        targets[2] = (byte) ((src >> 16) & 0xff);
        //最高位,无符号右移
        targets[3] = (byte) (src >>> 24);
        return targets;
    }

    public static String bcdToStr(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append((byte) ((bytes[i] & 0xf0) >>> 4));
            sb.append((byte) (bytes[i] & 0x0f));
        }
        return "0".equalsIgnoreCase(sb.toString().substring(0, 1)) ? sb.toString().substring(1) : sb.toString();
    }

    public static String bcdToStr(byte bytes) {
        StringBuffer sb = new StringBuffer(2);
        sb.append((byte) ((bytes & 0xf0) >>> 4));
        sb.append((byte) (bytes & 0x0f));
        return "0".equalsIgnoreCase(sb.toString().substring(0, 1)) ? sb.toString().substring(1) : sb.toString();
    }

    public static byte[] strToBcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte[] abt;
        if (len >= 2) {
            len = len / 2;
        }
        byte[] bbt = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    public static int get808PackCheckCode(byte[] bs) {
        int checkCode = 0;
        if (bs.length < 3) {
            checkCode = 0;
        } else {
            for (int i = 1; i < bs.length - 2; i++) {
                checkCode = checkCode ^ (int) bs[i];
            }
        }
        return checkCode;
    }

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {
        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }
    }

    /**
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) HEX_CHAR.indexOf(c);
    }
}
