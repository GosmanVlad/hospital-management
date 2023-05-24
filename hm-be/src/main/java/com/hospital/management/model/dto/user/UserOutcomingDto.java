package com.hospital.management.model.dto.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserOutcomingDto {
    private Date createdDate;
    private String email;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String city;
    private String country;
    private String homeAddress;
    private String personalNumber;
    private String phone;
}
