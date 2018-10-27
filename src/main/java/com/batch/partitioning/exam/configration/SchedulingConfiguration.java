package com.batch.partitioning.exam.configration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Configuration
@EnableScheduling
public class SchedulingConfiguration {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job movieJob;

	@Scheduled(initialDelay = 10000, fixedDelay = 100000000)
    public void executeMyJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
				.addString("randomKey", UUID.randomUUID().toString())
				.toJobParameters();
        JobExecution jobExecution = jobLauncher.run(movieJob, jobParameters);

        System.out.println(jobExecution.getStatus());
        System.out.println(jobExecution.getExitStatus());
    }
}
