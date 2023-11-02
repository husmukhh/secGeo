package com.secgeo.assesment.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.secgeo.assesment.entities.GeologicalClass;
import com.secgeo.assesment.entities.Section;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelReadWriteManager {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public List<Section> readXLS(String filePath){
        DataFormatter dataFormatter = new DataFormatter();
        ObjectMapper mapper = new ObjectMapper();
        // hold the excel data sheet wise
        ObjectNode excelData = mapper.createObjectNode();
        FileInputStream fis = null;
        Workbook workbook = null;
        List<Section> sheetData = null;
        try {
            File file = new File(filePath);
            String filename = file.getName().toLowerCase();
            if(filename.endsWith(".xls")){
                fis = new FileInputStream(file);
                workbook = new HSSFWorkbook(fis);

                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    String sheetName = sheet.getSheetName();

                    List<String> headers = new ArrayList<String>();
                    sheetData = new ArrayList<>(sheet.getLastRowNum() - 1);
                    // Reading each row of the sheet
                    for (int j = 0; j <= sheet.getLastRowNum(); j++) {
                        Row row = sheet.getRow(j);
                        if (j == 0) {
                            // reading sheet header's name
                            for (int k = 0; k < row.getLastCellNum(); k++) {
                                headers.add(row.getCell(k).getStringCellValue());
                            }
                        } else {
                            // reading work sheet data
                            Section section = new Section();
                                for (int k = 0; k < headers.size(); k++) {
                                    Cell cell = row.getCell(k);
                                    if (cell != null) {
                                        if( k == 0){
                                            section.setName(cell.getStringCellValue());
                                            sheetData.add(section);
                                        }else{
                                            GeologicalClass geologicalClass = new GeologicalClass();
                                            LOG.info(" Cell Value : {}", cell.getStringCellValue() );
                                            geologicalClass.setName(cell.getStringCellValue());
                                            if(k+1 < headers.size()){
                                                cell = row.getCell(k+1);
                                                if (cell != null) {
                                                    geologicalClass.setCode(cell.getStringCellValue());
                                                    k = k+1;
                                                }
                                            }
                                            section.getGeologicalClasses().add(geologicalClass);
                                        }
                                    } else {
                                        LOG.info("Cell value is null , adding empty geoclass for this");
                                        GeologicalClass geologicalClass = new GeologicalClass();
                                        geologicalClass.setCode("");
                                        geologicalClass.setName("");
                                        section.getGeologicalClasses().add(geologicalClass);

                                    }
                                }
                        } // row else header or record
                        LOG.info("Row no : {}", j-1);
                    LOG.info("Sheet completed : ");
                    }// sheet for
                } // work book for
            }// xls if
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sheetData;
    }
}
