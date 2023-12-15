package ru.softlab.efr.services.insurance.controllers;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.utils.TestUtils;
import ru.softlab.efr.test.infrastructure.transport.rest.MockRest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.INSURANCE_MODEL_1;
import static ru.softlab.efr.services.insurance.stubs.TestData.USER_PRINCIPAL_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class AttachmentsControllerTest {

    private static final String GET_ATTACHMENTS_REQUEST = "/insurance-service/v2/contract/%s/attachment";
    private static final String GET_TEMP_ATTACHMENTS_REQUEST = "/insurance-service/v2/contract/temp/%s/attachment";
    private static final String POST_ATTACHMENT_REQUEST = "/insurance-service/v2/contract/%s/attachment/%s";
    private static final String POST_TEMP_ATTACHMENT_REQUEST = "/insurance-service/v2/contract/temp/%s/attachment/%s";
    private static final String POST_CERTIFICATION_REQUEST = "/insurance-service/v2/contract/%s/attachment/certification";
    private static final String POST_TEMP_CERTIFICATION_REQUEST = "/insurance-service/v2/contract/temp/%s/attachment/certification";
    private static final String GET_CERTIFICATION_REQUEST = "/insurance-service/v2/contract/%s/attachments/certification";
    private static final String GET_TEMP_CERTIFICATION_REQUEST = "/insurance-service/v2/contract/temp/%s/attachments/certification";
    private static final String GET_ATTACHMENT_BY_ID_REQUEST = "/insurance-service/v2/contract/attachment/%s";
    private static final String DELETE_ATTACHMENT_REQUEST = "/insurance-service/v2/contract/attachment/%s";
    private static final String POST_CREATE_CONTRACT_REQUEST = "/insurance-service/v2/contracts";
    private static final String GET_CONTRACT_BY_ID_REQUEST = "/insurance-service/v2/contracts/{id}";


    private static final Resource INPUT_STREAM = new ClassPathResource("hibernate.properties");

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
     * Тест сервиса GET /insurance-service/v1/programs
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllAttachmentSuccess() throws Exception {

        mockMvc.perform(get(String.format(GET_ATTACHMENTS_REQUEST, "2"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].name", is("Документ, удостоверяющий личность.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents[0].name", is("test1.xml")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents[0].attachedDate", is("01.01.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[1].name", is("Форма самосертификации.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[1].documents", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[1].documents[0].name", is("test2.xml")))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/programs
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllAttachmentFail() throws Exception {

        mockMvc.perform(get(String.format(GET_ATTACHMENTS_REQUEST, "4312-asdf"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
        ;
        mockMvc.perform(get(String.format(GET_ATTACHMENTS_REQUEST, "4312"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(204))//not_content
        ;
    }

    /**
     * Тест сервиса DELETE /insurance-service/v2/contract/%s/attachment/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteAttachmentSuccess() throws Exception {
        mockMvc.perform(delete(String.format(DELETE_ATTACHMENT_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Тест сервиса DELETE /insurance-service/v2/contract/%s/attachment/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteAttachmentFail() throws Exception {
        mockMvc.perform(delete(String.format(DELETE_ATTACHMENT_REQUEST, "4321"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(204));//not_content
    }


    /**
     * Тест сервиса GET /insurance-service/v2/contract/%s/attachment/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAttachmentByIdSuccess() throws Exception {
        byte[] bytes = new byte[]{0, 49}; //1
        mockMvc.perform(get(String.format(GET_ATTACHMENT_BY_ID_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(bytes));
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/%s/attachment/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAttachmentByIdFail() throws Exception {
        mockMvc.perform(get(String.format(GET_ATTACHMENT_BY_ID_REQUEST, "15"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().is(204));//not_content
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/%s/attachment
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createAttachmentSuccess() throws Exception {

        Integer documentType = 1;

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_ATTACHMENT_REQUEST, "2", documentType.toString()))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(get(String.format(GET_ATTACHMENTS_REQUEST, "2"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].id", is(documentType)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents[4].name", is(INPUT_STREAM.getFilename())));
        System.out.println(TestUtils.extractDataFromResultJson(resultActions, "$").toString());

    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/%s/attachment
     * для проверки процесса загрузки и просмотра и удаления документов без сохранения докмента.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createTempAttachmentSuccess() throws Exception {

        Integer documentType = 1;

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        //прикрепляем скан документа
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_TEMP_ATTACHMENT_REQUEST, "undefined", documentType.toString()))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());
        //получаем uuid договора
        String uuid = TestUtils.extractDataFromResultJson(resultActions, "$.uuid");

        //прикрепляем форму самосертификации для застрахованного
        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_TEMP_CERTIFICATION_REQUEST, uuid))
                .file(firstFile)
                .param("blockId", "insured")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        //проверяем что скан загрузился
        resultActions = mockMvc.perform(get(String.format(GET_TEMP_ATTACHMENTS_REQUEST, uuid))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].id", is(documentType)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents[0].name", is(INPUT_STREAM.getFilename())))
        ;

        //получаем идентификатор загруженного документа
        String idDoc = TestUtils.extractDataFromResultJson(resultActions, "$.attachedDocs[0].documents[0].id");

        //проверка удаления скана
        mockMvc.perform(delete(String.format(DELETE_ATTACHMENT_REQUEST, idDoc))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        //получаем форму самосертификации здя застрахованного
        resultActions = mockMvc.perform(get(String.format(GET_TEMP_CERTIFICATION_REQUEST, uuid))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .param("type", "insured")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
        ;

        idDoc = TestUtils.extractDataFromResultJson(resultActions, "$.id");

        //проверка скачивания формы самосертификации
        mockMvc.perform(get(String.format(GET_ATTACHMENT_BY_ID_REQUEST, idDoc))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
        ;

        //проверка удаления скана
        mockMvc.perform(delete(String.format(DELETE_ATTACHMENT_REQUEST, idDoc))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        //проверяем список вложений после удаления
        mockMvc.perform(get(String.format(GET_TEMP_ATTACHMENTS_REQUEST, uuid))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(204));//not_content
    }

    /**
     * Тест сервиса POST /insurance-service/v2/contract/%s/attachment
     * для проверки процесса загрузки документов, сохранения договора и наличия документов после сохранения.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createTempAttachmentAndSaveContractSuccess() throws Exception {

        Integer documentType = 1;

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        //сохраняем скан
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_TEMP_ATTACHMENT_REQUEST, "undefined", documentType.toString()))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        String uuid = TestUtils.extractDataFromResultJson(resultActions, "$.uuid");

        //сохраняем форму самосертификации для застрахованного
        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_TEMP_CERTIFICATION_REQUEST, uuid))
                .file(firstFile)
                .param("blockId", "insured")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        //сохраняем форму самосертификации для страхователя
        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_TEMP_CERTIFICATION_REQUEST, uuid))
                .file(firstFile)
                .param("blockId", "policyHolder")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        //получаем список сканов
        resultActions = mockMvc.perform(get(String.format(GET_TEMP_ATTACHMENTS_REQUEST, uuid))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].id", is(documentType)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents[0].name", is(INPUT_STREAM.getFilename())))
        ;

        String attachDocId = TestUtils.extractDataFromResultJson(resultActions, "$.attachedDocs[0].documents[0].id");

        //получаем форму самосертификации для застрахованного
        resultActions = mockMvc.perform(get(String.format(GET_TEMP_CERTIFICATION_REQUEST, uuid))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .param("type", "insured")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
        ;

        String certificationInsuredDocId = TestUtils.extractDataFromResultJson(resultActions, "$.id");

        //получаем форму самосертификации для страхователя
        resultActions = mockMvc.perform(get(String.format(GET_TEMP_CERTIFICATION_REQUEST, uuid))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .param("type", "policyHolder")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
        ;

        String certificationPolicyHolderDocId = TestUtils.extractDataFromResultJson(resultActions, "$.id");

        INSURANCE_MODEL_1.setUuid(uuid);

        //сохраняем договор
        resultActions = mockMvc.perform(post(POST_CREATE_CONTRACT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(INSURANCE_MODEL_1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());

        int id = TestUtils.extractDataFromResultJson(resultActions, "$.id");

        //проверка доступности скана после сохранения
        mockMvc.perform(get(String.format(GET_ATTACHMENTS_REQUEST, id))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].id", is(documentType)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents[0].name", is(INPUT_STREAM.getFilename())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attachedDocs[0].documents[0].id", is(attachDocId)))
        ;

        //удаление скана после сохранения
        mockMvc.perform(delete(String.format(DELETE_ATTACHMENT_REQUEST, attachDocId))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        //проверка доступности формы самосертификации для застрахованного после сохранения договора
        mockMvc.perform(get(String.format(GET_CERTIFICATION_REQUEST, id))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .param("type", "insured")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(certificationInsuredDocId)))
        ;

        //проверка доступности формы самосертификации для страхователя после сохранения договора
        mockMvc.perform(get(String.format(GET_CERTIFICATION_REQUEST, id))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .param("type", "policyHolder")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(certificationPolicyHolderDocId)))
        ;

    }

}
