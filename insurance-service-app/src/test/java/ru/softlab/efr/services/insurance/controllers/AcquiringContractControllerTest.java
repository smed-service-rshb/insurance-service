package ru.softlab.efr.services.insurance.controllers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.rest.AcquiringFindByCodeRq;
import ru.softlab.efr.services.insurance.model.rest.AcquiringInsuranceRq;
import ru.softlab.efr.services.insurance.model.rest.Gender;
import ru.softlab.efr.services.insurance.services.ClientService;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.services.StatusService;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;
import static ru.softlab.efr.services.insurance.stubs.TestData.CLIENT_PRINCIPAL_DATA;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class AcquiringContractControllerTest {

    private static final String GET_ACQUIRING_PROGRAM_LIST = "/insurance-service/public/v2/contract/acquiring/programs";
    private static final String POST_ACQUIRING_REGISTRATION = "/insurance-service/public/v2/contract/acquiring/registration";
    private static final String POST_ACQUIRING_ISSUE = "/insurance-service/public/v2/contract/acquiring/issue";
    private static final String GET_ALL_CONTRACT_REQUEST = "/insurance-service/v2/contracts";
    private static final String GET_PAYMENT_INFO = "/insurance-service/public/v2/contract/acquiring/info/{orderId}/order";
    private static final String GET_CONTRACT_BY_CODE = "/insurance-service/public/v2/contract/acquiring/findByCode";
    private static final int ALL_INSURANCE_COUNT = 6;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Autowired
    private ClientService clientService;

    @Rule
    @Autowired
    public HibernateCacheInvalidationRule hibernateCacheInvalidationRule;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    /**
     * Тест сервиса GET /insurance-service/v2/contract/acquiring/programs
     * для проверки получения списка программ для оформления в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAcquiringProgramNotAuthorizedZoneSuccess() throws Exception {

        mockMvc.perform(get(GET_ACQUIRING_PROGRAM_LIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[1].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].title", is("Наименование 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].name", is("Коробка")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].image", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].description", is("описание 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].link", is("http://rshbins.ru")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].canBay", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].maxAge", is(54)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].minAge", is(18)))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contract/acquiring/programs
     * для проверки получения списка программ для оформления в авторизованной зоне
     * список программ должен быть отфильтрован по возрасту клиента
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllAcquiringProgramAuthorizedZoneSuccess() throws Exception {

        mockMvc.perform(get(GET_ACQUIRING_PROGRAM_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[1].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].title", is("Наименование 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].name", is("Коробка")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].image", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].description", is("описание 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].link", is("http://rshbins.ru")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].canBay", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].maxAge", is(54)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].minAge", is(18)))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contract/acquiring/programs
     * для проверки получения списка программ для оформления в авторизованной зоне
     * недоступность программы по минимальному возрасту клиента
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllAcquiringProgramAuthorizedMinAgeNotAllowed() throws Exception {

        ClientEntity clientEntity = clientService.get(CLIENT_PRINCIPAL_DATA);
        clientEntity.setBirthDate(LocalDate.now());
        clientService.save(clientEntity);
        mockMvc.perform(get(GET_ACQUIRING_PROGRAM_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(0)))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contract/acquiring/programs
     * для проверки получения списка программ для оформления в авторизованной зоне
     * недоступность программы по минимальному возрасту клиента
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllAcquiringProgramAuthorizedMaxAgeNotAllowed() throws Exception {

        ClientEntity clientEntity = clientService.get(CLIENT_PRINCIPAL_DATA);
        clientEntity.setBirthDate(LocalDate.now().plusYears(200));
        clientService.save(clientEntity);
        mockMvc.perform(get(GET_ACQUIRING_PROGRAM_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(0)))
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registrationAcquiringNotAuthorizedZoneExistClientSuccess() throws Exception {

        AcquiringInsuranceRq data = new AcquiringInsuranceRq();
        data.setFirstName("Семён");
        data.setSurName("Семёнов");
        data.setMiddleName("Семёнович");
        data.setBirthDate(LocalDate.parse("01.01.1975", FORMATTER));
        data.setPhoneNumber("+791100000003");
        data.setGender(Gender.MALE);
        data.setDocNumber("111111");
        data.setDocSeries("1111");
        data.setProgramId(2L);
        data.setEmail("email@email.ru");
        data.setIsMobile(false);
        mockMvc.perform(post(POST_ACQUIRING_REGISTRATION)
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Коробка")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyIso", is("RUB")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentAmount", is(50000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceAmount", is(50000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insurancePremium", is(50000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreate", is(LocalDate.now().format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().plusDays(15).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is(LocalDate.now().plusDays(14).plusYears(7).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks[0]", is("Дожитие")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.surName", is("Семёнов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.firstName", is("Семён")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.middleName", is("Семёнович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.birthDate", is("01.01.1975")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docSeries", is("1111")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docNumber", is("111111")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.phoneNumber", is("+791100000003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.email", is("email@email.ru")))
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registrationAcquiringNotAuthorizedZoneNotExistClientSuccess() throws Exception {
        mockMvc.perform(post(POST_ACQUIRING_REGISTRATION)
                .content(TestUtils.convertObjectToJson(getRequestDataWithProgramId(2L)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Коробка")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyIso", is("RUB")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentAmount", is(50000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceAmount", is(50000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insurancePremium", is(50000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreate", is(LocalDate.now().format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().plusDays(15).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is(LocalDate.now().plusDays(14).plusYears(7).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks[0]", is("Дожитие")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.id", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.surName", is("Фамилия")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.firstName", is("Имя")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.middleName", is("Отчество")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.birthDate", is("01.01.1980")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docSeries", is("1111")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docNumber", is("111111")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.phoneNumber", is("+791100000083")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.email", is("email@email.ru")))
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registrationAcquiringAuthorizedZoneSuccess() throws Exception {

        AcquiringInsuranceRq data = new AcquiringInsuranceRq();
        data.setProgramId(1L);
        data.setIsMobile(false);

        mockMvc.perform(post(POST_ACQUIRING_REGISTRATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Коробка")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyIso", is("RUB")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentAmount", is(50000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceAmount", is(50000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insurancePremium", is(50000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreate", is(LocalDate.now().format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().plusDays(15).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is(LocalDate.now().plusDays(14).plusYears(7).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks[0]", is("Дожитие")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.id", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.surName", is(CLIENT_PRINCIPAL_DATA.getSecondName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.firstName", is(CLIENT_PRINCIPAL_DATA.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.middleName", is(CLIENT_PRINCIPAL_DATA.getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docSeries", is("5499")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docNumber", is("769999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.phoneNumber", is(CLIENT_PRINCIPAL_DATA.getMobilePhone())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.email", is("ivanov@example.org")))
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registrationAcquiringNotAuthorizedZoneProgramNotAllowedInNotAuthorizedForbidden() throws Exception {
        mockMvc.perform(post(POST_ACQUIRING_REGISTRATION)
                .content(TestUtils.convertObjectToJson(getRequestDataWithProgramId(3L)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registrationAcquiringNotAuthorizedZoneProgramStartDateIsAfterForbidden() throws Exception {
        mockMvc.perform(post(POST_ACQUIRING_REGISTRATION)
                .content(TestUtils.convertObjectToJson(getRequestDataWithProgramId(5L)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registrationAcquiringNotAuthorizedZoneProgramEndDateIsBeforeForbidden() throws Exception {
        mockMvc.perform(post(POST_ACQUIRING_REGISTRATION)
                .content(TestUtils.convertObjectToJson(getRequestDataWithProgramId(4L)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registrationAcquiringNotAuthorizedZoneProgramNotFoundForbidden() throws Exception {
        mockMvc.perform(post(POST_ACQUIRING_REGISTRATION)
                .content(TestUtils.convertObjectToJson(getRequestDataWithProgramId(40L)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Ignore
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void issueAcquiringNotAuthorizedZoneSuccess() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();
            AcquiringInsuranceRq requestDataWithProgramId = getRequestDataWithProgramId(2L);
            requestDataWithProgramId.setUuid("222");
            mockMvc.perform(post(POST_ACQUIRING_ISSUE)
                    .content(TestUtils.convertObjectToJson(requestDataWithProgramId))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceId").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceNumber").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.programId", is(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Коробка")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.currencyIso", is("RUB")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.paymentAmount", is(7142.86)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceAmount", is(1.0)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insurancePremium", is(7142.86)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreate", is(LocalDate.now().format(FORMATTER))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().plusDays(15).format(FORMATTER))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is(LocalDate.now().plusDays(14).plusYears(7).format(FORMATTER))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.risks", hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.risks[0]", is("Дожитие")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insured.id").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insured.surName", is("Фамилия")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insured.firstName", is("Имя")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insured.middleName", is("Отчество")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insured.birthDate", is("01.01.1980")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insured.gender", is("MALE")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docSeries", is("1111")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docNumber", is("111111")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insured.phoneNumber", is("+791100000083")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.insured.email", is("email@email.ru")));

            assertNotNull(GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));

            MimeMessage message = greenMail.getReceivedMessages()[0];
            MimeMultipart body = (MimeMultipart) message.getContent();
            assertTrue(body.getContentType().startsWith("multipart/mixed"));
            assertEquals(2, body.getCount());

            BodyPart text = body.getBodyPart(0);

            System.out.println(text.getContent());

            BodyPart attachment = body.getBodyPart(1);

            InputStream attachmentStream = (InputStream) attachment.getContent();
            byte[] bytes = IOUtils.toByteArray(attachmentStream);
            assertNotNull(bytes);
            assertTrue(bytes.length > 130000);
        } finally {
            greenMail.stop();
        }

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(ALL_INSURANCE_COUNT + 1)));
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Ignore
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void issueAcquiringNotAuthorizedZoneErrorSendEmail() throws Exception {
        AcquiringInsuranceRq requestDataWithProgramId = getRequestDataWithProgramId(2L);
        requestDataWithProgramId.setUuid("222");
        mockMvc.perform(post(POST_ACQUIRING_ISSUE)
                .content(TestUtils.convertObjectToJson(requestDataWithProgramId))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]",
                        is("К сожалению, оформление покупки страхового продукта невозможно. Попробуйте повторить позже.")))

        ;

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(ALL_INSURANCE_COUNT)));
    }


    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void issueAcquiringGetPaymentInfo() throws Exception {
        mockMvc.perform(get(GET_PAYMENT_INFO, 2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceNumber").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceStatus", is("MADE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Коробка")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyIso", is("RUB")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentAmount", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceAmount", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insurancePremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreate", is(LocalDate.now().format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().plusDays(15).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is(LocalDate.now().plusDays(14).plusYears(7).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks[0]", is("Дожитие")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.surName", is("Фамилия")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.firstName", is("Имя")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.middleName", is("Отчество")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.birthDate", is("01.01.1980")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docSeries", is("1111")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docNumber", is("111111")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.phoneNumber", is("+791100000083")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.email", is("email@email.ru")))
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Ignore
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void issueAcquiringGetPaymentInfoPaymentNotComleted() throws Exception {
        mockMvc.perform(get(GET_PAYMENT_INFO, 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceNumber").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceStatus", is("MADE_NOT_COMPLETED")));
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findContractByCodeNotAuthorizedZoneExistAcquiringProgramSuccess() throws Exception {
        AcquiringFindByCodeRq rq = new AcquiringFindByCodeRq();
        rq.setCode("9999");
        rq.setSurName("Иванов");
        Insurance insurance = insuranceService.findById(11);
        insurance.setCode("9999");
        insurance.setStatus(statusService.getByCode(InsuranceStatusCode.PROJECT));
        insuranceService.update(insurance);
        mockMvc.perform(post(GET_CONTRACT_BY_CODE)
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Коробка")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyIso", is("RUB")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentAmount", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceAmount", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insurancePremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreate", is(LocalDate.now().format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().plusDays(15).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is(LocalDate.now().plusDays(14).plusYears(7).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks[0]", is("Дожитие")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.id", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.surName", is("Иванов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.firstName", is("Иван")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.middleName", is("Петрович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docSeries", is("5499")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docNumber", is("769999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.phoneNumber", is("+79999999999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.email", is("ivanov@example.org")))
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findContractByCodeNotAuthorizedZoneErrorContractNotFound() throws Exception {
        AcquiringFindByCodeRq rq = new AcquiringFindByCodeRq();
        rq.setCode("9993");
        rq.setSurName("Иванов");

        mockMvc.perform(post(GET_CONTRACT_BY_CODE)
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]",
                        is("Продолжение покупки страхового продукта невозможно.")))
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findContractByCodeNotAuthorizedZoneErrorContractNotProject() throws Exception {
        AcquiringFindByCodeRq rq = new AcquiringFindByCodeRq();
        rq.setCode("9999");
        rq.setSurName("Иванов");
        Insurance insurance = insuranceService.findById(11);
        insurance.setCode("9999");
        insuranceService.update(insurance);
        mockMvc.perform(post(GET_CONTRACT_BY_CODE)
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]",
                        is("Продолжение покупки страхового продукта невозможно.")))
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findContractByCodeNotAuthorizedZoneErrorClientNotMatch() throws Exception {
        AcquiringFindByCodeRq rq = new AcquiringFindByCodeRq();
        rq.setCode("9999");
        rq.setSurName("Злодейев");
        Insurance insurance = insuranceService.findById(11);
        insurance.setCode("9999");
        insurance.setStatus(statusService.getByCode(InsuranceStatusCode.PROJECT));
        insuranceService.update(insurance);
        mockMvc.perform(post(GET_CONTRACT_BY_CODE)
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())

        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findContractByCodeNotAuthorizedZoneNotExistAcquiringProgramSuccess() throws Exception {
        AcquiringFindByCodeRq rq = new AcquiringFindByCodeRq();
        rq.setCode("9998");
        rq.setSurName("Иванов");
        Insurance insurance = insuranceService.findById(10);
        insurance.setCode("9998");
        insurance.setStatus(statusService.getByCode(InsuranceStatusCode.PROJECT));
        insuranceService.update(insurance);
        mockMvc.perform(post(GET_CONTRACT_BY_CODE)
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programId").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyIso", is("RUB")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentAmount", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceAmount", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insurancePremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreate", is(LocalDate.now().format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().plusDays(15).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is(LocalDate.now().plusDays(14).plusYears(1).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.id", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.surName", is("Иванов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.firstName", is("Иван")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.middleName", is("Петрович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docSeries", is("5499")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docNumber", is("769999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.phoneNumber", is("+79999999999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.email", is("ivanov@example.org")))
        ;
    }


    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findContractByCodeAuthorizedZoneExistAcquiringProgramSuccess() throws Exception {
        AcquiringFindByCodeRq rq = new AcquiringFindByCodeRq();
        rq.setCode("9999");
        Insurance insurance = insuranceService.findById(11);
        insurance.setStatus(statusService.getByCode(InsuranceStatusCode.PROJECT));
        insurance.setCode("9999");
        insuranceService.update(insurance);
        mockMvc.perform(post(GET_CONTRACT_BY_CODE)
                .content(TestUtils.convertObjectToJson(rq))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Коробка")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyIso", is("RUB")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentAmount", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceAmount", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insurancePremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreate", is(LocalDate.now().format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().plusDays(15).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is(LocalDate.now().plusDays(14).plusYears(7).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks[0]", is("Дожитие")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.id", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.surName", is("Иванов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.firstName", is("Иван")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.middleName", is("Петрович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docSeries", is("5499")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docNumber", is("769999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.phoneNumber", is("+79999999999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.email", is("ivanov@example.org")))
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/acquiring/registration
     * начало оформления договора в не авторизованной зоне
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findContractByCodeAuthorizedZoneNotExistAcquiringProgramSuccess() throws Exception {
        AcquiringFindByCodeRq rq = new AcquiringFindByCodeRq();
        rq.setCode("9998");
        Insurance insurance = insuranceService.findById(10);
        insurance.setStatus(statusService.getByCode(InsuranceStatusCode.PROJECT));
        insurance.setCode("9998");
        insuranceService.update(insurance);
        mockMvc.perform(post(GET_CONTRACT_BY_CODE)
                .content(TestUtils.convertObjectToJson(rq))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programId").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyIso", is("RUB")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentAmount", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceAmount", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insurancePremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreate", is(LocalDate.now().format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().plusDays(15).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is(LocalDate.now().plusDays(14).plusYears(1).format(FORMATTER))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.id", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.surName", is("Иванов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.firstName", is("Иван")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.middleName", is("Петрович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docSeries", is("5499")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.docNumber", is("769999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.phoneNumber", is("+79999999999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insured.email", is("ivanov@example.org")))
        ;
    }


    public AcquiringInsuranceRq getRequestDataWithProgramId(Long programId) {
        AcquiringInsuranceRq data = new AcquiringInsuranceRq();
        data.setUuid("111");
        data.setId(2L);
        data.setFirstName("Имя");
        data.setSurName("Фамилия");
        data.setMiddleName("Отчество");
        data.setBirthDate(LocalDate.parse("01.01.1980", FORMATTER));
        data.setPhoneNumber("+791100000083");
        data.setGender(Gender.MALE);
        data.setDocNumber("111111");
        data.setDocSeries("1111");
        data.setProgramId(programId);
        data.setEmail("email@email.ru");
        data.setIsMobile(false);
        return data;
    }
}
