package com.hospital.management.service.implementation;

import com.hospital.management.model.Department;
import com.hospital.management.model.dto.department.DepartmentCsvLayout;
import com.hospital.management.model.dto.department.DepartmentImportRequest;
import com.hospital.management.model.dto.department.DepartmentRequest;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.service.util.DepartmentServiceUtil;
import com.hospital.management.service.util.FileServiceUtil;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentServiceUtil {
    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    FileServiceUtil fileUploadService;

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

    @Override
    public void importFromCsv(DepartmentImportRequest importDTO) throws Exception {
        String csvPath = fileUploadService.createDirectoriesAndSaveFile(importDTO.getDoc());
        File file = new File(csvPath);

        List<DepartmentCsvLayout> result = readFile(file);
        for (DepartmentCsvLayout row : result) {
            Department department = new Department();
            department.setDepartmentName(row.getDepartmentName());
            departmentRepository.save(department);
        }
    }

    public List<DepartmentCsvLayout> readFile(File csvFile) throws Exception {
        List<DepartmentCsvLayout> beans = new CsvToBeanBuilder(new FileReader(csvFile))
                .withType(DepartmentCsvLayout.class)
                .withSeparator(',')
                //.withSkipLines(skipLine)
                .build()
                .parse();

        return beans;
    }
}
