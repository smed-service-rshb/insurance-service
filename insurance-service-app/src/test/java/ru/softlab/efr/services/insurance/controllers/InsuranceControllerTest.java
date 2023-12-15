package ru.softlab.efr.services.insurance.controllers;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.common.utilities.rest.RestPageImpl;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.service.ContractNumberSequenceServiceTest;
import ru.softlab.efr.services.insurance.service.ExtractServiceLock;
import ru.softlab.efr.services.insurance.utils.OperationLogServiceStatistics;
import ru.softlab.efr.services.insurance.utils.PrintTemplateSaveService;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.*;
import static ru.softlab.efr.services.insurance.stubs.TestDataExtraModels.INSURANCE_MODEL_COMULATED;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class InsuranceControllerTest {

    private static final String POST_CREATE_CONTRACT_REQUEST = "/insurance-service/v2/contracts";
    private static final String GET_ALL_CONTRACT_REQUEST = "/insurance-service/v2/contracts";
    private static final String GET_CONTRACT_BY_ID_REQUEST = "/insurance-service/v2/contracts/{id}";
    private static final String PUT_CONTRACT_BY_ID_REQUEST = "/insurance-service/v2/contracts/{id}";
    private static final String PRINT_CONTRACT_BY_ID_REQUEST = "/insurance-service/v2/contracts/{id}/print/{templateId}";
    private static final String CHANGE_CONTRACT_STATUS_REQUEST = "/insurance-service/v2/contracts/{id}/setStatus";
    private static final String CHANGE_FULL_SET_DOCUMENT_REQUEST = "/insurance-service/v2/contracts/fullSetDocument/{id}";
    private static final String GET_CONTRACTS_UNIVERSAL_EXTRACT_REQUEST = "/insurance-service/v2/contracts/extract/universal?startDate={startDate}&endDate={endDate}&kind={kind}";
    private static final String GET_CONTRACTS_EXTRACT_REQUEST = "/insurance-service/v2/contracts/extract/?startDate={startDate}&endDate={endDate}&kind={kind}";
    private static final String GET_CONTRACTS_EXTRACT_REQUEST_STATUS = "/insurance-service/v2/contracts/extract/{uuid}/status";
    private static final String GET_CONTRACTS_EXTRACT_CONTENT_REQUEST = "/insurance-service/v2/contracts/extract/{uuid}/content";
    private static final String POST_SET_INDIVIDUAL_RATE_REQUEST = "/insurance-service/v1/contract/{insuranceId}/setRate";
    private static final String INSURANCE_SERVICE_V_2_CONTRACTS_BY_CLIENT_ID_EXISTS = "/insurance-service/v2/contracts/by-client/{id}/exists";


    private static final List<String> printFormFileNames = Arrays.asList("SMS_payment_order");

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Rule
    @Autowired
    public HibernateCacheInvalidationRule hibernateCacheInvalidationRule;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PrintTemplateSaveService printTemplateSaveService;

    @Autowired
    private OperationLogServiceStatistics operationLogServiceStatistics;

    @Autowired
    private ExtractServiceLock extractServiceLock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    private ParameterizedTypeReference<RestPageImpl<ViewInsuranceModel>> restPage() {
        return new ParameterizedTypeReference<RestPageImpl<ViewInsuranceModel>>() {
        };
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contracts
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Ignore
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateContractSuccess() throws Exception {
        operationLogServiceStatistics.reset();

        ResultActions resultActions = mockMvc.perform(post(POST_CREATE_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(INSURANCE_MODEL_1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.contractNumber").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderId").isNotEmpty());

        int id = TestUtils.extractDataFromResultJson(resultActions, "$.id");

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("CREATE_CONTRACT", logs.get(0).getOperationKey());
        assertEquals((long) id, logs.get(0).getOperationParameters().get("id"));
        assertNull(logs.get(0).getOperationParameters().get("contractNumber"));
        assertNotNull(logs.get(0).getOperationParameters().get("creationDate"));
        assertEquals(OperationState.SUCCESS, logs.get(0).getOperationState());

        mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, (long) id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programSettingId", is(INSURANCE_MODEL_1.getProgramSettingId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", is(INSURANCE_MODEL_1.getDuration())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.periodicity", is(INSURANCE_MODEL_1.getPeriodicity().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calendarUnit", is(INSURANCE_MODEL_1.getCalendarUnit().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderEqualsInsured", is(INSURANCE_MODEL_1.isHolderEqualsInsured())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[1].taxResidence", is(INSURANCE_MODEL_1.getRecipientList().get(0).getTaxResidence().name().toLowerCase())))

        ;
    }

    @Test
    @Sql(value = {"classpath:test-script.sql", "classpath:comulation-test-script.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateContractComulationCheckFail() throws Exception {
        mockMvc.perform(post(POST_CREATE_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(INSURANCE_MODEL_COMULATED))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]", is("Страховая сумма по всем действующим договорам клиента с учетом текущего превышает заданный порог 1000 RUB. Доступная для оформления сумма: 400.00 RUB. Оформление невозможно.")))
                ;
    }

    /**
     * Тест сервиса создания договора страхования POST /insurance-service/v2/contracts
     * для проверки получения корректного ответа.
     * <p>
     * Сохраняется договор, связанный с набором параметров программы страхования, у которого нет рисков.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateContractSuccessProgramSettingWithoutRisks() throws Exception {

        ResultActions resultActions = mockMvc.perform(post(POST_CREATE_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(INSURANCE_MODEL_2))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderId").isNotEmpty());

        int id = TestUtils.extractDataFromResultJson(resultActions, "$.id");

        mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, (long) id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programSettingId", is(INSURANCE_MODEL_2.getProgramSettingId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", is(INSURANCE_MODEL_2.getDuration())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.periodicity", is(INSURANCE_MODEL_2.getPeriodicity().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calendarUnit", is(INSURANCE_MODEL_2.getCalendarUnit().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderEqualsInsured", is(INSURANCE_MODEL_2.isHolderEqualsInsured())));
    }

    /**
     * Тест сервиса создания договора страхования POST /insurance-service/v2/contracts
     * для проверки получения корректного ответа.
     * <p>
     * Сохраняется договор, связанный с набором параметров программы страхования, у которого нет рисков.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateContractSuccessProgramSettingWithHolderData() throws Exception {
        String saveClientRequest = IOUtils.toString(getClass().getResourceAsStream("../../../../../../create-insurance-contract-with-holder-data.json"), "UTF-8");
        //String saveClientRequest = TestUtils.convertObjectToJson(INSURANCE_MODEL_3);

        ResultActions resultActions = mockMvc.perform(post(POST_CREATE_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(saveClientRequest)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderId").isNotEmpty());

        int id = TestUtils.extractDataFromResultJson(resultActions, "$.id");

        mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, (long) id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.surName", is("выа")));
    }

    /**
     * Тест сервиса создания договора страхования POST /insurance-service/v2/contracts
     * для проверки получения корректного ответа.
     * <p>
     * Сохраняется договор, связанный с набором параметров программы страхования, у которого нет рисков.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeBadRequestOnCreateContractWithoutHolderData() throws Exception {
        String saveClientRequest = IOUtils.toString(getClass().getResourceAsStream("../../../../../../create-insurance-contract-without-holder-data.json"), "UTF-8");
        //String saveClientRequest = TestUtils.convertObjectToJson(INSURANCE_MODEL_3);

        ResultActions resultActions = mockMvc.perform(put(PUT_CONTRACT_BY_ID_REQUEST, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(saveClientRequest)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors", hasSize(17)));

        System.out.println(TestUtils.extractDataFromResultJson(resultActions, "$").toString());


        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[0].key", is("holderData.surName")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[0].value", is("Поле \"%s\" должно быть заполнено")))

                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[1].key", is("holderData.firstName")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[1].value", is("Поле \"%s\" должно быть заполнено")))

                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[2].key", is("holderData.phones")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[2].value", is("Поле \"%s\" должно быть заполнено")))

                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[3].key", is("holderData.documents[0].issuedBy")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[3].value", is("Поле \"Кем выдан документ\" должно быть заполнено")))

                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[7].key", is("insuredData.surName")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[7].value", is("Поле \"%s\" должно быть заполнено")))

                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[8].key", is("insuredData.firstName")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[8].value", is("Поле \"%s\" должно быть заполнено")))

                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[9].key", is("insuredData.phones")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[9].value", is("Поле \"%s\" должно быть заполнено")))

//                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[10].key", is("scan_2")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[10].value", is("Необходимо прикрепить скан: Платежное поручение/платежный документ.")))

                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[14].key", is("recipientList.share")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[14].value", is("Суммарная доля выгодоприобретателей должна составлять 100 процентов (сейчас 200)")))

                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[15].key", is("holderData.birthDate")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[15].value", containsString("должен быть в диапазоне от 18 до 54")))

                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[16].key", is("insuredData.birthDate")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors[16].value", containsString("должен быть в диапазоне от 18 до 54")));
    }

    /**
     * Тест сервиса создания договора страхования POST /insurance-service/v2/contracts
     * для проверки получения ответа с ошибкой, что указаны несуществующие значения для holderId и insuredId.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateContractFailHolderNotFound() throws Exception {
        operationLogServiceStatistics.reset();

        BaseInsuranceModel insurance = new BaseInsuranceModel();
        insurance.setProgramSettingId(2L);
        insurance.setStartDate(LocalDate.now());
        // Указаны несуществующие идентификаторы holderId и insuredId.
        insurance.setHolderId(-123L);
        insurance.setInsuredId(-1234L);
        insurance.setHolderEqualsInsured(false);
        insurance.setCalendarUnit(CalendarUnit.YEAR);
        insurance.setDuration(5);
        insurance.setAmount(new BigDecimal(200000.0));
        insurance.setPremium(new BigDecimal(20000.0));
        insurance.setRurAmount(new BigDecimal(200000.0));
        insurance.setRurPremium(new BigDecimal(20000.0));
        insurance.setCurrencyId(1L);
        insurance.setPeriodicity(PaymentPeriodicity.MONTHLY);
        insurance.setType(FindProgramType.SUM);

        mockMvc.perform(post(POST_CREATE_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(insurance))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]", is("Не удаётся найти клиента(Страхователь) по id =-123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[1]", is("Не удаётся найти клиента(Застрахованный) по id =-1234")));

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("CREATE_CONTRACT", logs.get(0).getOperationKey());
        assertEquals(OperationState.SYSTEM_ERROR, logs.get(0).getOperationState());
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contracts
     * для получения отказа по причине отсутствия прав на создание договора страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateContractForbidden() throws Exception {
        mockMvc.perform(post(POST_CREATE_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(INSURANCE_MODEL_1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contracts
     * для получения отказа в случае, когда указан несуществующий идентификатор набора параметров программы страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateContractBadRequest() throws Exception {
        BaseInsuranceModel insuranceModel = new BaseInsuranceModel();
        insuranceModel.setProgramSettingId(-1L);

        mockMvc.perform(post(POST_CREATE_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(insuranceModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractByIdSuccess() throws Exception {
        mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contractNumber", is("23К010110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programSettingId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", is(3)))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.currency", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.premium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rurPremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rurAmount", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.periodicity", is("ONCE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calendarUnit", is("YEAR")))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setIndividualRateSuccess() throws Exception {
        IndividualRateRq data = new IndividualRateRq();
        data.setRate(BigDecimal.valueOf(100));
        mockMvc.perform(post(POST_SET_INDIVIDUAL_RATE_REQUEST, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJson(data)))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exchangeRate", is(100.00)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.individualRate", is(true)))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setIndividualRateForbidden() throws Exception {
        IndividualRateRq data = new IndividualRateRq();
        data.setRate(BigDecimal.valueOf(100));
        mockMvc.perform(post(POST_SET_INDIVIDUAL_RATE_REQUEST, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJson(data)))
                .andExpect(status().isForbidden())
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании только параметров пегинации.
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllContractWithoutFilterByAdminSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(7)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].id", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].contractNumber", is("23К010112345679")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].creationDate", is("14.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].startDate", is("29.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].endDate", is("28.12.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].conclusionDate", is("14.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.surName", is("Семёнов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.firstName", is("Семён")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.middleName", is("Семёнович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.birthDate", is("01.01.1975")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docSeries", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docNumber", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.phoneNumber", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.email", is("semenov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].rurPremium", is(1000.0)))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[4].branchId", is(1000))) // TODO действительно ли надо возвращать branchId?
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].subdivisionId", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].subdivisionName", is("2311")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].employeeId", is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].employeeName", is("Викторов Виктор Викторович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(7)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании только параметров пегинации.
     * Запрос выполняется от имени пользователя с ролью "Начальник отдела розничных продаж".
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllContractWithoutFilterByDepHeadSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(7)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].id", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].contractNumber", is("23К010112345679")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].creationDate", is("14.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].startDate", is("29.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].endDate", is("28.12.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.surName", is("Семёнов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.firstName", is("Семён")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.middleName", is("Семёнович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.birthDate", is("01.01.1975")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docSeries", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docNumber", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.phoneNumber", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.email", is("semenov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].rurPremium", is(1000.0)))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[4].branchId", is(1000))) // TODO действительно ли надо возвращать branchId?
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].subdivisionId", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].subdivisionName", is("2311")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].employeeId", is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].employeeName", is("Викторов Виктор Викторович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(7)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании только параметров пегинации.
     * Запрос выполняется от имени руководителя ВСП.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllContractWithoutFilterByVSPHeadSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(VSP_HEAD_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].contractNumber", is("23К010112345678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].creationDate", is("13.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].startDate", is("04.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].endDate", is("03.12.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.surName", is("Петров")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.firstName", is("Пётр")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.middleName", is("Петрович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.birthDate", is("01.01.1989")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docType", is("PASSPORT_RF")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docSeries", is("2345")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docNumber", is("234567")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.phoneNumber", is("+791100000002")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.email", is("petrov@example.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].rurPremium", is(1000.0)))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[4].branchId", is(1000))) // TODO действительно ли надо возвращать branchId?
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].subdivisionId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].subdivisionName", is("2310")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].employeeId", is(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].employeeName", is("Антонов Антон Антонович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(6)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании только параметров пегинации.
     * Запрос выполняется от имени пользователя с ролью "Пользователь".
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllContractWithoutFilterByUserSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].contractNumber", is("23К010110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].creationDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].startDate", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].endDate", is("26.11.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.surName", is("Иванов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.firstName", is("Иван")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.middleName", is("Иванович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docType", is("PASSPORT_RF")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docSeries", is("1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.docNumber", is("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.phoneNumber", is("79110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].clientData.email", is("ivanov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].rurPremium", is(1000.0)))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[4].branchId", is(1000))) // TODO действительно ли надо возвращать branchId?
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].subdivisionId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].subdivisionName", is("2310")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].employeeId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].employeeName", is("Фёдоров Фёдор Фёдорович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(5)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации по имени страхователя
     * и части номера договора (к строкам специально добавлены пробелы, чтобы проверить, что выполняется trim).
     * Запрос выполняется от имени пользователя с ролью "Пользователь".
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByHolderSurnameAndContractNumberByUserSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(null,CLIENT_1.getSurName().toUpperCase() + " ", null, null, " 0000", null, null, null, null, null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].contractNumber", is("23К010110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].creationDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].startDate", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].endDate", is("26.11.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].clientData.id", is(Integer.valueOf(CLIENT_1.getId()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].clientData.surName", is(CLIENT_1.getSurName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].clientData.firstName", is(CLIENT_1.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].clientData.middleName", is(CLIENT_1.getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].clientData.birthDate",
                        is(CLIENT_1.getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].clientData.gender", is(CLIENT_1.getGender().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].clientData.docType", is(CLIENT_1.getDocuments().get(0).getDocType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].clientData.docSeries", is(CLIENT_1.getDocuments().get(0).getDocSeries())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].clientData.docNumber", is(CLIENT_1.getDocuments().get(0).getDocNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].clientData.phoneNumber", is(CLIENT_1.getPhones().get(1).getNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].clientData.email", is(CLIENT_1.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].rurPremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].subdivisionId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].subdivisionName", is("2310")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].employeeId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].employeeName", is("Фёдоров Фёдор Фёдорович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(3)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании идентификатора клиента.
     * Запрос выполняется от имени пользователя с ролью "Пользователь".
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByClientIDSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(1L,null, null, null, " 0000", null, null, null, null, null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contractNumber", is("23К010110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].creationDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].startDate", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endDate", is("26.11.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.id", is(Integer.valueOf(CLIENT_1.getId()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.surName", is(CLIENT_1.getSurName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.firstName", is(CLIENT_1.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.middleName", is(CLIENT_1.getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(1)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }
    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации только по
     * части номера договора.
     * Запрос выполняется от имени руководителя ВСП.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByContractNumberByVSPHeadSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(VSP_HEAD_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(null, null, null, null, "23К010110000001", null, null, null, null, null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contractNumber", is("23К010110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].creationDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].startDate", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endDate", is("26.11.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.id", is(Integer.valueOf(CLIENT_1.getId()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.surName", is(CLIENT_1.getSurName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.firstName", is(CLIENT_1.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.middleName", is(CLIENT_1.getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.birthDate",
                        is(CLIENT_1.getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.gender", is(CLIENT_1.getGender().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docType", is(CLIENT_1.getDocuments().get(0).getDocType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docSeries", is(CLIENT_1.getDocuments().get(0).getDocSeries())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docNumber", is(CLIENT_1.getDocuments().get(0).getDocNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.phoneNumber", is(CLIENT_1.getPhones().get(1).getNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.email", is(CLIENT_1.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rurPremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionName", is("2310")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeName", is("Фёдоров Фёдор Фёдорович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(1)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации только по
     * отчеству страхователя (к стороке специально добавлены пробелы, чтобы проверить, что работает trim).
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByMiddleNameByAdminSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(null, null, null, " Иванович ", null, null, null, null, null, null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].contractNumber", is("23К010110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].creationDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].startDate", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].endDate", is("26.11.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.id", is(Integer.valueOf(CLIENT_1.getId()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.surName", is(CLIENT_1.getSurName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.firstName", is(CLIENT_1.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.middleName", is(CLIENT_1.getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.birthDate",
                        is(CLIENT_1.getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.gender", is(CLIENT_1.getGender().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.docType", is(CLIENT_1.getDocuments().get(0).getDocType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.docSeries", is(CLIENT_1.getDocuments().get(0).getDocSeries())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.docNumber", is(CLIENT_1.getDocuments().get(0).getDocNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.phoneNumber", is(CLIENT_1.getPhones().get(1).getNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.email", is(CLIENT_1.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].rurPremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].subdivisionId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].subdivisionName", is("2310")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeName", is("Фёдоров Фёдор Фёдорович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(2)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации только по
     * старту периода создания договора.
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByStartDateByAdminSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(
                        null, null, null, null,
                        null, null, null,
                        null, LocalDate.parse("14.12.2018", DateTimeFormatter.ofPattern("dd.MM.yyyy")), null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].contractNumber", is("23К010112345679")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].creationDate", is("14.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].startDate", is("29.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].endDate", is("28.12.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.surName", is("Семёнов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.firstName", is("Семён")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.middleName", is("Семёнович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.birthDate", is("01.01.1975")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.docType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.docSeries", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.docNumber", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.phoneNumber", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clientData.email", is("semenov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].rurPremium", is(1000.0)))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].branchId", is(1000))) // TODO действительно ли надо возвращать branchId?
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].subdivisionId", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].subdivisionName", is("2311")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeId", is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeName", is("Викторов Виктор Викторович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(2)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации только по
     * концу периода создания договора.
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByStartDateAndEndDateByAdminSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(
                        null, null, null, null,
                        null, null, null,
                        null, LocalDate.parse("13.12.2018", DateTimeFormatter.ofPattern("dd.MM.yyyy")), LocalDate.parse("13.12.2018", DateTimeFormatter.ofPattern("dd.MM.yyyy")), null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contractNumber", is("23К010112345678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].creationDate", is("13.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].startDate", is("04.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endDate", is("03.12.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.surName", is("Петров")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.firstName", is("Пётр")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.middleName", is("Петрович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.birthDate", is("01.01.1989")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docType", is("PASSPORT_RF")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docSeries", is("2345")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docNumber", is("234567")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.phoneNumber", is("+791100000002")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.email", is("petrov@example.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rurPremium", is(1000.0)))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].branchId", is(1000))) // TODO действительно ли надо возвращать branchId?
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionName", is("2310")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeId", is(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeName", is("Антонов Антон Антонович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(1)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации по
     * статусу договора, виду программы страхования.
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByStatusAndProgramKindByAdminSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(null, null, null, null, null, InsuranceStatusType.DRAFT, ProgramKind.ISJ, null, null, null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contractNumber", is("23К010112345679")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].creationDate", is("14.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].startDate", is("29.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endDate", is("28.12.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.surName", is("Семёнов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.firstName", is("Семён")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.middleName", is("Семёнович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.birthDate", is("01.01.1975")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docSeries", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docNumber", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.phoneNumber", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.email", is("semenov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rurPremium", is(1000.0)))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branchId", is(1000))) // TODO действительно ли надо возвращать branchId?
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionId", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionName", is("2311")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeId", is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeName", is("Викторов Виктор Викторович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(3)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации только по
     * идентификатору программы страхования (указана существующая программа, по которой не создано ни одного договора).
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByProgramIdByAdminSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(null, null, null, null, null, null, null, 1L, null, null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contractNumber", is("23К010112345679")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].creationDate", is("14.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].startDate", is("29.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endDate", is("28.12.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.surName", is("Семёнов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.firstName", is("Семён")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.middleName", is("Семёнович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.birthDate", is("01.01.1975")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docSeries", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docNumber", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.phoneNumber", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.email", is("semenov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rurPremium", is(1000.0)))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branchId", is(1000))) // TODO действительно ли надо возвращать branchId?
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionId", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionName", is("2311")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeId", is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeName", is("Викторов Виктор Викторович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(2)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации только по
     * идентификатору программы страхования (указана существующая программа, по которой не создано ни одного договора).
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByProgramIdByAdminEmptyListSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(null, null, null, null, null, null, null, 3L, null, null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(0)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации только по
     * статусу договора (указан статус, в котором не находится ни один договор).
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByStatusByAdminEmptyListSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(null, null, null, null, null, InsuranceStatusType.NEED_WITHDRAW_APPLICATION, null, null, null, null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(0)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации только по
     * признаку полного комплекта документов.
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByFullSetDocumentSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(null, null, null, null, null, null, null, null, null, null, null, null, true)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contractNumber", is("23К010110000003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(1)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации только по
     * виду договора страхования (указан вид договора страхования, в котором не находится ни один договор).
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByProgramKindByAdminEmptyListSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(null, null, null, null, null, null, ProgramKind.RENT, null, null, null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(0)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }


    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании параметров пегинации и фильтрации по
     * дате заключения договора.
     * Запрос выполняется от имени администратора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsFilteredByStartConclusionDateAndEndConclusionDateByAdminSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(
                        null, null, null, null,
                        null, null, null,
                        null, null, null,
                        LocalDate.parse("10.12.2018", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        LocalDate.parse("13.12.2018", DateTimeFormatter.ofPattern("dd.MM.yyyy")), null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(4)));

        assertSelectCount(2);
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(
                        null, null, null, null,
                        null, null, null,
                        null, null, null,
                        LocalDate.parse("13.12.2018", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        LocalDate.parse("13.12.2018", DateTimeFormatter.ofPattern("dd.MM.yyyy")), null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contractNumber", is("23К010112345678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].creationDate", is("13.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].startDate", is("04.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endDate", is("03.12.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.surName", is("Петров")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.firstName", is("Пётр")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.middleName", is("Петрович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.birthDate", is("01.01.1989")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docType", is("PASSPORT_RF")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docSeries", is("2345")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docNumber", is("234567")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.phoneNumber", is("+791100000002")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.email", is("petrov@example.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rurPremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionName", is("2310")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeId", is(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeName", is("Антонов Антон Антонович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(1)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts/{id}/print/{templateId}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void printContractTemplateByIdSuccess() throws Exception {
        for (String templateName : printFormFileNames) {
            printContractByIdRequest(templateName);
        }
    }

    private void printContractByIdRequest(String templateName) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(PRINT_CONTRACT_BY_ID_REQUEST, 2, templateName)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
        printTemplateSaveService.save("filled-".concat(templateName), resultActions.andReturn().getResponse().getContentAsByteArray());
    }


    /**
     * Тест сервиса GET /insurance-service/v2/contracts/{id}/print/{templateId}
     * для получения отказа по причине отсутствия прав на создание просмотр договора страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void printContractByIdForbidden() throws Exception {
        mockMvc.perform(get(PRINT_CONTRACT_BY_ID_REQUEST, 2, UUID.randomUUID().toString())
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts/{id}/print/{templateId}
     * для получения отказа в случае, когда указан несуществующий идентификатор договора страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void printContractByIdNotFound() throws Exception {
        mockMvc.perform(get(PRINT_CONTRACT_BY_ID_REQUEST, -1, UUID.randomUUID().toString())
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts/{id}/print/{templateId}
     * для получения корректного ответа, при печати пустого шаблона.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void printEmptyContractSuccess() throws Exception {
        for (String templateName : printFormFileNames) {
            printEmptyContractSuccess(templateName);
        }
    }

    private void printEmptyContractSuccess(String templateName) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(PRINT_CONTRACT_BY_ID_REQUEST, 0, templateName)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
        printTemplateSaveService.save("not-filled-".concat(templateName), resultActions.andReturn().getResponse().getContentAsByteArray());
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putContractSuccess() throws Exception {
        mockMvc.perform(put(PUT_CONTRACT_BY_ID_REQUEST, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(INSURANCE_MODEL_3))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", is(INSURANCE_MODEL_3.getType().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contractNumber", is("23К010110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programNumber", is("002")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programSettingId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderId", is(INSURANCE_MODEL_3.getHolderId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.id", is(INSURANCE_MODEL_3.getHolderData().getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.surName", is(INSURANCE_MODEL_3.getHolderData().getSurName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.firstName", is(INSURANCE_MODEL_3.getHolderData().getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.middleName", is(INSURANCE_MODEL_3.getHolderData().getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.birthDate", is(INSURANCE_MODEL_3.getHolderData().getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.gender", is(INSURANCE_MODEL_3.getHolderData().getGender().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.birthPlace", is(INSURANCE_MODEL_3.getHolderData().getBirthPlace())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.birthCountry", is(INSURANCE_MODEL_3.getHolderData().getBirthCountry())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.citizenship", is(INSURANCE_MODEL_3.getHolderData().getCitizenship().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.taxResidence", is(INSURANCE_MODEL_3.getHolderData().getTaxResidence().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.snils", is(INSURANCE_MODEL_3.getHolderData().getSnils())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.taxPayerNumber", is(INSURANCE_MODEL_3.getHolderData().getTaxPayerNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents", hasSize(INSURANCE_MODEL_3.getHolderData().getDocuments().size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].docType", is(INSURANCE_MODEL_3.getHolderData().getDocuments().get(0).getDocType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].docSeries", is(INSURANCE_MODEL_3.getHolderData().getDocuments().get(0).getDocSeries())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].docNumber", is(INSURANCE_MODEL_3.getHolderData().getDocuments().get(0).getDocNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].issuedBy", is(INSURANCE_MODEL_3.getHolderData().getDocuments().get(0).getIssuedBy())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].issuedDate", is(INSURANCE_MODEL_3.getHolderData().getDocuments().get(0).getIssuedDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].isActive", is(INSURANCE_MODEL_3.getHolderData().getDocuments().get(0).isIsActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].isMain", is(INSURANCE_MODEL_3.getHolderData().getDocuments().get(0).isIsMain())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].divisionCode", is(INSURANCE_MODEL_3.getHolderData().getDocuments().get(0).getDivisionCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.email", is(INSURANCE_MODEL_3.getHolderData().getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones", hasSize(INSURANCE_MODEL_3.getHolderData().getPhones().size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones[0].number", is(INSURANCE_MODEL_3.getHolderData().getPhones().get(0).getNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones[0].type", is(INSURANCE_MODEL_3.getHolderData().getPhones().get(0).getType().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones[0].main", is(INSURANCE_MODEL_3.getHolderData().getPhones().get(0).isMain())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses", hasSize(INSURANCE_MODEL_3.getHolderData().getAddresses().size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].addressType", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getAddressType().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].country", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getCountry())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].region", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getRegion())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].area", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getArea())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].city", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getCity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].locality", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getLocality())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].street", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getStreet())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].house", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getHouse())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].construction", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getConstruction())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].housing", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getHousing())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].apartment", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getApartment())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].index", is(INSURANCE_MODEL_3.getHolderData().getAddresses().get(0).getIndex())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].fullAddress", notNullValue(String.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.publicOfficialStatus", is(INSURANCE_MODEL_3.getHolderData().getPublicOfficialStatus().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.foreignPublicOfficialType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.russianPublicOfficialType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.relations", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.publicOfficialPosition", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.publicOfficialNameAndPosition", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.beneficialOwner", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.businessRelations", is(INSURANCE_MODEL_3.getHolderData().getBusinessRelations())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.activitiesGoal", is(INSURANCE_MODEL_3.getHolderData().getActivitiesGoal())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.businessRelationsGoal", is(INSURANCE_MODEL_3.getHolderData().getBusinessRelationsGoal())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.businessReputation", is(INSURANCE_MODEL_3.getHolderData().getBusinessReputation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.financialStability", is(INSURANCE_MODEL_3.getHolderData().getFinancialStability())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.financesSource", is(INSURANCE_MODEL_3.getHolderData().getFinancesSource())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.personalDataConsent", is(INSURANCE_MODEL_3.getHolderData().isPersonalDataConsent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredId", is(INSURANCE_MODEL_3.getInsuredId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.id", is(INSURANCE_MODEL_3.getInsuredData().getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.surName", is(INSURANCE_MODEL_3.getInsuredData().getSurName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.firstName", is(INSURANCE_MODEL_3.getInsuredData().getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.middleName", is(INSURANCE_MODEL_3.getInsuredData().getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.birthDate", is(INSURANCE_MODEL_3.getInsuredData().getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.gender", is(INSURANCE_MODEL_3.getInsuredData().getGender().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.birthPlace", is(INSURANCE_MODEL_3.getInsuredData().getBirthPlace())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.birthCountry", is(INSURANCE_MODEL_3.getInsuredData().getBirthCountry())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.citizenship", is(INSURANCE_MODEL_3.getInsuredData().getCitizenship().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.taxResidence", is(INSURANCE_MODEL_3.getInsuredData().getTaxResidence().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.snils", is(INSURANCE_MODEL_3.getInsuredData().getSnils())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.taxPayerNumber", is(INSURANCE_MODEL_3.getInsuredData().getTaxPayerNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.documents", hasSize(INSURANCE_MODEL_3.getInsuredData().getDocuments().size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.documents[0].docType", is(INSURANCE_MODEL_3.getInsuredData().getDocuments().get(0).getDocType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.documents[0].docSeries", is(INSURANCE_MODEL_3.getInsuredData().getDocuments().get(0).getDocSeries())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.documents[0].docNumber", is(INSURANCE_MODEL_3.getInsuredData().getDocuments().get(0).getDocNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.documents[0].issuedBy", is(INSURANCE_MODEL_3.getInsuredData().getDocuments().get(0).getIssuedBy())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.documents[0].issuedDate", is(INSURANCE_MODEL_3.getInsuredData().getDocuments().get(0).getIssuedDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.documents[0].isActive", is(INSURANCE_MODEL_3.getInsuredData().getDocuments().get(0).isIsActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.documents[0].isMain", is(INSURANCE_MODEL_3.getInsuredData().getDocuments().get(0).isIsMain())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.documents[0].divisionCode", is(INSURANCE_MODEL_3.getInsuredData().getDocuments().get(0).getDivisionCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.email", is(INSURANCE_MODEL_3.getInsuredData().getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.phones", hasSize(INSURANCE_MODEL_3.getInsuredData().getPhones().size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.phones[0].number", is(INSURANCE_MODEL_3.getInsuredData().getPhones().get(0).getNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.phones[0].type", is(INSURANCE_MODEL_3.getInsuredData().getPhones().get(0).getType().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.phones[0].main", is(INSURANCE_MODEL_3.getInsuredData().getPhones().get(0).isMain())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses", hasSize(INSURANCE_MODEL_3.getInsuredData().getAddresses().size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].addressType", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getAddressType().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].country", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getCountry())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].region", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getRegion())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].area", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getArea())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].city", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getCity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].locality", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getLocality())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].street", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getStreet())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].house", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getHouse())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].construction", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getConstruction())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].housing", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getHousing())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].apartment", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getApartment())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.addresses[0].index", is(INSURANCE_MODEL_3.getInsuredData().getAddresses().get(0).getIndex())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.publicOfficialStatus", is(INSURANCE_MODEL_3.getInsuredData().getPublicOfficialStatus().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.foreignPublicOfficialType", is(INSURANCE_MODEL_3.getInsuredData().getForeignPublicOfficialType().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.russianPublicOfficialType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.relations", is(INSURANCE_MODEL_3.getInsuredData().getRelations().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.publicOfficialPosition", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.publicOfficialNameAndPosition", is(INSURANCE_MODEL_3.getInsuredData().getPublicOfficialNameAndPosition())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.beneficialOwner", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.businessRelations", is(INSURANCE_MODEL_3.getInsuredData().getBusinessRelations())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.activitiesGoal", is(INSURANCE_MODEL_3.getInsuredData().getActivitiesGoal())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.businessRelationsGoal", is(INSURANCE_MODEL_3.getInsuredData().getBusinessRelationsGoal())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.businessReputation", is(INSURANCE_MODEL_3.getInsuredData().getBusinessReputation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.financialStability", is(INSURANCE_MODEL_3.getInsuredData().getFinancialStability())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.financesSource", is(INSURANCE_MODEL_3.getInsuredData().getFinancesSource())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredData.personalDataConsent", is(INSURANCE_MODEL_3.getInsuredData().isPersonalDataConsent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderEqualsInsured", is(INSURANCE_MODEL_3.isHolderEqualsInsured())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", is(INSURANCE_MODEL_3.getDuration())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calendarUnit", is(INSURANCE_MODEL_3.getCalendarUnit().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyId", is(INSURANCE_MODEL_3.getCurrencyId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.periodicity", is(INSURANCE_MODEL_3.getPeriodicity().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", is(INSURANCE_MODEL_3.getAmount().doubleValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rurAmount", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.premium", comparesEqualTo(9000.00)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rurPremium", comparesEqualTo(9000.00)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.premiumWithoutDiscount", comparesEqualTo(10000.00)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount", comparesEqualTo(10.00)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.growth", is(INSURANCE_MODEL_3.getGrowth())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.weight", is(INSURANCE_MODEL_3.getWeight())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.upperPressure", is(INSURANCE_MODEL_3.getUpperPressure())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lowerPressure", is(INSURANCE_MODEL_3.getLowerPressure())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.guaranteeLevel", is(INSURANCE_MODEL_3.getGuaranteeLevel().doubleValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientEqualsHolder", is(INSURANCE_MODEL_3.isRecipientEqualsHolder())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.riskInfoList", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addRiskInfoList", hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList", hasSize(INSURANCE_MODEL_3.getRecipientList().size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyId", is(INSURANCE_MODEL_3.getStrategyId().intValue())));
    }


    /**
     * Тест сервиса PUT /insurance-service/v2/contracts/{id}
     * для проверки получения корректного ответа при обновлении договора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void changeStatusOnlySuccess() throws Exception {
        checkChangeStatus(2);
    }

    private String checkChangeStatus(int id) throws Exception {
        mockMvc.perform(post(CHANGE_CONTRACT_STATUS_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(CHANGE_STATUS_INSURANCE_MODEL_1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        ResultActions resultActions = mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("PROJECT")));

        return TestUtils.extractDataFromResultJson(resultActions, "$.contractNumber");
    }

    private int createContract() throws Exception {

        ResultActions resultActions = mockMvc.perform(post(POST_CREATE_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(INSURANCE_MODEL_1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.contractNumber").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderId").isNotEmpty());

        return TestUtils.extractDataFromResultJson(resultActions, "$.id");
    }

    /**
     * Тест сервиса PUT /insurance-service/v2/contracts/{id}
     * для проверки получения корректного ответа при обновлении договора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Ignore
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeSuccessChangeStatusMultiThread() throws Exception {
        List<Thread> threads = new ArrayList<>();
        List<String> contracts = new ArrayList<>();
        IntStream.rangeClosed(0, 5).forEach(id -> threads.add(new Thread(() -> {
            try {
                contracts.add(checkChangeStatus(createContract()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        })));

        threads.parallelStream().forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }

        //Проверка того, что каждый каждый запрос пользователя был обслужен
        assertEquals(threads.size(), contracts.size());
        //Проверка каждого полученного договора на соответствие регулярному выражению
        contracts.forEach(ContractNumberSequenceServiceTest::checkShortGeneratedContractNumber);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts/extract
     * для проверки получения корректного ответа при формировании выписки по оформленным договорам
     * за указанный период.
     * Запрос выполняется от пользователя с ролью "Начальник отдела розничных продаж".
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsExtractByDepHeadSuccess() throws Exception {
        getExtractStatusAndContent(GET_CONTRACTS_EXTRACT_REQUEST);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts/extract/universal
     * для проверки получения корректного ответа при формировании выписки по оформленным договорам
     * за указанный период.
     * Запрос выполняется от пользователя с ролью "Начальник отдела розничных продаж".
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsUniversalExtractByDepHeadSuccess() throws Exception {
        getExtractStatusAndContent(GET_CONTRACTS_UNIVERSAL_EXTRACT_REQUEST);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts/extract
     * для для ситуации, когда данный отчет уже формируется.
     * <p>
     * Запрос выполняется от пользователя с ролью "Начальник отдела розничных продаж".
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsExistingExtractByDepHeadSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        final String uuid = "d1bd76e2-5762-4b6a-adee-5c5c46826240";

        CountDownLatch lockTaken = new CountDownLatch(1);
        CountDownLatch lockMustBeRelease = new CountDownLatch(1);

        Thread thread = new Thread(() -> extractServiceLock.lockExtract(uuid, lockTaken, lockMustBeRelease));
        thread.start();

        lockTaken.await();

        String startDate = "08.07.2016";
        String endDate = "20.12.2018";
        mockMvc.perform(get(GET_CONTRACTS_EXTRACT_REQUEST, startDate, endDate, "ISJ")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid", is(uuid)));

        lockMustBeRelease.countDown();

        // Должно выполниться 2 select-запроса к БД:
        // 1 запрос к right_to_permission
        // 1 на проверку существования Extract
        assertSelectCount(2);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts/extract
     * для для ситуации, когда данный отчет с такой же хэш-суммой уже был, но "завис" в статусе CREATING.
     * В этом случае необходимо запускать генерацию отчёта снова.
     * <p>
     * Запрос выполняется от пользователя с ролью "Начальник отдела розничных продаж".
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getContractsExtractWithHungExtractByDepHeadSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        String startDate = "08.07.2016";
        String endDate = "20.12.2018";
        mockMvc.perform(get(GET_CONTRACTS_EXTRACT_REQUEST, startDate, endDate, "ISJ")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid", not("d1bd76e2-5762-4b6a-adee-5c5c46826240")));

        // Должно выполниться 4 select-запроса к БД:
        // 1 запрос к right_to_permission
        // 1 на проверку существования Extract
        // 1 запрос на получение списка договоров
        // 1 на поиск сущности Extract для обновления
        assertSelectCount(4);

        // Должен выполниться 1 insert-запрос к БД:
        // 1 на создание сущности Extract
        assertInsertCount(1);

        // Должен выполниться 2 setInsuranceCodeSettingsNumber-запрос к БД:
        // 1 на обновление статуса Extract
        // 1 на сохранение файла с выгрузкой в БД
        assertUpdateCount(2);
    }

    public void getExtractStatusAndContent(String url) throws Exception {
        SQLStatementCountValidator.reset();

        String startDate = "07.07.2016";
        String endDate = "20.12.2018";

        ResultActions resultActions = mockMvc.perform(get(url, startDate, endDate, "ISJ")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Должно выполниться 4 select-запроса к БД:
        // 1 запрос к right_to_permission
        // 1 на проверку существования Extract
        // 1 запрос на получение списка договоров
        // 1 на поиск сущности Extract для обновления
        assertSelectCount(4);

        // Должен выполниться 1 insert-запрос к БД:
        // 1 на создание сущности Extract
        assertInsertCount(1);

        // Должен выполниться 2 setInsuranceCodeSettingsNumber-запрос к БД:
        // 1 на обновление статуса Extract
        // 1 на сохранение файла с выгрузкой в БД
        assertUpdateCount(2);

        SQLStatementCountValidator.reset();

        String uuid = TestUtils.extractDataFromResultJson(resultActions, "$.uuid");

        mockMvc.perform(get(GET_CONTRACTS_EXTRACT_REQUEST_STATUS, uuid)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("SAVE")));

        // Должно выполниться 2 запроса к БД при каждом обращении:
        // 1 запрос к right_to_permission
        // 1 на получение информации по статусу загрузки
        assertSelectCount(2);

        SQLStatementCountValidator.reset();

        mockMvc.perform(get(GET_CONTRACTS_EXTRACT_CONTENT_REQUEST, uuid)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос к extract для получения информации о загрузке
        // 1 запрос к extract для контента загрузки
        assertSelectCount(3);
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Ignore //TODO починить тест
    public void setRevokedReplacementStatusSuccess() throws Exception {

        //проверяем, что количество договоров при просмотре под пользователем равно трём
        ResultActions resultActions = mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].contractNumber", is("23К010110000003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].startDate", is("28.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].status", is("MADE")));

        Integer contractId = TestUtils.extractDataFromResultJson(resultActions, "$.content[1].id");

        //проверяем, что количество договоров при просмотре под админом равно пяти
        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id", is(contractId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].contractNumber", is("23К010110000003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].status", is("MADE")));

        //Проверка, что пользователю не доступен перевод в статус Анулирован по замене
        mockMvc.perform(post(CHANGE_CONTRACT_STATUS_REQUEST, contractId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(CHANGE_STATUS_REVOKED_REPLACEMENT_MODEL))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(400));
        //Проверка, успешного перевода договора в статус анулирован по замене
        mockMvc.perform(post(CHANGE_CONTRACT_STATUS_REQUEST, contractId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(CHANGE_STATUS_REVOKED_REPLACEMENT_MODEL))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        //проверяем, что после перевода в статус количество договоров при просмотре под пользователем не изменилось,
        //но изменился статус и id договора
        resultActions = mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contractNumber", is("23К010110000003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status", is("DRAFT")));

        Integer copyContractId = TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id");

        assertNotEquals(contractId, copyContractId);

        //проверяем, что для админа доступны для просмотра оба договора, копия и исходный, что для них отображаются одинаковые номера договора
        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(7)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(copyContractId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contractNumber", is("23К010110000003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].id", is(contractId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].contractNumber", is("23К010110000003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].status", is("REVOKED_REPLACEMENT")));

        //проверка, что при фильтрации по номеру для админа отображаются оба договора
        mockMvc.perform(put(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterContractsRq(null, "", null, null, "23К010110000003", null, null, null, null, null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(copyContractId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contractNumber", is("23К010110000003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id", is(contractId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].contractNumber", is("23К010110000003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].status", is("REVOKED_REPLACEMENT")));
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contracts/fullSetDocument/{id}
     * для проверки установки признака получения полного комплекта документов.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setFullSetDocuments() throws Exception {

        mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullSetDocument", is(false)));

        FullSetDocumentData data = new FullSetDocumentData();
        data.setFullSetDocument(true);
        data.setCommentForNotFullSetDocument("Невалидный документ");
        mockMvc.perform(post(CHANGE_FULL_SET_DOCUMENT_REQUEST, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

        mockMvc.perform(post(CHANGE_FULL_SET_DOCUMENT_REQUEST, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullSetDocument", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentForNotFullSetDocument", is("Невалидный документ")));
    }

    /**
     * Тест сервиса GET /insurance-service/v2/contracts
     * для проверки получения корректного ответа при указании только параметров пегинации.
     * Запрос выполняется от имени руководителя ВСП.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script-filter.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllContractWithFilterByVSPSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_4))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)));

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(8)));

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(4)));

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_5))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(8)));

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_6))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(6)));
    }

    @Test
    @Sql(value = "classpath:test-script-filter.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void existsContractByClient_notFoundClient() throws Exception {
        mockMvc.perform(get(INSURANCE_SERVICE_V_2_CONTRACTS_BY_CLIENT_ID_EXISTS, 99999)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_4)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(Boolean.FALSE)));
    }

    @Test
    @Sql(value = "classpath:test-script-filter.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void existsContractByClient_statusDraft() throws Exception {
        mockMvc.perform(get(INSURANCE_SERVICE_V_2_CONTRACTS_BY_CLIENT_ID_EXISTS, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_4)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(Boolean.FALSE)));
    }

    @Test
    @Sql(value = "classpath:test-script-filter.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void existsContractByClient_statusMade() throws Exception {
        mockMvc.perform(get(INSURANCE_SERVICE_V_2_CONTRACTS_BY_CLIENT_ID_EXISTS, 4)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_4)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(Boolean.TRUE)));
    }
}