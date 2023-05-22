package com.hospital.management.service.implementation;

import com.hospital.management.model.Invoice;
import com.hospital.management.model.InvoiceItem;
import com.hospital.management.model.dto.raport.InvoiceRaportOutcomingDto;
import com.hospital.management.model.dto.raport.RaportParams;
import com.hospital.management.repository.InvoiceRepository;
import com.hospital.management.service.util.InvoiceServiceUtil;
import com.hospital.management.service.util.RaportServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RaportServiceImpl implements RaportServiceUtil {

    @Autowired
    InvoiceRepository invoiceRepository;

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
            List<Invoice> invoices = invoiceRepository.findByDateBetweenAndDoctorId(raportParams.getDoctorId(), startDate, endDate);

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

                    if(invoice.getStatus().equals("pending")) {
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
}
