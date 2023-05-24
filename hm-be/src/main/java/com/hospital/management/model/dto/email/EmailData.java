package com.hospital.management.model.dto.email;

import lombok.Data;

@Data
public class EmailData {
    private String sendTo;
    private String body;
    private String subject;
    private String attachment;
}
