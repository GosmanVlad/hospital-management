package com.hospital.management.model.dto.appointment;

import lombok.Data;

@Data
public class AppointmentParams {
    private int page = 0;
    private int size = 20;
    private String sortField = "appointmentId";
    private String direction = "DESC";
    private Long departmentId;
    private Long employeeId;
    private Long userId;
    private String status;
}
