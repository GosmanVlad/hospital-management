package com.hospital.management.model.dto.appointment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentOutcomingDto {
    private Long appointmentId;
    private Long departmentId;
    private LocalDateTime date;
    private String details;
    private String status;
    private Long employeeId;
    private String employeeName;
    private String departmentName;
    private Long userId;
}
