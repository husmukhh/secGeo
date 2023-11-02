package com.secgeo.assesment.entities;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;




@Document
@NoArgsConstructor
public class Section {
    @Id
    private String name;
    private  List<GeologicalClass> geologicalClasses = new ArrayList<>();


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<GeologicalClass> getGeologicalClasses() {
        return geologicalClasses;
    }

}
