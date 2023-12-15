package ru.softlab.efr.services.insurance.controllers;

import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.common.utilities.rest.RestPageImpl;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.rest.ClientTemplateData;
import ru.softlab.efr.services.insurance.model.rest.ClientTemplateDataForList;
import ru.softlab.efr.services.insurance.model.rest.FilterClientTemplates;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import java.time.LocalDate;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class ClientTemplatesControllerTest {

    private static final String PUT_CLIENT_TEMPLATES_PAGE_LIST = "/insurance-service/v1/client-templates";
    private static final String POST_CLIENT_TEMPLATES = "/insurance-service/v1/client-templates";
    private static final String GET_CLIENT_TEMPLATES_BY_ID = "/insurance-service/v1/client-templates/{id}";
    private static final String GET_CLIENT_TEMPLATES_LIST_BY_CONTRACT_ID = "/insurance-service/v1/consumers/client-templates/{insuranceId}";
    private static final String POST_ATTACH_CLIENT_TEMPLATES = "/insurance-service/v1/attach/client-templates";
    private static final String GET_ATTACH_CLIENT_TEMPLATES = "/insurance-service/v1/attach/client-templates/{attachId}";
    private static final String DELETE_ATTACH_CLIENT_TEMPLATES = "/insurance-service/v1/attach/client-templates/{attachId}";

    private static final Resource INPUT_STREAM = new ClassPathResource("emptyImage.jpg");

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

    private ParameterizedTypeReference<RestPageImpl<ClientTemplateDataForList>> restPage() {
        return new ParameterizedTypeReference<RestPageImpl<ClientTemplateDataForList>>() {
        };
    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getPageClientTemplatesWithOutFilterSuccess() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(put(PUT_CLIENT_TEMPLATES_PAGE_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterClientTemplates(null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].programName", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].isTemplate", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("С видом и программой 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].startDate", is("05.01.2019")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endDate", is("05.01.2020")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].sortPriority", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].programName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].kind").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].isTemplate", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name", is("Без вида")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].startDate", is("04.01.2019")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].endDate", is("04.01.2020")))
        ;

        // Должен выполниться 1 запроса к БД:
        assertSelectCount(1);
    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getPageClientTemplatesWithFilterSuccess() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(put(PUT_CLIENT_TEMPLATES_PAGE_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterClientTemplates(ProgramKind.ISJ, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].programName", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].programName", is("Купонный ИСЖ")))
        ;

        // Должен выполниться 1 запроса к БД:
        assertSelectCount(1);

        SQLStatementCountValidator.reset();

        mockMvc.perform(put(PUT_CLIENT_TEMPLATES_PAGE_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterClientTemplates(null, 2L, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].programName", is("Купонный ИСЖ")))
        ;

        // Должен выполниться 1 запроса к БД:
        assertSelectCount(1);

        SQLStatementCountValidator.reset();

        mockMvc.perform(put(PUT_CLIENT_TEMPLATES_PAGE_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterClientTemplates(null, null, true)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].isTemplate", is(true)))
        ;

        // Должен выполниться 1 запроса к БД:
        assertSelectCount(1);
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(PUT_CLIENT_TEMPLATES_PAGE_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterClientTemplates(null, null, false)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].isTemplate", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].isTemplate", is(false)))
        ;

        // Должен выполниться 1 запроса к БД:
        assertSelectCount(1);
        SQLStatementCountValidator.reset();

        mockMvc.perform(put(PUT_CLIENT_TEMPLATES_PAGE_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterClientTemplates(ProgramKind.ISJ, 2L, true)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(0)))
        ;

        // Должен выполниться 1 запроса к БД:
        assertSelectCount(1);
    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getPageClientTemplatesForbidden() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(put(PUT_CLIENT_TEMPLATES_PAGE_LIST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(new FilterClientTemplates(null, null, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

        // Должно выполниться 0 запросов к БД:
        assertSelectCount(0);
    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateClientTemplatesSuccess() throws Exception {

        ClientTemplateData request = new ClientTemplateData();
        request.setKind(ProgramKind.ISJ);
        request.setProgram(1L);
        request.setIsTemplate(false);
        request.setName("Шаблон 5");
        request.setDescription("Описание 5");
        request.setLink("link");
        request.setStartDate(LocalDate.of(2019, 1, 5));
        request.setEndDate(LocalDate.of(2020, 1, 5));
        request.setAttachId("5");

        mockMvc.perform(post(POST_CLIENT_TEMPLATES)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.program", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isTemplate", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Шаблон 5")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Описание 5")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link", is("link")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is("05.01.2019")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is("05.01.2020")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachId", is("5")))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateClientTemplatesForbidden() throws Exception {

        ClientTemplateData request = new ClientTemplateData();
        request.setKind(ProgramKind.ISJ);
        request.setProgram(1L);
        request.setIsTemplate(false);
        request.setName("Шаблон 5");
        request.setDescription("Описание 5");
        request.setLink("link");
        request.setStartDate(LocalDate.of(2019, 1, 5));
        request.setEndDate(LocalDate.of(2020, 1, 5));
        request.setAttachId("5");

        mockMvc.perform(post(POST_CLIENT_TEMPLATES)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateClientTemplatesNotFound() throws Exception {

        ClientTemplateData request = new ClientTemplateData();
        request.setKind(ProgramKind.ISJ);
        request.setProgram(19L);
        request.setIsTemplate(false);
        request.setName("Шаблон 5");
        request.setDescription("Описание 5");
        request.setLink("link");
        request.setStartDate(LocalDate.of(2019, 1, 5));
        request.setEndDate(LocalDate.of(2020, 1, 5));
        request.setAttachId("5");

        mockMvc.perform(post(POST_CLIENT_TEMPLATES)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getClientTemplateByIdSuccess() throws Exception {

        mockMvc.perform(get(GET_CLIENT_TEMPLATES_BY_ID, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.program").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isTemplate", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Без программы")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Описание 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link", is("http://google.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is("01.01.2019")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is("01.01.2020")))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getClientTemplateByIdNotFound() throws Exception {

        mockMvc.perform(get(GET_CLIENT_TEMPLATES_BY_ID, 19)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())

        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getClientTemplateByIdForbidden() throws Exception {

        mockMvc.perform(get(GET_CLIENT_TEMPLATES_BY_ID, 19)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())

        ;
    }


    /**
     * Тест сервиса GET /insurance-service/v1/client-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postUploadAttachClientTemplateSuccess() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(POST_ATTACH_CLIENT_TEMPLATES)
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postUploadAttachClientTemplateForbidden() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(POST_ATTACH_CLIENT_TEMPLATES)
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());

    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAttachClientTemplateSuccess() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.fileUpload(POST_ATTACH_CLIENT_TEMPLATES)
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk());
        String id = TestUtils.extractDataFromResultJson(result, "$.uuid");
        mockMvc.perform(get(GET_ATTACH_CLIENT_TEMPLATES, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
        ;

    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAttachClientTemplateForbidden() throws Exception {

        mockMvc.perform(get(GET_ATTACH_CLIENT_TEMPLATES, 5)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden())
        ;

    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteAttachClientTemplateSuccess() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.fileUpload(POST_ATTACH_CLIENT_TEMPLATES)
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk());
        String id = TestUtils.extractDataFromResultJson(result, "$.uuid");
        mockMvc.perform(delete(DELETE_ATTACH_CLIENT_TEMPLATES, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
        ;
        mockMvc.perform(get(GET_ATTACH_CLIENT_TEMPLATES, id)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isNotFound())
        ;

    }

    /**
     * Тест сервиса GET /insurance-service/v1/client-templates/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteAttachClientTemplateForbidden() throws Exception {

        mockMvc.perform(delete(DELETE_ATTACH_CLIENT_TEMPLATES, 5)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden())
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/consumers/client-templates/{insuranceId}
     * для проверки получения отсортированного списка шаблонов заявлений и инструкций
     *
     * @throws Exception при падении теста
     */
    @Test
    @Ignore
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getTemplatesListSuccess() throws Exception {

        mockMvc.perform(get(GET_CLIENT_TEMPLATES_LIST_BY_CONTRACT_ID, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.templates", hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions[0].name", is("Без вида")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions[0].description", is("Описание 4")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions[0].link", is("http://google.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions[0].attachId").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions[1].name", is("Без программы")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions[2].name", is("С видом и программой 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions[3].name", is("С видом и программой 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions[3].attachId", is("2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions[3].attachName", is("test2.xml")))
        ;
    }
}
