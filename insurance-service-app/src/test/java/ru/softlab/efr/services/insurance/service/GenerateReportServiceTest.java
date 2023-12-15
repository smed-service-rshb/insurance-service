package ru.softlab.efr.services.insurance.service;

import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.ExtractType;
import ru.softlab.efr.services.insurance.repositories.ClientCheckRepository;
import ru.softlab.efr.services.insurance.services.ExtractCreateProcessResult;
import ru.softlab.efr.services.insurance.services.ExtractProcessInfoService;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.USER_PRINCIPAL_DATA;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class GenerateReportServiceTest {

    private static final String GET_GENERATE_NON_RESIDENT_REPORT = "/insurance-service/v2/non-resident-report/generate?startDate={startDate}&endDate={endDate}";

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Rule
    @Autowired
    public HibernateCacheInvalidationRule hibernateCacheInvalidationRule;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private ClientCheckRepository repository;
    @Autowired
    private GenerateReportService generateReportService;
    @Autowired
    private OnNonResidentService nonResidentService;
    @Autowired
    private ExtractProcessInfoService extractProcessInfoService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createReportForTerrorists() throws IOException {
        SQLStatementCountValidator.reset();
        String fileNameForTerrorist = "Проверка по справочнику экстремистов";
        fileNameForTerrorist = String.format("%s %s %s", fileNameForTerrorist,
                GenerateReportService.presentLocalDateTime(LocalDateTime.now()),
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm")));
        ByteArrayOutputStream byteArrayOutputStream = generateReportService.createReportForTerrorists(null);
        String fileName = generateReportService.fileName(CheckUnitTypeEnum.TERRORIST, false);
        assertEquals(fileNameForTerrorist, fileName);

        try (OutputStream outputStream = new FileOutputStream(generateReportService.fileName(CheckUnitTypeEnum.TERRORIST, false) + ".xlsx")) {
            byteArrayOutputStream.writeTo(outputStream);
        }
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(fileNameForTerrorist + ".xlsx"))) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Row row = sheet.getRow(3);
            assertEquals("1", dataFormatter.formatCellValue(row.getCell(0)));
            assertEquals("Иванов", dataFormatter.formatCellValue(row.getCell(1)));
            assertEquals("Иван", dataFormatter.formatCellValue(row.getCell(2)));
            assertEquals("Иванович", dataFormatter.formatCellValue(row.getCell(3)));
            assertEquals("30.03.1986", dataFormatter.formatCellValue(row.getCell(4)));
            assertEquals("Российская Федерация", dataFormatter.formatCellValue(row.getCell(5)));
        } finally {
            File file = new File(fileNameForTerrorist + ".xlsx");
            file.delete();
        }
        assertSelectCount(2);
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void generateReportOnNonResident() throws IOException {
        SQLStatementCountValidator.reset();
        LocalDate startDate = LocalDate.parse("2018-06-01");
        LocalDate endDate = LocalDate.parse("2019-01-29");
        String fileName = nonResidentService.getFileName();
        ExtractCreateProcessResult processInfo = extractProcessInfoService.createProcessInfo(
                ExtractType.CHANGE_REPORT, fileName, null);
        byte[] byteArray = nonResidentService.generateReportForNonResident(
                startDate.atStartOfDay(), endDate.atTime(23, 59, 59), processInfo.getExtract().getUuid());

        FileUtils.writeByteArrayToFile(new File("target/".concat(fileName)), byteArray);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("target/".concat(fileName)))) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Row row = sheet.getRow(4);
            assertEquals("Иванов Иван Петрович", dataFormatter.formatCellValue(row.getCell(0)));
            assertEquals("23К010110000002", dataFormatter.formatCellValue(row.getCell(1)));
            assertEquals("12.12.2018", dataFormatter.formatCellValue(row.getCell(2)));
            assertEquals("(не указано)", dataFormatter.formatCellValue(row.getCell(3)));
            assertEquals("10000,00", dataFormatter.formatCellValue(row.getCell(4)));
            assertEquals("(не указано)", dataFormatter.formatCellValue(row.getCell(5)));
        } finally {
            File file = new File("target/".concat(fileName));
            file.delete();
        }

        assertSelectCount(9);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/non-resident-report?startDate={startDate}&endDate={endDate}
     * для проверки получения корректного ответа при отсутствии прав.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getGenerateNonResidentReportForbidden() throws Exception {
        SQLStatementCountValidator.reset();
        String startDate = "01.12.2018";
        String endDate = "29.01.2019";

        mockMvc.perform(get(GET_GENERATE_NON_RESIDENT_REPORT, startDate, endDate)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

        assertSelectCount(0);

    }
}
