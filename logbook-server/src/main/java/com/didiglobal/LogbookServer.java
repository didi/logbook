package com.didiglobal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author liyanling
 * @date 2021/10/26
 */
@SpringBootApplication
@EnableAutoConfiguration
@MapperScan("com.didiglobal.mybatis")
public class LogbookServer
{
    public static void main( String[] args )
    {
        try {
            ConfigurableApplicationContext context = SpringApplication.run(LogbookServer.class, args);
        } catch (Throwable t) {
            System.out.println("caught error when start LogbookServer");
            t.printStackTrace();
            System.exit(1);
        }

    }
}
