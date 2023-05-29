package com.hospital.management.service.implementation;

import com.hospital.management.constant.AppointmentStatus;
import com.hospital.management.exception.appointment.AppointmentFieldsException;
import com.hospital.management.exception.appointment.AppointmentOverlapException;
import com.hospital.management.model.*;
import com.hospital.management.model.dto.appointment.AppointmentOutcomingDto;
import com.hospital.management.model.dto.appointment.AppointmentParams;
import com.hospital.management.model.dto.appointment.AppointmentRequest;
import com.hospital.management.model.dto.email.EmailData;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.repository.EmployeeRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.service.util.AppointmentServiceUtil;
import com.hospital.management.service.util.EmailServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentServiceUtil {
    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    EmailServiceUtil emailService;

    @Override
    public void add(AppointmentRequest appointmentRequest) throws AppointmentFieldsException, AppointmentOverlapException {
        if (appointmentRequest.getDepartmentId() == null || appointmentRequest.getDate() == null || appointmentRequest.getEmployeeId() == null) {
            throw new AppointmentFieldsException("some_fields_empty");
        }

        Department department = departmentRepository.findByDepartmentId(appointmentRequest.getDepartmentId());
        User patient = userRepository.findByUserId(appointmentRequest.getPatientId());
        Employee doctor = employeeRepository.findByEmployeeId(appointmentRequest.getEmployeeId());
        Appointment checkAppointment = appointmentRepository.findByDate(appointmentRequest.getDate());

        if (checkAppointment != null) {
            throw new AppointmentOverlapException("overlap_appointment");
        }


        Appointment appointment = new Appointment();

        appointment.setDetails(appointmentRequest.getDetails());
        appointment.setDate(appointmentRequest.getDate());
        appointment.setDepartment(department);
        appointment.setUser(patient);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setEmployee(doctor);
        appointmentRepository.save(appointment);

        EmailData emailData = new EmailData();
        emailData.setSendTo(doctor.getUser().getEmail());
        emailData.setSubject("[HM] Programare noua");
        emailData.setBody("O noua programare asteapta raspunsul dvs.\n" +
                "Poti administra programarile din platforma interna Hospital Management!\n\n" +
                "Pacient: " + patient.getFirstName() + " " + patient.getLastName() + "\n" +
                "Data: " + appointmentRequest.getDate() + "\n" +
                "Alte detalii: " + appointmentRequest.getDetails());
        emailService.sendMail(emailData);
    }

    @Override
    public Page<Appointment> findByFilter(AppointmentParams appointmentParams, Pageable pageable) {
        Specification<Appointment> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (appointmentParams.getDepartmentId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("department").get("departmentId"), appointmentParams.getDepartmentId()));
            }

            if (appointmentParams.getUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("user").get("userId"), appointmentParams.getUserId()));
            }

            if (appointmentParams.getEmployeeId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("employee").get("employeeId"), appointmentParams.getEmployeeId()));
            }

            if (appointmentParams.getStatus() != null && !appointmentParams.getStatus().equals("")) {
                predicates.add(criteriaBuilder.equal(root.get(Appointment_.status), appointmentParams.getStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
        return appointmentRepository.findAll(specification, pageable);
    }

    @Override
    public List<?> mapEntityListToDtoList(Page<Appointment> appointments, Class<AppointmentOutcomingDto> appointmentOutcomingDtoClass) {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<Appointment, AppointmentOutcomingDto> propertyMapper = modelMapper.createTypeMap(Appointment.class, AppointmentOutcomingDto.class);

        // Map departmentId
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> src.getDepartment().getDepartmentId(), AppointmentOutcomingDto::setDepartmentId)
        );

        // Map userId
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> src.getUser().getUserId(), AppointmentOutcomingDto::setUserId)
        );

        // Map employeeId
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> src.getEmployee().getEmployeeId(), AppointmentOutcomingDto::setEmployeeId)
        );

        // Map department
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> src.getDepartment().getDepartmentName(), AppointmentOutcomingDto::setDepartmentName)
        );

        // Map department
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> src.getEmployee().getUser().getUsername(), AppointmentOutcomingDto::setEmployeeName)
        );

        // Map applicant
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> src.getUser().getFirstName(), AppointmentOutcomingDto::setFirstNameApplicant)
        );

        propertyMapper.addMappings(
                mapper -> mapper.map(src -> src.getUser().getLastName(), AppointmentOutcomingDto::setLastNameApplicant)
        );
        return ResponseUtils.mapEntityListToDtoList(modelMapper, appointments, appointmentOutcomingDtoClass);
    }

    @Override
    public void changeAppointmentStatus(Long appointmentId, String status) {
        appointmentRepository.changeAppointmentStatus(appointmentId, status);
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);

        if (appointment != null) {
            EmailData emailData = new EmailData();
            emailData.setSendTo(appointment.getUser().getEmail());
            emailData.setSubject("[HM] Informatii noi despre programare");

            if (status.equals("declined")) {
                emailData.setBody("Programarea efectuata de dvs pentru departamentul " + appointment.getDepartment().getDepartmentName() + " a fost respinsa!\n" +
                        "Poti contacta doctorul " + appointment.getEmployee().getUser().getLastName() + " " + appointment.getEmployee().getUser().getFirstName() + " pentru mai multe detalii \n\n" +
                        "Identificator programare: " + appointment.getAppointmentId() + "\n" +
                        "Data: " + appointment.getDate() + "\n" +
                        "Alte detalii: " + appointment.getDetails());
                emailService.sendMail(emailData);
            } else if (status.equals("accepted")) {
                emailData.setBody("Programarea efectuata de dvs pentru departamentul " + appointment.getDepartment().getDepartmentName() + " a fost acceptata!\n\n" +
                        "Doctor: " + appointment.getEmployee().getUser().getLastName() + " " + appointment.getEmployee().getUser().getFirstName() + "\n" +
                        "Identificator programare: " + appointment.getAppointmentId() + "\n" +
                        "Data: " + appointment.getDate() + "\n" +
                        "Alte detalii: " + appointment.getDetails());
                emailService.sendMail(emailData);
            }
        }
    }
}
