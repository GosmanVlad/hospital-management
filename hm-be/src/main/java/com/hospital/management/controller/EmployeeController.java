package com.hospital.management.controller;

import com.hospital.management.service.util.EmployeeServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    EmployeeServiceUtil employeeService;

    @GetMapping("/departments/{departmentId}")
    public ResponseEntity<?> getEmployeeByDepartmentId(@PathVariable(value = "departmentId") Long departmentId) {
        Map<String, Object> responseMap;

        try {
            responseMap = ResponseUtils.createResponseMap(false, "success_msg", employeeService.findByDepartmentId(departmentId));
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @GetMapping
    public ResponseEntity<?> getEmployees() {
        Map<String, Object> responseMap;

        try {
            responseMap = ResponseUtils.createResponseMap(false, "success_msg", employeeService.findAll());
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }
}
