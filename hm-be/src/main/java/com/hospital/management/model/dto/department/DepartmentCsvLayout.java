package com.hospital.management.model.dto.department;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class DepartmentCsvLayout {

    @CsvBindByName(column = "name")
    private String departmentName;
}
