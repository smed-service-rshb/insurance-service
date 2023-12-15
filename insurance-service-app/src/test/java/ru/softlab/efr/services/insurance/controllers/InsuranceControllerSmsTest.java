package ru.softlab.efr.services.insurance.controllers;

import com.vladmihalcea.sql.SQLStatementCountValidator;
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
import ru.softlab.efr.services.insurance.model.rest.ViewInsuranceModel;
import ru.softlab.efr.services.insurance.service.ExtractServiceLock;
import ru.softlab.efr.services.insurance.utils.OperationLogServiceStatistics;
import ru.softlab.efr.services.insurance.utils.PrintTemplateSaveService;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.SmsTestData.CHANGE_STATUS_INSURANCE_SMS_MODEL_1;
import static ru.softlab.efr.services.insurance.stubs.SmsTestData.INSURANCE_SMS_MODEL_1;
import static ru.softlab.efr.services.insurance.stubs.TestData.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class InsuranceControllerSmsTest {

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

    @Test
    @Sql(value = "classpath:sms-test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllContractWithoutFilterByAdminSuccess() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(get(GET_ALL_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contractNumber", is("KB12000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].creationDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].startDate", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endDate", is("26.11.2021")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].closeDate", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].conclusionDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.surName", is("Иванов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.firstName", is("Иван")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.middleName", is("Иванович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docType", is("PASSPORT_RF")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docSeries", is("1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.docNumber", is("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.phoneNumber", is("79110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clientData.email", is("ivanov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("SMS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].currency", is(RUB_CURRENCY.getLiteralISO())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].premium", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rurPremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branchName", is("Амурский РФ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subdivisionName", is("2310")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status", is("MADE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeName", is("Фёдоров Фёдор Фёдорович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].programName", is("Медсоветник. Индивидуальный")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(1)))
        ;

        // Должно выполниться 2 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка договоров
        assertSelectCount(2);
    }

    @Test
    @Ignore
    @Sql(value = "classpath:sms-test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateContractSuccess() throws Exception {
        operationLogServiceStatistics.reset();

        ResultActions resultActions = mockMvc.perform(post(POST_CREATE_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(INSURANCE_SMS_MODEL_1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.contractNumber").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderId").isNotEmpty());

        int id = TestUtils.extractDataFromResultJson(resultActions, "$.id");

//        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
//        assertEquals(2, logs.size());
//        assertEquals("REGISTER_PROMO_CODE", logs.get(0).getOperationKey());
//        assertEquals("Регистрация промокода", logs.get(0).getOperationDescription());
//        assertEquals("{\n" +
//                "  \"_hash\": \"AD751B07AB2E60A1B0CBB1D67B2AA10577228875287B1A08879C9BA43A922C2B9BD8AC319EFFB0E797E7502ECCE3917AFAAA4483007247FF2C5D97500604E71F\",\n" +
//                "  \"productCode\": \"KB28000001\",\n" +
//                "  \"productId\": \"001\",\n" +
//                "  \"active\": \"false\"\n" +
//                "}", logs.get(0).getOperationParameters().get("request"));
//        assertEquals("CREATE_CONTRACT", logs.get(1).getOperationKey());
//        assertEquals((long) id, logs.get(1).getOperationParameters().get("id"));
//        assertEquals(TestUtils.extractDataFromResultJson(resultActions, "$.contractNumber"), logs.get(1).getOperationParameters().get("contractNumber"));
//        assertNotNull(logs.get(1).getOperationParameters().get("creationDate"));
//        assertEquals(OperationState.SUCCESS, logs.get(1).getOperationState());

        mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, (long) id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programSettingId", is(INSURANCE_SMS_MODEL_1.getProgramSettingId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", is(INSURANCE_SMS_MODEL_1.getDuration())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.periodicity", is(INSURANCE_SMS_MODEL_1.getPeriodicity().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calendarUnit", is(INSURANCE_SMS_MODEL_1.getCalendarUnit().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderEqualsInsured", is(INSURANCE_SMS_MODEL_1.isHolderEqualsInsured())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[1].taxResidence", is(INSURANCE_SMS_MODEL_1.getRecipientList().get(0).getTaxResidence().name().toLowerCase())))
        ;
    }

    @Test
    @Sql(value = "classpath:sms-test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void changeStatusOnlySuccess() throws Exception {
        checkChangeStatus(1);
    }

    private String checkChangeStatus(int id) throws Exception {
        mockMvc.perform(post(CHANGE_CONTRACT_STATUS_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(CHANGE_STATUS_INSURANCE_SMS_MODEL_1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        ResultActions resultActions = mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("CRM_IMPORTED")));

        return TestUtils.extractDataFromResultJson(resultActions, "$.contractNumber");
    }
}