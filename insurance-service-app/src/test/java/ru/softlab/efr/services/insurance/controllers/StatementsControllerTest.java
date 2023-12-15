package ru.softlab.efr.services.insurance.controllers;

import org.junit.Before;
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
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.rest.StatementCompleteStatus;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;
import static ru.softlab.efr.services.insurance.stubs.TestData.CLIENT_PRINCIPAL_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class StatementsControllerTest {

    private static final String GET_STATEMENT_BY_ID = "/insurance-service/v2/contract/attachments/statements/%s";
    private static final String GET_STATEMENTS_BY_INSURANCE_ID = "/insurance-service/v2/contract/%s/attachments/statements";
    private static final String POST_STATEMENT_CREATE = "/insurance-service/v2/contract/%s/attachments/statements/%s";
    private static final String POST_UPLOAD_STATEMENT_ATTACH = "/insurance-service/v2/contract/attachments/statements/%s/%s";
    private static final String POST_CHANGE_STATEMENT_COMMENT = "/insurance-service/v2/contract/attachments/statements/%s/comment?comment=%s";
    private static final String POST_CHANGE_STATEMENT_DOCUMENT_STATUS = "/insurance-service/v2/contract/attachments/statements/%s/status?statementCompleteStatus=%s";


    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();
    private static final Resource INPUT_STREAM = new ClassPathResource("hibernate.properties");

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createStatement() throws Exception {
        mockMvc.perform(post(String.format(POST_STATEMENT_CREATE, 10L, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED.toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementCompleteStatus").isNotEmpty());
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addFileToStatement() throws Exception {
        ResultActions resultActionsAddStatement = mockMvc.perform(post(String.format(POST_STATEMENT_CREATE, 10L, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED.toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementCompleteStatus").isNotEmpty());

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_UPLOAD_STATEMENT_ATTACH,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString(),
                1L))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addFileToStatementCheckFileEqualsNameForbidden() throws Exception {
        ResultActions resultActionsAddStatement = mockMvc.perform(post(String.format(POST_STATEMENT_CREATE, 10L, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED.toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementCompleteStatus").isNotEmpty());

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_UPLOAD_STATEMENT_ATTACH,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString(),
                1L))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_UPLOAD_STATEMENT_ATTACH,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString(),
                1L))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getStatementById() throws Exception {
        ResultActions resultActionsAddStatement = mockMvc.perform(post(String.format(POST_STATEMENT_CREATE, 10L, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED.toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementCompleteStatus").isNotEmpty());

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_UPLOAD_STATEMENT_ATTACH,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString(),
                1L))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_STATEMENT_BY_ID,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementCompleteStatus").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.documents").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.documents", hasSize(1)));
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getStatementListByInsuranceId() throws Exception {
        ResultActions resultActionsAddStatement = mockMvc.perform(post(String.format(POST_STATEMENT_CREATE, 10L, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED.toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementCompleteStatus").isNotEmpty());

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_UPLOAD_STATEMENT_ATTACH,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString(),
                1L))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_STATEMENTS_BY_INSURANCE_ID, 10L))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementTypes", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementTypes[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementTypes[0].code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementTypes[0].statementCompleteStatus").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementTypes[0].documents").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementTypes[0].documents", hasSize(1)));
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void changeStatementComment() throws Exception {
        ResultActions resultActionsAddStatement =mockMvc.perform(post(String.format(POST_STATEMENT_CREATE, 10L, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED.toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementCompleteStatus").isNotEmpty());


        mockMvc.perform(post(String.format(POST_CHANGE_STATEMENT_COMMENT,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString(),
                "test"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_STATEMENT_BY_ID,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementCompleteStatus").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.info", is("test")));
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setPostChangeStatementDocumentStatus() throws Exception {
        ResultActions resultActionsAddStatement =mockMvc.perform(post(String.format(POST_STATEMENT_CREATE, 10L, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED.toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementCompleteStatus").isNotEmpty());


        mockMvc.perform(post(String.format(POST_CHANGE_STATEMENT_COMMENT,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString(),
                "test"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_STATEMENT_BY_ID,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementCompleteStatus").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.info", is("test")));

        mockMvc.perform(post(String.format(POST_CHANGE_STATEMENT_DOCUMENT_STATUS,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString(),
                StatementCompleteStatus.FULL.toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_STATEMENT_BY_ID,
                TestUtils.extractDataFromResultJson(resultActionsAddStatement, "$.id").toString()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statementCompleteStatus", is(StatementCompleteStatus.FULL.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.info").isEmpty());
    }

}
