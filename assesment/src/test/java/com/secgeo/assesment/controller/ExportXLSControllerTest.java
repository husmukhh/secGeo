package com.secgeo.assesment.controller;

import com.secgeo.assesment.resp.JobMessage;
import com.secgeo.assesment.service.ExcelProcessingService;
import com.secgeo.assesment.service.JobManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ExportXLSController.class)
class ExportXLSControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExcelProcessingService mockExcelProcessingService;
    @MockBean
    private JobManager mockJobsService;

    @Test
    void testDownloadFile() throws Exception {
        // Setup
        // Run the test


        final MockHttpServletResponse response = mockMvc.perform(get("/export")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString() != null);
        verify(mockJobsService).putJob(eq(response.getContentAsString()), any(CompletableFuture.class));
        verify(mockExcelProcessingService).exportExcel(response.getContentAsString());
    }

    @Test
    void testExportStatus() throws Exception {
        // Setup
        // Configure JobManager.getJob(...).
        final CompletableFuture<? extends JobMessage> completableFuture = CompletableFuture.completedFuture(
                new JobMessage("DONE"));
        doReturn(completableFuture).when(mockJobsService).getJob("jobId");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/export/{jobId}", "jobId")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"status\":\"DONE\"}");
    }

    @Test
    void testExportStatus_JobManagerReturnsNull() throws Exception {
        // Setup
        doReturn(null).when(mockJobsService).getJob("jobId");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/export/{jobId}", "jobId")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.TOO_EARLY.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"status\":\"ERROR\"}");
    }

    @Test
    void testExportStatus_JobManagerReturnsFailure() throws Exception {
        // Setup
        // Configure JobManager.getJob(...).
        final CompletableFuture<JobMessage> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(new Exception("message"));
        doReturn(completableFuture).when(mockJobsService).getJob("jobId");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/export/{jobId}", "jobId")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"status\":\"ERROR\"}");
    }

    @Test
    void testExportFile() throws Exception {
        // Setup
        // Configure JobManager.getJob(...).
        final CompletableFuture<? extends JobMessage> completableFuture = CompletableFuture.completedFuture(
                new JobMessage("status"));
        doReturn(completableFuture).when(mockJobsService).getJob("jobId");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/export/{jobId}/file", "jobId")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.containsHeader("Content-Disposition"));
        assertThat(response.getContentType().equalsIgnoreCase("application/vnd.ms-excel"));
    }

    @Test
    void testExportFile_JobManagerReturnsNull() throws Exception {
        // Setup
        doReturn(null).when(mockJobsService).getJob("jobId");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/export/{jobId}/file", "jobId")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testExportFile_JobManagerReturnsFailure() throws Exception {
        // Setup
        // Configure JobManager.getJob(...).
        final CompletableFuture<JobMessage> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(new Exception("Error occured while exporting"));
        doReturn(completableFuture).when(mockJobsService).getJob("jobId");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/export/{jobId}/file", "jobId")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isNotEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }
}
