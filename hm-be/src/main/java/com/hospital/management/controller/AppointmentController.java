package com.hospital.management.controller;

import com.hospital.management.exception.appointment.AppointmentFieldsException;
import com.hospital.management.exception.appointment.AppointmentOverlapException;
import com.hospital.management.model.Appointment;
import com.hospital.management.model.dto.appointment.AppointmentOutcomingDto;
import com.hospital.management.model.dto.appointment.AppointmentParams;
import com.hospital.management.model.dto.appointment.AppointmentRequest;
import com.hospital.management.service.util.AppointmentServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    AppointmentServiceUtil appointmentService;

    @SuppressWarnings("unchecked")
    @GetMapping
    public ResponseEntity<?> getAppointments(AppointmentParams appointmentParams) {
        Map<String, Object> responseMap;

        try {
            Sort sort = Sort.by(appointmentParams.getSortField());
            sort = appointmentParams.getDirection().equalsIgnoreCase("DESC") ? sort.descending() : sort.ascending();

            Pageable pageable = PageRequest.of(appointmentParams.getPage(), appointmentParams.getSize(), sort);
            Page<Appointment> appointments = appointmentService.findByFilter(appointmentParams, pageable);
            List<AppointmentOutcomingDto> appointmentOutcomingDtos = (List<AppointmentOutcomingDto>) appointmentService.mapEntityListToDtoList(appointments, AppointmentOutcomingDto.class);

            responseMap = ResponseUtils.createResponseMap(false, "success_msg", new PageImpl<>(appointmentOutcomingDtos, pageable, appointments.getTotalElements()));

            if (appointments.isEmpty()) {
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
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        Map<String, Object> responseMap;

        try {
            appointmentService.add(appointmentRequest);

            responseMap = ResponseUtils.createResponseMap(false, "success_msg", null);
            return ResponseEntity.ok(responseMap);
        } catch (AppointmentOverlapException overlapException) {
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", overlapException.getMessage());
            return ResponseEntity.status(409).body(responseMap);
        } catch (AppointmentFieldsException exception) {
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", exception.getMessage());
            return ResponseEntity.internalServerError().body(responseMap);
        } catch (Exception e) {
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.getMessage());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PutMapping("/{appointmentId}/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable(value = "appointmentId") Long appointmentId,
                                          @PathVariable(value = "status") String status) {
        Map<String, Object> responseMap;

        try {
            appointmentService.changeAppointmentStatus(appointmentId, status);

            responseMap = ResponseUtils.createResponseMap(false, "success_msg", null);
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.getMessage());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }
}
