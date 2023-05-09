package com.hospital.management.model.dto.auth;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class RegisterRequest {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Size(min = 2, max = 75)
    private String password;
    @NotEmpty
    @Size(min = 5, max = 255)
    @Email(message = "Customer email should be valid")
    private String email;
    private String role;
    private String personalNumber;
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String homeAddress;
    private String city;
    private String country;
}
