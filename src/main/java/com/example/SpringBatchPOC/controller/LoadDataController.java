package com.example.SpringBatchPOC.controller;

import com.example.SpringBatchPOC.dto.UserDTO;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoadDataController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job myJob;

    @GetMapping("/load")
    public BatchStatus load() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParameters(getJobParamsMap());
        JobExecution jobExecution = jobLauncher.run(myJob, jobParameters);
        return jobExecution.getStatus();
    }

    private Map<String, JobParameter> getJobParamsMap() {
        Map<String, JobParameter> jobsParam = new HashMap();
        jobsParam.put("time", new JobParameter(System.currentTimeMillis()));
        return jobsParam;
    }
}
