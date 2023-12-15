package ru.softlab.efr.services.insurance.controllers;


import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.rest.ProgramData;
import ru.softlab.efr.services.insurance.model.rest.StrategyData;
import ru.softlab.efr.services.insurance.model.rest.StrategyProperty;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.USER_PRINCIPAL_DATA;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class StrategyControllerTest {

    private static final String GET_ENTITYLIST_REQUEST = "/insurance-service/v1/dict/strategies";
    private static final String GET_ENTITY_BY_ID_REQUEST = "/insurance-service/v1/dict/strategy/%s";
    private static final String CREATE_ENTITY_REQUEST = "/insurance-service/v1/dict/strategy";
    private static final String UPDATE_ENTITY_REQUEST = "/insurance-service/v1/dict/strategy/%s";
    private static final String DELETE_ENTITY_REQUEST = "/insurance-service/v1/dict/strategy/%s";
    private static final String PUT_STRATEGIES_BY_RATE = "/insurance-service/v1/dict/strategies?coefficient=%s";

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


    /**
     * Тест сервиса GET /insurance-service/v1/program
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllStrategiesSuccess() throws Exception {

        mockMvc.perform(get(GET_ENTITYLIST_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("Глобальные облигации")));
    }

    /**
     * Тест сервиса PUT "/insurance-service/v1/dict/strategies?coefficient=%s"
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getStrategiesByRate() throws Exception {
        ResultActions response = mockMvc.perform(put(String.format(PUT_STRATEGIES_BY_RATE, "1.00"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        String responseString = response.andReturn().getResponse().getContentAsString();
        Pattern pattern = Pattern.compile("\"coefficient\":(.*?),");
        Matcher matcher = pattern.matcher(responseString);
        if (matcher.find()) {
            Assert.assertEquals("1.00", (matcher.group(1)));
        }
    }

    /**
     * Тест сервиса GET /insurance-service/v1/strategies
     * для проверки при получении ошибки.
     *
     * @throws Exception при падении теста
     */
    /*@Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllStrategiesError() throws Exception {

        mockMvc.perform(get(GET_ENTITYLIST_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }*/


    /**
     * Тест сервиса GET /insurance-service/v1/dict/strategies/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getStrategyByIdSuccess() throws Exception {

        mockMvc.perform(get(String.format(GET_ENTITY_BY_ID_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Глобальные облигации")));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/strategies/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createStrategySuccess() throws Exception {

        StrategyData entity = new StrategyData();
        entity.setName("SOMETHING");
        entity.setStartDate(LocalDate.now());
        entity.setEndDate(LocalDate.now());
        entity.setPolicyCode(1);
        entity.setStrategyProperties(Arrays.asList(new StrategyProperty(null, BigDecimal.ONE, "TICKER", null, null),
                new StrategyProperty(null, BigDecimal.TEN, "TICKER", null, null)));
        entity.setDescription("DESCRIPTION");

        mockMvc.perform(post(CREATE_ENTITY_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(entity))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties[0].coefficient", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties[1].coefficient", is(10)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/strategies/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createStrategyFail() throws Exception {

        StrategyData entity = new StrategyData();
        entity.setName("SOMETHING");
        entity.setStartDate(LocalDate.now());
        entity.setEndDate(LocalDate.now());
        entity.setPolicyCode(1);
        entity.setStrategyProperties(Arrays.asList(new StrategyProperty(null, null, "TICKER", null, null),
                new StrategyProperty(null, BigDecimal.TEN, "TICKER", null, null)));
        entity.setDescription("DESCRIPTION");

        mockMvc.perform(post(CREATE_ENTITY_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(entity))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)));
                //.andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]", is("В одном из наборов не указано обязательное поле - коэффициент участия")));
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/strategies/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateStrategySuccess() throws Exception {

        StrategyData entity = new StrategyData();
        entity.setId(1L);
        entity.setName("SOMETHING");
        entity.setStartDate(LocalDate.now());
        entity.setEndDate(LocalDate.now());
        entity.setPolicyCode(1);
        entity.setStrategyProperties(Arrays.asList(new StrategyProperty(2L, BigDecimal.valueOf(123L), "TICKER", null, null),new StrategyProperty(null, BigDecimal.valueOf(321L), "TICKER", null, null)));

        mockMvc.perform(get(String.format(GET_ENTITY_BY_ID_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Глобальные облигации")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties[0].coefficient", is(1.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties[1].coefficient", is(2.0)));

        mockMvc.perform(put(String.format(UPDATE_ENTITY_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(entity))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_ENTITY_BY_ID_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("SOMETHING")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties[0].coefficient", is(123.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties[1].id", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyProperties[1].coefficient", is(321.0)));
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/strategies/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateStrategyFail() throws Exception {

        ProgramData programData = new ProgramData();
        programData.setId(1L);
        programData.setName("SOMETHING");

        mockMvc.perform(put(UPDATE_ENTITY_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }


    /**
     * Тест сервиса DELETE /insurance-service/v1/dict/strategies/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteStrategySuccess() throws Exception {

        mockMvc.perform(delete(String.format(DELETE_ENTITY_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

}
