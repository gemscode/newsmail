package com.zeroxcc.delivery.newsletter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class NewsletterApplication {

	public static void main(String[] args) {
		//SpringApplication.run(NewsletterApplication.class, args);

		SpringApplication app = new SpringApplication(NewsletterApplication.class);
		ConfigurableApplicationContext ctx = app.run(args);

		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
		Job job = ctx.getBean("NewsletterJob", Job.class);
		//JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
		JobParameters jobParameters = new JobParametersBuilder().addLong("time",System.currentTimeMillis()).toJobParameters();

		try {
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			BatchStatus batchStatus = jobExecution.getStatus();

			log.info("job status "+batchStatus);
		} catch (Exception e) {
			log.info("exception from main: "+e);
		}
	}

}
