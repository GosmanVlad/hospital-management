package com.hospital.management.service.util;

import com.hospital.management.model.Department;
import com.hospital.management.model.dto.department.DepartmentImportRequest;
import com.hospital.management.model.dto.department.DepartmentRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DepartmentServiceUtil {
    List<Department> findAll();

    void delete(Long departmentId);

    void add(DepartmentRequest departmentRequest);

    void importFromCsv(DepartmentImportRequest importDTO) throws Exception;

}
