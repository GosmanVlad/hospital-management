package com.hospital.management.model.dto.hospitalization;

import lombok.Data;

import java.util.Date;

@Data
public class HospitalizationRequest {
    private Long doctorId;
    private Long patientId;
    private Long departmentId;
    private Date startDate;
    private Date endDate;
    private String diagnosis;
    private Long salonId;
}
