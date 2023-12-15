package ru.softlab.efr.services.insurance.controllers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
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
import ru.softlab.efr.services.insurance.model.rest.AttachmentData;
import ru.softlab.efr.services.insurance.model.rest.ClientRequestRq;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.CLIENT_PRINCIPAL_DATA;
import static ru.softlab.efr.services.insurance.stubs.TestData.CLIENT_PRINCIPAL_DATA_2;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class RequestAttachmentsControllerTest {

    private static final String POST_CREATE_CLIENT_REQUEST = "/insurance-service/v2/client-request";
    private static final String POST_CREATE_CLIENT_REQUEST_ATTACHMENT = "/insurance-service/v2/client-request/temp/%s/attachment";
    private static final String POST_CREATE_CLIENT_REQUEST_ATTACHMENT_BASE64 = "/insurance-service/v2/client-request/temp/%s/attachment/base64";
    private static final String GET_ATTACHMENTS_TO_REQUEST = "/insurance-service/v2/client-request/temp/%s/attachment";
    private static final String DELETE_ATTACHMENTS_TO_REQUEST = "/insurance-service/v2/client-request/%s/attachment";
    private static final String GET_ATTACHMENT_CONTENT_TO_REQUEST = "/insurance-service/v2/client-request/%s/attachment";


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
    public void addFileToRequestTest() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            MockMultipartFile firstFile = new MockMultipartFile(
                    "content", INPUT_STREAM.getFilename(), "text/plain",
                    StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

            ClientRequestRq requestAddInfo = new ClientRequestRq();
            requestAddInfo.setRequestsTopicId(0L);
            requestAddInfo.setInsuranceId(10L);
            requestAddInfo.setEmail("mail@mailforspam.com");
            requestAddInfo.setPhone("+877777777");
            requestAddInfo.setRequestText("test");
            ResultActions resultActions = mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(requestAddInfo))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            int requestId = TestUtils.extractDataFromResultJson(resultActions, "$.id");

            mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_CREATE_CLIENT_REQUEST_ATTACHMENT, requestId))
                    .file(firstFile)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                    .andExpect(status().isOk());
        } finally {
            greenMail.stop();
        }
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getFileToRequestTest() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            MockMultipartFile firstFile = new MockMultipartFile(
                    "content", INPUT_STREAM.getFilename(), "text/plain",
                    StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

            ClientRequestRq requestAddInfo = new ClientRequestRq();
            requestAddInfo.setRequestsTopicId(0L);
            requestAddInfo.setInsuranceId(10L);
            requestAddInfo.setEmail("mail@mailforspam.com");
            requestAddInfo.setPhone("+877777777");
            requestAddInfo.setRequestText("test");
            ResultActions resultActions = mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(requestAddInfo))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            int requestId = TestUtils.extractDataFromResultJson(resultActions, "$.id");

            mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_CREATE_CLIENT_REQUEST_ATTACHMENT, requestId))
                    .file(firstFile)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                    .andExpect(status().isOk());

            mockMvc.perform(get(String.format(GET_ATTACHMENTS_TO_REQUEST, requestId))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.elements", hasSize(1)));
        } finally {
            greenMail.stop();
        }
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setFileDeleteToRequestTest() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            MockMultipartFile firstFile = new MockMultipartFile(
                    "content", INPUT_STREAM.getFilename(), "text/plain",
                    StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

            ClientRequestRq requestAddInfo = new ClientRequestRq();
            requestAddInfo.setRequestsTopicId(0L);
            requestAddInfo.setInsuranceId(10L);
            requestAddInfo.setEmail("mail@mailforspam.com");
            requestAddInfo.setPhone("+877777777");
            requestAddInfo.setRequestText("test");
            ResultActions resultActions = mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(requestAddInfo))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            int requestId = TestUtils.extractDataFromResultJson(resultActions, "$.id");

            mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_CREATE_CLIENT_REQUEST_ATTACHMENT, requestId))
                    .file(firstFile)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                    .andExpect(status().isOk());

            ResultActions resultActionsFiles = mockMvc.perform(get(String.format(GET_ATTACHMENTS_TO_REQUEST, requestId))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.elements", hasSize(1)));

            int requestAttachId = TestUtils.extractDataFromResultJson(resultActionsFiles, "$.elements[0].requestAttachId");

            mockMvc.perform(delete(String.format(DELETE_ATTACHMENTS_TO_REQUEST, requestAttachId))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk());

            mockMvc.perform(get(String.format(GET_ATTACHMENTS_TO_REQUEST, requestId))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.elements", hasSize(0)));
        } finally {
            greenMail.stop();
        }
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getFileToRequestDownloadTest() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            MockMultipartFile firstFile = new MockMultipartFile(
                    "content", INPUT_STREAM.getFilename(), "text/plain",
                    StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

            ClientRequestRq requestAddInfo = new ClientRequestRq();
            requestAddInfo.setRequestsTopicId(0L);
            requestAddInfo.setInsuranceId(10L);
            requestAddInfo.setEmail("mail@mailforspam.com");
            requestAddInfo.setPhone("+877777777");
            requestAddInfo.setRequestText("test");
            ResultActions resultActions = mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(requestAddInfo))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            int requestId = TestUtils.extractDataFromResultJson(resultActions, "$.id");

            mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_CREATE_CLIENT_REQUEST_ATTACHMENT, requestId))
                    .file(firstFile)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                    .andExpect(status().isOk());

            ResultActions resultActionsFiles = mockMvc.perform(get(String.format(GET_ATTACHMENTS_TO_REQUEST, requestId))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.elements", hasSize(1)));

            int requestAttachId = TestUtils.extractDataFromResultJson(resultActionsFiles, "$.elements[0].requestAttachId");

            mockMvc.perform(get(String.format(GET_ATTACHMENT_CONTENT_TO_REQUEST, requestAttachId))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk());
        } finally {
            greenMail.stop();
        }
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void uploadRequestAttachmentBase64Success() throws Exception {

        AttachmentData attachmentData = new AttachmentData();
        attachmentData.setFileName("test.txt");
        attachmentData.setContent(Base64.getEncoder().encodeToString("Содержимое вложения".getBytes(StandardCharsets.UTF_8.name())));

        mockMvc.perform(post(String.format(POST_CREATE_CLIENT_REQUEST_ATTACHMENT_BASE64, 0))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJson(attachmentData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestAttachId").isNotEmpty());

        ResultActions resultActionsFiles = mockMvc.perform(get(String.format(GET_ATTACHMENTS_TO_REQUEST, 0))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].requestAttachId").isNotEmpty());

        int requestAttachId = TestUtils.extractDataFromResultJson(resultActionsFiles, "$.elements[0].requestAttachId");

        mockMvc.perform(get(String.format(GET_ATTACHMENT_CONTENT_TO_REQUEST, requestAttachId))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void uploadRequestAttachmentBase64Forbidden() throws Exception {

        AttachmentData attachmentData = new AttachmentData();
        attachmentData.setFileName("test.txt");
        attachmentData.setContent(Base64.getEncoder().encodeToString("Содержимое вложения".getBytes(StandardCharsets.UTF_8.name())));

        mockMvc.perform(post(String.format(POST_CREATE_CLIENT_REQUEST_ATTACHMENT_BASE64, 0))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJson(attachmentData)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void uploadRequestAttachmentBase64ForbiddenAnotherClient() throws Exception {

        AttachmentData attachmentData = new AttachmentData();
        attachmentData.setFileName("test.txt");
        attachmentData.setContent(Base64.getEncoder().encodeToString("Содержимое вложения".getBytes(StandardCharsets.UTF_8.name())));

        mockMvc.perform(post(String.format(POST_CREATE_CLIENT_REQUEST_ATTACHMENT_BASE64, 0))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA_2))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJson(attachmentData)))
                .andExpect(status().isForbidden());
    }
}