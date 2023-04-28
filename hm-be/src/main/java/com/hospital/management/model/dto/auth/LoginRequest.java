package com.hospital.management.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @JsonProperty(required = true)
    @Size(min = 5, max = 255)
    @Email(message = "Customer email should be valid")
    private String email;
    @JsonProperty(required = true)
    @Size(min = 5, max = 255)
    private String password;
}
