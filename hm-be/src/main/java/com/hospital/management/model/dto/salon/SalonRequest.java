package com.hospital.management.model.dto.salon;

import lombok.Data;

@Data
public class SalonRequest {
    private String salonName;
    private Long seats;
    private Long departmentId;
}
