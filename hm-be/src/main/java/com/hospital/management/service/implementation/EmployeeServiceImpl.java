package com.hospital.management.service.implementation;

import com.hospital.management.model.Employee;
import com.hospital.management.repository.EmployeeRepository;
import com.hospital.management.service.util.EmployeeServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeServiceUtil {
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }
}
