package com.yinda.utils;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 10:00
 * @UpdateUser:
 * @UpdateDate:2019/5/31 10:00
 * @UpdateRemark:
 * @Version:
 */
public class DateTimeUtil {
    public static final String FORMAT_STANDARD_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式化
     *
     * @return
     */
    public static DateTimeFormatter dtf(String pattern) {
        return DateTimeFormat.forPattern(pattern);
    }

    /**
     * 格式化字符串日期转Date(时间格式保持一致)
     *
     * @param time    2019-01-09 17:19
     * @param pattern yyyy-MM-dd HH:mm
     * @return
     */
    public static Date stringToDate(String time, String pattern) {
        return stringToLocalDateTime(time, pattern).toDate();
    }

    /**
     * date转String
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(pattern);
    }

    /**
     * 格式化字符串日期转LocalDateTime(时间格式保持一致)
     *
     * @param time    2019-01-09 17:19
     * @param pattern yyyy-MM-dd HH:mm
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String time, String pattern) {
        return LocalDateTime.parse(time, dtf(pattern));
    }
}
