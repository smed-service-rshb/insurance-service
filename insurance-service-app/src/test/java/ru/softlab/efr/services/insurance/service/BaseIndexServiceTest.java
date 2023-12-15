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
import ru.softlab.efr.services.insurance.services.StrategyBaseIndexService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class BaseIndexServiceTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    private StrategyBaseIndexService baseIndexService;

    @Autowired
    private InsuranceService insuranceService;

    // у договора с id 2 установлена стратегия с id 1 Глобальные облигации type = CLASSIC c двумя наборами настроек
    // первый набор для получения базовых индексов ссылается на стратегию Глобальные облигации
    // второй набор для получения базовых индексов ссылается на стратегию Европейские лидеры
    // дата начала действия договора равна 2018-11-27 список динамики дохода должен начинаться с этой даты или с ближайшего значения
    // даты должны идти в порядке возростания
    // бд присутствуют только 5 записей с индексами для стратегии Глобальные облигации
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void generateReport() throws IOException {

        Insurance insurance = insuranceService.findById(2);
        byte[] excelReport = baseIndexService.getExcelReport(insurance, insurance.getStrategy());
        String title = String.format("Отчет по базовым активам за период: %s - %s",
                insurance.getStartDate().format(FORMATTER), LocalDate.now().format(FORMATTER));

        FileUtils.writeByteArrayToFile(new File("target/".concat("Отчет по базовым активам за период.xlsx")), excelReport);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("target/".concat("Отчет по базовым активам за период.xlsx")))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(title, sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("Дата", sheet.getRow(2).getCell(0).getStringCellValue());
            assertEquals("Значение базового актива", sheet.getRow(2).getCell(1).getStringCellValue());
            assertEquals("17.12.2018", sheet.getRow(3).getCell(0).getStringCellValue());
            assertEquals(1368.16, sheet.getRow(3).getCell(1).getNumericCellValue(), 0.01);
            assertEquals("18.12.2018", sheet.getRow(4).getCell(0).getStringCellValue());
            assertEquals(1399.7, sheet.getRow(4).getCell(1).getNumericCellValue(), 0.01);
            assertEquals("27.12.2018", sheet.getRow(5).getCell(0).getStringCellValue());
            assertEquals(1367.61, sheet.getRow(5).getCell(1).getNumericCellValue(), 0.01);
            assertEquals("28.12.2018", sheet.getRow(6).getCell(0).getStringCellValue());
            assertEquals(1368.0, sheet.getRow(6).getCell(1).getNumericCellValue(), 0.01);
            assertEquals("29.12.2018", sheet.getRow(7).getCell(0).getStringCellValue());
            assertEquals(1399.7, sheet.getRow(7).getCell(1).getNumericCellValue(), 0.01);
        } finally {
            File file = new File("Отчет по базовым активам за период.xlsx");
            file.delete();
        }
    }
}
