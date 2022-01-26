package com.sahikran.config;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

@Configuration
public class SpringAsyncEventConfig {
    
    private static final Logger log = LoggerFactory.getLogger(SpringAsyncEventConfig.class);
    
    @Bean(name = "appEventMultiCaster")
    public ApplicationEventMulticaster simpleAppEventMultiCaster(@Qualifier("asyncExecutor") final Executor executor){
        final SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = 
            new SimpleApplicationEventMulticaster();
        simpleApplicationEventMulticaster.setTaskExecutor(executor);
        log.info("simple app event multiple caster object created");
        return simpleApplicationEventMulticaster;
    }
}
