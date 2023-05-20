package com.hospital.management.controller;

import com.hospital.management.exception.appointment.AppointmentFieldsException;
import com.hospital.management.exception.appointment.AppointmentOverlapException;
import com.hospital.management.model.Employee;
import com.hospital.management.model.Hospitalization;
import com.hospital.management.model.dto.appointment.AppointmentOutcomingDto;
import com.hospital.management.model.dto.appointment.AppointmentRequest;
import com.hospital.management.model.dto.hospitalization.HospitalizationOutcomingDto;
import com.hospital.management.model.dto.hospitalization.HospitalizationParams;
import com.hospital.management.model.dto.hospitalization.HospitalizationRequest;
import com.hospital.management.service.implementation.HospitalizationExcelExporterService;
import com.hospital.management.service.util.HospitalizationServiceUtil;
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
@RequestMapping("/hospitalizations")
public class HospitalizationController {
    @Autowired
    HospitalizationServiceUtil hospitalizationService;

    @Autowired
    PDFGeneratorServiceUtil pdfGeneratorService;

    @Value("${pdfGenerator.path}")
    private String pdfGenerator;

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = HospitalizationOutcomingDto.class))})})
    @SuppressWarnings("unchecked")
    @GetMapping
    public ResponseEntity<?> getHospitalizations(HospitalizationParams hospitalizationParams) {
        Map<String, Object> responseMap;

        try {
            Sort sort = Sort.by(hospitalizationParams.getSortField());
            sort = hospitalizationParams.getDirection().equalsIgnoreCase("DESC") ? sort.descending() : sort.ascending();

            Pageable pageable = PageRequest.of(hospitalizationParams.getPage(), hospitalizationParams.getSize(), sort);
            Page<Hospitalization> hospitalizations = hospitalizationService.findByFilter(hospitalizationParams, pageable);
            List<HospitalizationOutcomingDto> hospitalizationOutcomingDtos = (List<HospitalizationOutcomingDto>) hospitalizationService.mapEntityListToDtoList(hospitalizations, HospitalizationOutcomingDto.class);

            responseMap = ResponseUtils.createResponseMap(false, "success_msg", new PageImpl<>(hospitalizationOutcomingDtos, pageable, hospitalizations.getTotalElements()));

            if (hospitalizations.isEmpty()) {
                responseMap = ResponseUtils.createResponseMap(false, "no_hospitalizations", null);
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
    public ResponseEntity<?> createHospitalization(@RequestBody HospitalizationRequest hospitalizationRequest) {
        Map<String, Object> responseMap;

        try {
            hospitalizationService.add(hospitalizationRequest);

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
    public ResponseEntity<?> exportExcelHospitalization(HttpServletResponse response,
                                                        HospitalizationParams hospitalizationParams) {
        Map<String, Object> responseMap;

        try {
            List<Hospitalization> hospitalizations = hospitalizationService.findByFilter(hospitalizationParams);
            HospitalizationExcelExporterService hospitalizationExcelExporterService = new HospitalizationExcelExporterService(hospitalizations);
            hospitalizationExcelExporterService.export(response);

            responseMap = ResponseUtils.createResponseMap(false, "success_msg", "exported");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @GetMapping("/generate-hospitalization/{hospitalizationId}")
    public ResponseEntity<?> generateHospitalization(@PathVariable(value = "hospitalizationId") Long hospitalizationId) throws DocumentException, IOException {
        Map<String, Object> responseMap;
        Hospitalization hospitalization = hospitalizationService.findByHospitalizationId(hospitalizationId);
        Context context = hospitalizationService.mapThymeleafVariables(hospitalization);
        String test = pdfGeneratorService.parseThymeleafTemplate("hospitalization", context);
        String fileName = "hospitalization_" + hospitalizationId;
        String path = pdfGeneratorService.generatePdfFromHtml(test, fileName, "hospitalizations");

        hospitalizationService.updateHospitalizationDocPath(hospitalizationId, path);
        responseMap = ResponseUtils.createResponseMap(false, "success_msg", this.pdfGenerator + "hospitalizations/" + fileName + ".pdf");
        return ResponseEntity.ok(responseMap);
    }
}
