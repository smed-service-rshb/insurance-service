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
import ru.softlab.efr.services.insurance.model.rest.PaymentPeriodicity;
import ru.softlab.efr.services.insurance.model.rest.RedemptionCoefficientData;
import ru.softlab.efr.services.insurance.model.rest.RedemptionData;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class RedemptionControllerTest {

    private static final String POST_CREATE_REDEMPTION_REQUEST = "/insurance-service/v1/dict/redemption";
    private static final String GET_ALL_REDEMPTION_REQUEST = "/insurance-service/v1/dict/redemption";
    private static final String GET_REDEMPTION_BY_ID_REQUEST = "/insurance-service/v1/dict/redemption/{id}";
    private static final String PUT_REDEMPTION_BY_ID_REQUEST = "/insurance-service/v1/dict/redemption/{id}";
    private static final String DELETE_REDEMPTION_BY_ID_REQUEST = "/insurance-service/v1/dict/redemption/{id}";

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

    private ParameterizedTypeReference<RestPageImpl<RedemptionData>> restPage() {
        return new ParameterizedTypeReference<RestPageImpl<RedemptionData>>() {
        };
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/redemption
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateRedemptionSuccess() throws Exception {
        RedemptionData redemption = new RedemptionData();
        redemption.setProgramId(1L);
        redemption.setDuration(1);
        redemption.setCurrencyId(1L);
        redemption.setPeriodicity(PaymentPeriodicity.YEARLY);
        redemption.setPaymentPeriod(PaymentPeriodicity.YEARLY);
        RedemptionCoefficientData coefficient = new RedemptionCoefficientData();
        coefficient.setPeriod(1);
        coefficient.setCoefficient(BigDecimal.TEN);
        redemption.setCoefficientList(Collections.singletonList(coefficient));
        mockMvc.perform(post(POST_CREATE_REDEMPTION_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(redemption))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/redemption
     * для проверки получения ошибки при попытки вставить сущность без заполненных полей.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateRedemptionErrorEmptyField() throws Exception {
        RedemptionData redemption = new RedemptionData();

        mockMvc.perform(post(POST_CREATE_REDEMPTION_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(redemption))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/redemption
     * для проверки ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateRedemptionError() throws Exception {
        RedemptionData redemption = new RedemptionData();
        redemption.setProgramId(1L);
        redemption.setDuration(1);
        redemption.setCurrencyId(1L);
        redemption.setPeriodicity(PaymentPeriodicity.YEARLY);
        redemption.setPaymentPeriod(PaymentPeriodicity.YEARLY);
        RedemptionCoefficientData coefficient = new RedemptionCoefficientData();
        coefficient.setPeriod(1);
        coefficient.setCoefficient(BigDecimal.TEN);
        redemption.setCoefficientList(Collections.singletonList(coefficient));
        mockMvc.perform(post(POST_CREATE_REDEMPTION_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(redemption))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/redemption
     * для проверки ответа при дублировании связки прогррамма, срок, валюта, периодичность.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateRedemptionDuplicateError() throws Exception {
        RedemptionData redemption = new RedemptionData();
        redemption.setProgramId(1L);
        redemption.setDuration(1);
        redemption.setCurrencyId(1L);
        redemption.setPeriodicity(PaymentPeriodicity.YEARLY);
        redemption.setPaymentPeriod(PaymentPeriodicity.YEARLY);
        RedemptionCoefficientData coefficient = new RedemptionCoefficientData();
        coefficient.setPeriod(1);
        coefficient.setCoefficient(BigDecimal.TEN);
        redemption.setCoefficientList(Collections.singletonList(coefficient));

        mockMvc.perform(post(POST_CREATE_REDEMPTION_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(redemption))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(post(POST_CREATE_REDEMPTION_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(redemption))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/redemption
     * для проверки получения корректного ответа при указании параметров пегинации.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllRedemptionWithParameterSuccess() throws Exception {

        mockMvc.perform(get(GET_ALL_REDEMPTION_REQUEST)
                .param("sort", "id")
                .param("page", "0")
                .param("size", "10")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].programId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].programName", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].currencyId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duration", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].periodicity", is("YEARLY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].paymentPeriod", is("YEARLY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[0].period", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[0].coefficient", is(1.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[1].period", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[1].coefficient", is(2.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[2].period", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[2].coefficient", is(1.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(2)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/redemption
     * для проверки получения корректного ответа без указания параметров пегинации.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllRedemptionSuccess() throws Exception {

        mockMvc.perform(get(GET_ALL_REDEMPTION_REQUEST)
                .param("sort", "id")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].programId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].programName", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].currencyId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duration", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].paymentPeriod", is("YEARLY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].periodicity", is("YEARLY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[0].period", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[0].coefficient", is(1.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[1].period", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[1].coefficient", is(2.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[2].period", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[2].coefficient", is(1.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(2)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/redemption
     * для проверки получения ответа для пользователя с правом создания договоров.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllRedemptionAtCreationContractSuccess() throws Exception {

        mockMvc.perform(get(GET_ALL_REDEMPTION_REQUEST)
                .param("sort", "id")
                .param("page", "0")
                .param("size", "10")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].programId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].programName", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].currencyId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duration", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].periodicity", is("YEARLY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].paymentPeriod", is("YEARLY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[0].period", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[0].coefficient", is(1.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[1].period", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[1].coefficient", is(2.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[2].period", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coefficientList[2].coefficient", is(1.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(2)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/redemption
     * для проверки получения ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllRedemptionError() throws Exception {

        mockMvc.perform(get(GET_ALL_REDEMPTION_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/redemption/:id
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRedemptionByIdSuccess() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(GET_ALL_REDEMPTION_REQUEST)
                .param("sort", "id")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        int id = TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id");

        mockMvc.perform(get(GET_REDEMPTION_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(id)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/redemption/:id
     * для проверки получения корректного ответа пользователя с правами создания договора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRiskByIdAtCreationContractSuccess() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(GET_ALL_REDEMPTION_REQUEST)
                .param("sort", "id")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        int id = TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id");

        mockMvc.perform(get(GET_REDEMPTION_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(id)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/redemption/:id
     * для проверки получения ответа при отсутствии записи в справочнике.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRedemptionByIdNotFound() throws Exception {

        mockMvc.perform(get(GET_REDEMPTION_BY_ID_REQUEST, 5000)
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
    public void getRedemptionByIdError() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(GET_ALL_REDEMPTION_REQUEST)
                .param("sort", "id")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        int id = TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id");

        mockMvc.perform(get(GET_REDEMPTION_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/redemption/:id
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putRedemptionSuccess() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(GET_ALL_REDEMPTION_REQUEST)
                .param("sort", "id")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        int id = TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id");
        int id1 = TestUtils.extractDataFromResultJson(resultActions, "$.content[0].coefficientList[0].id");

        RedemptionData redemption = new RedemptionData();
        redemption.setProgramId(1L);
        redemption.setDuration(1);
        redemption.setCurrencyId(3L);
        redemption.setPeriodicity(PaymentPeriodicity.ONCE);
        redemption.setPaymentPeriod(PaymentPeriodicity.MONTHLY);
        RedemptionCoefficientData coefficient = new RedemptionCoefficientData();
        coefficient.setPeriod(1);
        coefficient.setId((long) id1);
        coefficient.setCoefficient(BigDecimal.TEN);
        redemption.setCoefficientList(Collections.singletonList(coefficient));
        mockMvc.perform(put(PUT_REDEMPTION_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(redemption))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_REDEMPTION_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Классический ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyId", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.periodicity", is("ONCE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentPeriod", is("MONTHLY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coefficientList", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coefficientList[0].period", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coefficientList[0].coefficient", is(10.0)));
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/redemption/:id
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putRedemptionDuplicateError() throws Exception {
        RedemptionData redemption = new RedemptionData();
        redemption.setProgramId(1L);
        redemption.setDuration(1);
        redemption.setCurrencyId(1L);
        redemption.setPeriodicity(PaymentPeriodicity.YEARLY);
        redemption.setPaymentPeriod(PaymentPeriodicity.YEARLY);
        RedemptionCoefficientData coefficient = new RedemptionCoefficientData();
        coefficient.setPeriod(1);
        coefficient.setCoefficient(BigDecimal.TEN);
        redemption.setCoefficientList(Collections.singletonList(coefficient));

        mockMvc.perform(post(POST_CREATE_REDEMPTION_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(redemption))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(put(PUT_REDEMPTION_BY_ID_REQUEST, 3)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(redemption))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict());
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/redemption/:id
     * для проверки получения ответа при отсутствии записи в справочнике.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putRedemptionByIdNotFound() throws Exception {
        RedemptionData redemption = new RedemptionData();
        redemption.setProgramId(1L);
        redemption.setDuration(1);
        redemption.setCurrencyId(3L);
        redemption.setPeriodicity(PaymentPeriodicity.MONTHLY);
        redemption.setPaymentPeriod(PaymentPeriodicity.MONTHLY);
        RedemptionCoefficientData coefficient = new RedemptionCoefficientData();
        coefficient.setPeriod(1);
        coefficient.setCoefficient(BigDecimal.TEN);
        redemption.setCoefficientList(Collections.singletonList(coefficient));
        mockMvc.perform(put(PUT_REDEMPTION_BY_ID_REQUEST, 10000)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(redemption))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/redemption/:id
     * для проверки получения ответа при отсутствии прав.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putRedemptionByIdError() throws Exception {
        RedemptionData redemption = new RedemptionData();
        redemption.setProgramId(1L);
        redemption.setDuration(1);
        redemption.setCurrencyId(3L);
        redemption.setPeriodicity(PaymentPeriodicity.MONTHLY);
        redemption.setPaymentPeriod(PaymentPeriodicity.MONTHLY);
        RedemptionCoefficientData coefficient = new RedemptionCoefficientData();
        coefficient.setPeriod(1);
        coefficient.setCoefficient(BigDecimal.TEN);
        redemption.setCoefficientList(Collections.singletonList(coefficient));
        mockMvc.perform(put(PUT_REDEMPTION_BY_ID_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(redemption))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса DELETE /insurance-service/v1/dict/redemption/:id
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteRedemptionByIdSuccess() throws Exception {

        mockMvc.perform(delete(DELETE_REDEMPTION_BY_ID_REQUEST, 3)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_ALL_REDEMPTION_REQUEST)
                .param("sort", "id")
                .param("page", "0")
                .param("size", "10")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)));
    }

    /**
     * Тест сервиса DELETE /insurance-service/v1/dict/redemption
     * для проверки получения ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteRedemptionByIdError() throws Exception {

        mockMvc.perform(delete(DELETE_REDEMPTION_BY_ID_REQUEST, 3)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());
    }

}