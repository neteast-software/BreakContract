package com.neteast.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author lzp
 * @date 2024年01月10 16:44
 */

@SpringBootApplication
public class BreakContractApplication {

    public static void main(String[] args) {
        SpringApplication.run(BreakContractApplication.class,args);
        System.out.println("(♥◠‿◠)ﾉﾞ  港后铁路违约清单服务启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }

}
