package com.yinda.server;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 11:09
 * @UpdateUser:
 * @UpdateDate:2019/5/31 11:09
 * @UpdateRemark:
 * @Version:
 */
public class ChannelMap {
    public static int channelNum = 0;
    private static ConcurrentHashMap<String, Channel> channelHashMap = null;

    public static ConcurrentHashMap<String, Channel> getChannelHashMap() {
        return channelHashMap;
    }

    public static Channel getChannelByName(String name) {
        if (channelHashMap == null || channelHashMap.isEmpty()) {
            return null;
        }
        return channelHashMap.get(name);
    }

    public static void addChannel(String name, Channel channel) {
        if (channelHashMap == null) {
            channelHashMap = new ConcurrentHashMap<>(100);
        }
        channelHashMap.put(name, channel);
        channelNum++;
    }

    public static int removeChannelByName(String name) {
        if (channelHashMap.containsKey(name)) {
            channelHashMap.remove(name);
            return 0;
        } else {
            return 1;
        }
    }
}
