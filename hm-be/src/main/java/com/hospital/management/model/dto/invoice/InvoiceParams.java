package com.hospital.management.model.dto.invoice;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class InvoiceParams {
    private int page = 0;
    private int size = 20;
    private String sortField = "invoiceId";
    private String direction = "DESC";
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private Long doctorId;
    private String search;
    private Long patientId;
}
