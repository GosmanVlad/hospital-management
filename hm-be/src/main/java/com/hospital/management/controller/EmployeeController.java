package com.hospital.management.controller;

import com.hospital.management.model.Employee;
import com.hospital.management.model.dto.appointment.AppointmentOutcomingDto;
import com.hospital.management.service.util.EmployeeServiceUtil;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    EmployeeServiceUtil employeeService;

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = Employee.class))})})
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

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = Employee.class))})})
    @GetMapping
    public ResponseEntity<?> getEmployees(@RequestParam(required = false, name = "pagination", defaultValue = "false") Boolean pagination,
                                          @RequestParam(required = false, name = "page", defaultValue = "0") int page,
                                          @RequestParam(required = false, name = "size", defaultValue = "20") int size,
                                          @RequestParam(required = false, name = "sortField", defaultValue = "employeeId") String sortField,
                                          @RequestParam(required = false, name = "direction", defaultValue = "DESC") String direction,
                                          @RequestParam(required = false, name = "employeeId", defaultValue = "") Long employeeId,
                                          @RequestParam(required = false, name = "role", defaultValue = "") String role) {
        Map<String, Object> responseMap;

        try {
            if(!pagination) {
                List<Employee> employeeList = employeeService.findAll();
                responseMap = ResponseUtils.createResponseMap(false, "success_msg", employeeList);
            } else {
                Sort sort = Sort.by(sortField);
                sort = direction.equalsIgnoreCase("DESC") ? sort.descending() : sort.ascending();
                Pageable pageable = PageRequest.of(page, size, sort);
                Page<Employee> employeeList = employeeService.findAllByPagination(employeeId, role, pageable);
                responseMap = ResponseUtils.createResponseMap(false, "success_msg", employeeList);
            }
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap = ResponseUtils.createResponseMap(true, "error_msg", e.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

}
