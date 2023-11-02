package com.secgeo.assesment.service;

import com.secgeo.assesment.entities.GeologicalClass;
import com.secgeo.assesment.resp.JobMessage;
import com.secgeo.assesment.util.ExcelReadWriteManager;
import com.secgeo.assesment.entities.Section;
import com.secgeo.assesment.repo.SectionRepository;
import com.secgeo.assesment.util.JobStatus;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.Date;

@Service
public class ExcelProcessingService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private JobManager jobsService;
    @Autowired
    private ExcelReadWriteManager excelReader;
    @Autowired
    private SectionRepository sectionRepository;
    @Value("${excel.dir}")
    private String uploadDir;

    @Value("${download.dir}")
    private String downloadDir;

    @Async("asyncTaskExecutor")
    public void processExcel(MultipartFile file, String jobId) {
        LOG.info("processExcel method with configured executor - {}", Thread.currentThread().getName());

        Path copyLocation = null;
        CompletableFuture task = jobsService.getJob(jobId);
        try {
            if( task != null){
                String filePath = uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename());
                copyLocation = Paths.get(filePath);
                Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                List<Section> data = excelReader.readXLS(filePath);
                sectionRepository.saveAll(data);
                Thread.sleep(30000L);
                task.completedFuture(JobStatus.DONE.status());
                task.complete(JobStatus.DONE.status());
            }
        } catch (Exception e) {
            task.completedFuture(JobStatus.ERROR.status());
            task.completeExceptionally(e);
            LOG.error("Error occured during file upload : {}" , e);
        }finally {
            //TODO delete file..................
        }
    }


    public CompletableFuture<? extends JobMessage> getJobStatus(String jobId){
        LOG.info("getJobStatus method  {}", Thread.currentThread().getName());
        return jobsService.getJob(jobId);
    }
    @Async("asyncTaskExecutor")
    public void exportExcel(String jobId) {
        CompletableFuture task = jobsService.getJob(jobId);
        List<Section> allData = sectionRepository.findAll();
        String headers [] = getHeaders(allData);
      //  response.setHeader("Content-Disposition", "attachment; filename=\"Sections.xls\"");
        try(Workbook workbook = new HSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Sections");
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.MEDIUM);
            cellStyle.setBorderRight(BorderStyle.MEDIUM);
            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);

            Row header = sheet.createRow(0);
            for(int i = 0 ; i < headers.length ; i++){
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(cellStyle);
            }
            int rowCount = 1;
            for(Section section: allData){
                Row sectionRow = sheet.createRow(rowCount++);
                Cell cell = sectionRow.createCell(0);
                cell.setCellValue(section.getName());
                cell.setCellStyle(cellStyle);
                int colCount = 1;
                for(GeologicalClass geoClass : section.getGeologicalClasses()){
                    Cell cellName = sectionRow.createCell(colCount++);
                    cellName.setCellValue(geoClass.getName());
                    cellName.setCellStyle(cellStyle);
                    Cell cellCode = sectionRow.createCell(colCount++);
                    cellCode.setCellValue(geoClass.getCode());
                    cellCode.setCellStyle(cellStyle);
                }
            }
            task.completedFuture(JobStatus.DONE.status());
            task.complete(JobStatus.DONE.status());

            FileOutputStream outputStream = new FileOutputStream(downloadDir+jobId+".xls");
            workbook.write(outputStream);
        } catch (EncryptedDocumentException | IOException e) {
            LOG.error("Error occured in creating workboo object {}" , e);
            task.completeExceptionally(e);
        }

    }

    private  String[] getHeaders(List<Section> allData) {
        Optional<Section> max = allData.stream().reduce((tempSection, section) ->
        {
            Section section1 = tempSection.getGeologicalClasses().size() > section.getGeologicalClasses().size() ? tempSection : section;
            return section1;
        });

        String[] headers = new String[max.get().getGeologicalClasses().size()];
        headers[0] = "Section name";
        String classNamePrefix = "Class ";
        String classNameSuffix = " name";
        for(int i = 1  ; i < headers.length; i++){
            StringBuilder stringBuilder = new StringBuilder(classNamePrefix);
            stringBuilder.append(i).append(classNameSuffix);
            headers[i] = stringBuilder.toString();
        }
        return headers;
    }


}
