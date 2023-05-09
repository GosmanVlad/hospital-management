package com.hospital.management.controller;

import com.hospital.management.exception.appointment.AppointmentFieldsException;
import com.hospital.management.exception.appointment.AppointmentOverlapException;
import com.hospital.management.model.Hospitalization;
import com.hospital.management.model.dto.appointment.AppointmentOutcomingDto;
import com.hospital.management.model.dto.appointment.AppointmentRequest;
import com.hospital.management.model.dto.hospitalization.HospitalizationOutcomingDto;
import com.hospital.management.model.dto.hospitalization.HospitalizationParams;
import com.hospital.management.model.dto.hospitalization.HospitalizationRequest;
import com.hospital.management.service.util.HospitalizationServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hospitalizations")
public class HospitalizationController {
    @Autowired
    HospitalizationServiceUtil hospitalizationService;

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
                responseMap = ResponseUtils.createResponseMap(false, "list_is_empty.", null);
            }

            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

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
}
