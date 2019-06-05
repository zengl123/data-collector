package com.yinda.bean;

import com.yinda.utils.RedisHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 17:18
 * @UpdateUser:
 * @UpdateDate:2019/5/31 17:18
 * @UpdateRemark:
 * @Version:
 */
@Component
public class BeanManger {

    @Bean
    public RedisHelper createRedisBean() {
        return new RedisHelper();
    }
}
