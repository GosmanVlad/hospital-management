package com.hospital.management.service.util;

import com.hospital.management.exception.appointment.AppointmentFieldsException;
import com.hospital.management.exception.salon.SalonSeatsException;
import com.hospital.management.model.Hospitalization;
import com.hospital.management.model.dto.hospitalization.HospitalizationOutcomingDto;
import com.hospital.management.model.dto.hospitalization.HospitalizationParams;
import com.hospital.management.model.dto.hospitalization.HospitalizationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.context.Context;

import java.util.List;

public interface HospitalizationServiceUtil {
    Page<Hospitalization> findByFilter(HospitalizationParams hospitalizationParams, Pageable pageable);

    List<?> mapEntityListToDtoList(Page<Hospitalization> hospitalizations, Class<HospitalizationOutcomingDto> hospitalizationOutcomingDtoClass);

    void add(HospitalizationRequest hospitalizationRequest) throws AppointmentFieldsException, SalonSeatsException;

    List<Hospitalization> findByFilter(HospitalizationParams hospitalizationParams);

    Hospitalization findByHospitalizationId(Long hospitalizationId);

    Context mapThymeleafVariables(Hospitalization hospitalization);

    void updateHospitalizationDocPath(Long hospitalizationId, String path);
}
