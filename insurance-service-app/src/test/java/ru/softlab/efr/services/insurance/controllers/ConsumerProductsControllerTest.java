package ru.softlab.efr.services.insurance.controllers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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
import ru.softlab.efr.common.utilities.rest.RestPageImpl;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.rest.ViewInsuranceModel;
import ru.softlab.efr.services.insurance.services.AcquiringProgramService;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManagerFactory;
import java.io.InputStream;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.*;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class ConsumerProductsControllerTest {

    private static final String GET_ALL_INSURANCE_CONTRACTS_REQUEST = "/insurance-service/v2/consumer/contracts";
    private static final String GET_INSURANCE_CONTRACT_BY_ID_REQUEST = "/insurance-service/v2/consumer/contracts/{id}";
    private static final String GET_SEND_DOCUMENT_REQUEST = "/insurance-service/v2/consumer/contracts/{id}/document/send?email={email}";
    private static final String GET_DOCUMENT_LIST_REQUEST = "/insurance-service/v2/consumer/contracts/{id}/document/templates";
    private static final String GET_PRINT_DOCUMENT_REQUEST = "/insurance-service/v2/consumer/contracts/{id}/document/{templateId}/print";
    private static final String GET_INSURANCE_CONTRACT_REDEMPTION_REQUEST = "/insurance-service/v2/consumer/contracts/{contractId}/redemption";

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();
    @Rule
    @Autowired
    public HibernateCacheInvalidationRule hibernateCacheInvalidationRule;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private AcquiringProgramService acquiringProgramService;
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    @Autowired
    private EntityManagerFactory factory;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    private ParameterizedTypeReference<RestPageImpl<ViewInsuranceModel>> restPage() {
        return new ParameterizedTypeReference<RestPageImpl<ViewInsuranceModel>>() {
        };
    }


    //*******************************************************************************
    //*******************************************************************************
    //                              insurance tests
    //*******************************************************************************
    //*******************************************************************************

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getInsuranceByIdSuccess() throws Exception {
        mockMvc.perform(get(GET_INSURANCE_CONTRACT_BY_ID_REQUEST, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contractNumber", is("23К010110000002")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programSettingId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.premium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rurPremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rurAmount", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.periodicity", is("YEARLY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("MADE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyName", is("Цифровое будущее")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.guaranteeLevel", is(22.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderEqualsInsured", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentTerm", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties[0].coefficient", is(1.0)))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts
     * для проверки получения корректного ответа при указании только параметров пегинации.
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Ignore
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllInsurancesSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(get(GET_ALL_INSURANCE_CONTRACTS_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].contractNumber", is("23К010110000003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].creationDate", is("15.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].duration", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].startDate", is("28.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].endDate", is("27.12.2032")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.id", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.surName", is("Иванов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.firstName", is("Иван")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.middleName", is("Петрович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.docType", is("PASSPORT_RF")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.docSeries", is("5499")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.docNumber", is("769999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.phoneNumber", is("+79999999999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.email", is("ivanov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].premium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].rurPremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].subdivisionId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].subdivisionName", is("2310")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeName", is("Фёдоров Фёдор Фёдорович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(3)));
        //assertSelectCount(28);
    }


    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}
     * для проверки получения отказа по причине отсутствия прав на просмотр договора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getInsuranceByIdForbidden() throws Exception {
        mockMvc.perform(get(GET_INSURANCE_CONTRACT_BY_ID_REQUEST, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}
     * для проверки получения ошибки.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getInsuranceByIdNotFound() throws Exception {
        mockMvc.perform(get(GET_INSURANCE_CONTRACT_BY_ID_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts
     * для проверки получения отказа по причине отсутствия прав на просмотр списка договоров страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllInsurancesForbidden() throws Exception {
        mockMvc.perform(get(GET_ALL_INSURANCE_CONTRACTS_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/send
     * Отправка документов на email пользователя
     *
     * @throws Exception при падении теста
     */
    @Ignore
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void sendDocumentsOfficeSourceSuccess() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();
            String email = "ivanov@gmail.com";
            mockMvc.perform(get(GET_SEND_DOCUMENT_REQUEST, 9, email)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                    .andExpect(status().isOk());

            assertNotNull(GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));

            MimeMessage message = greenMail.getReceivedMessages()[0];
            MimeMultipart body = (MimeMultipart) message.getContent();
            assertTrue(body.getContentType().startsWith("multipart/mixed"));
            assertEquals(3, body.getCount());

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
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/send
     * Отправка документов на email пользователя
     *
     * @throws Exception при падении теста
     */
    @Ignore
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void sendDocumentsLkSourceSuccess() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        Insurance insurance = insuranceService.findById(10);
        insurance.setAcquiringProgram(acquiringProgramService.findById(2L));
        insuranceService.update(insurance);
        try {
            greenMail.start();
            String email = "ivanov@example.org";
            mockMvc.perform(get(GET_SEND_DOCUMENT_REQUEST, 10, email)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                    .andExpect(status().isOk());

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
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/send
     * Отправка документов на email пользователя
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void sendDocumentsSendError() throws Exception {
        mockMvc.perform(get(GET_SEND_DOCUMENT_REQUEST, 9, null)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/send
     * Отправка документов на email пользователя
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void sendDocumentsForbidden() throws Exception {
        mockMvc.perform(get(GET_SEND_DOCUMENT_REQUEST, 2, null)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/send
     * Отправка документов на email пользователя
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void sendDocumentsNotFound() throws Exception {
        mockMvc.perform(get(GET_SEND_DOCUMENT_REQUEST, 20, null)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isNotFound());
    }


    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/templates
     * Получить список документов договора
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDocumentsListOfficeSourceSuccess() throws Exception {
        mockMvc.perform(get(GET_DOCUMENT_LIST_REQUEST, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.templates", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.templates[0].id", is("RshbInsuranceRent01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.templates[0].name", is("RshbInsuranceRent01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.templates[1].id", is("RshbInsuranceRent02")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.templates[1].name", is("RshbInsuranceRent02")))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/templates
     * Получить список документов договора
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDocumentsListLkSourceSuccess() throws Exception {

        Insurance insurance = insuranceService.findById(10);
        insurance.setAcquiringProgram(acquiringProgramService.findById(2L));
        insuranceService.update(insurance);

        mockMvc.perform(get(GET_DOCUMENT_LIST_REQUEST, 10)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.templates", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.templates[0].id", is("RshbInsuranceRent01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.templates[0].name", is("RshbInsuranceRent01")))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/templates
     * Получить список документов договора
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDocumentsListForbidden() throws Exception {
        mockMvc.perform(get(GET_DOCUMENT_LIST_REQUEST, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/templates
     * Получить список документов договора
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDocumentsListNotFound() throws Exception {
        mockMvc.perform(get(GET_DOCUMENT_LIST_REQUEST, 20)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isNotFound());
    }


    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/{templateId}/print
     * Получить pdf документ договора
     *
     * @throws Exception при падении теста
     */
    @Ignore
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDocumentSuccess() throws Exception {
        mockMvc.perform(get(GET_PRINT_DOCUMENT_REQUEST, 9, "RshbInsuranceRent01")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/templates
     * Получить список документов договора
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDocumentForbidden() throws Exception {
        mockMvc.perform(get(GET_PRINT_DOCUMENT_REQUEST, 2, "RshbInsuranceRent01")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{id}/document/templates
     * Получить список документов договора
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDocumentNotFound() throws Exception {
        mockMvc.perform(get(GET_PRINT_DOCUMENT_REQUEST, 20, "RshbInsuranceRent01")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isNotFound());
    }


    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{contractId}/redemption
     * для проверки получения списка выкупных сумм по договору.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void gelInsurancesRedemptionSuccess() throws Exception {
        mockMvc.perform(get(GET_INSURANCE_CONTRACT_REDEMPTION_REQUEST, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[0].periodNumber", is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[0].startPeriod", is("27.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[0].endPeriod", is("26.12.2019")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[0].redemptionAmount", is("1000,00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[1].periodNumber", is("2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[1].startPeriod", is("27.12.2019")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[1].endPeriod", is("26.12.2020")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[1].redemptionAmount", is("2000,00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[2].periodNumber", is("3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[2].startPeriod", is("27.12.2020")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[2].endPeriod", is("26.12.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData[2].redemptionAmount", is("1000,00")))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{contractId}/redemption
     * для проверки получения отказа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void gelInsurancesRedemptionForbidden() throws Exception {
        mockMvc.perform(get(GET_INSURANCE_CONTRACT_REDEMPTION_REQUEST, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden())
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{contractId}/redemption
     * для проверки ответа при отсутствии договора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void gelInsurancesRedemptionNotFound() throws Exception {
        mockMvc.perform(get(GET_INSURANCE_CONTRACT_REDEMPTION_REQUEST, 90)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isNotFound())
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer/contracts/{contractId}/redemption
     * для проверки получения пустого списка.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void gelInsurancesRedemptionSuccessEmptyList() throws Exception {
        mockMvc.perform(get(GET_INSURANCE_CONTRACT_REDEMPTION_REQUEST, 10)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redemptionData", hasSize(0)))
        ;
    }
}