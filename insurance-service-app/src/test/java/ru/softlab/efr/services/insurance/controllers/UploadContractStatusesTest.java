package ru.softlab.efr.services.insurance.controllers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.pojo.InsuranceParseResult;
import ru.softlab.efr.services.insurance.service.UploadXLSContractStatusesService;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class UploadContractStatusesTest {

    private static final Resource INPUT_STREAM = new ClassPathResource("contract-statuses.xlsx");
    @Rule
    @Autowired
    public HibernateCacheInvalidationRule hibernateCacheInvalidationRule;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UploadXLSContractStatusesService uploadXLSContractStatusesService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    @Test
    @Sql(value = {"classpath:test-script.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeSuccessLoadContractStatusesService() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        InsuranceParseResult parseResult = uploadXLSContractStatusesService.loadContractStatusesFromXLSX(
                multipartFile.getBytes(), multipartFile.getOriginalFilename());

        assertTrue(parseResult.getErrors().stream()
                .map(AbstractMap.SimpleEntry::getValue)
                .collect(Collectors.toList()).containsAll(Arrays.asList(
                        "Значение в поле 'Статус' должно быть заполнено",
                        "Значение в поле 'Уникальный код клиента' должно быть заполнено",
                        "Не найдено договора 'null' в системе",
                        "Значение 'ХАХАХА' в поле 'Статус' должно соответствовать любому из списка: [Оформлен, Черновик, Расторгнут, Оплачен, Выгружен в CRM]",
                        "Договор не найден - дальнейшая работа над данной записью невозможна.",
                        "Не найдено договора '652345' в системе",
                        "Договор не найден - дальнейшая работа над данной записью невозможна.",
                        "Не найдено договора 'ячмсячмс' в системе",
                        "Значение 'Котировка' в поле 'Статус' должно соответствовать любому из списка: [Оформлен, Черновик, Расторгнут, Оплачен, Выгружен в CRM]",
                        "Договор не найден - дальнейшая работа над данной записью невозможна."
                        )));
    }
}
