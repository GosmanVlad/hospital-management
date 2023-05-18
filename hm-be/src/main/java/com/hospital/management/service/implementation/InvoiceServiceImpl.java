package com.hospital.management.service.implementation;

import com.hospital.management.model.*;
import com.hospital.management.model.dto.hospitalization.HospitalizationOutcomingDto;
import com.hospital.management.model.dto.invoice.InvoiceOutcomingDto;
import com.hospital.management.model.dto.invoice.InvoiceParams;
import com.hospital.management.model.dto.invoice.InvoiceRequest;
import com.hospital.management.repository.EmployeeRepository;
import com.hospital.management.repository.InvoiceItemRepository;
import com.hospital.management.repository.InvoiceRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.service.util.InvoiceServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceServiceUtil {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceItemRepository invoiceItemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Page<Invoice> findByFilter(InvoiceParams invoiceParams, Pageable pageable) {
        Specification<Invoice> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (invoiceParams.getPatientId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("patient").get("userId"), invoiceParams.getPatientId()));
            }

            if (invoiceParams.getDoctorId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("employee").get("employeeId"), invoiceParams.getDoctorId()));
            }

            if (invoiceParams.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Invoice_.date), invoiceParams.getStartDate()));
            }

            if (invoiceParams.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Invoice_.date), invoiceParams.getEndDate()));
            }

            if (invoiceParams.getSearch() != null) {
                Expression<String> searchExpresion = criteriaBuilder.concat(
                        criteriaBuilder.concat(root.join("patient").get("lastName"), " "), root.join("patient").get("firstName"));
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(searchExpresion), "%" + invoiceParams.getSearch().toLowerCase() + "%")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
        return invoiceRepository.findAll(specification, pageable);
    }

    @Override
    public List<Invoice> findByFilter(InvoiceParams invoiceParams) {
        Specification<Invoice> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (invoiceParams.getPatientId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("patient").get("userId"), invoiceParams.getPatientId()));
            }

            if (invoiceParams.getDoctorId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("employee").get("employeeId"), invoiceParams.getDoctorId()));
            }

            if (invoiceParams.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Invoice_.date), invoiceParams.getStartDate()));
            }

            if (invoiceParams.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Invoice_.date), invoiceParams.getEndDate()));
            }

            if (invoiceParams.getSearch() != null) {
                Expression<String> searchExpresion = criteriaBuilder.concat(
                        criteriaBuilder.concat(root.join("patient").get("lastName"), " "), root.join("patient").get("firstName"));
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(searchExpresion), "%" + invoiceParams.getSearch().toLowerCase() + "%")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
        return invoiceRepository.findAll(specification);
    }

    @Override
    public List<?> mapEntityListToDtoList(Page<Invoice> invoices, Class<InvoiceOutcomingDto> invoiceOutcomingDtoClass) {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<Invoice, InvoiceOutcomingDto> propertyMapper = modelMapper.createTypeMap(Invoice.class, InvoiceOutcomingDto.class);

        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getEmployee().getUser().getUsername(), InvoiceOutcomingDto::setDoctorName));
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getPatient().getFirstName(), InvoiceOutcomingDto::setFirstNamePatient));
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getPatient().getLastName(), InvoiceOutcomingDto::setLastNamePatient));


        return ResponseUtils.mapEntityListToDtoList(modelMapper, invoices, invoiceOutcomingDtoClass);
    }

    @Override
    public void add(InvoiceRequest invoiceRequest) {
        Invoice invoice = new Invoice();
        invoice.setDate(invoiceRequest.getDate());
        invoice.setVatPercentage(invoiceRequest.getVatPercentage());

        Employee doctor = employeeRepository.findByEmployeeId(invoiceRequest.getDoctorId());
        if(doctor != null) {
            invoice.setEmployee(doctor);
        }

        User patient = userRepository.findByUserId(invoiceRequest.getPatientId());
        if(patient != null) {
            invoice.setPatient(patient);
        }

        invoiceRepository.save(invoice);

        List<InvoiceItem> invoiceItems = new ArrayList<>();
        /* Add items */
        for(InvoiceItem invoiceItem : invoiceRequest.getInvoiceItems()) {
            InvoiceItem invoiceService = new InvoiceItem();

            invoiceService.setService(invoiceItem.getService());
            invoiceService.setBrutCost(invoiceItem.getBrutCost());
            invoiceService.setVatPercentage(invoiceItem.getVatPercentage());
            invoiceService.setInvoice(invoice);

            invoiceItemRepository.save(invoiceService);
            invoiceItems.add(invoiceService);
        }
        invoice.setInvoiceItems(invoiceItems);
        invoiceRepository.save(invoice);
    }

    @Override
    public Invoice findByInvoiceId(Long invoiceId) {
        return invoiceRepository.findByInvoiceId(invoiceId);
    }

    @Override
    public Context mapThymeleafVariables(Invoice invoice) {
        Context context = new Context();
        double[] totalValue = {0.0};
        DecimalFormat df = new DecimalFormat("0.00");

        List<Context> invoiceItems = mapProductsThymeleafVariables(invoice, totalValue);

        context.setVariable("invoiceNumber", invoice.getInvoiceId());
        context.setVariable("invoiceDate", invoice.getDate());
        context.setVariable("patientName", invoice.getPatient().getFirstName() + " " + invoice.getPatient().getLastName());
        context.setVariable("patientPhoneNumber", invoice.getPatient().getPhone());
        context.setVariable("patientCNP", invoice.getPatient().getPersonalNumber());
        context.setVariable("invoiceItems", invoiceItems);
        context.setVariable("bannerPath", "https://static.vecteezy.com/system/resources/thumbnails/000/490/784/small/cardio_logo_02.jpg");
        context.setVariable("total", df.format(totalValue[0]) + " RON");
        context.setVariable("doctorName", invoice.getEmployee().getUser().getFirstName() + " " + invoice.getEmployee().getUser().getLastName());
        context.setVariable("doctorPhone", invoice.getEmployee().getUser().getPhone());
        context.setVariable("doctorDepartment", invoice.getEmployee().getDepartment().getDepartmentName());
        return context;
    }

    private List<Context> mapProductsThymeleafVariables(Invoice invoice, double[] totalValue) {
        List<Context> invoiceItemsContext = new ArrayList<>();
        double total = 0.0;
        int number = 0;

        Collection<InvoiceItem> invoiceItems = invoice.getInvoiceItems();
        for (InvoiceItem item : invoiceItems) {
            number++;
            Context invoiceItemContext = new Context();

            invoiceItemContext.setVariable("number", number);
            invoiceItemContext.setVariable("service", item.getService());
            invoiceItemContext.setVariable("value", item.getBrutCost());
            invoiceItemContext.setVariable("valueTVA", (double) Math.round((double) invoice.getVatPercentage() / 100 * item.getBrutCost() * 100)/100);
            invoiceItemContext.setVariable("tvaPercent", invoice.getVatPercentage() + "%");
            invoiceItemsContext.add(invoiceItemContext);

            total = total + item.getBrutCost();
        }
        totalValue[0] = total;
        return invoiceItemsContext;
    }
}
