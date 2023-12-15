package ru.softlab.efr.services.insurance.service;

import org.apache.commons.io.FileUtils;
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
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.services.ShareService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class ShareServiceTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    private ShareService shareService;

    @Autowired
    private InsuranceService insuranceService;

    // у договора с id 3 установлена стратегия с id 2 Цифровое будущее type = COUPON
    // дата начала действия договора равна 2018-12-04 список акций должен начинаться с этой даты или с ближайшего большего значения
    // даты должны идти в порядке возростания
    // cо стратегией договора связано 2 акции (FUTURE1 - 4 котировки, FUTURE2 - 1 котировка)
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void generateReport() throws IOException {

        Insurance insurance = insuranceService.findById(3);
        byte[] excelReport = shareService.getExcelReport(insurance);
        String title = String.format("Отчет о котировках акций за период: %s - %s",
                insurance.getStartDate().format(FORMATTER), LocalDate.now().format(FORMATTER));

        FileUtils.writeByteArrayToFile(new File("target/".concat("Отчет о котировках акций.xlsx")), excelReport);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("target/".concat("Отчет о котировках акций.xlsx")))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(title, sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("Дата", sheet.getRow(2).getCell(0).getStringCellValue());
            assertEquals("FUTURE1", sheet.getRow(2).getCell(1).getStringCellValue());
            assertEquals("FUTURE2", sheet.getRow(2).getCell(2).getStringCellValue());
            assertEquals("04.12.2018", sheet.getRow(3).getCell(0).getStringCellValue());
            assertEquals("05.12.2018", sheet.getRow(4).getCell(0).getStringCellValue());
            assertEquals("06.12.2018", sheet.getRow(5).getCell(0).getStringCellValue());
            assertEquals("18.12.2018", sheet.getRow(17).getCell(0).getStringCellValue());
            assertEquals(10.0, sheet.getRow(17).getCell(1).getNumericCellValue(), 0.001);
            assertEquals("", sheet.getRow(17).getCell(2).getStringCellValue());
            assertEquals("10.02.2019", sheet.getRow(71).getCell(0).getStringCellValue());
            assertEquals(10.0, sheet.getRow(71).getCell(1).getNumericCellValue(), 0.001);
            assertEquals(10.0, sheet.getRow(71).getCell(2).getNumericCellValue(), 0.001);
            assertEquals("11.02.2019", sheet.getRow(72).getCell(0).getStringCellValue());
            assertEquals(11.0, sheet.getRow(72).getCell(1).getNumericCellValue(), 0.001);
            assertEquals("12.02.2019", sheet.getRow(73).getCell(0).getStringCellValue());
            assertEquals(12.0, sheet.getRow(73).getCell(1).getNumericCellValue(), 0.001);
        } finally {
            File file = new File("Отчет о котировках акций.xlsx");
            file.delete();
        }
    }
}
