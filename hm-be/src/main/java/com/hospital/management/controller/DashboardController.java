package com.hospital.management.controller;

import com.hospital.management.model.Employee;
import com.hospital.management.model.dto.raport.InvoiceRaportOutcomingDto;
import com.hospital.management.model.dto.raport.RaportParams;
import com.hospital.management.service.util.AppointmentServiceUtil;
import com.hospital.management.service.util.HospitalizationServiceUtil;
import com.hospital.management.service.util.InvoiceServiceUtil;
import com.hospital.management.service.util.RaportServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/raports")
public class DashboardController {

    @Autowired
    RaportServiceUtil raportService;

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = Employee.class))})})
    @GetMapping("/invoices")
    public ResponseEntity<?> getInvoicesRaport(RaportParams raportParams) {
        Map<String, Object> responseMap;

        try {
            List<InvoiceRaportOutcomingDto> invoicesRaport = raportService.getInvoicesRaport(raportParams);
            responseMap = ResponseUtils.createResponseMap(false, "success_msg", invoicesRaport);
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

}
