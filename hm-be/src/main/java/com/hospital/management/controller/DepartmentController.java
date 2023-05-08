package com.hospital.management.controller;

import com.hospital.management.service.util.DepartmentServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    DepartmentServiceUtil departmentService;

    @GetMapping
    public ResponseEntity<?> getDepartments() {
        Map<String, Object> responseMap;

        try {
            responseMap = ResponseUtils.createResponseMap(false, "success_msg", departmentService.findAll());
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

}
