package com.hospital.management.service.implementation;

import com.hospital.management.model.Appointment;
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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class AppointmentExcelExporterService {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Appointment> appointments;

    public AppointmentExcelExporterService(List<Appointment> appointments) {
        this.appointments = appointments;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Appointments");

        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Numar programare", style);
        createCell(row, 1, "Nume aplicant", style);
        createCell(row, 2, "Data", style);
        createCell(row, 3, "Detalii", style);
        createCell(row, 4, "Status", style);

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

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        for (Appointment appointment : appointments) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, appointment.getAppointmentId(), style);
            createCell(row, columnCount++, appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName(), style);
            createCell(row, columnCount++, dateFormat.format(appointment.getDate()), style);
            createCell(row, columnCount++, appointment.getDetails(), style);
            createCell(row, columnCount++, appointment.getStatus(), style);
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
