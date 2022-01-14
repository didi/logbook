package com.didiglobal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.script.ScriptEngineManager;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liyanling
 * @date 2021/11/19 3:15 下午
 */
@Configuration
public class CommonBeanConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(36);
        executor.setQueueCapacity(50);
        executor.setKeepAliveSeconds(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                executor.shutdown();
            }
        }));

        return executor;
    }

    @Bean
    public ScriptEngineManager scriptEngineManager(){
        return new ScriptEngineManager();
    }

}
