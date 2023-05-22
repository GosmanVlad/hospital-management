package com.hospital.management.service.util;

import com.hospital.management.model.dto.raport.CardRaportOutcomingDto;
import com.hospital.management.model.dto.raport.InvoiceRaportOutcomingDto;
import com.hospital.management.model.dto.raport.RaportParams;

import java.util.List;

public interface RaportServiceUtil {
    List<InvoiceRaportOutcomingDto> getInvoicesRaport(RaportParams raportParams);

    CardRaportOutcomingDto getCardsRaport(RaportParams raportParams);

    CardRaportOutcomingDto getCardsRaportForPatient(RaportParams raportParams);
}
