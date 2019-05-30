package com.yinda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Copyright (C), 2018-${YEAR}, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author: ZENGLING
 * @钉钉: 17363645521
 * @CreateDate: 2019/5/30 14:41
 * @UpdateUser:
 * @UpdateDate: 2019/5/30 14:41
 * @UpdateRemark:
 * @Version:
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class DataCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataCollectorApplication.class, args);
    }

}
