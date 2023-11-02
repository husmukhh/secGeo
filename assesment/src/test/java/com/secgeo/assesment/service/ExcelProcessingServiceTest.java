package com.secgeo.assesment.service;

import com.secgeo.assesment.entities.GeologicalClass;
import com.secgeo.assesment.entities.Section;
import com.secgeo.assesment.repo.SectionRepository;
import com.secgeo.assesment.resp.JobMessage;
import com.secgeo.assesment.util.ExcelReadWriteManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcelProcessingServiceTest {

    @Mock
    private JobManager mockJobsService;
    @Mock
    private ExcelReadWriteManager mockExcelReader;
    @Mock
    private SectionRepository mockSectionRepository;

    @InjectMocks
    private ExcelProcessingService excelProcessingServiceUnderTest;

    @BeforeEach
    void setUp() {
        //ReflectionTestUtils.setField(excelProcessingServiceUnderTest, "uploadDir", "uploadDir");
        //ReflectionTestUtils.setField(excelProcessingServiceUnderTest, "downloadDir", "downloadDir");
        MockitoAnnotations.initMocks(this);
        //MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessExcel() {
        // Setup
   /*     final MultipartFile file = new MockMultipartFile("temp.xls", "content".getBytes());
        UUID uuid = UUID.randomUUID();
        // Configure JobManager.getJob(...).
        final CompletableFuture<? extends JobMessage> completableFuture = CompletableFuture.completedFuture(
                new JobMessage("DONE"));
        doReturn(completableFuture).when(mockJobsService).getJob(uuid.toString());

        // Configure ExcelReadWriteManager.readXLS(...).
        final Section section = new Section();
        section.setName("Section1");
        final GeologicalClass geologicalClass = new GeologicalClass();
        geologicalClass.setName("Class1");
        geologicalClass.setCode("C1");
        section.getGeologicalClasses().addAll(Arrays.asList(geologicalClass));
        final List<Section> sections = Arrays.asList(section);
        when(mockExcelReader.readXLS("filePath")).thenReturn(sections);

        // Run the test
        excelProcessingServiceUnderTest.processExcel(file, uuid.toString());
        when(mockSectionRepository.saveAll(sections)).thenReturn(sections);
        // Verify the results
        // Confirm SectionRepository.saveAll(...).
        final Section section1 = new Section();
        section1.setName("Section1");
        final GeologicalClass geologicalClass1 = new GeologicalClass();
        geologicalClass1.setName("Class1");
        geologicalClass1.setCode("C1");
        section1.getGeologicalClasses().addAll(Arrays.asList(geologicalClass1));
        final List<Section> iterable = Arrays.asList(section1);
        verify(mockSectionRepository).saveAll(iterable);*/
    }

    @Test
    void testProcessExcel_JobManagerReturnsNull() {
        // Setup
/*        final MultipartFile file = new MockMultipartFile("name", "content".getBytes());
        doReturn(null).when(mockJobsService).getJob("jobId");

        // Configure ExcelReadWriteManager.readXLS(...).
        final Section section = new Section();
        section.setName("name");
        final GeologicalClass geologicalClass = new GeologicalClass();
        geologicalClass.setName("name");
        geologicalClass.setCode("code");
        section.getGeologicalClasses().addAll(Arrays.asList(geologicalClass));
        final List<Section> sections = Arrays.asList(section);
        when(mockExcelReader.readXLS("filePath")).thenReturn(sections);

        // Run the test
        excelProcessingServiceUnderTest.processExcel(file, "jobId");

        // Verify the results
        // Confirm SectionRepository.saveAll(...).
        final Section section1 = new Section();
        section1.setName("name");
        final GeologicalClass geologicalClass1 = new GeologicalClass();
        geologicalClass1.setName("name");
        geologicalClass1.setCode("code");
        section1.getGeologicalClasses().addAll(Arrays.asList(geologicalClass1));
        final List<Section> iterable = Arrays.asList(section1);
        verify(mockSectionRepository).saveAll(iterable);*/
    }

    @Test
    void testProcessExcel_JobManagerReturnsFailure() {
        // Setup
 /*       final MultipartFile file = new MockMultipartFile("name", "content".getBytes());

        // Configure JobManager.getJob(...).
        final CompletableFuture<JobMessage> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(new Exception("message"));
        doReturn(completableFuture).when(mockJobsService).getJob("jobId");

        // Configure ExcelReadWriteManager.readXLS(...).
        final Section section = new Section();
        section.setName("name");
        final GeologicalClass geologicalClass = new GeologicalClass();
        geologicalClass.setName("name");
        geologicalClass.setCode("code");
        section.getGeologicalClasses().addAll(Arrays.asList(geologicalClass));
        final List<Section> sections = Arrays.asList(section);
        when(mockExcelReader.readXLS("filePath")).thenReturn(sections);

        // Run the test
        excelProcessingServiceUnderTest.processExcel(file, "jobId");

        // Verify the results
        // Confirm SectionRepository.saveAll(...).
        final Section section1 = new Section();
        section1.setName("name");
        final GeologicalClass geologicalClass1 = new GeologicalClass();
        geologicalClass1.setName("name");
        geologicalClass1.setCode("code");
        section1.getGeologicalClasses().addAll(Arrays.asList(geologicalClass1));
        final List<Section> iterable = Arrays.asList(section1);
        verify(mockSectionRepository).saveAll(iterable);*/
    }

    @Test
    void testProcessExcel_ExcelReadWriteManagerReturnsNoItems() {
        // Setup
  /*      final MultipartFile file = new MockMultipartFile("name", "content".getBytes());

        // Configure JobManager.getJob(...).
        final CompletableFuture<? extends JobMessage> completableFuture = CompletableFuture.completedFuture(
                new JobMessage("status"));
        doReturn(completableFuture).when(mockJobsService).getJob("jobId");

        when(mockExcelReader.readXLS("filePath")).thenReturn(Collections.emptyList());

        // Run the test
        excelProcessingServiceUnderTest.processExcel(file, "jobId");

        // Verify the results
        // Confirm SectionRepository.saveAll(...).
        final Section section = new Section();
        section.setName("name");
        final GeologicalClass geologicalClass = new GeologicalClass();
        geologicalClass.setName("name");
        geologicalClass.setCode("code");
        section.getGeologicalClasses().addAll(Arrays.asList(geologicalClass));
        final List<Section> iterable = Arrays.asList(section);
        verify(mockSectionRepository).saveAll(iterable);*/
    }

    @Test
    void testGetJobStatus() {
   /*     // Setup
        // Configure JobManager.getJob(...).
        final CompletableFuture<? extends JobMessage> completableFuture = CompletableFuture.completedFuture(
                new JobMessage("status"));
        doReturn(completableFuture).when(mockJobsService).getJob("jobId");

        // Run the test
        final CompletableFuture<? extends JobMessage> result = excelProcessingServiceUnderTest.getJobStatus("jobId");*/

        // Verify the results
    }

    @Test
    void testGetJobStatus_JobManagerReturnsFailure() {
        // Setup
    /*    // Configure JobManager.getJob(...).
        final CompletableFuture<JobMessage> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(new Exception("message"));
        doReturn(completableFuture).when(mockJobsService).getJob("jobId");

        // Run the test
        final CompletableFuture<? extends JobMessage> result = excelProcessingServiceUnderTest.getJobStatus("jobId");
*/
        // Verify the results
    }

    @Test
    void testExportExcel() {
        // Setup
        // Configure JobManager.getJob(...).
  /*      final CompletableFuture<? extends JobMessage> completableFuture = CompletableFuture.completedFuture(
                new JobMessage("status"));
        doReturn(completableFuture).when(mockJobsService).getJob("jobId");

        // Configure SectionRepository.findAll(...).
        final Section section = new Section();
        section.setName("name");
        final GeologicalClass geologicalClass = new GeologicalClass();
        geologicalClass.setName("name");
        geologicalClass.setCode("code");
        section.getGeologicalClasses().addAll(Arrays.asList(geologicalClass));
        final List<Section> sections = Arrays.asList(section);
        when(mockSectionRepository.findAll()).thenReturn(sections);

        // Run the test
        excelProcessingServiceUnderTest.exportExcel("jobId");*/

        // Verify the results
    }

    @Test
    void testExportExcel_JobManagerReturnsFailure() {
        // Setup
        // Configure JobManager.getJob(...).
 /*       final CompletableFuture<JobMessage> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(new Exception("message"));
        doReturn(completableFuture).when(mockJobsService).getJob("jobId");

        // Configure SectionRepository.findAll(...).
        final Section section = new Section();
        section.setName("name");
        final GeologicalClass geologicalClass = new GeologicalClass();
        geologicalClass.setName("name");
        geologicalClass.setCode("code");
        section.getGeologicalClasses().addAll(Arrays.asList(geologicalClass));
        final List<Section> sections = Arrays.asList(section);
        when(mockSectionRepository.findAll()).thenReturn(sections);

        // Run the test
        excelProcessingServiceUnderTest.exportExcel("jobId");
*/
        // Verify the results
    }

    @Test
    void testExportExcel_SectionRepositoryReturnsNoItems() {
        // Setup
        // Configure JobManager.getJob(...).
 /*       final CompletableFuture<? extends JobMessage> completableFuture = CompletableFuture.completedFuture(
                new JobMessage("status"));
        doReturn(completableFuture).when(mockJobsService).getJob("jobId");

        when(mockSectionRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        excelProcessingServiceUnderTest.exportExcel("jobId");*/

        // Verify the results
    }
}
