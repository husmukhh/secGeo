package com.secgeo.assesment.controller;

import com.secgeo.assesment.resp.JobMessage;
import com.secgeo.assesment.service.ExcelProcessingService;
import com.secgeo.assesment.service.JobManager;
import com.secgeo.assesment.util.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/import")
public class ImportXLSController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());


    private ExcelProcessingService excelProcessingService;
    private JobManager jobsService;

    public ImportXLSController(ExcelProcessingService excelProcessingService , JobManager jobsService){
        this.excelProcessingService = excelProcessingService;
        this.jobsService = jobsService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String jobId = UUID.randomUUID().toString();
        CompletableFuture<JobMessage> task = new CompletableFuture<>();
        jobsService.putJob(jobId,task);
        excelProcessingService.processExcel(file,jobId);
        return new ResponseEntity<>(jobId,
                HttpStatus.OK);
    }

    @GetMapping(path = "/{jobId}", produces = "application/json")

    public ResponseEntity<JobMessage> getJobStatus(@PathVariable(name = "jobId") String jobId)  {
        CompletableFuture<? extends JobMessage> task = jobsService.getJob(jobId);

        if(task == null)
            return new ResponseEntity<>(new JobMessage(JobStatus.ERROR.status()),
                    HttpStatus.TOO_EARLY);
        else {

                if(task.isDone())
                  return new ResponseEntity<>(new JobMessage(JobStatus.DONE.status()),
                        HttpStatus.OK);
                else
                    return new ResponseEntity<>(new JobMessage(JobStatus.IN_PROGRESS.status()),
                            HttpStatus.OK);
            }
        }


}
