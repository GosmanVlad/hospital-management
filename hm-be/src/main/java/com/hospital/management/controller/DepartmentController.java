package com.hospital.management.controller;

import com.hospital.management.model.dto.department.DepartmentImportRequest;
import com.hospital.management.model.dto.department.DepartmentRequest;
import com.hospital.management.service.util.DepartmentServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable(value = "departmentId") Long departmentId) {
        Map<String, Object> responseMap;

        try {
            departmentService.delete(departmentId);
            responseMap = ResponseUtils.createResponseMap(false, "success_msg", "");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PostMapping
    public ResponseEntity<?> addDepartment(@RequestBody DepartmentRequest departmentRequest) {
        Map<String, Object> responseMap;

        try {
            departmentService.add(departmentRequest);
            responseMap = ResponseUtils.createResponseMap(false, "success_msg", "");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importDocs(@ModelAttribute DepartmentImportRequest importDTO) {

        Map<String, Object> responseMap = new HashMap<>();
        try {
            departmentService.importFromCsv(importDTO);
        } catch (Exception exception) {
            exception.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
        responseMap.put("error", false);
        responseMap.put("message", "XML files generated successfully!");
        return ResponseEntity.ok(responseMap);
    }

}
