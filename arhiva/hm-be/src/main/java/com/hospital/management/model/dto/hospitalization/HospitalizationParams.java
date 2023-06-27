package com.hospital.management.model.dto.hospitalization;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class HospitalizationParams {
    private int page = 0;
    private int size = 20;
    private String sortField = "hospitalizationId";
    private String direction = "DESC";
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private Long doctorId;
    private String search;
    private Long patientId;
}
