package com.hospital.management.service.implementation;

import com.hospital.management.model.Department;
import com.hospital.management.model.Salon;
import com.hospital.management.model.dto.department.DepartmentCsvLayout;
import com.hospital.management.model.dto.salon.SalonCsvLayout;
import com.hospital.management.model.dto.salon.SalonImportRequest;
import com.hospital.management.model.dto.salon.SalonRequest;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.repository.SalonRepository;
import com.hospital.management.service.util.FileServiceUtil;
import com.hospital.management.service.util.SalonServiceUtil;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.List;

@Service
public class SalonServiceImpl implements SalonServiceUtil {

    @Autowired
    SalonRepository salonRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    FileServiceUtil fileUploadService;

    @Override
    public List<Salon> findAll() {
        return salonRepository.findAll();
    }

    @Override
    public Page<Salon> findAllWithPagination(Pageable pageable) {
        return salonRepository.findAll(pageable);
    }

    @Override
    public void delete(Long salonId) {
        Salon salon = salonRepository.findBySalonId(salonId);
        salon.setDepartment(null);
        salonRepository.delete(salon);
    }

    @Override
    public void add(SalonRequest salonRequest) {
        Salon salon = new Salon();
        Department department = departmentRepository.findByDepartmentId(salonRequest.getDepartmentId());

        salon.setSalonName(salonRequest.getSalonName());
        salon.setSeats(salonRequest.getSeats());

        if(department != null) {
            salon.setDepartment(department);
        }

        salonRepository.save(salon);
    }

    @Override
    public void importFromCsv(SalonImportRequest importDTO) throws Exception {
        try {
            String csvPath = fileUploadService.createDirectoriesAndSaveFile(importDTO.getDoc());
            File file = new File(csvPath);

            List<SalonCsvLayout> result = readFile(file);
            for (SalonCsvLayout row : result) {
                Salon salon = new Salon();
                Department department = departmentRepository.findByDepartmentName(row.getDepartmentName());

                if (department != null) {
                    salon.setSalonName(row.getSalonName());
                    salon.setSeats(row.getSeats());
                    salon.setDepartment(department);
                    salonRepository.save(salon);
                }
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public List<SalonCsvLayout> readFile(File csvFile) throws Exception {
        List<SalonCsvLayout> beans = new CsvToBeanBuilder(new FileReader(csvFile))
                .withType(SalonCsvLayout.class)
                .withSeparator(',')
                //.withSkipLines(skipLine)
                .build()
                .parse();

        return beans;
    }
}
