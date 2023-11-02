package com.secgeo.assesment.util;

public enum JobStatus {

    DONE("DONE"), IN_PROGRESS("IN PROGRESS"),ERROR("ERROR");
    private String status;
    JobStatus(String status){
        this.status = status;
    }

    public String status(){
        return status;
    }

}
