package com.hospital.management.service.util;

import com.hospital.management.exception.appointment.AppointmentFieldsException;
import com.hospital.management.exception.appointment.AppointmentOverlapException;
import com.hospital.management.model.Appointment;
import com.hospital.management.model.dto.appointment.AppointmentOutcomingDto;
import com.hospital.management.model.dto.appointment.AppointmentParams;
import com.hospital.management.model.dto.appointment.AppointmentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppointmentServiceUtil {
    void add(AppointmentRequest appointmentRequest) throws AppointmentFieldsException, AppointmentOverlapException;

    Page<Appointment> findByFilter(AppointmentParams appointmentParams, Pageable pageable);

    List<?> mapEntityListToDtoList(Page<Appointment> appointments, Class<AppointmentOutcomingDto> appointmentOutcomingDtoClass);

    void changeAppointmentStatus(Long appointmentId, String status);
}
