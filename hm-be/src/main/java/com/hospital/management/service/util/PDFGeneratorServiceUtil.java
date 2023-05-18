package com.hospital.management.service.util;

import com.lowagie.text.DocumentException;
import org.thymeleaf.context.Context;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface PDFGeneratorServiceUtil {
    String parseThymeleafTemplate(String templateFileName, Context mappedData);
    String generatePdfFromHtml(String html, String fileName, String type) throws FileNotFoundException, IOException, DocumentException;
}
