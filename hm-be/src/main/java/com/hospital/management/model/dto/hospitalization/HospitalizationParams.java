package com.hospital.management.model.dto.hospitalization;

import lombok.Data;

import java.util.Date;

@Data
public class HospitalizationParams {
    private int page = 0;
    private int size = 20;
    private String sortField = "hospitalizationId";
    private String direction = "DESC";
    private Date startDate;
    private Date endDate;
    private Long doctorId;
    private String search;
}
