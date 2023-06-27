package com.hospital.management.model.dto.raport;

import lombok.Data;

import java.util.Date;

@Data
public class InvoiceRaportOutcomingDto {
    private Date date;
    private Long unpaidInvoices;
    private Long paidInvoices;
    private Long pendingInvoices;
}
