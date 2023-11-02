package com.secgeo.assesment.controller;

import com.secgeo.assesment.resp.JobMessage;
import com.secgeo.assesment.service.ExcelProcessingService;
import com.secgeo.assesment.service.JobManager;
import com.secgeo.assesment.util.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/export")
public class ExportXLSController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Value("${download.dir}")
    private String downloadDir;
    private ExcelProcessingService excelProcessingService;
    private JobManager jobsService;

    public ExportXLSController(ExcelProcessingService excelProcessingService , JobManager jobsService){
        this.excelProcessingService = excelProcessingService;
        this.jobsService = jobsService;
    }
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> downloadFile(HttpServletResponse response)  {
        String jobId = UUID.randomUUID().toString();
        CompletableFuture<JobMessage> task = new CompletableFuture<>();
        jobsService.putJob(jobId,task);
        excelProcessingService.exportExcel(jobId);
        return  new ResponseEntity<>(jobId,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{jobId}" , method = RequestMethod.GET,  produces = "application/json")
    public ResponseEntity<JobMessage> exportStatus(@PathVariable(name = "jobId") String jobId){
        CompletableFuture<? extends JobMessage> task = jobsService.getJob(jobId);
        if(task == null)
            return new ResponseEntity<>(new JobMessage(JobStatus.ERROR.status()),
                     HttpStatus.TOO_EARLY);
        else {
            try {
                return new ResponseEntity<>(task.get(),
                        HttpStatus.OK);
            } catch (InterruptedException  |  ExecutionException  e) {
                LOG.error("Error occured in fetching status of Job : {}" , e);
                return new ResponseEntity<>(new JobMessage(JobStatus.ERROR.status()),
                        HttpStatus.INTERNAL_SERVER_ERROR);

            }

        }
    }

    @RequestMapping(value = "/{jobId}/file", method = RequestMethod.GET,produces = "application/octet-stream")
    public ResponseEntity<Resource> exportFile(@PathVariable(name = "jobId") String jobId)  {
        CompletableFuture<? extends JobMessage> task = jobsService.getJob(jobId);

        if(task != null && task.isDone()) {
            String fileName = jobId + ".xls";
            InputStreamResource file = null;
            try {
                file = new InputStreamResource(new FileInputStream(downloadDir + fileName));
            } catch (FileNotFoundException e) {
               LOG.error("File not found for given job :{}",jobId);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(file);
        }else{
            return new ResponseEntity<>(HttpStatus.PROCESSING);
        }

    }

}
