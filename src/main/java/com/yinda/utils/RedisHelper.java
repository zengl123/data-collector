package com.yinda.utils;

import com.yinda.exception.ExceptionHelper;
import com.yinda.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 17:13
 * @UpdateUser:
 * @UpdateDate:2019/5/31 17:13
 * @UpdateRemark:
 * @Version:
 */
@Slf4j
public class RedisHelper {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public <T> boolean setT(String key, T t) {
        return setT(key, t, 0);
    }

    public <T> boolean setT(String key,T t, long expireTime) {
        return setT(key, t, expireTime, TimeUnit.SECONDS);
    }

    public boolean setString(String key, String value) {
        return setString(key, value, 0);
    }

    public boolean setString(String key, String value, long expireTime) {
        return setString(key, value, expireTime, TimeUnit.SECONDS);
    }

    public boolean setString(String key, String value, long expireTime, TimeUnit unit) {
        try {
            if (expireTime > 0) {
                stringRedisTemplate.expire(key, expireTime, unit);
            }
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("key:[{}]-value:[{}]-expire:[{}]-unit:-[{}] 数据缓存异常", key, value, expireTime, unit, ExceptionHelper.hand(e));
            return false;
        }
    }

    public <T> boolean setT(String key, T t, long expireTime, TimeUnit unit) {
        try {
            if (expireTime > 0 && null != unit) {
                redisTemplate.expire(key, expireTime, unit);
            }
            redisTemplate.opsForValue().set(key, t);
            return true;
        } catch (Exception e) {
            log.error("key:[{}]-value:[{}]-expire:[{}]-unit:-[{}] 数据缓存异常 [{}]", key, t, expireTime, unit, ExceptionHelper.hand(e));
            return false;
        }
    }

    public String getString(String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("获取缓存数据异常 key:[{}]", key, ExceptionHelper.hand(e));
            throw new MyException("Redis取值失败", e);
        }
    }

    public <T> T getT(String key) {
        try {
            return (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("获取缓存数据异常 key:[{}]", key, ExceptionHelper.hand(e));
            throw new MyException("Redis取值失败", e);
        }
    }
}
