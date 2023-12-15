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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class DbImageControllerTest {

    private static final String GET_IMAGE_BI_ID = "/insurance-service/public/v2/image/{id}";
    private static final String DELETE_IMAGE_BY_ID = "/insurance-service/v2/image/{id}";
    private static final String POST_UPLOAD_IMAGE = "/insurance-service/v2/image";

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    private static final Resource INPUT_STREAM = new ClassPathResource("emptyImage.jpg");

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
     * Тест сервиса POST /insurance-service/v2/image
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void uploadImageSuccess() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(POST_UPLOAD_IMAGE)
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(3)))
        ;

        mockMvc.perform(get(GET_IMAGE_BI_ID, 3))
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isOk())
        ;

    }

    /**
     * Тест сервиса POST /insurance-service/v2/image
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void uploadImageForbidden() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(POST_UPLOAD_IMAGE)
                .file(firstFile))
                .andExpect(status().isForbidden())
        ;

    }

    /**
     * Тест сервиса DELETE /insurance-service/v2/image/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteImageSuccess() throws Exception {

        mockMvc.perform(delete(DELETE_IMAGE_BY_ID, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
        ;
    }

    /**
     * Тест сервиса DELETE /insurance-service/v2/image/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteImageForbidden() throws Exception {

        mockMvc.perform(delete(DELETE_IMAGE_BY_ID, 2))
                .andExpect(status().isForbidden())
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/image/{id}
     * для проверки получения корректного ответа. проверка прав не проводится
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getImageSuccess() throws Exception {

        mockMvc.perform(get(GET_IMAGE_BI_ID, 2))
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v2/image/{id}
     * для проверки получения корректного ответа. проверка прав не проводится
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getImageNotFound() throws Exception {

        mockMvc.perform(get(GET_IMAGE_BI_ID, 2))
                .andExpect(status().isNotFound())
        ;
    }
}
