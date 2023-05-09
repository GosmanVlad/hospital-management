package com.hospital.management.model.dto.hospitalization;

import lombok.Data;

import java.util.Date;

@Data
public class HospitalizationOutcomingDto {
    private Long hospitalizationId;
    private Long departmentId;
    private Date startDate;
    private Date endDate;
    private Long doctorId;
    private String doctorName;
    private String departmentName;
    private Long patientId;
    private String firstNamePatient;
    private String lastNamePatient;
    private String salonName;
}
