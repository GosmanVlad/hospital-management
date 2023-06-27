package com.hospital.management.service.util;

import com.hospital.management.model.Salon;
import com.hospital.management.model.dto.salon.SalonImportRequest;
import com.hospital.management.model.dto.salon.SalonRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalonServiceUtil {
    List<Salon> findAll();
    Page<Salon> findAllWithPagination(Pageable pageable);

    void delete(Long salonId);

    void add(SalonRequest salonRequest);

    void importFromCsv(SalonImportRequest importDTO) throws Exception;
}
