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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.common.utilities.rest.RestPageImpl;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.rest.RequiredDocumentData;
import ru.softlab.efr.services.insurance.model.rest.ViewInsuranceModel;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class RequiredDocumentControllerTest {

    private static final String POST_CREATE_REQUIRED_DOCUMENT_REQUEST = "/insurance-service/v1/dict/requiredDocuments";
    private static final String GET_ALL_REQUIRED_DOCUMENT_REQUEST = "/insurance-service/v1/dict/requiredDocuments";
    private static final String GET_REQUIRED_DOCUMENT_BY_ID_REQUEST = "/insurance-service/v1/dict/requiredDocuments/{id}";
    private static final String PUT_REQUIRED_DOCUMENT_BY_ID_REQUEST = "/insurance-service/v1/dict/requiredDocuments/{id}";
    private static final String DELETE_REQUIRED_DOCUMENT_BY_ID_REQUEST = "/insurance-service/v1/dict/requiredDocuments/{id}";

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

    private ParameterizedTypeReference<RestPageImpl<ViewInsuranceModel>> restPage() {
        return new ParameterizedTypeReference<RestPageImpl<ViewInsuranceModel>>() {
        };
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/requiredDocuments
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateRequiredDocumentSuccess() throws Exception {
        RequiredDocumentData documentData = new RequiredDocumentData();
        documentData.setType("Тестовый документ");
        documentData.setActiveFlag(true);

        mockMvc.perform(post(POST_CREATE_REQUIRED_DOCUMENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(documentData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/requiredDocuments
     * для проверки ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateRequiredDocumentError() throws Exception {
        RequiredDocumentData documentData = new RequiredDocumentData();
        documentData.setType("Тестовый документ");
        documentData.setActiveFlag(true);

        mockMvc.perform(post(POST_CREATE_REQUIRED_DOCUMENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(documentData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/requiredDocuments
     * для проверки получения корректного ответа при указании параметров пегинации.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllDocumentWithParameterSuccess() throws Exception {

        mockMvc.perform(get(GET_ALL_REQUIRED_DOCUMENT_REQUEST)
                .param("sort", "type")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type", is("Договор страхования.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].activeFlag", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(14)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/requiredDocuments
     * для проверки получения корректного ответа без указания параметров пегинации.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllDocumentSuccess() throws Exception {

        mockMvc.perform(get(GET_ALL_REQUIRED_DOCUMENT_REQUEST)
                .param("sort", "type")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type", is("Договор страхования.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].activeFlag", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(14)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/requiredDocuments
     * для проверки получения ответа для пользователя с правом создания договоров.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllDocumentAtCreationContractSuccess() throws Exception {

        mockMvc.perform(get(GET_ALL_REQUIRED_DOCUMENT_REQUEST)
                .param("sort", "type")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type", is("Договор страхования.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].activeFlag", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(14)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/requiredDocuments
     * для проверки получения ответа при отсутствии прав доступа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllDocumentError() throws Exception {

        mockMvc.perform(get(GET_ALL_REQUIRED_DOCUMENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/requiredDocuments/:id
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRequiredDocumentByIdSuccess() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(GET_ALL_REQUIRED_DOCUMENT_REQUEST)
                .param("sort", "type")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        contentAsString = contentAsString.substring(contentAsString.indexOf("id") + 4);
        Integer id = Integer.parseInt(contentAsString.substring(0, contentAsString.indexOf("\"") - 1));

        mockMvc.perform(get(GET_REQUIRED_DOCUMENT_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(id)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/requiredDocuments/:id
     * для проверки получения корректного ответа пользователя с правами создания договора.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRequiredDocumentByIdAtCreationContractSuccess() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(GET_ALL_REQUIRED_DOCUMENT_REQUEST)
                .param("sort", "type")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        contentAsString = contentAsString.substring(contentAsString.indexOf("id") + 4);
        Integer id = Integer.parseInt(contentAsString.substring(0, contentAsString.indexOf("\"") - 1));

        mockMvc.perform(get(GET_REQUIRED_DOCUMENT_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(id)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/requiredDocuments/:id
     * для проверки получения ответа при отсутствии записи в справочнике.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRequiredDocumentByIdNotFound() throws Exception {

        mockMvc.perform(get(GET_REQUIRED_DOCUMENT_BY_ID_REQUEST, 10)
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
    public void getRequiredDocumentByIdError() throws Exception {

        mockMvc.perform(get(GET_REQUIRED_DOCUMENT_BY_ID_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(DEP_HEAD_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/requiredDocuments/:id
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putRequiredDocumentSuccess() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(GET_ALL_REQUIRED_DOCUMENT_REQUEST)
                .param("sort", "type")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        contentAsString = contentAsString.substring(contentAsString.indexOf("id") + 4);
        Integer id = Integer.parseInt(contentAsString.substring(0, contentAsString.indexOf("\"") - 1));
        RequiredDocumentData documentData = new RequiredDocumentData();
        documentData.setType("Тестовый документ");
        documentData.setActiveFlag(false);
        mockMvc.perform(put(PUT_REQUIRED_DOCUMENT_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(documentData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_REQUIRED_DOCUMENT_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", is("Тестовый документ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.activeFlag", is(false)));
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/requiredDocuments/:id
     * для проверки получения ответа при отсутствии записи в справочнике.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putRequiredDocumentByIdNotFound() throws Exception {
        RequiredDocumentData documentData = new RequiredDocumentData();
        documentData.setType("Тестовый документ");
        documentData.setActiveFlag(false);
        mockMvc.perform(put(PUT_REQUIRED_DOCUMENT_BY_ID_REQUEST, 10)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(documentData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/requiredDocuments/:id
     * для проверки получения ответа при отсутствии прав.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putRequiredDocumentByIdError() throws Exception {
        RequiredDocumentData documentData = new RequiredDocumentData();
        documentData.setType("Тестовый документ");
        documentData.setActiveFlag(false);
        mockMvc.perform(put(PUT_REQUIRED_DOCUMENT_BY_ID_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(documentData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса DELETE /insurance-service/v1/dict/requiredDocuments/:id
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteRequiredDocumentSuccess() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(GET_ALL_REQUIRED_DOCUMENT_REQUEST)
                .param("sort", "type")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        contentAsString = contentAsString.substring(contentAsString.indexOf("id") + 4);
        Integer id = Integer.parseInt(contentAsString.substring(0, contentAsString.indexOf("\"") - 1));

        mockMvc.perform(delete(DELETE_REQUIRED_DOCUMENT_BY_ID_REQUEST, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_ALL_REQUIRED_DOCUMENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(13)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(13)));
    }

    /**
     * Тест сервиса DELETE /insurance-service/v1/dict/requiredDocuments/:id
     * для проверки получения ответа при отсутствии записи в справочнике.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteRequiredDocumentByIdNotFound() throws Exception {

        mockMvc.perform(delete(DELETE_REQUIRED_DOCUMENT_BY_ID_REQUEST, 10)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(GET_ALL_REQUIRED_DOCUMENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(14)));
    }

    /**
     * Тест сервиса DELETE /insurance-service/v1/dict/requiredDocuments/:id
     * для проверки получения ответа при отсутствии прав.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteRequiredDocumentByIdError() throws Exception {
        RequiredDocumentData documentData = new RequiredDocumentData();
        documentData.setType("Тестовый документ");
        documentData.setActiveFlag(false);
        mockMvc.perform(delete(DELETE_REQUIRED_DOCUMENT_BY_ID_REQUEST, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(documentData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

        mockMvc.perform(get(GET_ALL_REQUIRED_DOCUMENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(14)));
    }
}
