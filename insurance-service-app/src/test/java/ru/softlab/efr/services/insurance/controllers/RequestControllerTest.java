package ru.softlab.efr.services.insurance.controllers;


import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
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
import ru.softlab.efr.services.insurance.model.rest.ClientRequestRq;
import ru.softlab.efr.services.insurance.model.rest.FilterRequestsRq;
import ru.softlab.efr.services.insurance.model.rest.ProcessingClientRequestRq;
import ru.softlab.efr.services.insurance.model.rest.RequestStatus;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class RequestControllerTest {

    private static final String POST_CREATE_CLIENT_REQUEST = "/insurance-service/v2/client-request";
    private static final String PUT_CLIENT_REQUEST = "/insurance-service/v2/client-request";
    private static final String GET_CLIENT_REQUEST = "/insurance-service/v2/client-request";
    private static final String POST_PROCESSING_REQUEST = "/insurance-service/v2/client-request/%s/processing";
    private static final String POST_SET_REQUEST_CLOSE = "/insurance-service/v2/client-request/%s/close";
    private static final String GET_REQUEST = "/insurance-service/v2/client-request/%s/get";
    private static final String POST_CREATE_REQUEST_ATTACHMENT = "/insurance-service/v2/client-request/temp/%s/attachment";
    private static final String GET_ATTACHMENTS_TO_REQUEST = "/insurance-service/v2/client-request/temp/%s/attachment";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Sql(value = {"classpath:test-script.sql", "classpath:test-script-double_clients.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addRequestTest() throws Exception {
        ClientRequestRq clientRequestRq = new ClientRequestRq();
        clientRequestRq.setRequestsTopicId(0L);
        clientRequestRq.setInsuranceId(10L);
        clientRequestRq.setEmail("mail@mailforspam.com");
        clientRequestRq.setPhone("+877777777");
        clientRequestRq.setRequestText("test");

        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();

            mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(clientRequestRq))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            MimeMessage message = greenMail.getReceivedMessages()[0];
            assertEquals("testuser@mailforspam.com", message.getRecipients(Message.RecipientType.TO)[0].toString());
            assertNotNull(GreenMailUtil.getBody(message));
            MimeMultipart body = (MimeMultipart) message.getContent();
            String emailHtml = (String) body.getBodyPart(0).getContent();
            assertTrue(emailHtml.contains("Фамилия: Иванов"));
            assertTrue(emailHtml.contains("Имя: Иван"));
            assertTrue(emailHtml.contains("Отчество: Петрович"));
            assertTrue(emailHtml.contains("Тема обращения: Расторжение договора"));
            assertTrue(emailHtml.contains("Номер договора: 23К010110000003"));
            assertTrue(emailHtml.contains("Контактный номер телефона: +877777777"));
            assertTrue(emailHtml.contains("E-mail: mail@mailforspam.com"));
            assertTrue(emailHtml.contains("Текст обращения: test"));
        } finally {
            greenMail.stop();
        }
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addRequestToNotOwnedInsuranceTest() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();

            ClientRequestRq clientRequestRq = new ClientRequestRq();
            clientRequestRq.setRequestsTopicId(0L);
            clientRequestRq.setInsuranceId(2L);
            clientRequestRq.setEmail("mail@mailforspam.com");
            clientRequestRq.setPhone("+877777777");
            clientRequestRq.setRequestText("test");

            mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(clientRequestRq))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isForbidden());
        } finally {
            greenMail.stop();
        }
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addRequestNoRequestTest() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();

            ClientRequestRq clientRequestRq = new ClientRequestRq();
            clientRequestRq.setRequestsTopicId(0L);
            clientRequestRq.setEmail("mail@mailforspam.com");
            clientRequestRq.setPhone("+877777777");
            clientRequestRq.setRequestText("test");
            mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(clientRequestRq))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        } finally {
            greenMail.stop();
        }
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getClientRequestTest() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();

            ClientRequestRq clientRequestRq = new ClientRequestRq();
            clientRequestRq.setRequestsTopicId(0L);
            clientRequestRq.setInsuranceId(10L);
            clientRequestRq.setEmail("mail@mailforspam.com");
            clientRequestRq.setPhone("+877777777");
            clientRequestRq.setRequestText("test");
            //заполнение данных для CLIENT_PRINCIPAL_DATA
            mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(clientRequestRq))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            //получение элемента для CLIENT_PRINCIPAL_DATA
            mockMvc.perform(get(GET_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").isNotEmpty());
            //получение элемента для CLIENT_PRINCIPAL_DATA_2, не должно возвращаться ничего в контенте
            mockMvc.perform(get(GET_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA_2))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(0)));
        } finally {
            greenMail.stop();
        }
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRequestTest() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();

            ClientRequestRq clientRequestRq = new ClientRequestRq();
            clientRequestRq.setRequestsTopicId(0L);
            clientRequestRq.setInsuranceId(10L);
            clientRequestRq.setEmail("mail@mailforspam.com");
            clientRequestRq.setPhone("+877777777");
            clientRequestRq.setRequestText("test");
            mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(clientRequestRq))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            //получение элемента для CLIENT_PRINCIPAL_DATA
            ResultActions resultActions = mockMvc.perform(get(GET_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").isNotEmpty());
            //получение подробных данных админом
            mockMvc.perform(get(String.format(GET_REQUEST, TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id").toString()))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
            //попытка получения подробных данных пользователем
            mockMvc.perform(get(String.format(GET_REQUEST, TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id").toString()))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().is(403));
        } finally {
            greenMail.stop();
        }
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void processingRequestTest() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();

            ClientRequestRq clientRequestRq = new ClientRequestRq();
            clientRequestRq.setRequestsTopicId(0L);
            clientRequestRq.setInsuranceId(10L);
            clientRequestRq.setEmail("mail@mailforspam.com");
            clientRequestRq.setPhone("+877777777");
            clientRequestRq.setRequestText("test");
            mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(clientRequestRq))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            //получение элемента для CLIENT_PRINCIPAL_DATA
            ResultActions resultActions = mockMvc.perform(get(GET_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").isNotEmpty());
            //смена статуса обращение
            ProcessingClientRequestRq rq = new ProcessingClientRequestRq();
            rq.setStatus(RequestStatus.PROCESSED);
            rq.setClientComment("комментарий для клиента");
            rq.setInfo("внутренний комментарий");
            mockMvc.perform(post(String.format(POST_PROCESSING_REQUEST,
                    TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id").toString()))
                    .content(TestUtils.convertObjectToJson(rq))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk());
            //проверка статуса обращения
            mockMvc.perform(get(String.format(GET_REQUEST, TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id").toString()))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(RequestStatus.PROCESSED.toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.clientComment", is("комментарий для клиента")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.additionalInfo", is("внутренний комментарий")))
            ;
        } finally {
            greenMail.stop();
        }
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void changeRequestActiveStatusByAdminTest() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();

            ClientRequestRq clientRequestRq = new ClientRequestRq();
            clientRequestRq.setRequestsTopicId(0L);
            clientRequestRq.setInsuranceId(10L);
            clientRequestRq.setEmail("mail@mailforspam.com");
            clientRequestRq.setPhone("+877777777");
            clientRequestRq.setRequestText("test");
            mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(clientRequestRq))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            //получение элемента для CLIENT_PRINCIPAL_DATA
            ResultActions resultActions = mockMvc.perform(get(GET_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").isNotEmpty());
            //закрытие обращение
            mockMvc.perform(post(String.format(POST_SET_REQUEST_CLOSE,
                    TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id").toString()))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk());
            //проверка статуса обращения
            mockMvc.perform(get(String.format(GET_REQUEST, TestUtils.extractDataFromResultJson(resultActions, "$.content[0].id").toString()))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.isActive", is(false)));
        } finally {
            greenMail.stop();
        }
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void filterClientRequestByAdminTest() throws Exception {

        ClientRequestRq clientRequestRq = new ClientRequestRq();
        clientRequestRq.setRequestsTopicId(0L);
        clientRequestRq.setInsuranceId(10L);
        clientRequestRq.setEmail("mail@mailforspam.com");
        clientRequestRq.setPhone("+877777777");
        clientRequestRq.setRequestText("test");
        mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(clientRequestRq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        FilterRequestsRq filterData = new FilterRequestsRq();
        //выполнение без фильтра
        mockMvc.perform(put(PUT_CLIENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("hasFilter", "false")
                .content(TestUtils.convertObjectToJson(filterData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)));
        //применение фильтра
        filterData.setClientName("noname");
        mockMvc.perform(put(PUT_CLIENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(filterData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(0)));

    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createRequestAndUploadAttachmentTest() throws Exception {
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();

            ClientRequestRq clientRequestRq = new ClientRequestRq();
            clientRequestRq.setRequestsTopicId(0L);
            clientRequestRq.setInsuranceId(10L);
            clientRequestRq.setEmail("mail@mailforspam.com");
            clientRequestRq.setPhone("+877777777");
            clientRequestRq.setRequestText("test");
            ResultActions resultActions = mockMvc.perform(post(POST_CREATE_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(clientRequestRq))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            int requestId = TestUtils.extractDataFromResultJson(resultActions, "$.id");

            ClassPathResource uploadFile = new ClassPathResource("request-attachment.txt");
            MockMultipartFile firstFile = new MockMultipartFile(
                    "content", uploadFile.getFilename(), "text/plain",
                    StreamUtils.copyToByteArray(uploadFile.getInputStream()));
            mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_CREATE_REQUEST_ATTACHMENT, requestId))
                    .file(firstFile)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                    .andExpect(status().isOk());

            mockMvc.perform(get(String.format(GET_ATTACHMENTS_TO_REQUEST, requestId))
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.elements").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.elements", hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].requestAttachId").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].fileName", is("request-attachment.txt")));
        } finally {
            greenMail.stop();
        }
    }
}
