package com.hospital.management.service.implementation;

import com.hospital.management.constant.AppointmentStatus;
import com.hospital.management.model.Appointment;
import com.hospital.management.model.Appointment_;
import com.hospital.management.model.Department;
import com.hospital.management.model.User;
import com.hospital.management.model.dto.appointment.AppointmentOutcomingDto;
import com.hospital.management.model.dto.appointment.AppointmentParams;
import com.hospital.management.model.dto.appointment.AppointmentRequest;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.service.util.AppointmentServiceUtil;
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
    AppointmentRepository appointmentRepository;

    @Override
    public void add(AppointmentRequest appointmentRequest) {
        Department department = departmentRepository.findByDepartmentId(appointmentRequest.getDepartmentId());
        User patient = userRepository.findByUserId(appointmentRequest.getPatientId());

        if(department != null && patient != null) {
            Appointment appointment = new Appointment();

            appointment.setDetails(appointmentRequest.getDetails());
            appointment.setDate(appointmentRequest.getDate());
            appointment.setDepartment(department);
            appointment.setUser(patient);
            appointment.setStatus(AppointmentStatus.PENDING);
            appointment.setEmployee(null);
            appointmentRepository.save(appointment);

        }
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

            if (appointmentParams.getStatus() != null && !appointmentParams.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Appointment_.status), appointmentParams.getStatus()));
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
        return ResponseUtils.mapEntityListToDtoList(modelMapper, appointments, appointmentOutcomingDtoClass);
    }
}
