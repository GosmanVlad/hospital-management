package com.hospital.management.service.implementation;

import com.hospital.management.model.Hospitalization;
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
import java.util.UUID;

public class HospitalizationExcelExporterService {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Hospitalization> hospitalizations;
    public HospitalizationExcelExporterService(List<Hospitalization> messages) {
        this.hospitalizations = messages;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Hospitalization");

        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Numar internare", style);
        createCell(row, 1, "Pacient", style);
        createCell(row, 2, "Salon", style);
        createCell(row, 3, "Data internarii", style);
        createCell(row, 4, "Data externarii", style);
        createCell(row, 5, "Doctor", style);

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

        for (Hospitalization hospitalization : hospitalizations) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, hospitalization.getHospitalizationId(), style);
            createCell(row, columnCount++, hospitalization.getPatient().getFirstName() + " " + hospitalization.getPatient().getLastName(), style);
            createCell(row, columnCount++, hospitalization.getSalon().getSalonName(), style);
            createCell(row, columnCount++, hospitalization.getStartDate(), style);
            createCell(row, columnCount++, hospitalization.getEndDate(), style);
            createCell(row, columnCount++, hospitalization.getDoctor().getUser().getFirstName() + " " + hospitalization.getDoctor().getUser().getLastName(), style);
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
