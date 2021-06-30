package com.zeroxcc.delivery.newsletter;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.zeroxcc.delivery.newsletter.model.MJMLContent;
import com.zeroxcc.delivery.newsletter.model.ScheduleQueue;
import com.zeroxcc.delivery.newsletter.processor.MJMLProcessor;
import com.zeroxcc.delivery.newsletter.processor.MailProcessor;
import com.zeroxcc.delivery.newsletter.processor.HTMLProcessor;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import java.util.AbstractMap;
import java.util.UUID;

@Component
@Slf4j
public class NewsletterJob extends JobExecutionListenerSupport {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("jpaTrx")
    PlatformTransactionManager jpaTransactionManager;

    @Autowired
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    @Autowired
    JavaMailSender mailSender;

    private HazelcastInstance hzInstance;

    @PostConstruct
    public void init() {
        hzInstance = Hazelcast.newHazelcastInstance();
    }

    @Bean
    @JobScope
    public ItemStreamReader<ScheduleQueue> reader() {
        log.info("--- Item stream reader invoked ---");

        String query = "Select new com.zeroxcc.delivery.newsletter.model.ScheduleQueue(toDoId, contentUrl, email, scheduleDate) from ScheduleQueue";

        JpaPagingItemReader<ScheduleQueue> jpaPagingItemReader = new JpaPagingItemReader<>();
        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactoryBean.getObject());
        jpaPagingItemReader.setQueryString(query);

        log.info("--- page size: "+jpaPagingItemReader.getPageSize());

        return jpaPagingItemReader;
    }

    @Bean
    @JobScope
    public ItemStreamReader<MJMLContent> mjmlContentReader() {
        log.info("--- Item stream reader invoked ---");

        String query = "SELECT new com.zeroxcc.delivery.newsletter.model.MJMLContent(id, content, status, createdDate, updatedDate, newsletterId) FROM MJMLContent WHERE status = 'posted'";

        JpaPagingItemReader<MJMLContent> jpaPagingItemReader = new JpaPagingItemReader<>();
        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactoryBean.getObject());
        jpaPagingItemReader.setQueryString(query);

        log.info("--- page size: "+jpaPagingItemReader.getPageSize());

        return jpaPagingItemReader;
    }

    @Bean
    @JobScope
    public MailWriter writer() {
        MailWriter mailWriter = new MailWriter();
        mailWriter.setMailSender(mailSender);

        return mailWriter;
    }

    @Bean
    @JobScope
    public HTMLContentWriter htmlContentWriter() {

        return new HTMLContentWriter();
    }

    @Bean
    @JobScope
    public MJMLContentWriter mjmlContentWriter() {

        return new MJMLContentWriter();
    }

    @Bean(name = "NewsletterJob")
    public Job NewsletterJob() {
        log.info("--- Newsletter job invoked ---");

        Step step1 = stepBuilderFactory.get("step-1")
                .<ScheduleQueue, SimpleMailMessage> chunk(1)
                .reader(reader())
                .processor(new MailProcessor())
                .writer(writer())
                .transactionManager(jpaTransactionManager)
                .startLimit(100)
                .allowStartIfComplete(true)
                .build();

        Step step2 = stepBuilderFactory.get("step-2")
                .<MJMLContent, AbstractMap.SimpleEntry<UUID, String>> chunk(1)
                .reader(mjmlContentReader())
                .processor(new MJMLProcessor())
                .writer(mjmlContentWriter())
                .transactionManager(jpaTransactionManager)
                .startLimit(100)
                .allowStartIfComplete(true)
                .build();

        Step step3 = stepBuilderFactory.get("step-3")
                .<MJMLContent, AbstractMap.SimpleEntry<UUID, String>> chunk(1)
                .reader(mjmlContentReader())
                .processor(new HTMLProcessor())
                .writer(htmlContentWriter())
                .transactionManager(jpaTransactionManager)
                .startLimit(100)
                .allowStartIfComplete(true)
                .build();

        Job job = jobBuilderFactory.get("NewsletterJob")
                .listener(this)
                .start(step1)
                .next(step2)
                .next(step3)
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