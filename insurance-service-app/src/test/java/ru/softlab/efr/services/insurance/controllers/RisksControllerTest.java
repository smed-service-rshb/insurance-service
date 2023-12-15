package ru.softlab.efr.services.insurance.controllers;

import org.junit.Before;
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
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.rest.PaymentMethod;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.RiskData;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.*;

@SuppressWarnings("Duplicates")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class RisksControllerTest {

    private static final String POST_CREATE_RISK_REQUEST = "/insurance-service/v1/dict/risks";
    private static final String GET_ALL_RISK_REQUEST = "/insurance-service/v1/dict/risks";
    private static final String GET_RISK_BY_ID_REQUEST = "/insurance-service/v1/dict/risks/{id}";
    private static final String PUT_RISK_BY_ID_REQUEST = "/insurance-service/v1/dict/risks/{id}";

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Rule
    @Autowired
    public HibernateCacheInvalidationRule hibernateCacheInvalidationRule;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    private ParameterizedTypeReference<RestPageImpl<RiskData>> restPage() {
        return new ParameterizedTypeReference<RestPageImpl<RiskData>>() {
        };
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/risks
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateRiskSuccess() throws Exception {
        RiskData riskData = new RiskData();
        riskData.setProgram(ProgramKind.KSP);
        riskData.setName("Test risk");
        riskData.setPaymentMethod(PaymentMethod.ONCE);
        riskData.setFullName("Test risk full name");

        mockMvc.perform(post(POST_CREATE_RISK_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(riskData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/risks
     * для проверки получения ошибки при попытки вставить сущность без заполненных полей.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateRiskErrorEmptyField() throws Exception {
        RiskData riskData = new RiskData();

        mockMvc.perform(post(POST_CREATE_RISK_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(riskData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/risks
     * для проверки ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateRiskError() throws Exception {
        RiskData riskData = new RiskData();
        riskData.setProgram(ProgramKind.KSP);
        riskData.setName("Test risk");
        riskData.setFullName("Test risk full name");

        mockMvc.perform(post(POST_CREATE_RISK_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(riskData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/risks
     * для проверки получения корректного ответа при указании параметров пегинации.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllRiskWithParameterSuccess() throws Exception {

        mockMvc.perform(get(GET_ALL_RISK_REQUEST)
                .param("sort", "id")
                .param("page", "0")
                .param("size", "10")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name", is("Смерть по любой причине")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].fullName", is("Смерть по любой причине")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].program", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(17)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/risks
     * для проверки получения корректного ответа без указания параметров пегинации.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllRiskSuccess() throws Exception {

        mockMvc.perform(get(GET_ALL_RISK_REQUEST)
                .param("sort", "id")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(17)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("ЖИТИЕ МОЕ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].program", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(17)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/risks
     * для проверки получения ответа для пользователя с правом создания договоров.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllRiskAtCreationContractSuccess() throws Exception {

        mockMvc.perform(get(GET_ALL_RISK_REQUEST)
                .param("sort", "id")
                .param("page", "0")
                .param("size", "10")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name", is("Смерть по любой причине")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].program", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(17)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/risks
     * для проверки получения ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllRiskError() throws Exception {

        mockMvc.perform(get(GET_ALL_RISK_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/risks/:id
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRiskByIdSuccess() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(GET_ALL_RISK_REQUEST)
                .param("sort", "id")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        int id = TestUtils.extractDataFromResultJson(resultActions,"$.content[0].id");

        mockMvc.perform(get(GET_RISK_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(id)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/risks/:id
     * для проверки получения корректного ответа пользователя с правами создания договора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRiskByIdAtCreationContractSuccess() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(GET_ALL_RISK_REQUEST)
                .param("sort", "id")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        int id = TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id");

        mockMvc.perform(get(GET_RISK_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(id)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/risks/:id
     * для проверки получения ответа при отсутствии записи в справочнике.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRiskByIdNotFound() throws Exception {

        mockMvc.perform(get(GET_RISK_BY_ID_REQUEST, 5000)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/requiredDocuments/:id
     * для проверки получения ответа при отсутствии прав.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRiskByIdError() throws Exception {

        mockMvc.perform(get(GET_RISK_BY_ID_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/risks/:id
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putRequiredDocumentSuccess() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(GET_ALL_RISK_REQUEST)
                .param("sort", "id")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        int id = TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id");

        RiskData riskData = new RiskData();
        riskData.setProgram(ProgramKind.KSP);
        riskData.setFullName("TestFull");
        riskData.setName("Test");
        mockMvc.perform(put(PUT_RISK_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(riskData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_RISK_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.program", is("KSP")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", is("TestFull")));
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/risks/:id
     * для проверки получения ответа при отсутствии записи в справочнике.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putRiskByIdNotFound() throws Exception {
        RiskData riskData = new RiskData();
        riskData.setProgram(ProgramKind.KSP);
        riskData.setFullName("TestFull");
        riskData.setName("Test");
        mockMvc.perform(put(PUT_RISK_BY_ID_REQUEST, 10000)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(riskData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/risks/:id
     * для проверки получения ответа при отсутствии прав.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putRequiredDocumentByIdError() throws Exception {
        RiskData riskData = new RiskData();
        riskData.setProgram(ProgramKind.KSP);
        riskData.setFullName("TestFull");
        riskData.setName("Test");
        mockMvc.perform(put(PUT_RISK_BY_ID_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(riskData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

}
