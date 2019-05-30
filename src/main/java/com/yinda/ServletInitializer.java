package com.yinda;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
/**
 *Copyright (C), 2018-${YEAR}, 湖南金峰信息科技有限公司
 * @Description:
 * @Author: ZENGLING
 * @钉钉: 17363645521
 * @CreateDate: 2019/5/30 14:41
 * @UpdateUser:
 * @UpdateDate: 2019/5/30 14:41
 * @UpdateRemark:
 * @Version:
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DataCollectorApplication.class);
    }

}
