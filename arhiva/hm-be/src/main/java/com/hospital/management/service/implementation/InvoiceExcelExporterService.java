package com.hospital.management.service.implementation;

import com.hospital.management.model.Hospitalization;
import com.hospital.management.model.Invoice;
import com.hospital.management.model.InvoiceItem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InvoiceExcelExporterService {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Invoice> invoices;

    public InvoiceExcelExporterService(List<Invoice> invoices) {
        this.invoices = invoices;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Facturi");

        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Numar factura", style);
        createCell(row, 1, "Data emiterii", style);
        createCell(row, 2, "Doctor", style);
        createCell(row, 3, "Pacient", style);
        createCell(row, 4, "Total factura", style);
        createCell(row, 5, "TVA", style);
        createCell(row, 6, "Incasat", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        try {
            sheet.autoSizeColumn(columnCount);

            Cell cell = row.createCell(columnCount);

            if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else {
                if (value instanceof Date) {
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    cell.setCellValue(dateFormat.format(value));
                } else if(value instanceof Long) {
                    cell.setCellValue(String.valueOf(value));
                } else
                    cell.setCellValue((String) value);
            }

            cell.setCellStyle(style);
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Invoice invoice : invoices) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, invoice.getInvoiceId(), style);
            createCell(row, columnCount++, invoice.getDate(), style);
            createCell(row, columnCount++, invoice.getEmployee().getUser().getFirstName() + " " + invoice.getEmployee().getUser().getLastName(), style);
            createCell(row, columnCount++, invoice.getPatient().getFirstName() + " " + invoice.getPatient().getLastName(), style);

            Long totalInvoice = 0L;
            for(InvoiceItem invoiceItem: invoice.getInvoiceItems()) {
                totalInvoice += invoiceItem.getBrutCost();
            }

            createCell(row, columnCount++, totalInvoice, style);
            createCell(row, columnCount++, invoice.getVatPercentage() + "% (" + (double) Math.round((double) invoice.getVatPercentage() / 100 * totalInvoice * 100)/100 + ")", style);
            createCell(row, columnCount++, totalInvoice - ((double) Math.round((double) invoice.getVatPercentage() / 100 * totalInvoice * 100)/100), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
