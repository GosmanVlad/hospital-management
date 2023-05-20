package com.hospital.management.model.dto.people;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.util.Date;

@Data
public class PeopleCsvLayout {

    @CsvBindByName(column = "firstName")
    private String firstName;

    @CsvBindByName(column = "lastName")
    private String lastName;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "phone")
    private String phone;

    @CsvDate(value = "yyyy-MM-dd")
    @CsvBindByName(column = "birthDate")
    private Date birthDate;

    @CsvBindByName(column = "country")
    private String country;

    @CsvBindByName(column = "city")
    private String city;

    @CsvBindByName(column = "homeAddress")
    private String homeAddress;

    @CsvBindByName(column = "personalNumber")
    private String personalNumber;

    @CsvBindByName(column = "role")
    private String role;

    @CsvBindByName(column = "departmentName")
    private String departmentName;
}
