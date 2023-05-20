package com.hospital.management.model.dto.invoice;

import com.hospital.management.model.InvoiceItem;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class InvoiceOutcomingDto {
    private Long invoiceId;
    private Date date;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String firstNamePatient;
    private String lastNamePatient;
    private List<InvoiceItem> invoiceItems;
    private Long vatPercentage;
    private String status;
}
