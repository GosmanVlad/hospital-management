package com.hospital.management.model.dto.invoice;

import com.hospital.management.model.InvoiceItem;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class InvoiceRequest {
    private Long doctorId;
    private Long patientId;
    private Date date;
    private Long vatPercentage;
    private List<InvoiceItem> invoiceItems;
}
