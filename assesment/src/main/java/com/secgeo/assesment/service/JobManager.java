package com.secgeo.assesment.service;

import com.secgeo.assesment.resp.JobMessage;
import com.secgeo.assesment.util.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@Service
public class JobManager {
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final ConcurrentMap<String, CompletableFuture<? extends JobMessage>> jobStore;

	public JobManager() {
		jobStore = new ConcurrentHashMap<String, CompletableFuture<? extends JobMessage>>();
	}

	public void putJob(String jobId, CompletableFuture<? extends JobMessage> theJob) {
		jobStore.put(jobId, theJob);
	}

	public CompletableFuture<? extends JobMessage> getJob(String jobId){
		LOG.info("getJob : {}" , jobId);
		if(jobStore.get(jobId) != null && jobStore.get(jobId).isDone()){
			return CompletableFuture.completedFuture(new JobMessage(JobStatus.DONE.status()));
		}else if(jobStore.get(jobId) != null){
			return jobStore.get(jobId);
		}else{
			return CompletableFuture.completedFuture(new JobMessage(JobStatus.ERROR.status()));
		}
	}

	public void removeJob(String jobId) {
		jobStore.remove(jobId);
	}
}
