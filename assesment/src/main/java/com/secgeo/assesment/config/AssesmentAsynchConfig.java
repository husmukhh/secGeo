package com.secgeo.assesment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AssesmentAsynchConfig  {

    @Bean(name = "asyncTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("Job Process-");
        executor.initialize();
        return executor;
    }


}
