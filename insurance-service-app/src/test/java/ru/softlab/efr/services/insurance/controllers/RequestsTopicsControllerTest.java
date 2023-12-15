package ru.softlab.efr.services.insurance.controllers;

import org.junit.Before;
import org.junit.Ignore;
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
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.rest.BaseTopicsModel;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;
import static ru.softlab.efr.services.insurance.stubs.TestData.USER_PRINCIPAL_DATA;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class RequestsTopicsControllerTest {

    private static final String GET_CLIENT_REQUEST_TOPICS = "/insurance-service/v2/client-request-topic/";
    private static final String GET_CLIENT_REQUEST_TOPIC_BY_ID = "/insurance-service/v2/client-request-topic/%s";
    private static final String GET_CLIENT_REQUEST_ADMIN_TOPICS = "/insurance-service/v2/admin-request-topic/";
    private static final String POST_CLIENT_REQUEST_TOPIC = "/insurance-service/v2/client-request-topic/";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void availableTopicsTest() throws Exception {
        mockMvc.perform(get(GET_CLIENT_REQUEST_TOPICS)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.topics", hasSize(5)));
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void availableTopicByIdTest() throws Exception {
        mockMvc.perform(get(String.format(GET_CLIENT_REQUEST_TOPIC_BY_ID, 0L))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Расторжение договора")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.topicDescription", is("")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is("testuser@mailforspam.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive", is(true)));
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void availablePaginatedTest() throws Exception {
        mockMvc.perform(get(GET_CLIENT_REQUEST_ADMIN_TOPICS)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(6)));
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void availablePostNewTest() throws Exception {
        BaseTopicsModel updateTopicRequest = new BaseTopicsModel();
        updateTopicRequest.setName("test");
        updateTopicRequest.setTopicDescription("test");
        updateTopicRequest.setEmail("test@mailforspam.com");
        updateTopicRequest.setIsActive(true);
        mockMvc.perform(post(POST_CLIENT_REQUEST_TOPIC)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(updateTopicRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        mockMvc.perform(get(GET_CLIENT_REQUEST_TOPICS)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.topics", hasSize(6)));
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void availablePostModifyTest() throws Exception {
        BaseTopicsModel updateTopicRequest = new BaseTopicsModel();
        updateTopicRequest.setId(0L);
        updateTopicRequest.setName("test");
        updateTopicRequest.setTopicDescription("test");
        updateTopicRequest.setEmail("test@mailforspam.com");
        updateTopicRequest.setIsActive(true);
        mockMvc.perform(post(POST_CLIENT_REQUEST_TOPIC)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(updateTopicRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_CLIENT_REQUEST_TOPIC_BY_ID, 0L))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.topicDescription", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is("test@mailforspam.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive", is(true)));
    }

}
