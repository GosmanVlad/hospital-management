package com.hospital.management.service.util;

import com.hospital.management.model.Employee;

import java.util.List;

public interface EmployeeServiceUtil {
    List<Employee> findByDepartmentId(Long departmentId);

    Employee findByUserId(Long userId);

    List<Employee> findAll();
}
