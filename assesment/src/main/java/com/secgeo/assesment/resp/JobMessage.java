package com.secgeo.assesment.resp;

public class JobMessage implements java.io.Serializable{


    public JobMessage(String status){
        this.status = status;
    }
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
