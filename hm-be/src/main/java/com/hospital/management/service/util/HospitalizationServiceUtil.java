package com.hospital.management.service.util;

import com.hospital.management.exception.appointment.AppointmentFieldsException;
import com.hospital.management.model.Hospitalization;
import com.hospital.management.model.dto.hospitalization.HospitalizationOutcomingDto;
import com.hospital.management.model.dto.hospitalization.HospitalizationParams;
import com.hospital.management.model.dto.hospitalization.HospitalizationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HospitalizationServiceUtil {
    Page<Hospitalization> findByFilter(HospitalizationParams hospitalizationParams, Pageable pageable);

    List<?> mapEntityListToDtoList(Page<Hospitalization> hospitalizations, Class<HospitalizationOutcomingDto> hospitalizationOutcomingDtoClass);

    void add(HospitalizationRequest hospitalizationRequest) throws AppointmentFieldsException;
}