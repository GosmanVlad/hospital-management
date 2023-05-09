package com.hospital.management.controller;

import com.hospital.management.service.util.SalonServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/salons")
public class SalonController {

    @Autowired
    SalonServiceUtil salonService;

    @GetMapping
    public ResponseEntity<?> getSalons() {
        Map<String, Object> responseMap;

        try {
            responseMap = ResponseUtils.createResponseMap(false, "success_msg", salonService.findAll());
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }
}
