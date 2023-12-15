package ru.softlab.efr.services.insurance.controllers;


import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import ru.softlab.efr.services.insurance.model.db.ClientShortData;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.repositories.ClientCheckDTO;
import ru.softlab.efr.services.insurance.services.ClientCheckService;
import ru.softlab.efr.services.insurance.services.ClientUnloadService;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class ClientControllerTest {

    private static final String SEARCH_CLIENTS_REQUEST = "/insurance-service/v2/clients/search?surName={surName}&firstName={firstName}&middleName={middleName}&birthDate={birthDate}&docType={docType}&docSeries={docSeries}&docNumber={docNumber}&phoneNumber={phoneNumber}&email={email}";
    private static final String SEARCH_CLIENTS_REQUEST2 = "/insurance-service/v2/private/clients/search?surName={surName}&firstName={firstName}&middleName={middleName}&birthDate={birthDate}&docType={docType}&docSeries={docSeries}&docNumber={docNumber}&phoneNumber={phoneNumber}&email={email}";
    private static final String SEARCH_CLIENTS_REQUEST3 = "/insurance-service/v2/clients?surName={surName}";
    private static final String SEARCH_CLIENTS_REQUEST4 = "/insurance-service/v2/clients?startCheckDate={startCheckDate}&endCheckDate={endCheckDate}";
    private static final String GET_ALL_CLIENTS_REQUEST = "/insurance-service/v2/clients?surName={surName}&firstName={firstName}&middleName={middleName}&birthDate={birthDate}&docType={docType}&docSeries={docSeries}&docNumber={docNumber}&phoneNumber={phoneNumber}&email={email}";
    private static final String SAVE_CLIENT_REQUEST = "/insurance-service/v2/clients/saveShortClientData";
    private static final String GET_CLIENT_REQUEST = "/insurance-service/v2/clients/{clientId}";
    private static final String PUT_CLIENT_REQUEST = "/insurance-service/v2/clients/{clientId}";
    private static final String GET_INSPECTION_RESULTS_REQUEST = "/insurance-service/v2/clients/{clientId}/inspectionResults";
    private static final String GET_CONSUMER_REQUEST = "/insurance-service/v2/consumer/";
    private static final String UNLOAD_CLIENTS_XML = "/insurance-service/v2/clients/unload/xml?contractIds={contractIds}&contractIds={contractIds}";
    private static final String UNLOAD_CLIENT_XML = "/insurance-service/v2/clients/{clientId}/unload/xml";
    private static final String GET_CHECK_CLIENTS = "/insurance-service/v2/clients/check/{updateId}?dictName={dictTypeId}";
    private static final String MANUAL_CHECK_CLIENTS = "/insurance-service/v2/clients/check";
    private static final String UNLOAD_CLIENT_WORD = "/insurance-service/v2/clients/{clientId}/unload/word";
    private static final String GET_CLIENT_HISTORY = "/insurance-service/v2/clients/{clientId}/historyChanges";
    private static final String POST_FIND_CLIENT_REQUEST = "/insurance-service/v2/clients/find";
    private static final String PUT_CLIENT_WORKFLOW_AGREEMENTS = "/insurance-service/v1/client/{id}/workflow-agreements?workflowAgreements={workflowAgreements}";

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Rule
    @Autowired
    public HibernateCacheInvalidationRule hibernateCacheInvalidationRule;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private ClientUnloadService clientUnloadService;

    @Autowired
    private ClientCheckService clientCheckService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    /**
     * Тест сервиса GET /insurance-service/v2/clients?surName={surName}&firstName={firstName}&middleName={middleName}&birthDate={birthDate}&docType={docType}&docSeries={docSeries}&docNumber={docNumber}&phoneNumber={phoneNumber}&email={email}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllClientsInternalServerError() throws Exception {
        String surName = "";
        String firstName = "";
        String middleName = "";
        String birthDate = "";
        String docType = "";
        String docSeries = "";
        String docNumber = "";
        String phoneNumber = "";
        String email = "";

        mockMvc.perform(get(GET_ALL_CLIENTS_REQUEST, surName, firstName, middleName, birthDate, docType, docSeries, docNumber, phoneNumber, email)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients?surName={surName}&firstName={firstName}&middleName={middleName}&birthDate={birthDate}&docType={docType}&docSeries={docSeries}&docNumber={docNumber}&phoneNumber={phoneNumber}&email={email}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllClientsBadRequest() throws Exception {
        String surName = "Иванов";
        String firstName = "Иван";
        String middleName = "";
        String birthDate = "32.03.1986";
        String docType = "";
        String docSeries = "";
        String docNumber = "";
        String phoneNumber = "";
        String email = "";

        mockMvc.perform(get(GET_ALL_CLIENTS_REQUEST, surName, firstName, middleName, birthDate, docType, docSeries, docNumber, phoneNumber, email)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients?surName={surName}&firstName={firstName}&middleName={middleName}&birthDate={birthDate}&docType={docType}&docSeries={docSeries}&docNumber={docNumber}&phoneNumber={phoneNumber}&email={email}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllClientsForbidden() throws Exception {
        String surName = "Иванов";
        String firstName = "Иван";
        String middleName = "Иванович";
        String birthDate = "30.03.1986";
        String docType = "PASSPORT_RF";
        String docSeries = "1234";
        String docNumber = "123456";
        String phoneNumber = "+791100000001";
        String email = "ivanov@example.org";

        mockMvc.perform(get(GET_ALL_CLIENTS_REQUEST, surName, firstName, middleName, birthDate, docType, docSeries, docNumber, phoneNumber, email)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }


    /**
     * Тест сервиса GET /insurance-service/v2/clients?surName={surName}&firstName={firstName}&middleName={middleName}&birthDate={birthDate}&docType={docType}&docSeries={docSeries}&docNumber={docNumber}&phoneNumber={phoneNumber}&email={email}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllClientsByFullDataSuccess() throws Exception {
        String surName = "Иванов";
        String firstName = "Иван";
        String middleName = "Иванович";
        String birthDate = "30.03.1986";
        String docType = "PASSPORT_RF";
        String docSeries = "1234";
        String docNumber = "123456";
        String phoneNumber = "79110000001";
        String email = "ivanov@example.org";

        mockMvc.perform(get(GET_ALL_CLIENTS_REQUEST, surName, firstName, middleName, birthDate, docType, docSeries, docNumber, phoneNumber, email)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalFounds", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", hasSize(1)));
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/search?surName={surName}&firstName={firstName}&middleName={middleName}&birthDate={birthDate}&docType={docType}&docSeries={docSeries}&docNumber={docNumber}&phoneNumber={phoneNumber}&email={email}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void searchClientsByFullDataSuccess() throws Exception {
        String surName = "Иванов";
        String firstName = "Иван";
        String middleName = "Иванович";
        String birthDate = "30.03.1986";
        String docType = "PASSPORT_RF";
        String docSeries = "1234";
        String docNumber = "123456";
        String phoneNumber = "+791100000001";
        String email = "ivanov@example.org";

        mockMvc.perform(get(SEARCH_CLIENTS_REQUEST, surName, firstName, middleName, birthDate, docType, docSeries, docNumber, phoneNumber, email)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalFounds", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", hasSize(2)));
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients?startCheckDate={startCheckDate}&endCheckDate={endCheckDate}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void searchClientsByPartSurNameDataSuccess() throws Exception {
        String startCheckDate = "10.01.2019";
        String endCheckDate = "15.01.2019";

        mockMvc.perform(get(SEARCH_CLIENTS_REQUEST4, startCheckDate, endCheckDate)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalFounds", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", hasSize(4)));

        mockMvc.perform(get(SEARCH_CLIENTS_REQUEST4, startCheckDate, startCheckDate)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalFounds", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", hasSize(0)));
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients?surName={surName}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void searchClientsByCheckPeriodDataSuccess() throws Exception {
        String surName = "ива";

        mockMvc.perform(get(SEARCH_CLIENTS_REQUEST3, surName)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalFounds", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", hasSize(3)));
    }

    /**
     * Тест сервиса GET /insurance-service/v2/private/clients/search?surName={surName}&firstName={firstName}&middleName={middleName}&birthDate={birthDate}&docType={docType}&docSeries={docSeries}&docNumber={docNumber}&phoneNumber={phoneNumber}&email={email}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void searchClientsByFullDataSuccess2() throws Exception {
        String surName = "Иванов";
        String firstName = "Иван";
        String middleName = "Иванович";
        String birthDate = "1986-03-30";
        String docType = "PASSPORT_RF";
        String docSeries = "1234";
        String docNumber = "123456";
        String phoneNumber = "+791100000001";
        String email = "ivanov@example.org";

        mockMvc.perform(get(SEARCH_CLIENTS_REQUEST2, surName, firstName, middleName, birthDate, docType, docSeries, docNumber, phoneNumber, email)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalFounds", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", hasSize(0)));
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients?surName={surName}&firstName={firstName}&middleName={middleName}&birthDate={birthDate}&docType={docType}&docSeries={docSeries}&docNumber={docNumber}&phoneNumber={phoneNumber}&email={email}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllClientsByShortDataSuccess() throws Exception {
        String surName = "Иванов";
        String firstName = "Иван";
        String middleName = "Иванович";
        String birthDate = "30.03.1986";
        String docType = "";
        String docSeries = "";
        String docNumber = "";
        String phoneNumber = "";
        String email = "";

        mockMvc.perform(get(GET_ALL_CLIENTS_REQUEST, surName, firstName, middleName, birthDate, docType, docSeries, docNumber, phoneNumber, email)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalFounds", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", hasSize(2)));
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/saveShortClientData
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveClientEmptyRequest() throws Exception {
        mockMvc.perform(post(SAVE_CLIENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/saveShortClientData
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveClientBadRequest() throws Exception {
        ShortClientData clientData = new ShortClientData(
                null,
                null,
                "Иван",
                "Иванович",
                LocalDate.of(1986, 3, 30),
                Gender.FEMALE,
                DocumentType.PASSPORT_RF,
                "1234",
                "123456",
                "+791100000001",
                "ivanov@example.org"
        );

        mockMvc.perform(post(SAVE_CLIENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(clientData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/saveShortClientData
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveClientForbidden() throws Exception {
        ShortClientData clientData = new ShortClientData();
        mockMvc.perform(post(SAVE_CLIENT_REQUEST)
                .content(TestUtils.convertObjectToJson(clientData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/saveShortClientData
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveClientSuccess() throws Exception {

        ShortClientData clientData = new ShortClientData(
                null,
                "Тестов",
                "Тест",
                "Тестович",
                LocalDate.of(1986, 3, 30),
                Gender.FEMALE,
                DocumentType.PASSPORT_RF,
                "1234",
                "123456",
                "+791100000123",
                "ivanov@example.domain"
        );

        ResultActions resultActions = mockMvc.perform(post(SAVE_CLIENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(clientData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());

        int clientId = TestUtils.extractDataFromResultJson(resultActions, "$");
        mockMvc.perform(get(GET_CLIENT_REQUEST, clientId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(MockMvcResultMatchers.jsonPath("$.surName", is("Тестов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("Тест")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.middleName", is("Тестович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender", is("FEMALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documents", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documents[0].docType", is("PASSPORT_RF")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documents[0].docSeries", is("1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documents[0].docNumber", is("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phones", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phones[0].number", is("+791100000123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is("ivanov@example.domain")))
        ;

    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/saveShortClientData
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putClientSuccess() throws Exception {

        Client clientData1 = new Client();
        clientData1.setId(String.valueOf(9));
        clientData1.setSurName("Иванов");
        clientData1.setFirstName("Иван");
        clientData1.setMiddleName("Иванович");
        clientData1.setBirthDate(LocalDate.of(1986, 3, 30));
        clientData1.setRiskLevelDesc("Проверка");
        clientData1.setWorkflowAgreements(true);

        mockMvc.perform(put(PUT_CLIENT_REQUEST, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(clientData1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(PUT_CLIENT_REQUEST, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(clientData1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.riskLevelDesc", is("Проверка")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.workflowAgreements", is(true)));
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putWorkFlowAgreements() throws Exception {
        mockMvc.perform(put(PUT_CLIENT_WORKFLOW_AGREEMENTS, 1,true)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
    /**
     * Тест сервиса GET /insurance-service/v2/clients/{clientId}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getClientForbidden() throws Exception {
        String clientId = "1";
        mockMvc.perform(get(GET_CLIENT_REQUEST, clientId)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }


    /**
     * Тест сервиса GET /insurance-service/v2/clients/{clientId}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getClientBadRequest() throws Exception {
        String clientId = "asdf";
        mockMvc.perform(get(GET_CLIENT_REQUEST, clientId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/{clientId}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getClientNotFound() throws Exception {
        String clientId = "6";
        mockMvc.perform(get(GET_CLIENT_REQUEST, clientId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/{clientId}/historyChanges
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getClientHistorySuccess() throws Exception {
        Long clientId = 1L;
        mockMvc.perform(get(GET_CLIENT_HISTORY, clientId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientData[0].userFullName", is("Иванов Иван Иванович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientData[0].client.surName", is("Кристиан")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientData[0].client.firstName", is("Бейл")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientData[0].client.middleName", is("Младший")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientData[0].client.inn", is("123456789123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientData[0].client.snils", is("12345678910")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientDocuments[1].document.docType", is("PASSPORT_RF")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientDocuments[1].lastModifiedDate", is("20-03-2019 10:43:09")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientPhones[0].lastModifiedDate", is("21-03-2019 11:44:18")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientPhones[1].phone.number", is("+791100000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientAddresses[0].lastModifiedDate", is("21-03-2019 11:22:26")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientAddresses[1].address.area", is("Пало-альто")));
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/{clientId}/historyChanges
     * для получения отказа по причине отсутствия прав на просмотр данных по клиенту.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getClientHistoryForbidden() throws Exception {
        Long clientId = 1L;
        mockMvc.perform(get(GET_CLIENT_HISTORY, clientId)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/{clientId}/historyChanges
     * для проверки получения корректного ответа в случае, когда клиента с указанным идентификатором нет.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getClientHistoryNotFound() throws Exception {
        Long clientId = 6L;
        mockMvc.perform(get(GET_CLIENT_HISTORY, clientId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/{clientId}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getClientSuccess() throws Exception {
        String clientId = "1";
        mockMvc.perform(get(GET_CLIENT_REQUEST, clientId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surName", is("Иванов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("Иван")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.middleName", is("Иванович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthPlace", is("СССР")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resident", is("russian")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789123")))
        ;
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getInspectionResultsSuccess() throws Exception {
        String clientId = "1";
        mockMvc.perform(get(GET_INSPECTION_RESULTS_REQUEST, clientId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].lastDateCheck", is("2019-01-15 10:51:26")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].type", is("TERRORIST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results.[0].result", is("TRUE")));
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getConsumerSuccess() throws Exception {
        mockMvc.perform(get(GET_CONSUMER_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is("9")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surName", is("Иванов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("Иван")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.middleName", is("Петрович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is("ivanov@example.org")));
    }

    /**
     * Тест сервиса GET /insurance-service/v2/consumer
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getConsumerNotFound() throws Exception {
        mockMvc.perform(get(GET_CONSUMER_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA_2))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/unload/xml
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeOkUnloadClientsXml() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(UNLOAD_CLIENTS_XML, 2, 3)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("<?xml version=\"1.0\" encoding=\"UTF-8\""));

        try (InputStream xsdSchema = ClientControllerTest.class.getResourceAsStream("/client/clientinfo.xsd")) {
            byte[] serviceResult = resultActions.andReturn().getResponse().getContentAsByteArray();
            assertTrue(clientUnloadService.getClientXmlErrors(serviceResult, xsdSchema).isEmpty());
        }
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/%s/unload/xml
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeOkUnloadClientXml() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(UNLOAD_CLIENT_XML, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("<?xml version=\"1.0\" encoding=\"UTF-8\""));

        try (InputStream xsdSchema = ClientControllerTest.class.getResourceAsStream("/client/clientinfo.xsd")) {
            byte[] serviceResult = resultActions.andReturn().getResponse().getContentAsByteArray();
            assertTrue(clientUnloadService.getClientXmlErrors(serviceResult, xsdSchema).isEmpty());
        }
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/check/{updateId}?dictName={dictTypeId}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeOkCheckClientTerrorist() throws Exception {
        performCheckClient(DictType.TERRORIST);
        SQLStatementCountValidator.reset();
        assertEquals(5, clientCheckService.countByType(CheckUnitTypeEnum.TERRORIST, null).longValue()); // все 5 клиентов проверены

        // 1 клиент (с идентификаторам 1) признан терорристом
        List<Long> clientIds = clientCheckService.findAllTerroristsAndBlocked(CheckUnitTypeEnum.TERRORIST, null)
                .stream()
                .map(ClientCheckDTO::getId)
                .collect(Collectors.toList());
        assertEquals(3, clientIds.size());
        assertTrue(clientIds.contains(1L));

        assertSelectCount(2);

    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/check/{updateId}?dictName={dictTypeId}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeOkCheckClientPassport() throws Exception {
        performCheckClient(DictType.INVALID_IDENTITY_DOC);

        assertEquals(5, clientCheckService.countByType(CheckUnitTypeEnum.INVALID_IDENTITY_DOC, null).longValue()); // все 5 клиентов проверены

        // 3 клиента (с идентификаторами 1, 2 и 4) признаны имеющими недействительные паспорта
        List<Long> clientIds = clientCheckService.findAllSuspiciousClients(CheckUnitTypeEnum.INVALID_IDENTITY_DOC, null)
                .stream()
                .map(ClientShortData::getId)
                .collect(Collectors.toList());
        assertEquals(3, clientIds.size());
        assertTrue(clientIds.contains(1L) && clientIds.contains(2L) && clientIds.contains(4L));

        // TODO: добавить проверку количества выполняемых SQL-запросов после оптимизации генерации отчёта
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/check/{updateId}?dictName={dictTypeId}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeOkCheckClientBlockage() throws Exception {
        performCheckClient(DictType.BLOCKAGE);
        SQLStatementCountValidator.reset();
        assertEquals(5, clientCheckService.countByType(CheckUnitTypeEnum.BLOCKAGE, null).longValue()); // все 5 клиентов проверены

        // 3 клиента (с идентификаторами 1, 2 и 4) признаны имеющими заблокированные счета
        List<Long> clientIds = clientCheckService.findAllTerroristsAndBlocked(CheckUnitTypeEnum.BLOCKAGE, null)
                .stream()
                .map(ClientCheckDTO::getId)
                .collect(Collectors.toList());
        assertEquals(3, clientIds.size());
        assertTrue(clientIds.contains(1L) && clientIds.contains(2L) && clientIds.contains(4L));

        assertSelectCount(2);
    }

    private void performCheckClient(DictType dictType) throws Exception {
        mockMvc.perform(get(GET_CHECK_CLIENTS, 1, dictType)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/check
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void manualCheckClientBlockageSuccess() throws Exception {
        CheckClientRq rq = new CheckClientRq();
        rq.addClientIdsItem(2L);
        rq.setAllClientCheck(false);
        rq.addDictTypesItem(DictType.BLOCKAGE);
        mockMvc.perform(post(MANUAL_CHECK_CLIENTS)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        assertEquals(1, clientCheckService.countByType(CheckUnitTypeEnum.BLOCKAGE, Collections.singletonList(2L)).longValue()); // 1 клиент проверен по справочнику блокировок/заморозок
        // 1 клиент (с идентификатором 2) признан имеющими заблокированные счета
        List<Long> clientIds = clientCheckService.findAllTerroristsAndBlocked(CheckUnitTypeEnum.BLOCKAGE, Collections.singletonList(2L))
                .stream()
                .map(ClientCheckDTO::getId)
                .collect(Collectors.toList());
        assertEquals(1, clientIds.size());
        assertTrue(clientIds.contains(2L));
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/check
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void manualCheckClientTerroristSuccess() throws Exception {
        CheckClientRq rq = new CheckClientRq();
        rq.addClientIdsItem(1L);
        rq.addClientIdsItem(2L);
        rq.setAllClientCheck(false);
        rq.addDictTypesItem(DictType.TERRORIST);
        mockMvc.perform(post(MANUAL_CHECK_CLIENTS)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        assertEquals(2, clientCheckService.countByType(CheckUnitTypeEnum.TERRORIST, Arrays.asList(1L, 2L)).longValue()); // 2 клиента проверены по справочнику террористов/экстремистов
        // 1 клиент (с идентификатором 1) признан террористом
        List<Long> clientIds = clientCheckService.findAllTerroristsAndBlocked(CheckUnitTypeEnum.BLOCKAGE, Arrays.asList(1L, 2L))
                .stream()
                .map(ClientCheckDTO::getId)
                .collect(Collectors.toList());
        assertEquals(1, clientIds.size());
        assertTrue(clientIds.contains(1L));
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/check
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void manualCheckClientTerroristAndPassportSuccess() throws Exception {
        CheckClientRq rq = new CheckClientRq();
        rq.addClientIdsItem(2L);
        rq.addClientIdsItem(3L);
        rq.addDictTypesItem(DictType.TERRORIST);
        rq.addDictTypesItem(DictType.INVALID_IDENTITY_DOC);
        rq.setAllClientCheck(false);
        mockMvc.perform(post(MANUAL_CHECK_CLIENTS)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        assertEquals(2, clientCheckService.countByType(CheckUnitTypeEnum.TERRORIST, Arrays.asList(2L, 3L)).longValue()); // 2 клиента проверены по справочнику террористов/экстремистов
        assertEquals(2, clientCheckService.countByType(CheckUnitTypeEnum.INVALID_IDENTITY_DOC, Arrays.asList(2L, 3L)).longValue()); // 2 клиента проверены по справочнику недействительных паспортов
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/check
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void manualCheckClientForbidden() throws Exception {
        CheckClientRq rq = new CheckClientRq();
        rq.addClientIdsItem(1L);
        rq.setAllClientCheck(false);
        rq.addDictTypesItem(DictType.BLOCKAGE);
        mockMvc.perform(post(MANUAL_CHECK_CLIENTS)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/clients/%s/unload/xml
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeFailUnloadClientXml() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(UNLOAD_CLIENT_XML, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        byte[] byteArrayResult = resultActions.andReturn().getResponse().getContentAsByteArray();
        String stringXml = new String(byteArrayResult, StandardCharsets.UTF_8).replace("123456789123", "1234567891231");
        assertTrue(stringXml.contains("<?xml version=\"1.0\" encoding=\"UTF-8\""));
        String errorText = clientUnloadService.getClientXmlErrors(stringXml.getBytes(StandardCharsets.UTF_8), ClientControllerTest.class.getResourceAsStream("/client/clientinfo.xsd"));
        assertTrue(errorText.contains("cvc-pattern-valid: Value '1234567891231' is not facet-valid with respect to pattern '[0-9]{12}|'"));


        resultActions = mockMvc.perform(get(UNLOAD_CLIENT_XML, 9)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        errorText = new String(resultActions.andReturn().getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
        assertTrue(errorText.contains("cvc-pattern-valid: Value '1234567891234' is not facet-valid with respect to pattern '[0-9]{12}|'"));
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void printCardClientSuccess() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(UNLOAD_CLIENT_WORD, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document")));
        FileUtils.writeByteArrayToFile(new File("target/".concat("word-card-for-client".concat(".docx"))), resultActions.andReturn().getResponse().getContentAsByteArray());
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/find
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findClientForbidden() throws Exception {

        SQLStatementCountValidator.reset();

        FindClientRq rq = new FindClientRq();
        rq.setSurName("Иванов");
        rq.setFirstName("Иван");
        rq.setMiddleName("Иванович");
        rq.setBirthDate(LocalDate.now());
        rq.setPhoneNumber("79999999999");
        mockMvc.perform(post(POST_FIND_CLIENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

        assertSelectCount(0);
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/find
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findClientNotExistClientSuccess() throws Exception {

        SQLStatementCountValidator.reset();

        FindClientRq rq = new FindClientRq();
        rq.setSurName("Иванов");
        rq.setFirstName("Иван");
        rq.setMiddleName("Иванович");
        rq.setBirthDate(LocalDate.now());
        rq.setPhoneNumber("79999999999");
        mockMvc.perform(post(POST_FIND_CLIENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
        ;

        // 1 запрос поиск по ФИО, дате рождения, номеру телефона
        // 2 запрос поиск по номеру телефона
        // 3 запрос поиск по ФИО, дате рождения
        assertSelectCount(3);
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/find
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findClientExistClientSuccess() throws Exception {

        SQLStatementCountValidator.reset();

        FindClientRq rq = new FindClientRq();
        rq.setSurName("Иванов");
        rq.setFirstName("Иван");
        rq.setMiddleName("Иванович");
        rq.setBirthDate(LocalDate.of(1986, 3, 30));
        rq.setPhoneNumber("79110000001");
        mockMvc.perform(post(POST_FIND_CLIENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
        ;

        // 1 запрос поиск по ФИО, дате рождения, номеру телефона
        // запросы по данным клиента из таблиц
        // client_employees, phones_for_client, person_decisions, documents_for_client, addresses_for_client
        assertSelectCount(6);
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/find
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findClientExistPhoneError() throws Exception {

        SQLStatementCountValidator.reset();

        FindClientRq rq = new FindClientRq();
        rq.setSurName("Телефон");
        rq.setFirstName("Телефон");
        rq.setMiddleName("Телефон");
        rq.setBirthDate(LocalDate.of(1986, 3, 30));
        rq.setPhoneNumber("79110000001");
        mockMvc.perform(post(POST_FIND_CLIENT_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]",
                        is("Указанный номер телефона уже зарегистрирован в Системе. " +
                                "Оформление договора невозможно. " +
                                "Необходимо изменить номер телефона или обратитесь в службу поддержки ")))
        ;

        // 1 запрос поиск по ФИО, дате рождения, номеру телефона
        // 2 запрос поиск количества клиентов по номеру телефона
        assertSelectCount(2);
    }

    /**
     * Тест сервиса POST /insurance-service/v2/clients/find
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findClientDifferentPhoneSuccess() throws Exception {

        SQLStatementCountValidator.reset();
        ServerSetup serverSetup = new ServerSetup(3025, "localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();

            FindClientRq rq = new FindClientRq();
            rq.setSurName("Иванов");
            rq.setFirstName("Иван");
            rq.setMiddleName("Иванович");
            rq.setBirthDate(LocalDate.of(1986, 3, 30));
            rq.setPhoneNumber("79119999991");
            mockMvc.perform(post(POST_FIND_CLIENT_REQUEST)
                    .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                    .content(TestUtils.convertObjectToJson(rq))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
            ;

            MimeMessage message = greenMail.getReceivedMessages()[0];
            assertEquals("test@test.ru", message.getRecipients(Message.RecipientType.TO)[0].toString());
            assertNotNull(GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));

            MimeMultipart body = (MimeMultipart) message.getContent();
            TestCase.assertTrue(body.getContentType().startsWith("multipart/mixed"));
            assertEquals(1, body.getCount());
            BodyPart text = body.getBodyPart(0);
            assertEquals("При оформлении договора возможно дублирование " +
                    "клиента с данными: Иванов Иван Иванович, 1986-03-30, 79119999991.", text.getContent());

        } finally {
            greenMail.stop();
        }

        // 1 запрос поиск по ФИО, дате рождения, номеру телефона
        // 2 запрос поиск количества клиентов по номеру телефона
        // 3 запрос поиск по ФИО, дате рождения
        // 4 запрос получения значения настройки с email адресом для уведомлений
        assertSelectCount(4);
    }

}
