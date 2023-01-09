package com.example.SpringBatchPOC.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class MyJobListener extends JobExecutionListenerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(MyJobListener.class);

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
            LOG.info("Enviando email...");
            LOG.info("La ejecucion completa del job id = {} fue exitosa!", jobExecution.getId());
        }
        if(jobExecution.getStatus().equals(BatchStatus.FAILED)) {
            LOG.info("Enviando email...");
            LOG.info("La ejecucion completa del job id = {} fallo!%n", jobExecution.getId());
        }
    }
}
