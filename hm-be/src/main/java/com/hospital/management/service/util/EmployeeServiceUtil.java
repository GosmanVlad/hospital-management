package com.hospital.management.service.util;

import com.hospital.management.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeServiceUtil {
    List<Employee> findByDepartmentId(Long departmentId);

    Employee findByUserId(Long userId);

    List<Employee> findAll();

    Page<Employee> findAllByPagination(Long employeeId, String role, Pageable pageable);

    void save(Employee employee);
}
