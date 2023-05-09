package com.hospital.management.service.implementation;

import com.hospital.management.service.util.PDFGeneratorServiceUtil;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;

@Service
public class PDFGeneratorServiceImpl implements PDFGeneratorServiceUtil {
    @Value("${pdfGenerator.path}")
    private String pdfGenerator;

    @Value("${thymeleafTemplates.path}")
    private String thymeleafTemplates;

    @Override
    public String parseThymeleafTemplate(String templateFileName, Context mappedData) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setPrefix(this.thymeleafTemplates);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setOrder(0);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(templateFileName, mappedData);
    }

    @Override
    public void generatePdfFromHtml(String html, String fileName) throws FileNotFoundException, IOException, DocumentException {
        String outputFolder = this.pdfGenerator + File.separator + fileName + ".pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);
        ITextRenderer renderer = new ITextRenderer();

        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }
}
