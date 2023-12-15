package ru.softlab.efr.services.insurance.controllers;

import org.junit.Before;
import org.junit.Ignore;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.Strategy;
import ru.softlab.efr.services.insurance.model.enums.StrategyType;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.services.StrategyService;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;
import static ru.softlab.efr.services.insurance.stubs.TestData.CLIENT_PRINCIPAL_DATA;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class InvestmentControllerTest {

    private static final String GET_SHARE_REQUEST = "/insurance-service/v1/investment/shares?contractId={contractId}";
    private static final String LOAD_SHARE_REQUEST = "/insurance-service/v1/investment/share/load";
    private static final String GET_INCOME_REQUEST = "/insurance-service/v1/investment/incomes?contractId={contractId}";
    private static final String LOAD_BASE_INDEX_REQUEST = "/insurance-service/v1/investment/baseIndex/load";
    private static final String GET_SHARE_REPORT_REQUEST = "/insurance-service/v1/investment/shares/report?contractId={contractId}";
    private static final String GET_INCOME_REPORT_REQUEST = "/insurance-service/v1/investment/incomes/report?contractId={contractId}&strategyId={strategyId}";

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

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private StrategyService strategyService;

    /**
     * Тест сервиса GET /insurance-service/v1/investment/share/load
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getLoadShareSuccess() throws Exception {
        // у договора с id 3 установлена стратегия с id 2 Цифровое будущее type = COUPON
        // дата начала действия договора равна 2018-12-04 список акций должен начинаться с этой даты или с ближайшего большего значения
        // даты должны идти в порядке возростания
        // до обновления справочников cо стратегией договора связано 2 акции (FUTURE1 - 4 котировки, FUTURE2 - 1 котировка)
        mockMvc.perform(get(GET_SHARE_REQUEST, 3L)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].name", is("FUTURE1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].description").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[1].name", is("FUTURE2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[1].description").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[1].quotes", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[0].date", is("18.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[1].date", is("10.02.2019")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[2].date", is("11.02.2019")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[3].date", is("12.02.2019")))
        ;

        /*
         * В файле предоставлены данные с 01.12.2018 по 19.12.2018 по 13 записей для каждой акции
         * При репликации загружаем список акций для стратегий с наименованием Глобальные облигации (3 акции GLOBAL1, GLOBAL2, GENERAL) c 04.12.2018 по 19.12.2018 - 12 записей
         * для стратегий с наименованием Цифровое будущее (3 акции FUTURE1, FUTURE2, GENERAL) c 04.12.2018 по 19.12.2018 - 12 записей
         * FUTURE1 пересекается с существующей в бд 1ой записью
         * GENERAL присутствует на двух разных вкладках файла репликации
         */
        mockMvc.perform(get(LOAD_SHARE_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_SHARE_REQUEST, 3L)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].name", is("FUTURE1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].description", is("DESCRIPTION 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes", hasSize(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[1].name", is("FUTURE3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[1].description", is("DESCRIPTION 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[1].quotes", hasSize(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[2].name", is("GENERAL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[2].description", is("DESCRIPTION 3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[2].quotes", hasSize(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[3].name", is("FUTURE2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[3].description").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[3].quotes", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[0].date", is("04.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[0].value", is(49.91)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[0].relativeValue", is(0.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[1].date", is("05.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[1].value", is(50.42)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[1].relativeValue", is(1.02)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[11].date", is("19.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shares[0].quotes[14].date", is("12.02.2019")))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/investment/share/report
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getShareReportSuccess() throws Exception {
        mockMvc.perform(get(GET_SHARE_REPORT_REQUEST, 3L)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/investment/share/report
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getShareReportForbidden() throws Exception {
        mockMvc.perform(get(GET_SHARE_REPORT_REQUEST, 3L))
                .andExpect(status().isForbidden())
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/investment/income/report
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getIncomeReportSuccess() throws Exception {
        mockMvc.perform(get(GET_INCOME_REPORT_REQUEST, 2L, 1L)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/investment/income/report
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getIncomeReportForbidden() throws Exception {
        mockMvc.perform(get(GET_INCOME_REPORT_REQUEST, 2L, 1L))
                .andExpect(status().isForbidden())
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/investment/share
     * для проверки получения ответа, когда для договора со стратегией типа CLASSIC запрашиваются данные по акциям.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getGetShareBadRequest() throws Exception {
        mockMvc.perform(get(GET_SHARE_REQUEST, 2L)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса GET /insurance-service/v1/investment/income
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getIncomeSuccess() throws Exception {

        // у договора с id 2 установлена стратегия с id 1 Глобальные облигации type = CLASSIC c двумя наборами настроек
        // первый набор для получения базовых индексов ссылается на стратегию Глобальные облигации
        // второй набор для получения базовых индексов ссылается на стратегию Европейские лидеры
        // дата начала действия договора равна 2018-11-27 список динамики дохода должен начинаться с этой даты или с ближайшего значения
        // даты должны идти в порядке возростания
        // до обновления справочников в бд присутствуют только 5 записей с индексами для стратегии Глобальные облигации
        mockMvc.perform(get(GET_INCOME_REQUEST, 2L)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].incomes").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].strategy", is("Европейские лидеры")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].ticker", is("тикер 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].strategy", is("Глобальные облигации")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].ticker", is("тикер 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].date", is("17.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].value", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].baseIndex", is(1368.16)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].relativeValue", is(0.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].date", is("18.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].value", is(2.5355)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].baseIndex", is(1399.7)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].relativeValue", is(2.305)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[2].date", is("27.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[2].value", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[2].baseIndex", is(1367.61)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[2].relativeValue", is(-0.04)))
        ;

        //при репликации заполняются данные для Глобальных облигаций с 01.11.2018 по 18.12.2018 (с 27.11.2018 15 записей 2 пересекаются с существующими)
        //для Европейских лидеров с 02.11.2018 по 30.11.2018 (с 27.11.2018 4 записи)
        mockMvc.perform(get(LOAD_BASE_INDEX_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());


        mockMvc.perform(get(GET_INCOME_REQUEST, 2L)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].incomes").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].incomes", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes", hasSize(18)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].date", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].value", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].baseIndex", is(1532.18)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].relativeValue", is(0.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].date", is("28.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].value", is(0.0917)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].baseIndex", is(1546.23)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].relativeValue", is(0.917)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[2].date", is("29.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[17].date", is("29.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].incomes[0].date", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].incomes[3].date", is("30.11.2018")))
        ;
    }

    /**
     * Тест сервиса GET /insurance-service/v1/investment/income
     * для проверки получения ответа, когда для страховки со стратегией COUPON запрашиваются данные по инвестиционному доходу.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getIncomeBadRequest() throws Exception {
        mockMvc.perform(get(GET_INCOME_REQUEST, 3L)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }


    /**
     * Тест сервиса GET /insurance-service/v1/investment/income
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getIncomeLocomotiveSuccess() throws Exception {

        Insurance insurance = insuranceService.findById(2L);
        Strategy strategy = strategyService.findById(1L);
        strategy.setStrategyType(StrategyType.LOCOMOTIVE);
        strategyService.save(strategy);
        insurance.setStrategy(strategy);
        insuranceService.update(insurance);

        // у договора с id 2 установлена стратегия с id 1 Глобальные облигации type = LOCOMOTIVE c двумя наборами настроек
        // первый набор для получения базовых индексов ссылается на стратегию Глобальные облигации
        // второй набор для получения базовых индексов ссылается на стратегию Европейские лидеры
        // дата начала действия договора равна 2018-11-27 список динамики дохода должен начинаться с этой даты или с ближайшего значения
        // даты должны идти в порядке возростания
        // до обновления справочников в бд присутствуют только 5 записей с индексами для стратегии Глобальные облигации
        mockMvc.perform(get(GET_INCOME_REQUEST, 2L)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].incomes").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].currentIncome").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].currentIncome", is("10%")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].strategy", is("Европейские лидеры")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].ticker", is("тикер 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].strategy", is("Глобальные облигации")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].ticker", is("тикер 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].date", is("17.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].value").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].relativeValue", is(0.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].date", is("18.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].value").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].relativeValue", is(2.305)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[2].date", is("27.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[2].value").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[2].relativeValue", is(-0.04)))
        ;

        //при репликации заполняются данные для Глобальных облигаций с 01.11.2018 по 18.12.2018 (с 27.11.2018 15 записей 2 пересекаются с существующими)
        //для Европейских лидеров с 02.11.2018 по 30.11.2018 (с 27.11.2018 4 записи)
        mockMvc.perform(get(LOAD_BASE_INDEX_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());


        mockMvc.perform(get(GET_INCOME_REQUEST, 2L)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].currentIncome", is("0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].incomes").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].currentIncome", is("509.00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].incomes", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes", hasSize(18)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].date", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].value").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[0].relativeValue", is(0.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].date", is("28.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].value").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[1].relativeValue", is(0.917)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[2].date", is("29.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[0].incomes[17].date", is("29.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].incomes[0].date", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incomesSet[1].incomes[3].date", is("30.11.2018")))
        ;
    }

}