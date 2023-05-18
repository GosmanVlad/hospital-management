package com.hospital.management.service.util;

import com.hospital.management.model.Invoice;
import com.hospital.management.model.dto.invoice.InvoiceOutcomingDto;
import com.hospital.management.model.dto.invoice.InvoiceParams;
import com.hospital.management.model.dto.invoice.InvoiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.context.Context;

import java.util.List;

public interface InvoiceServiceUtil {
    Page<Invoice> findByFilter(InvoiceParams invoiceParams, Pageable pageable);
    List<Invoice> findByFilter(InvoiceParams invoiceParams);

    List<?> mapEntityListToDtoList(Page<Invoice> invoices, Class<InvoiceOutcomingDto> invoiceOutcomingDtoClass);

    void add(InvoiceRequest invoiceRequest);

    Invoice findByInvoiceId(Long invoiceId);

    Context mapThymeleafVariables(Invoice invoice);
}
