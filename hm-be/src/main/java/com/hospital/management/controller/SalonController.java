package com.hospital.management.controller;

import com.hospital.management.model.Salon;
import com.hospital.management.model.dto.department.DepartmentImportRequest;
import com.hospital.management.model.dto.department.DepartmentRequest;
import com.hospital.management.model.dto.invoice.InvoiceOutcomingDto;
import com.hospital.management.model.dto.salon.SalonImportRequest;
import com.hospital.management.model.dto.salon.SalonRequest;
import com.hospital.management.service.util.SalonServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/salons")
public class SalonController {

    @Autowired
    SalonServiceUtil salonService;

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = Salon.class))})})
    @GetMapping
    public ResponseEntity<?> getSalons(@RequestParam(required = false, name = "pagination", defaultValue = "false") Boolean pagination,
                                       @RequestParam(required = false, name = "page", defaultValue = "0") int page,
                                       @RequestParam(required = false, name = "size", defaultValue = "20") int size,
                                       @RequestParam(required = false, name = "sortField", defaultValue = "salonId") String sortField,
                                       @RequestParam(required = false, name = "direction", defaultValue = "DESC") String direction) {
        Map<String, Object> responseMap;

        try {
            if (!pagination) {
                responseMap = ResponseUtils.createResponseMap(false, "success_msg", salonService.findAll());
            } else {
                Sort sort = Sort.by(sortField);
                sort = direction.equalsIgnoreCase("DESC") ? sort.descending() : sort.ascending();
                Pageable pageable = PageRequest.of(page, size, sort);

                Page<Salon> salons = salonService.findAllWithPagination(pageable);
                responseMap = ResponseUtils.createResponseMap(false, "success_msg", salons);
            }
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @DeleteMapping("/{salonId}")
    public ResponseEntity<?> deleteSalon(@PathVariable(value = "salonId") Long salonId) {
        Map<String, Object> responseMap;

        try {
            salonService.delete(salonId);
            responseMap = ResponseUtils.createResponseMap(false, "success_msg", "");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PostMapping
    public ResponseEntity<?> addSalon(@RequestBody SalonRequest salonRequest) {
        Map<String, Object> responseMap;

        try {
            salonService.add(salonRequest);
            responseMap = ResponseUtils.createResponseMap(false, "success_msg", "");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importDocs(@ModelAttribute SalonImportRequest importDTO) {

        Map<String, Object> responseMap = new HashMap<>();
        try {
            salonService.importFromCsv(importDTO);
        } catch (Exception exception) {
            exception.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
        responseMap.put("error", false);
        responseMap.put("message", "success");
        return ResponseEntity.ok(responseMap);
    }
}