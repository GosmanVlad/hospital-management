package com.hospital.management.model.dto.auth;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class ChangePasswordRequest {
    @NotEmpty
    private String password;
    @NotEmpty
    private String repeatPassword;
}
