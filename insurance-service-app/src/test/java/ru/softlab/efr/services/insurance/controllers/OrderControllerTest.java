package ru.softlab.efr.services.insurance.controllers;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.rest.OrderData;
import ru.softlab.efr.services.insurance.utils.TestUtils;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.USER_PRINCIPAL_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class OrderControllerTest {

    private static final String GET_ENTITYLIST_REQUEST = "/insurance-service/public/v2/order";
    private static final String GET_ENTITY_BY_ID_REQUEST = "/insurance-service/public/v2/order/{id}";
    private static final String POST_CREATE_ENTITY_REQUEST = "/insurance-service/public/v2/order/create";
    private static final String PUT_UPDATE_ENTITY_REQUEST = "/insurance-service/v2/order/{id}";

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Rule
    @Autowired
    public HibernateCacheInvalidationRule hibernateCacheInvalidationRule;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }


    /**
     * Тест сервиса GET /insurance-service/v2/order
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getListSuccess() throws Exception {
        mockMvc.perform(get(GET_ENTITYLIST_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(3)));
    }

    /**
     * Тест сервиса GET /insurance-service/v2/order
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createBadRequest() throws Exception {
        mockMvc.perform(get(POST_CREATE_ENTITY_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса GET /insurance-service/v2/order
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createSuccess() throws Exception {

        mockServer.expect(requestTo("https://web.rbsuat.com/rshb/payment/rest/register.do"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"orderId\":\"70906e55-7114-41d6-8332-4609dc6590f4\",\"formUrl\":\"https://server/\"}", MediaType.APPLICATION_JSON_UTF8));

        OrderData orderData = new OrderData();
        orderData.setAmount(12345L);
        orderData.setCurrency(810);
        orderData.setUrl("some-url");

        mockMvc.perform(post(POST_CREATE_ENTITY_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(orderData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redirectUrl", is("https://server/")));

        mockServer.reset();

        mockServer.expect(requestTo("https://web.rbsuat.com/rshb/payment/rest/getOrderStatus.do"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"expiration\":\"201512\",\"cardholderName\":\"trtr\",\"depositAmount\":789789,\"currency\":\"643\",\"approvalCode\":\"123456\",\"authCode\":2,\"clientId\":\"666\",\"bindingId\":\"07a90a5d-cc60-4d1b-a9e6-ffd15974a74f\",\"ErrorCode\":\"0\",\"ErrorMessage\":\"\",\"OrderStatus\":2,\"OrderNumber\":\"23asdafaf\",\"Pan\":\"411111**1111\",\"Amount\":789789}", MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(get(GET_ENTITY_BY_ID_REQUEST, "4")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderCode", is(2)));

    }

    /**
     * Тест сервиса GET /insurance-service/v2/order
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getByIdSuccess() throws Exception {
        mockMvc.perform(get(GET_ENTITY_BY_ID_REQUEST, "3")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    /**
     * Тест сервиса GET /insurance-service/v2/order
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getByIdNotFound() throws Exception {
        mockMvc.perform(get(GET_ENTITY_BY_ID_REQUEST, "4")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }
}
