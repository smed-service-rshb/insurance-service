package ru.softlab.efr.services.insurance.controllers;


import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.db.UserTemplateEntity;
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
public class UserTemplatesControllerTest {

    private static final String GET_ENTITYLIST_REQUEST = "/insurance-service/v1/user-templates";
    private static final String GET_ENTITY_BY_ID_REQUEST = "/insurance-service/v1/user-templates/{id}";
    private static final String GET_ENTITY_CONTENT_BY_ID_REQUEST = "/insurance-service/v1/user-templates/{id}/content";
    private static final String POST_CREATE_ENTITY_REQUEST = "/insurance-service/v1/user-templates";
    private static final String PUT_UPDATE_ENTITY_REQUEST = "/insurance-service/v1/user-templates/{id}";
    private static final String DELETE_ENTITY_REQUEST = "/insurance-service/v1/user-templates/{id}";

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
     * Тест сервиса GET /insurance-service/v1/user-templates
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllUserTemplatesSuccess() throws Exception {
        mockMvc.perform(get(GET_ENTITYLIST_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("Очень важный документ, который отображается первым")));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/user-templates/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOneTemplateSuccess() throws Exception {
        getResultOneTemplateRequest(1L)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Пример документа")));
    }

    private ResultActions getResultOneTemplateRequest(Long id) throws Exception {
        return mockMvc.perform(get(GET_ENTITY_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/user-templates/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOneTemplateNotFound() throws Exception {
        mockMvc.perform(get(GET_ENTITY_BY_ID_REQUEST, 100)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/user-templates/{id}/content
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getTemplateContent() throws Exception {
        byte[] bytes = IOUtils.toByteArray(new ClassPathResource("templates/templatesInRelease/test-template.jrxml").getInputStream());

        mockMvc.perform(get(GET_ENTITY_CONTENT_BY_ID_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(bytes));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/user-templates
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createTemplateSuccess() throws Exception {
        getResultCreateTemplateRequestByCredentials(CHIEF_ADMIN_PRINCIPAL_DATA)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(4)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/user-templates
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createTemplateForbidden() throws Exception {
        getResultCreateTemplateRequestByCredentials(USER_PRINCIPAL_DATA)
                .andExpect(status().isForbidden());
    }

    private ResultActions getResultCreateTemplateRequestByCredentials(PrincipalData principalData) throws Exception {
        return mockMvc.perform(post(POST_CREATE_ENTITY_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJson(getUserTemplateEntity()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(principalData)));
    }

    private UserTemplateEntity getUserTemplateEntity() {
        UserTemplateEntity entity = new UserTemplateEntity();
        entity.setName("SOMETHING");
        entity.setPriority(100);
        return entity;
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/user-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateTemplateSuccess() throws Exception {
        UserTemplateEntity entity = getUserTemplateEntity();

        Long id = 3L;
        entity.setId(id);

        mockMvc.perform(put(PUT_UPDATE_ENTITY_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(entity))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        getResultOneTemplateRequest(id)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(entity.getName())));
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/user-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateTemplateForbidden() throws Exception {
        mockMvc.perform(put(PUT_UPDATE_ENTITY_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(getUserTemplateEntity()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса DELETE /insurance-service/v1/user-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteTemplateSuccess() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete(DELETE_ENTITY_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(getUserTemplateEntity()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        getResultOneTemplateRequest(id).andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса DELETE /insurance-service/v1/user-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteTemplateForbidden() throws Exception {
        mockMvc.perform(delete(DELETE_ENTITY_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(getUserTemplateEntity()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }
}
