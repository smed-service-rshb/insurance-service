package ru.softlab.efr.services.insurance.controllers;


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
import ru.softlab.efr.services.insurance.model.rest.AcquiringProgramData;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;
import static ru.softlab.efr.services.insurance.stubs.TestData.USER_PRINCIPAL_DATA;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class AcquiringProgramControllerTest {

    private static final String GET_ACQUIRING_PROGRAM_PAGE_LIST = "/insurance-service/v2/dict/acquiring/program";
    private static final String POST_ACQUIRING_CLIENT_PROGRAM = "/insurance-service/v2/dict/acquiring/program";
    private static final String GET_ACQUIRING_PROGRAM = "/insurance-service/v2/dict/acquiring/program/{id}";
    private static final String PUT_UPDATE_ACQUIRING_PROGRAM = "/insurance-service/v2/dict/acquiring/program/{id}";

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
     * Тест сервиса GET /insurance-service/v2/dict/acquiring/program
     * для проверки получения корректного ответа.
     * программы отсортированны в порядке приоритета, с одинаковым приоритетом по наименованию
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllAcquiringProgramSuccess() throws Exception {

        mockMvc.perform(get(GET_ACQUIRING_PROGRAM_PAGE_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].kind", is("KSP")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].programSettingId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].startDate", is("18.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].endDate", is("10.01.2019")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].notAuthorizedZoneEnable", is(true)))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/dict/acquiring/program
     * для проверки ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllAcquiringProgramForbidden() throws Exception {

        mockMvc.perform(get(GET_ACQUIRING_PROGRAM_PAGE_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса POST /insurance-service/v2/dict/acquiring/program
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createAcquiringProgramSuccess() throws Exception {

        AcquiringProgramData data = new AcquiringProgramData();

        data.setKind(ProgramKind.KSP);
        data.setProgramSettingId(9L);
        data.setStartDate(LocalDate.now());
        data.setEndDate(LocalDate.now().plusDays(1));
        data.setTitle("test-title");
        data.setLink("link");
        data.setDescription("desc");
        data.setAuthorizedZoneEnable(true);
        data.setApplication(true);
        data.setNotAuthorizedZoneEnable(true);
        data.setImage(1L);

        mockMvc.perform(post(POST_ACQUIRING_CLIENT_PROGRAM)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", is("test-title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("KSP")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programSettingId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("desc")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link", is("link")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorizedZoneEnable", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.notAuthorizedZoneEnable", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.application", is(true)))
        ;
    }

    /**
     * Тест сервиса POST /insurance-service/v2/dict/acquiring/program
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createAcquiringProgramBedRequest() throws Exception {

        AcquiringProgramData data = new AcquiringProgramData();

        data.setKind(ProgramKind.KSP);
        data.setStartDate(LocalDate.now());
        data.setTitle("test-title");
        data.setLink("link");
        data.setDescription("desc");
        data.setAuthorizedZoneEnable(true);
        data.setNotAuthorizedZoneEnable(true);

        mockMvc.perform(post(POST_ACQUIRING_CLIENT_PROGRAM)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/dict/acquiring/program
     * для проверки ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createAcquiringProgramForbidden() throws Exception {
        AcquiringProgramData data = new AcquiringProgramData();

        mockMvc.perform(post(POST_ACQUIRING_CLIENT_PROGRAM)
                .content(TestUtils.convertObjectToJson(data))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

 /**
     * Тест сервиса PUT /insurance-service/v2/dict/acquiring/program/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateAcquiringProgramSuccess() throws Exception {

        AcquiringProgramData data = new AcquiringProgramData();

        data.setKind(ProgramKind.KSP);
        data.setProgramSettingId(9L);
        data.setStartDate(LocalDate.now());
        data.setEndDate(LocalDate.now().plusDays(1));
        data.setTitle("test-title");
        data.setLink("link");
        data.setDescription("desc");
        data.setAuthorizedZoneEnable(true);
        data.setApplication(true);
        data.setNotAuthorizedZoneEnable(true);
        data.setImage(1L);

        mockMvc.perform(put(PUT_UPDATE_ACQUIRING_PROGRAM, 3)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", is("test-title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("KSP")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programSettingId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("desc")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link", is("link")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorizedZoneEnable", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.notAuthorizedZoneEnable", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.application", is(true)))
        ;
    }

    /**
     * Тест сервиса PUT /insurance-service/v2/dict/acquiring/program/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateAcquiringProgramBedRequest() throws Exception {

        AcquiringProgramData data = new AcquiringProgramData();

        data.setKind(ProgramKind.KSP);
        data.setStartDate(LocalDate.now());
        data.setTitle("test-title");
        data.setLink("link");
        data.setDescription("desc");
        data.setAuthorizedZoneEnable(true);
        data.setNotAuthorizedZoneEnable(true);

        mockMvc.perform(put(PUT_UPDATE_ACQUIRING_PROGRAM, 3)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
        ;
    }

    /**
     * Тест сервиса PUT /insurance-service/v2/dict/acquiring/program/{id}
     * для проверки ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateAcquiringProgramForbidden() throws Exception {
        AcquiringProgramData data = new AcquiringProgramData();

        mockMvc.perform(put(PUT_UPDATE_ACQUIRING_PROGRAM, 3)
                .content(TestUtils.convertObjectToJson(data))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса PUT /insurance-service/v2/dict/acquiring/program/{id}
     * для проверки ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateAcquiringProgramNotFound() throws Exception {
        AcquiringProgramData data = new AcquiringProgramData();

        mockMvc.perform(put(PUT_UPDATE_ACQUIRING_PROGRAM, 3)
                .content(TestUtils.convertObjectToJson(data))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса POST /insurance-service/v2/dict/acquiring/program/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAcquiringProgramByIdSuccess() throws Exception {

        mockMvc.perform(get(GET_ACQUIRING_PROGRAM, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", is("Наименование 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("KSP")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programSettingId", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is("18.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is("18.12.2035")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("описание 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link", is("http://rshbins.ru")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorizedZoneEnable", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.notAuthorizedZoneEnable", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.application", is(true)))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/dict/acquiring/program
     * для проверки ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAcquiringProgramForbidden() throws Exception {

        mockMvc.perform(get(GET_ACQUIRING_PROGRAM, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }
    /**
     * Тест сервиса GET /insurance-service/v2/dict/acquiring/program
     * для проверки ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAcquiringProgramNotFound() throws Exception {

        mockMvc.perform(get(GET_ACQUIRING_PROGRAM, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }
}
