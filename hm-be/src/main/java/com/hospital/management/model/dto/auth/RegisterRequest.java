package com.hospital.management.model.dto.auth;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    @NotEmpty
    @Size(min = 2, max = 75)
    private String firstName;
    @NotEmpty
    @Size(min = 2, max = 75)
    private String lastName;
    @Size(min = 2, max = 75)
    private String password;
    @NotEmpty
    @Size(min = 5, max = 255)
    @Email(message = "Customer email should be valid")
    private String email;
    private String username;
    private String role;
}
