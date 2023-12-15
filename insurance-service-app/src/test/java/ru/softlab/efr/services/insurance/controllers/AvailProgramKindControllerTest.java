package ru.softlab.efr.services.insurance.controllers;


import org.junit.Before;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.rest.AvailProgramKindData;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;
import static ru.softlab.efr.services.insurance.stubs.TestData.USER_PRINCIPAL_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class AvailProgramKindControllerTest {

    private static final String GET_AVAIL_PROGRAM_KIND_REQUEST = "/insurance-service/v1/availprogramkind";
    private static final String GET_ENTITYLIST_REQUEST = "/insurance-service/v1/availprogramkind/dict";
    private static final String POST_ENTITYLIST_REQUEST = "/insurance-service/v1/availprogramkind/dict";
    private static final String PUT_ENTITYLIST_REQUEST = "/insurance-service/v1/availprogramkind/dict/{id}";
    private static final String DELETE_ENTITYLIST_REQUEST = "/insurance-service/v1/availprogramkind/dict/{id}";

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
     * Тест сервиса GET /insurance-service/v1/availprogramkind/dict
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAvailProgramKindListSuccess() throws Exception {
        getAvailProgramKindList()
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("ИСЖ")));
    }

    private ResultActions getAvailProgramKindList() throws Exception {
        return mockMvc.perform(get(GET_ENTITYLIST_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/availprogramkind
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAvailProgramKindSuccess() throws Exception {
        getAvailProgramKind().andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availProgramKinds", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availProgramKinds[0].name", is("ИСЖ")));
    }

    private ResultActions getAvailProgramKind() throws Exception {
        return mockMvc.perform(get(GET_AVAIL_PROGRAM_KIND_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    /**
     * Тест сервиса DELETE /insurance-service/v1/availprogramkind/dict/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteAvailProgramKindSuccess() throws Exception {
        mockMvc.perform(delete(DELETE_ENTITYLIST_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        getAvailProgramKind().andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availProgramKinds", hasSize(0)));
    }

    /**
     * Тест сервиса DELETE /insurance-service/v1/availprogramkind/dict/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteAvailProgramKindForbidden() throws Exception {
        mockMvc.perform(delete(DELETE_ENTITYLIST_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());
        getAvailProgramKind().andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availProgramKinds", hasSize(1)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/availprogramkind/dict
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createAvailProgramKindForbidden() throws Exception {
        AvailProgramKindData data = new AvailProgramKindData(null,
                "NEW_PROGRAM_KIND", "Новый вид программы страхования", true);


        mockMvc.perform(post(POST_ENTITYLIST_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

        getAvailProgramKindList().andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(5)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/availprogramkind/dict
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createAvailProgramKindSuccess() throws Exception {
        AvailProgramKindData data = new AvailProgramKindData(null,
                "NEW_PROGRAM_KIND", "Новый вид программы страхования", true);

        mockMvc.perform(post(POST_ENTITYLIST_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        getAvailProgramKindList().andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[5].name", is("NEW_PROGRAM_KIND")));

    }

    /**
     * Тест сервиса PUT /insurance-service/v1/availprogramkind/dict/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateAvailProgramKindForbidden() throws Exception {
        AvailProgramKindData data = new AvailProgramKindData(1L,
                "ISJ", "ИСЖ", false);

        mockMvc.perform(put(PUT_ENTITYLIST_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

        getAvailProgramKind().andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availProgramKinds", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availProgramKinds[0].isActive", is(true)));

    }

    /**
     * Тест сервиса PUT /insurance-service/v1/availprogramkind/dict/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateAvailProgramKindSuccess() throws Exception {
        AvailProgramKindData data = new AvailProgramKindData(1L,
                "ISJ", "ИСЖ", false);

        mockMvc.perform(put(PUT_ENTITYLIST_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        getAvailProgramKind().andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availProgramKinds", hasSize(0)));
    }
}
