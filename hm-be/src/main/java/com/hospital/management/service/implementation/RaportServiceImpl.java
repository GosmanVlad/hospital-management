package com.hospital.management.service.implementation;

import com.hospital.management.model.*;
import com.hospital.management.model.dto.raport.CardRaportOutcomingDto;
import com.hospital.management.model.dto.raport.InvoiceRaportOutcomingDto;
import com.hospital.management.model.dto.raport.PieRaportOutcomingDto;
import com.hospital.management.model.dto.raport.RaportParams;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.HospitalizationRepository;
import com.hospital.management.repository.InvoiceRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.service.util.RaportServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RaportServiceImpl implements RaportServiceUtil {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    HospitalizationRepository hospitalizationRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<InvoiceRaportOutcomingDto> getInvoicesRaport(RaportParams raportParams) {
        List<InvoiceRaportOutcomingDto> invoiceRaportOutcomingDtoList = new ArrayList<>();
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Bucharest");
        Calendar calendar = Calendar.getInstance(timeZone);
        int year = calendar.get(Calendar.YEAR);

        calendar.clear();
        calendar.set(Calendar.YEAR, year);

        for (int currentMonth = 0; currentMonth < 12; currentMonth++) {
            calendar.set(Calendar.MONTH, currentMonth);

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date startDate = calendar.getTime();

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            InvoiceRaportOutcomingDto invoiceRaportOutcomingDto = new InvoiceRaportOutcomingDto();
            List<Invoice> invoices = new ArrayList<>();

            if(raportParams.getDoctorId() != null) {
                invoices = invoiceRepository.findByDateBetweenAndDoctorId(raportParams.getDoctorId(), startDate, endDate);
            } else if(raportParams.getPatientId() != null) {
                invoices = invoiceRepository.findByDateBetweenAndPatientId(raportParams.getPatientId(), startDate, endDate);
            }

            Long paidInvoice = 0L;
            Long unpaidInvoice = 0L;
            Long pendingInvoice = 0L;

            for(Invoice invoice : invoices) {
                for(InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                    if(invoice.getStatus().equals("paid")) {
                        paidInvoice = paidInvoice + invoiceItem.getBrutCost();
                    }

                    if(invoice.getStatus().equals("unpaid")) {
                        unpaidInvoice = unpaidInvoice + invoiceItem.getBrutCost();
                    }

                    if(invoice.getStatus().equals("canceled")) {
                        pendingInvoice = pendingInvoice + invoiceItem.getBrutCost();
                    }
                }
            }

            invoiceRaportOutcomingDto.setDate(startDate);
            invoiceRaportOutcomingDto.setPaidInvoices(paidInvoice);
            invoiceRaportOutcomingDto.setUnpaidInvoices(unpaidInvoice);
            invoiceRaportOutcomingDto.setPendingInvoices(pendingInvoice);
            invoiceRaportOutcomingDtoList.add(invoiceRaportOutcomingDto);
        }
        return invoiceRaportOutcomingDtoList;
    }

    @Override
    public CardRaportOutcomingDto getCardsRaport(RaportParams raportParams) {
        List<Hospitalization> hospitalizations = hospitalizationRepository.findByDoctorId(raportParams.getDoctorId());
        List<Appointment> appointments = appointmentRepository.findByDoctorId(raportParams.getDoctorId());
        List<User> users = userRepository.findAllPatients();
        List<Invoice> invoices = invoiceRepository.findByDoctorId(raportParams.getDoctorId());

        CardRaportOutcomingDto cardRaportOutcomingDto = new CardRaportOutcomingDto();
        cardRaportOutcomingDto.setAppointments(appointments.size());
        cardRaportOutcomingDto.setHospitalizations(hospitalizations.size());
        cardRaportOutcomingDto.setPatients(users.size());
        cardRaportOutcomingDto.setInvoices(invoices.size());

        return cardRaportOutcomingDto;
    }

    @Override
    public CardRaportOutcomingDto getCardsRaportForPatient(RaportParams raportParams) {
        List<Hospitalization> hospitalizations = hospitalizationRepository.findByPatientId(raportParams.getPatientId());
        List<Appointment> appointments = appointmentRepository.findByPatientId(raportParams.getPatientId());
        List<Invoice> invoices = invoiceRepository.findByPatientIdAndStatusUnpaid(raportParams.getPatientId());

        CardRaportOutcomingDto cardRaportOutcomingDto = new CardRaportOutcomingDto();
        cardRaportOutcomingDto.setAppointments(appointments.size());
        cardRaportOutcomingDto.setHospitalizations(hospitalizations.size());
        cardRaportOutcomingDto.setInvoices(invoices.size());

        return cardRaportOutcomingDto;
    }

    @Override
    public PieRaportOutcomingDto getPieRaport(RaportParams raportParams) {
        PieRaportOutcomingDto pieRaportOutcomingDto = new PieRaportOutcomingDto();
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Bucharest");
        Calendar calendar = Calendar.getInstance(timeZone);
        int year = calendar.get(Calendar.YEAR);

        calendar.clear();
        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        Date endDate = calendar.getTime();

        List<Hospitalization> hospitalizations = hospitalizationRepository.findByEmployeeAndDateBetween(raportParams.getDoctorId(), startDate, endDate);
        List<Appointment> appointments = appointmentRepository.findByEmployeeAndDateBetween(raportParams.getDoctorId(), startDate, endDate);

        pieRaportOutcomingDto.setAppointments(appointments.size());
        pieRaportOutcomingDto.setHospitalizations(hospitalizations.size());
        return pieRaportOutcomingDto;
    }

    @Override
    public PieRaportOutcomingDto getPieRaportForNurse() {
        PieRaportOutcomingDto pieRaportOutcomingDto = new PieRaportOutcomingDto();
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Bucharest");
        Calendar calendar = Calendar.getInstance(timeZone);
        int year = calendar.get(Calendar.YEAR);

        calendar.clear();
        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        Date endDate = calendar.getTime();

        List<Hospitalization> hospitalizations = hospitalizationRepository.findDateBetween(startDate, endDate);
        List<Appointment> appointments = appointmentRepository.findDateBetween(startDate, endDate);

        pieRaportOutcomingDto.setAppointments(appointments.size());
        pieRaportOutcomingDto.setHospitalizations(hospitalizations.size());
        return pieRaportOutcomingDto;
    }

}
