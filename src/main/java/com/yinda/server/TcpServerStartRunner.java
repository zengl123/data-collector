package com.yinda.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/31 15:21
 * @UpdateUser:
 * @UpdateDate:2019/5/31 15:21
 * @UpdateRemark:
 * @Version:
 */
@Slf4j
@Component
public class TcpServerStartRunner implements CommandLineRunner {

    private TcpServer server;

    @Value("${gps.tcp.port}")
    private String port;

    @Override
    public void run(String... args) {
        if (null == server) {
            server = new TcpServer(Integer.valueOf(port));
            //启动线程
            server.startServer();
            log.info("终端通讯服务端-服务启动执行");
        }
    }
}

