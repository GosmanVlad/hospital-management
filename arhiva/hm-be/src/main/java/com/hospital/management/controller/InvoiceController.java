package com.hospital.management.controller;

import com.hospital.management.model.Hospitalization;
import com.hospital.management.model.Invoice;
import com.hospital.management.model.dto.hospitalization.HospitalizationOutcomingDto;
import com.hospital.management.model.dto.hospitalization.HospitalizationParams;
import com.hospital.management.model.dto.hospitalization.HospitalizationRequest;
import com.hospital.management.model.dto.invoice.InvoiceOutcomingDto;
import com.hospital.management.model.dto.invoice.InvoiceParams;
import com.hospital.management.model.dto.invoice.InvoiceRequest;
import com.hospital.management.service.implementation.HospitalizationExcelExporterService;
import com.hospital.management.service.implementation.InvoiceExcelExporterService;
import com.hospital.management.service.util.InvoiceServiceUtil;
import com.hospital.management.service.util.PDFGeneratorServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import com.lowagie.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoiceServiceUtil invoiceService;

    @Autowired
    PDFGeneratorServiceUtil pdfGeneratorService;

    @Value("${pdfGenerator.path}")
    private String pdfGenerator;

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = InvoiceOutcomingDto.class))})})
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

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
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

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @GetMapping("/export-excel")
    public ResponseEntity<?> exportExcelInvoices(HttpServletResponse response,
                                                 InvoiceParams invoiceParams) {
        Map<String, Object> responseMap;

        try {
            List<Invoice> invoices = invoiceService.findByFilter(invoiceParams);
            InvoiceExcelExporterService invoiceExcelExporterService = new InvoiceExcelExporterService(invoices);
            invoiceExcelExporterService.export(response);

            responseMap = ResponseUtils.createResponseMap(false, "success_msg", "exported");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @GetMapping("/generate-invoice/{invoiceId}")
    public ResponseEntity<?> generateHospitalization(@PathVariable(value = "invoiceId") Long invoiceId) throws DocumentException, IOException {
        Map<String, Object> responseMap;
        Invoice invoice = invoiceService.findByInvoiceId(invoiceId);
        Context context = invoiceService.mapThymeleafVariables(invoice);
        String test = pdfGeneratorService.parseThymeleafTemplate("invoiceTemplate", context);
        String fileName = "invoice_" + invoiceId;
        String path = pdfGeneratorService.generatePdfFromHtml(test, fileName, "invoices");

        responseMap = ResponseUtils.createResponseMap(false, "success_msg", this.pdfGenerator + "invoices/" + fileName + ".pdf");
        return ResponseEntity.ok(responseMap);
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PutMapping("/status/{invoiceId}/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable(value = "invoiceId") Long invoiceId,
                                          @PathVariable(value = "status") String status) {
        Map<String, Object> responseMap;

        try {
            invoiceService.updateStatus(invoiceId, status);

            responseMap = ResponseUtils.createResponseMap(false, "success_msg", null);
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.getMessage());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }
}
