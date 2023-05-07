package com.hospital.management.model.dto.appointment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    private String details;
    private LocalDateTime date;
    private Long departmentId;
    private Long patientId;
    private Long employeeId;
}
