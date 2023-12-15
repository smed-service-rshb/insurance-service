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
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import java.util.Arrays;
import java.util.Collections;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertInsertCount;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class ProgramControllerTest {

    private static final String PUT_PROGRAMS_REQUEST = "/insurance-service/v1/dict/programs";
    private static final String GET_PROGRAM_BY_ID_REQUEST = "/insurance-service/v1/dict/programs/%s";
    private static final String CREATE_PROGRAM_REQUEST = "/insurance-service/v1/dict/programs";
    private static final String UPDATE_PROGRAM_REQUEST = "/insurance-service/v1/dict/programs/%s";
    private static final String DELETE_PROGRAM_REQUEST = "/insurance-service/v1/dict/programs/%s";

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


    private ParameterizedTypeReference<RestPageImpl<ProgramData>> restPage() {
        return new ParameterizedTypeReference<RestPageImpl<ProgramData>>() {
        };
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/programs
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllProgramByUserSuccess() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(put(PUT_PROGRAMS_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_2))
                .param("hasFilter", "false")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterProgramsRq(null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("Классический ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nameForPrint", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].number", is("001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].policyCode", is("Я01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].option", is("01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coolingPeriod", is(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].waitingPeriod").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].relatedOfficeFilterType").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].relatedGroupFilterType").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].relatedOffices").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].relatedGroups").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].segment").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].activeFlag", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].number", is("002")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].policyCode", is("Ю99")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].option", is("01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].coolingPeriod", is(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].waitingPeriod").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].relatedOfficeFilterType", is("INCLUDE_ALL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].relatedGroupFilterType", is("INCLUDE_ALL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].relatedOffices").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].relatedGroups").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].segment").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].activeFlag", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].name", is("Рента")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].kind", is("RENT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].number", is("006")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].policyCode", is("Р99")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].option", is("01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].coolingPeriod", is(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].waitingPeriod").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].relatedOfficeFilterType", is("INCLUDE_ALL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].relatedGroupFilterType", is("INCLUDE_ALL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].relatedOffices").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].relatedGroups").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].segment").doesNotExist());

        // Должно выполниться 4 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка программ и сегментов
        // 1 запрос на получение списка связанных элементов организационной структуры организации
        // 1 запрос на получение списка связанных с программой страхования групп пользователей
        assertSelectCount(4);
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllProgramByUserSuccessFiltered() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(put(PUT_PROGRAMS_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_2))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterProgramsRq( ProgramKind.ISJ, "Классический ИСЖ", null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("Классический ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nameForPrint", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].number", is("001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].policyCode", is("Я01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].option", is("01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coolingPeriod", is(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].waitingPeriod").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].relatedOfficeFilterType").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].relatedOffices").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].segment").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].activeFlag", is(true)));


        // Должно выполниться 4 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка программ и сегментов
        // 1 запрос на получение списка связанных элементов организационной структуры организации
        // 1 запрос на получение списка связанных с программой страхования групп пользователей
        assertSelectCount(4);
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/programs
     * для проверки получения корректного ответа.
     * Должны быть выбраны только программы страхования, доступные для группы пользователей "АЛЬФА Страхование".
     * Это программы страхования с идентификаторами 1, 2, 6, 7, 8.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkFilteringByEmployeeGroups() throws Exception {

        mockMvc.perform(put(PUT_PROGRAMS_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_3))
                .param("hasFilter", "false")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterProgramsRq(null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[?(@.id==5)]", hasSize(0)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/programs
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllProgramByProductAdminSuccess() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(put(PUT_PROGRAMS_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .param("hasFilter", "false")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterProgramsRq(null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("Классический ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nameForPrint", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].number", is("001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].policyCode", is("Я01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].option", is("01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].coolingPeriod", is(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].waitingPeriod").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].relatedOfficeFilterType").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].relatedOffices").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].segment").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].activeFlag", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].number", is("002")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].policyCode", is("Ю99")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].option", is("01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].coolingPeriod", is(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].waitingPeriod").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].relatedOfficeFilterType", is("INCLUDE_ALL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].relatedOffices").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].segment").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].activeFlag", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].name", is("Упрощённый НСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].kind", is("NSJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].activeFlag", is(true)))
                ;

        // Должно выполниться 4 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение списка программ и сегментов
        // 1 запрос на получение списка связанных элементов организационной структуры организации
        // 1 запрос на получение списка связанных с программой страхования групп пользователей
        assertSelectCount(4);
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/programs
     * для получения отказа по причине отсутствия прав на просмотр записей в справочнике программ страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllProgramForbidden() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(put(PUT_PROGRAMS_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(COACH_PRINCIPAL_DATA))
                .param("hasFilter", "false")
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterProgramsRq(null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

        // Должен выполниться 1 запрос к right_to_permission
        assertSelectCount(1);
    }


    /**
     * Тест сервиса GET /insurance-service/v1/dict/programs/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getProgramByIdSuccess() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(get(String.format(GET_PROGRAM_BY_ID_REQUEST, "2"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Купонный ИСЖ")));

        // Должно выполниться 4 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение программы и сегмента
        // 1 запрос на получение списка связанных элементов групп пользователей
        // 1 запрос на получение списка связанных с программой страхования групп пользователей
        assertSelectCount(4);

        mockMvc.perform(get(String.format(GET_PROGRAM_BY_ID_REQUEST, "2"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Купонный ИСЖ")));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/programs/{id}
     * для получения отказа по причине отсутствия у текущего пользователя доступа к существующей программе страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getProgramByIdNotFoundBecauseNoAccess() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(get(String.format(GET_PROGRAM_BY_ID_REQUEST, "3"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_2))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        // Должно выполниться 4 запроса к БД:
        // 1 запрос к right_to_permission
        // 1 запрос на получение программы и сегмента
        assertSelectCount(2);

        mockMvc.perform(get(String.format(GET_PROGRAM_BY_ID_REQUEST, "4"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(String.format(GET_PROGRAM_BY_ID_REQUEST, "5"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/programs/{id}
     * для получения отказа по причине отсутствия прав на просмотр записей в справочнике программ страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getProgramByIdForbidden() throws Exception {

        mockMvc.perform(get(String.format(GET_PROGRAM_BY_ID_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(COACH_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/programs создания записи в справочнике программ страхования.
     * Ожидается получение получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Ignore
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createProgramSuccess() throws Exception {

        ProgramData programData = new ProgramData();
        programData.setName("Тестовый ИСЖ");
        programData.setNameForPrint("Программа страхования \"Тестовый ИСЖ\"");
        programData.setKind(ProgramKind.ISJ);
        programData.setNumber("123");
        programData.setPolicyCode("Б12");
        programData.setOption("12");
        programData.setCoolingPeriod(15);
        programData.setSegment(new Segment(2L, "Премиальный"));
        programData.setRelatedOfficeFilterType(RelatedOfficeFilterType.INCLUDE);
        programData.setRelatedOffices(Arrays.asList("2310", "0001"));
        programData.setComulation(10L);
        programData.setProgramCode("A");
        programData.setProgramTariff("S");
        programData.setProgramCharCode("ММ");
        programData.setActiveFlag(true);

        SQLStatementCountValidator.reset();

        ResultActions resultActions = mockMvc.perform(post(CREATE_PROGRAM_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Должно выполниться 5 запросов к БД:
        // 1 select-запрос к таблице program_v2
        // 1 select-запрос к таблице segment
        // 1 insert-запрос на вставку данных в таблицу program_v2
        // 2 insert-запроса на вставку данных в таблицу program_related_offices
        assertSelectCount(2);
        assertInsertCount(3);

        int programId = TestUtils.extractDataFromResultJson(resultActions, "$.id");

        mockMvc.perform(get(String.format(GET_PROGRAM_BY_ID_REQUEST, programId))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Тестовый ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nameForPrint", is("Программа страхования \"Тестовый ИСЖ\"")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number", is("123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.policyCode", is("Б12")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.option", is("12")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coolingPeriod", is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.waitingPeriod").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.relatedOfficeFilterType", is("INCLUDE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.relatedOffices", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.segment.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.segment.name", is("Премиальный")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comulation", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programCode", is("A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programCharCode", is("ММ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.activeFlag", is(true)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/programs создания записи в справочнике программ страхования.
     * Ожидается получение получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Ignore
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createProgramBadRequestPolicyCodeAndOptionAlreadyExists() throws Exception {
        ProgramData programData = new ProgramData();
        programData.setName("Тестовый ИСЖ");
        programData.setNameForPrint("Программа страхования \"Тестовый ИСЖ\"");
        programData.setKind(ProgramKind.ISJ);
        programData.setNumber("123");
        programData.setPolicyCode("Я01");
        programData.setOption("01");
        programData.setCoolingPeriod(15);
        programData.setSegment(new Segment(2L, "Премиальный"));
        programData.setRelatedOfficeFilterType(RelatedOfficeFilterType.INCLUDE);
        programData.setRelatedOffices(Arrays.asList("2310", "0001"));
        programData.setActiveFlag(true);

        SQLStatementCountValidator.reset();

        mockMvc.perform(post(CREATE_PROGRAM_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        // Должен выполниться 1 запрос к БД:
        // 1 select-запрос к таблице program_v2
        assertSelectCount(1);
        assertInsertCount(0);
    }


    /**
     * Тест сервиса POST /insurance-service/v1/dict/programs
     * для получения отказа по причине отсутствия прав на создание записи в справочнике программ страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createProgramForbidden() throws Exception {

        ProgramData programData = new ProgramData();
        programData.setName("SOMETHING");
        programData.setKind(ProgramKind.ISJ);
        programData.setNumber("123");
        programData.setPolicyCode("Б12");
        programData.setOption("12");
        programData.setCoolingPeriod(15);
        programData.setSegment(new Segment(1L, "vip"));
        programData.setActiveFlag(true);

        mockMvc.perform(post(CREATE_PROGRAM_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/programs
     * для получения отказа по причине неправильного формата запроса (не указан вид программы страхования).
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createProgramBadRequest() throws Exception {

        ProgramData programData = new ProgramData();
        programData.setName("SOMETHING");
        programData.setNumber("123");
        programData.setPolicyCode("Б12");
        programData.setOption("12");
        programData.setCoolingPeriod(15);
        programData.setSegment(new Segment(1L, "vip"));
        programData.setActiveFlag(true);

        mockMvc.perform(post(CREATE_PROGRAM_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/programs/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateProgramSuccess() throws Exception {

        ProgramData programData = new ProgramData();
        programData.setId(3L);
        programData.setName("Тестовый ИСЖ");
        programData.setNameForPrint("Программа страхования \"Тестовый ИСЖ\"");
        programData.setNumber("123");
        programData.setKind(ProgramKind.ISJ);
        programData.setPolicyCode("К01");
        programData.setCoolingPeriod(1);
        programData.setActiveFlag(true);
        programData.setOption("12");
        Segment segment = new Segment();
        segment.setName("vip");
        segment.setId(2L);
        programData.setSegment(segment);
        programData.setRelatedOfficeFilterType(RelatedOfficeFilterType.EXCLUDE);
        programData.setRelatedOffices(Collections.singletonList("2310"));
        programData.setRelatedGroupFilterType(RelatedOfficeFilterType.EXCLUDE);
        programData.setRelatedGroups(Arrays.asList("benefit","alpha"));

        mockMvc.perform(put(String.format(UPDATE_PROGRAM_REQUEST, "3"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_PROGRAM_BY_ID_REQUEST, "3"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Тестовый ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nameForPrint", is("Программа страхования \"Тестовый ИСЖ\"")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number", is("123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.policyCode", is("К01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.option", is("12")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coolingPeriod", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.waitingPeriod").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.relatedOfficeFilterType", is("EXCLUDE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.relatedGroupFilterType", is("EXCLUDE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.relatedOffices", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.relatedGroups", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.segment.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.segment.name", is("Премиальный")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.activeFlag", is(true)));
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/programs/{id}
     * для получения отказа по причине отсутствия прав на редактирование записи справочника программ страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateProgramForbidden() throws Exception {

        ProgramData programData = new ProgramData();
        programData.setId(1L);
        programData.setName("SOMETHING");
        programData.setNumber("123");
        programData.setKind(ProgramKind.ISJ);
        programData.setPolicyCode("К01");
        programData.setCoolingPeriod(1);
        programData.setActiveFlag(true);
        programData.setOption("12");
        Segment segment = new Segment();
        segment.setName("vip");
        segment.setId(2L);
        programData.setSegment(segment);

        mockMvc.perform(put(String.format(UPDATE_PROGRAM_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/programs/{id}
     * для получения отказа по причине отсутствия записи с указанным идентификатором в справочнике программ страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateProgramNotFound() throws Exception {

        ProgramData programData = new ProgramData();
        programData.setId(-1L);
        programData.setName("SOMETHING");
        programData.setNumber("123");
        programData.setKind(ProgramKind.ISJ);
        programData.setPolicyCode("К01");
        programData.setCoolingPeriod(1);
        programData.setActiveFlag(true);
        programData.setOption("12");
        Segment segment = new Segment();
        segment.setName("vip");
        segment.setId(2L);
        programData.setSegment(segment);

        mockMvc.perform(put(String.format(UPDATE_PROGRAM_REQUEST, "-1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/programs/{id}
     * для получения отказа по причине вхождения в список запрещённых групп пользователей для данной программы.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeNotFoundBecauseRestrictedGroup() throws Exception {
        mockMvc.perform(get(String.format(GET_PROGRAM_BY_ID_REQUEST, "5"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_3))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/programs/{id}
     * для получения успеха по причине отсутствия вхождения в список запрещённых групп пользователей для данной программы.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeOkBecauseAvailGroup() throws Exception {
        mockMvc.perform(get(String.format(GET_PROGRAM_BY_ID_REQUEST, "5"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/programs/{id}
     * для получения отказа по причине неправильного формата запроса.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateProgramBadRequest() throws Exception {

        ProgramData programData = new ProgramData();
        programData.setId(1L);
        programData.setName("SOMETHING");

        mockMvc.perform(put(UPDATE_PROGRAM_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }


    /**
     * Тест сервиса DELETE /insurance-service/v1/dict/programs/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteProgramSuccess() throws Exception {

        mockMvc.perform(delete(String.format(DELETE_PROGRAM_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Тест сервиса DELETE /insurance-service/v1/dict/programs/{id}
     * для получения отказа по причине отсутствия прав на удаление записи в справочнике программ страхования.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteProgramForbidden() throws Exception {

        mockMvc.perform(delete(String.format(DELETE_PROGRAM_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

}
