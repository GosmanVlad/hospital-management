package com.hospital.management.service.implementation;

import com.hospital.management.exception.appointment.AppointmentFieldsException;
import com.hospital.management.exception.salon.SalonSeatsException;
import com.hospital.management.model.*;
import com.hospital.management.model.dto.email.EmailData;
import com.hospital.management.model.dto.hospitalization.HospitalizationOutcomingDto;
import com.hospital.management.model.dto.hospitalization.HospitalizationParams;
import com.hospital.management.model.dto.hospitalization.HospitalizationRequest;
import com.hospital.management.repository.*;
import com.hospital.management.service.util.EmailServiceUtil;
import com.hospital.management.service.util.HospitalizationServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    @Autowired
    SalonRepository salonRepository;

    @Autowired
    EmailServiceUtil emailService;

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

            if (hospitalizationParams.getPatientId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("patient").get("userId"), hospitalizationParams.getPatientId()));
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
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getSalon().getSalonName(), HospitalizationOutcomingDto::setSalonName));


        return ResponseUtils.mapEntityListToDtoList(modelMapper, hospitalizations, hospitalizationOutcomingDtoClass);
    }

    @Override
    public void add(HospitalizationRequest hospitalizationRequest) throws AppointmentFieldsException, SalonSeatsException {
        Employee doctor = employeeRepository.findByUserId(hospitalizationRequest.getDoctorId());
        Salon salon = salonRepository.findBySalonId(hospitalizationRequest.getSalonId());

        if (salon == null || doctor.getDepartment().getDepartmentId() == null || hospitalizationRequest.getStartDate() == null || hospitalizationRequest.getDoctorId() == null) {
            throw new AppointmentFieldsException("some_fields_empty");
        }

        //Check salons
        List<Hospitalization> checkMaximulSeats = hospitalizationRepository.findBySalonIdAndDate(salon.getSalonId(), hospitalizationRequest.getStartDate());
        if(checkMaximulSeats.size() + 1 > salon.getSeats()) {
            throw new SalonSeatsException("no_more_seats");
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
        hospitalization.setSalon(salon);
        hospitalizationRepository.save(hospitalization);

        /* Send email to the patient */
        EmailData emailData = new EmailData();
        emailData.setSendTo(patient.getEmail());
        emailData.setSubject("[HM] Internare noua");
        emailData.setBody("Doctorul " + hospitalization.getDoctor().getUser().getLastName() + " " + hospitalization.getDoctor().getUser().getFirstName() + " tocmai a efectuat o internare.\n\n" +
                "Data internare: " + hospitalization.getStartDate() + "\n" +
                "Data externare: " + hospitalization.getEndDate() + "\n" +
                "Departament: " + hospitalization.getDepartment().getDepartmentName() + "\n" +
                "Diagnostic: " + hospitalization.getDiagnosis() + "\n" +
                "Salon: " + hospitalization.getSalon().getSalonName());
        emailService.sendMail(emailData);
    }

    @Override
    public List<Hospitalization> findByFilter(HospitalizationParams hospitalizationParams) {
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
        return hospitalizationRepository.findAll(specification);
    }

    @Override
    public Hospitalization findByHospitalizationId(Long hospitalizationId) {
        return hospitalizationRepository.findByHospitalizationId(hospitalizationId);
    }

    @Override
    public Context mapThymeleafVariables(Hospitalization hospitalization) {
        Context context = new Context();

        context.setVariable("hospitalizationId", hospitalization.getHospitalizationId());
        context.setVariable("date", hospitalization.getStartDate());
        context.setVariable("patient", hospitalization.getPatient().getFirstName() + " " + hospitalization.getPatient().getLastName());
        context.setVariable("startDate", hospitalization.getStartDate());
        context.setVariable("endDate", hospitalization.getEndDate());
        context.setVariable("doctor", hospitalization.getDoctor().getUser().getFirstName() + " " + hospitalization.getDoctor().getUser().getLastName());
        context.setVariable("department", hospitalization.getDepartment().getDepartmentName());
        context.setVariable("diagnosis", hospitalization.getDiagnosis());
        context.setVariable("cnp", hospitalization.getPatient().getPersonalNumber());
        context.setVariable("birthDate", hospitalization.getPatient().getBirthdate());
        context.setVariable("phone", hospitalization.getPatient().getPhone());
        context.setVariable("country", hospitalization.getPatient().getCountry());
        context.setVariable("city", hospitalization.getPatient().getCity());
        context.setVariable("address", hospitalization.getPatient().getHomeAddress());
        context.setVariable("doctorPhone", hospitalization.getDoctor().getUser().getPhone());
        context.setVariable("department", hospitalization.getDoctor().getDepartment().getDepartmentName());
        return context;
    }

    @Override
    public void updateHospitalizationDocPath(Long hospitalizationId, String path) {
        hospitalizationRepository.updateHospitalizationDocPath(hospitalizationId, path);
    }
}
