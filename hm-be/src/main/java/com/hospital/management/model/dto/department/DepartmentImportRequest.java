package com.hospital.management.model.dto.department;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DepartmentImportRequest {
    private MultipartFile doc;
}
