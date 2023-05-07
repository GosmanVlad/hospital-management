package com.hospital.management.service.implementation;

import com.hospital.management.model.Department;
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
}
