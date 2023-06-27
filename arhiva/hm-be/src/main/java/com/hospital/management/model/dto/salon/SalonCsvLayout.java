package com.hospital.management.model.dto.salon;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class SalonCsvLayout {
    @CsvBindByName(column = "salonName")
    private String salonName;

    @CsvBindByName(column = "seats")
    private Long seats;

    @CsvBindByName(column = "departmentName")
    private String departmentName;
}
