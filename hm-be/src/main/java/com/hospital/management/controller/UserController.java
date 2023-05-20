package com.hospital.management.controller;

import com.hospital.management.model.Hospitalization;
import com.hospital.management.model.User;
import com.hospital.management.model.dto.department.DepartmentImportRequest;
import com.hospital.management.model.dto.hospitalization.HospitalizationOutcomingDto;
import com.hospital.management.model.dto.hospitalization.HospitalizationParams;
import com.hospital.management.model.dto.people.PeopleRequestImport;
import com.hospital.management.service.util.UserServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceUtil userService;

    @GetMapping("/patients")
    public ResponseEntity<?> getPatients(@RequestParam(required = false, name = "pagination", defaultValue = "false") Boolean pagination,
                                         @RequestParam(required = false, name = "page", defaultValue = "0") int page,
                                         @RequestParam(required = false, name = "size", defaultValue = "20") int size,
                                         @RequestParam(required = false, name = "sortField", defaultValue = "userId") String sortField,
                                         @RequestParam(required = false, name = "direction", defaultValue = "DESC") String direction,
                                         @RequestParam(required = false, name = "userId", defaultValue = "") Long userId) {
        Map<String, Object> responseMap;

        try {
            if(!pagination) {
                List<User> patients = userService.findAllPatients();
                responseMap = ResponseUtils.createResponseMap(false, "success_msg", patients);
            }
            else {
                Sort sort = Sort.by(sortField);
                sort = direction.equalsIgnoreCase("DESC") ? sort.descending() : sort.ascending();
                Pageable pageable = PageRequest.of(page, size, sort);

                Page<User> patients = userService.findAllPatientsWithPagination(userId, pageable);
                responseMap = ResponseUtils.createResponseMap(false, "success_msg", patients);
            }

            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importDocs(@ModelAttribute PeopleRequestImport importDTO) {

        Map<String, Object> responseMap = new HashMap<>();
        try {
            userService.importFromCsv(importDTO);
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
