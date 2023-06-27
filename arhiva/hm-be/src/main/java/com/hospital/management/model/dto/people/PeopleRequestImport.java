package com.hospital.management.model.dto.people;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PeopleRequestImport {
    private MultipartFile doc;
}
