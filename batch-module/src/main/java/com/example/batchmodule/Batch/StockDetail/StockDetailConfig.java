package com.example.batchmodule.Batch.StockDetail;

import com.example.batchmodule.Domain.Stock;
import com.example.batchmodule.Domain.StockDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;


import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StockDetailConfig {
    private final JobRepository jobRepository;
    private final StockDetailReader stockDetailReader;
    private final StockDetailProcessor stockDetailProcessor;
    private final StockDetailUpdateProcessor stockDetailUpdateProcessor;
    private final StockDetailWriter stockDetailWriter;
    private final PlatformTransactionManager platformTransactionManager;
    private final StockReader stockReader;
    private final StockWriter stockWriter;


    @Bean
    public Job stockDetailJob() {
        log.info("stockDetailJob 시작!@@@@@@@");
        return new JobBuilder("stockDetailJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener())
                .start(stockUpdateStep())
                .next(stockDetailStep())
                .build();
    }
    @Bean
    public Step stockUpdateStep() {
        return new StepBuilder("stockDetailStep", jobRepository)
                .<List<Stock>, List<Stock>>chunk(10, platformTransactionManager)
                .reader(stockReader)
                .writer(stockWriter)
                .taskExecutor(taskExecutor())
                .build();
    }


    @Bean
    public Step stockDetailStep() {
        return new StepBuilder("stockDetailStep", jobRepository)
                .<Stock, List<StockDetail>>chunk(10, platformTransactionManager)
                .reader(stockDetailReader)
                .processor(stockDetailUpdateProcessor)
                //.processor(stockDetailProcessor)
                .writer(stockDetailWriter)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);

        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("stock-detail-");
        executor.initialize();
        return executor;
    }
    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                // 작업 시작 전 로그
                log.info("stockDetailJob 시작!@@@@@@@");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                // 작업 종료 후 로그
                //log.info("stockDetailJob 종료! 실행 상태: {}, 실행 시간 : {}", jobExecution.getStatus());
                log.info("종료 시간 :"+ String.valueOf(System.currentTimeMillis()));
            }
        };
    }

}

