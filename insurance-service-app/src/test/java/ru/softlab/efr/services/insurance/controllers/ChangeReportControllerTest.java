package ru.softlab.efr.services.insurance.controllers;


import org.junit.Before;
import org.junit.Rule;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.services.ClientUnloadService;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;
import static ru.softlab.efr.services.insurance.stubs.TestData.USER_PRINCIPAL_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class ChangeReportControllerTest {

    private static final String GET_CONTRACTS_EXTRACT_REQUEST_STATUS = "/insurance-service/v2/contracts/extract/{uuid}/status";
    private static final String GET_CONTRACTS_EXTRACT_CONTENT_REQUEST = "/insurance-service/v2/contracts/extract/{uuid}/content";
    private static final String GET_GENERATE_CHANGE_REPORT = "/insurance-service/v2/changereport/generate?createDate={createDate}&endDate={endDate}";

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Rule
    @Autowired
    public HibernateCacheInvalidationRule hibernateCacheInvalidationRule;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    ClientUnloadService clientUnloadService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void generateChangeReportShouldBeForbidden() throws Exception {
        mockMvc.perform(get(GET_GENERATE_CHANGE_REPORT, "01.01.2017", "01.01.2099")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void generateChangeReportShouldBeSuccess() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(GET_GENERATE_CHANGE_REPORT, "01.01.2017", "01.01.2099")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").isNotEmpty());
        String uuid = TestUtils.extractDataFromResultJson(resultActions, "$.uuid");

        mockMvc.perform(get(GET_CONTRACTS_EXTRACT_REQUEST_STATUS, uuid)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("SAVE")));

        mockMvc.perform(get(GET_CONTRACTS_EXTRACT_CONTENT_REQUEST, uuid)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }
}