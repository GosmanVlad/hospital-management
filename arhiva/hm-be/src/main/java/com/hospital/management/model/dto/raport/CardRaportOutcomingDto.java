package com.hospital.management.model.dto.raport;

import lombok.Data;

@Data
public class CardRaportOutcomingDto {
    private int patients;
    private int invoices;
    private int hospitalizations;
    private int appointments;
}
