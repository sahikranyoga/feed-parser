package com.sahikran.config;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {
    
    private static final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("async-exec");
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.afterPropertiesSet();
        threadPoolTaskExecutor.initialize();
        log.info("async executor intialized");
        return threadPoolTaskExecutor;
    } 
}
