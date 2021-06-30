package com.zeroxcc.content.newsletter;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.zeroxcc.content.newsletter.model.MJMLContent;
import com.zeroxcc.content.newsletter.processor.HTMLProcessor;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.UUID;

@Component
@Slf4j
public class HTMLContentJob extends JobExecutionListenerSupport {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("jpaTrx")
    PlatformTransactionManager jpaTransactionManager;

    @Autowired
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    @Bean
    @JobScope
    public ItemStreamReader<MJMLContent> reader() {
        log.info("--- Item stream reader invoked ---");

        String query = "SELECT new com.zeroxcc.content.newsletter.model.MJMLContent(id, content, status, createdDate, updatedDate, newsletterId) FROM MJMLContent WHERE status = 'posted'";

        JpaPagingItemReader<MJMLContent> jpaPagingItemReader = new JpaPagingItemReader<>();
        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactoryBean.getObject());
        jpaPagingItemReader.setQueryString(query);

        log.info("--- page size: "+jpaPagingItemReader.getPageSize());

        return jpaPagingItemReader;
    }

    @Bean
    @JobScope
    public HTMLContentWriter writer() {

        HTMLContentWriter htmlContentWriter = new HTMLContentWriter();

        return htmlContentWriter;
    }

    @Bean(name = "HTMLContentJob")
    public Job HTMLContentJob() {
        log.info("--- Newsletter job invoked ---");

        Step step = stepBuilderFactory.get("step-0")
                .<MJMLContent, Pair<UUID, String>>chunk(1)
                .reader(reader())
                .processor(new HTMLProcessor())
                .writer(writer())
                .transactionManager(jpaTransactionManager)
                .startLimit(100)
                .allowStartIfComplete(true)
                .build();

        Job job = jobBuilderFactory.get("HTMLContentJob")
                .listener(this)
                .start(step)
                .build();

        return job;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("BATCH JOB COMPLETED SUCCESSFULLY");
        }
    }

}