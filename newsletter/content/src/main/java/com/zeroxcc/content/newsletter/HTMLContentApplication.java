package com.zeroxcc.content.newsletter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class HTMLContentApplication {

	public static void main(String[] args) {
		//SpringApplication.run(HTMLContentApplication.class, args);

		SpringApplication app = new SpringApplication(HTMLContentApplication.class);
		ConfigurableApplicationContext ctx = app.run(args);

		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
		Job job = ctx.getBean("HTMLContentJob", Job.class);
		JobParameters jobParameters = new JobParametersBuilder().toJobParameters();

		try {
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			BatchStatus batchStatus = jobExecution.getStatus();

			log.info("job status "+batchStatus);
		} catch (Exception e) {
			log.info("exception from main: "+e);
		}
	}

}
