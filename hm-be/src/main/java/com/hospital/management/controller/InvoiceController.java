package com.hospital.management.controller;

import com.hospital.management.model.Hospitalization;
import com.hospital.management.model.Invoice;
import com.hospital.management.model.dto.hospitalization.HospitalizationOutcomingDto;
import com.hospital.management.model.dto.hospitalization.HospitalizationParams;
import com.hospital.management.model.dto.hospitalization.HospitalizationRequest;
import com.hospital.management.model.dto.invoice.InvoiceOutcomingDto;
import com.hospital.management.model.dto.invoice.InvoiceParams;
import com.hospital.management.model.dto.invoice.InvoiceRequest;
import com.hospital.management.service.util.InvoiceServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoiceServiceUtil invoiceService;

    @GetMapping
    public ResponseEntity<?> getInvoices(InvoiceParams invoiceParams) {
        Map<String, Object> responseMap;

        try {
            Sort sort = Sort.by(invoiceParams.getSortField());
            sort = invoiceParams.getDirection().equalsIgnoreCase("DESC") ? sort.descending() : sort.ascending();

            Pageable pageable = PageRequest.of(invoiceParams.getPage(), invoiceParams.getSize(), sort);
            Page<Invoice> invoices = invoiceService.findByFilter(invoiceParams, pageable);
            List<InvoiceOutcomingDto> invoiceOutcomingDtos = (List<InvoiceOutcomingDto>) invoiceService.mapEntityListToDtoList(invoices, InvoiceOutcomingDto.class);

            responseMap = ResponseUtils.createResponseMap(false, "success_msg", new PageImpl<>(invoiceOutcomingDtos, pageable, invoices.getTotalElements()));

            if (invoices.isEmpty()) {
                responseMap = ResponseUtils.createResponseMap(false, "no_invoices", null);
            }

            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        Map<String, Object> responseMap;

        try {
            invoiceService.add(invoiceRequest);

            responseMap = ResponseUtils.createResponseMap(false, "success_msg", null);
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.getMessage());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }
}
