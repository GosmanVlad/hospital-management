package com.hospital.management.service.util;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

public interface FileServiceUtil {
    String store(MultipartFile file, String path, String fileName);

    MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName);
    String createDirectoriesAndSaveFile(MultipartFile file);
}
