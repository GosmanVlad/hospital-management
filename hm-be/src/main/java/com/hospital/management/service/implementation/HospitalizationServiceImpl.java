package com.hospital.management.service.implementation;

import com.hospital.management.exception.appointment.AppointmentFieldsException;
import com.hospital.management.model.*;
import com.hospital.management.model.dto.hospitalization.HospitalizationOutcomingDto;
import com.hospital.management.model.dto.hospitalization.HospitalizationParams;
import com.hospital.management.model.dto.hospitalization.HospitalizationRequest;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.repository.EmployeeRepository;
import com.hospital.management.repository.HospitalizationRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.service.util.HospitalizationServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalizationServiceImpl implements HospitalizationServiceUtil {

    @Autowired
    HospitalizationRepository hospitalizationRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Page<Hospitalization> findByFilter(HospitalizationParams hospitalizationParams, Pageable pageable) {
        Specification<Hospitalization> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hospitalizationParams.getSearch() != null) {
                Expression<String> searchExpresion = criteriaBuilder.concat(
                        criteriaBuilder.concat(root.join("patient").get("lastName"), " "), root.join("patient").get("firstName"));
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(searchExpresion), "%" + hospitalizationParams.getSearch().toLowerCase() + "%")));
            }

            if (hospitalizationParams.getDoctorId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("employee").get("employeeId"), hospitalizationParams.getDoctorId()));
            }

            if (hospitalizationParams.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Hospitalization_.startDate), hospitalizationParams.getStartDate()));
            }

            if (hospitalizationParams.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Hospitalization_.endDate), hospitalizationParams.getEndDate()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
        return hospitalizationRepository.findAll(specification, pageable);
    }

    @Override
    public List<?> mapEntityListToDtoList(Page<Hospitalization> hospitalizations, Class<HospitalizationOutcomingDto> hospitalizationOutcomingDtoClass) {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<Hospitalization, HospitalizationOutcomingDto> propertyMapper = modelMapper.createTypeMap(Hospitalization.class, HospitalizationOutcomingDto.class);

        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getDepartment().getDepartmentId(), HospitalizationOutcomingDto::setDepartmentId));
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getDoctor().getEmployeeId(), HospitalizationOutcomingDto::setDoctorId));
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getDoctor().getUser().getUsername(), HospitalizationOutcomingDto::setDoctorName));
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getDepartment().getDepartmentName(), HospitalizationOutcomingDto::setDepartmentName));
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getPatient().getFirstName(), HospitalizationOutcomingDto::setFirstNamePatient));
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getPatient().getLastName(), HospitalizationOutcomingDto::setLastNamePatient));


        return ResponseUtils.mapEntityListToDtoList(modelMapper, hospitalizations, hospitalizationOutcomingDtoClass);
    }

    @Override
    public void add(HospitalizationRequest hospitalizationRequest) throws AppointmentFieldsException {
        Employee doctor = employeeRepository.findByUserId(hospitalizationRequest.getDoctorId());

        if (doctor.getDepartment().getDepartmentId() == null || hospitalizationRequest.getStartDate() == null || hospitalizationRequest.getDoctorId() == null) {
            throw new AppointmentFieldsException("some_fields_empty");
        }

        Hospitalization hospitalization = new Hospitalization();
        Department department = departmentRepository.findByDepartmentId(doctor.getDepartment().getDepartmentId());
        User patient = userRepository.findByUserId(hospitalizationRequest.getPatientId());

        hospitalization.setStartDate(hospitalizationRequest.getStartDate());
        hospitalization.setEndDate(hospitalizationRequest.getEndDate());
        hospitalization.setDepartment(department);
        hospitalization.setDoctor(doctor);
        hospitalization.setPatient(patient);
        hospitalization.setDiagnosis(hospitalizationRequest.getDiagnosis());
        hospitalizationRepository.save(hospitalization);
    }
}