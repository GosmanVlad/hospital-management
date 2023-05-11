package com.hospital.management.service.implementation;

import com.hospital.management.model.Department;
import com.hospital.management.model.dto.department.DepartmentRequest;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.service.util.DepartmentServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentServiceUtil {
    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public void delete(Long departmentId) {
        Department department = departmentRepository.findByDepartmentId(departmentId);
        departmentRepository.delete(department);
    }

    @Override
    public void add(DepartmentRequest departmentRequest) {
        Department department = new Department();
        department.setDepartmentName(departmentRequest.getDepartmentName());
        departmentRepository.save(department);
    }
}
