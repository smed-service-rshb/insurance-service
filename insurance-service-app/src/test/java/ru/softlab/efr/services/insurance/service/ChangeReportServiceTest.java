package ru.softlab.efr.services.insurance.service;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.exception.EmptyReportException;
import ru.softlab.efr.services.insurance.model.enums.ExtractType;
import ru.softlab.efr.services.insurance.services.ExtractCreateProcessResult;
import ru.softlab.efr.services.insurance.services.ExtractProcessInfoService;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class ChangeReportServiceTest {

    @Autowired
    private ChangesReportService changesReportService;

    @Autowired
    private ExtractProcessInfoService extractProcessInfoService;

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void generateReport() throws IOException, EmptyReportException {

        LocalDateTime start = LocalDate.of(2017, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(2099, 2, 4).atStartOfDay();

        String fileName = changesReportService.getFileName();
        ExtractCreateProcessResult processInfo = extractProcessInfoService.createProcessInfo(
                ExtractType.CHANGE_REPORT, fileName, null);

        byte[] byteArray = changesReportService
                .getChangeReportByCreateDateOutputStream(start, end, processInfo.getExtract().getUuid());

        FileUtils.writeByteArrayToFile(new File("target/".concat(fileName)), byteArray);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("target/".concat(fileName)))) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Row row = sheet.getRow(3);
            assertEquals("1", dataFormatter.formatCellValue(row.getCell(0)));
            assertEquals("23К010110000001", dataFormatter.formatCellValue(row.getCell(1)));
            assertEquals("12.12.2018", dataFormatter.formatCellValue(row.getCell(2)));
            assertEquals("Иванов Иван Иванович", dataFormatter.formatCellValue(row.getCell(3)));
            assertEquals("01.01.2018", dataFormatter.formatCellValue(row.getCell(4)));
            assertEquals("12:12:12", dataFormatter.formatCellValue(row.getCell(5)));
        } finally {
            File file = new File(fileName);
            file.delete();
        }
    }
}