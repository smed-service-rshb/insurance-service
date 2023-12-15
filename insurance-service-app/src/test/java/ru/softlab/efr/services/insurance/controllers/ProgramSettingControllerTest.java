package ru.softlab.efr.services.insurance.controllers;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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
import ru.softlab.efr.common.utilities.rest.RestPageImpl;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.insurance.config.HibernateCacheInvalidationRule;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.utils.TestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class ProgramSettingControllerTest {

    private static final String PUT_PROGRAMSETTINGSETTINGS_REQUEST = "/insurance-service/v1/dict/program-settings";
    private static final String GET_PROGRAMSETTING_BY_ID_REQUEST = "/insurance-service/v1/dict/program-settings/%s";
    private static final String CREATE_PROGRAMSETTING_REQUEST = "/insurance-service/v1/dict/program-settings";
    private static final String UPDATE_PROGRAMSETTING_REQUEST = "/insurance-service/v1/dict/program-settings/%s";
    private static final String DELETE_PROGRAMSETTING_REQUEST = "/insurance-service/v1/dict/program-settings/%s";
    private static final String FIND_PROGRAMSETTING_REQUEST = "/insurance-service/v1/dict/find/program-settings";

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

    private ParameterizedTypeReference<RestPageImpl<ProgramSettingDataForList>> restPage() {
        return new ParameterizedTypeReference<RestPageImpl<ProgramSettingDataForList>>() {
        };
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/program-settings
     * для проверки получения корректного ответа для запроса без фильтра
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllProgramSettingNoFilerSuccess() throws Exception {
        FilterProgramSettingsRq filterData = new FilterProgramSettingsRq();
        filterData.setKind(ProgramKind.NSJ);
        filterData.setStrategyId(1L);
        mockMvc.perform(put(PUT_PROGRAMSETTINGSETTINGS_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(filterData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[?(@.id==6)]", hasSize(0))) //id 6 = Program.relatedEmployeeGroupType = EXCLUDE_ALL
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].policyCode", is("Я01")));
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/program-settings
     * для проверки получения корректного ответа для запроса с примененным фильтром
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllProgramSettingFilterSuccess() throws Exception {
        FilterProgramSettingsRq filterData = new FilterProgramSettingsRq();
        filterData.setKind(ProgramKind.NSJ);
        filterData.setStrategyId(1L);
        mockMvc.perform(put(PUT_PROGRAMSETTINGSETTINGS_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(filterData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(0)));
    }


    /**
     * Тест сервиса PUT /insurance-service/v1/dict/program-settings
     * для проверки получения корректного ответа для запроса с примененным фильтром имеющим все null поля
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllProgramSettingFilterNullSuccess() throws Exception {
        FilterProgramSettingsRq filterData = new FilterProgramSettingsRq();
        mockMvc.perform(put(PUT_PROGRAMSETTINGSETTINGS_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .param("hasFilter", "true")
                .content(TestUtils.convertObjectToJson(filterData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(11)));
    }

    /**
     * Тест сервиса  PUT /insurance-service/v1/dict/program-settings
     * для проверки получения корректного ответа без фильтрации и с задаными параметрмаи страницы
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllProgramSettingSuccessCheckRestrictedGroup() throws Exception {
        mockMvc.perform(put(PUT_PROGRAMSETTINGSETTINGS_REQUEST)
                .param("hasFilter", "false")
                .content(TestUtils.convertObjectToJson(restPage()))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_3))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[?(@.id==7)]", hasSize(0)));
    }

    /**
     * Тест сервиса GET /insurance-service/v1/dict/program-settings/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getProgramSettingByIdSuccess() throws Exception {

        mockMvc.perform(get(String.format(GET_PROGRAMSETTING_BY_ID_REQUEST, "2"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/program-settings
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createProgramSettingSuccess() throws Exception {

        ProgramSettingData programSettingData = new ProgramSettingData();
        programSettingData.setProgramId(1L);
        Risk riskSetting = new Risk();
        riskSetting.setId(1L);
        riskSetting.setName("SOME STRANGE");
        riskSetting.setCalculationCoefficient(BigDecimal.ONE);
        riskSetting.setRiskAmount(BigDecimal.TEN);
        riskSetting.setCalculationType(RiskCalculationType.FIXED);
        riskSetting.setRiskReturnRate(BigDecimal.ZERO);
        riskSetting.setCalculationCoefficientPremium(BigDecimal.ZERO);
        riskSetting.setMaxRiskAmount(BigDecimal.TEN);
        riskSetting.setMinRiskAmount(BigDecimal.TEN);
        riskSetting.setSignAmount(true);
        riskSetting.setRiskPremium(BigDecimal.ONE);
        riskSetting.setOtherRiskParam("TEST");
        riskSetting.setRulesDetails("TEST");
        riskSetting.setRiskDependence(3L);

        programSettingData.setRisks(Collections.singletonList(riskSetting));

        Risk additionalRiskSetting = new Risk();
        additionalRiskSetting.setId(2L);
        additionalRiskSetting.setName("SOME STRANGE2");
        additionalRiskSetting.setCalculationCoefficient(BigDecimal.ONE);
        additionalRiskSetting.setRiskAmount(BigDecimal.TEN);
        additionalRiskSetting.setCalculationType(RiskCalculationType.FIXED);
        additionalRiskSetting.setRiskReturnRate(BigDecimal.ZERO);
        additionalRiskSetting.setCalculationCoefficientPremium(BigDecimal.ZERO);
        additionalRiskSetting.setMaxRiskAmount(BigDecimal.TEN);
        additionalRiskSetting.setMinRiskAmount(BigDecimal.TEN);
        additionalRiskSetting.setSignAmount(true);
        additionalRiskSetting.setRiskPremium(BigDecimal.ONE);
        additionalRiskSetting.setOtherRiskParam("TEST");
        additionalRiskSetting.setRulesDetails("TEST");
        additionalRiskSetting.setRiskDependence(3L);

        programSettingData.setOptionalRisks(Collections.singletonList(additionalRiskSetting));

        programSettingData.setProgramKind(ProgramKind.ISJ);

        RiskDocument riskDocument = new RiskDocument();
        riskDocument.setId(1L);
        riskDocument.setName("RISK DOCUMENT");
        riskDocument.setState("RIST DOCUMENT STATE");
        programSettingData.setDocuments(Collections.singletonList(riskDocument));

        programSettingData.setUnderwriting(UnderwritingKind.APPLICATION);

        Strategy strategy = new Strategy();
        strategy.setId(1L);
        strategy.setName("SUPER STRATEGY");
        programSettingData.setStrategy(Collections.singletonList(strategy));
        programSettingData.setGuaranteeLevel(new BigDecimal(11.5));
        RequiredField field = new RequiredField();
        field.setName("REQUIRED FIELD1");
        field.setStrId("REQUIRED_FIELD1");
        programSettingData.setRequiredFields(Collections.singletonList(field));

        programSettingData.setCalendarUnit(CalendarUnit.DAY);
        programSettingData.setCoefficient(BigDecimal.ONE);
        programSettingData.setCurrency(1L);
        programSettingData.setStartDate(LocalDate.now());
        programSettingData.setEndDate(LocalDate.now());
        programSettingData.setGender(Gender.FEMALE);
        programSettingData.setMaxAgeHolder(100);
        programSettingData.setMinAgeHolder(10);
        programSettingData.setMaxAgeInsured(100);
        programSettingData.setMinAgeInsured(10);
        programSettingData.setMaxGrowth(100);
        programSettingData.setMinGrowth(100);
        programSettingData.setMaxLowerPressure(100);
        programSettingData.setMinLowerPressure(10);
        programSettingData.setMinUpperPressure(100);
        programSettingData.setMaxUpperPressure(100);
        programSettingData.setMaximumTerm(10);
        programSettingData.setMinimumTerm(10);
        programSettingData.setMaxPremium(BigDecimal.TEN);
        programSettingData.setMaxSum(BigDecimal.TEN);
        programSettingData.setMinSum(BigDecimal.ONE);
        programSettingData.setMinWeight(100);
        programSettingData.setMaxWeight(100);
        programSettingData.setPeriodicity(PaymentPeriodicity.MONTHLY);
        programSettingData.setPolicyholderInsured(true);

        mockMvc.perform(post(CREATE_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programSettingData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", notNullValue()));
    }


    /**
     * Тест сервиса PUT /insurance-service/v1/dict/program-settings/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateProgramSettingSuccess() throws Exception {

        mockMvc.perform(get(String.format(GET_PROGRAMSETTING_BY_ID_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.guaranteeLevel", is(75.50)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks[0].calculationCoefficient", is(0.55)));

        ProgramSettingData programSettingData = new ProgramSettingData();
        programSettingData.setId(1L);
        programSettingData.setProgramId(1L);
        Risk riskSetting = new Risk();
        riskSetting.setId(6L);
        riskSetting.setName("SOME STRANGE");
        riskSetting.setCalculationCoefficient(BigDecimal.ONE);
        riskSetting.setRiskAmount(BigDecimal.TEN);
        riskSetting.setCalculationType(RiskCalculationType.FIXED);
        riskSetting.setRiskReturnRate(BigDecimal.ZERO);
        riskSetting.setCalculationCoefficientPremium(BigDecimal.ZERO);
        riskSetting.setMaxRiskAmount(BigDecimal.TEN);
        riskSetting.setMinRiskAmount(BigDecimal.TEN);
        riskSetting.setSignAmount(true);
        riskSetting.setRiskPremium(BigDecimal.ONE);
        riskSetting.setOtherRiskParam("TEST");
        riskSetting.setRulesDetails("TEST");
        riskSetting.setRiskDependence(2L);
        riskSetting.setSortPriority(new BigDecimal(4));

        programSettingData.setRisks(Collections.singletonList(riskSetting));

        programSettingData.setProgramKind(ProgramKind.ISJ);

        RiskDocument riskDocument = new RiskDocument();
        riskDocument.setId(1L);
        riskDocument.setName("RISK DOCUMENT");
        riskDocument.setState("RISk DOCUMENT STATE");
        programSettingData.setDocuments(Collections.singletonList(riskDocument));

        programSettingData.setUnderwriting(UnderwritingKind.APPLICATION);

        Strategy strategy = new Strategy();
        strategy.setId(1L);
        strategy.setName("SUPER STRATEGY");
        programSettingData.setStrategy(Collections.singletonList(strategy));

        RequiredField field = new RequiredField();
        field.setName("REQUIRED FIELD1");
        field.setStrId("REQUIRED_FIELD1");
        programSettingData.setRequiredFields(Collections.singletonList(field));

        programSettingData.setCalendarUnit(CalendarUnit.DAY);
        programSettingData.setCoefficient(BigDecimal.ONE);
        programSettingData.setCurrency(1L);
        programSettingData.setStartDate(LocalDate.now());
        programSettingData.setEndDate(LocalDate.now());
        programSettingData.setGender(Gender.FEMALE);
        programSettingData.setMaxAgeHolder(100);
        programSettingData.setMinAgeHolder(10);
        programSettingData.setMaxAgeInsured(100);
        programSettingData.setMinAgeInsured(10);
        programSettingData.setMaxGrowth(100);
        programSettingData.setMinGrowth(100);
        programSettingData.setMaxLowerPressure(100);
        programSettingData.setMinLowerPressure(10);
        programSettingData.setMinUpperPressure(100);
        programSettingData.setMaxUpperPressure(100);
        programSettingData.setMaximumTerm(10);
        programSettingData.setMinimumTerm(10);
        programSettingData.setMaxPremium(BigDecimal.TEN);
        programSettingData.setMaxSum(BigDecimal.TEN);
        programSettingData.setMinSum(BigDecimal.ONE);
        programSettingData.setMinWeight(100);
        programSettingData.setMaxWeight(100);
        programSettingData.setPeriodicity(PaymentPeriodicity.MONTHLY);
        programSettingData.setPolicyholderInsured(true);

        mockMvc.perform(put(String.format(UPDATE_PROGRAMSETTING_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programSettingData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_PROGRAMSETTING_BY_ID_REQUEST, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks[0].sortPriority", is(4.00)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.risks[0].otherRiskParam", is("TEST")));
    }

    /**
     * Тест сервиса PUT /insurance-service/v1/dict/program-settings/%s
     * для получения ошибки по причине некорректности запроса.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateProgramSettingBadRequest() throws Exception {

        ProgramData programData = new ProgramData();
        programData.setId(1L);
        programData.setName("SOMETHING");

        mockMvc.perform(put(UPDATE_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(programData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }


    /**
     * Тест сервиса DELETE /insurance-service/v1/dict/program-settings/%s
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteProgramSettingSuccess() throws Exception {

        mockMvc.perform(delete(String.format(DELETE_PROGRAMSETTING_REQUEST, "2"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(PRODUCT_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingWithoutProgramBySumSuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(BigDecimal.ONE);
        request.setCurrency(1L);
        request.setTerm(50);
        request.setCalendarUnit(CalendarUnit.YEAR);
        //request.setProgramId(1L);
        request.setInsuredBirthDate(LocalDate.from(LocalDate.parse(LocalDate.now().getYear() - 19 + "-01-01")));
        request.setPolicyHolderBirthDate(LocalDate.from(LocalDate.parse(LocalDate.now().getYear() - 52 + "-01-01")));

        request.setType(FindProgramType.SUM);
        request.setProgramKind(ProgramKind.ISJ);

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programName", is("Классический ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].currency", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].sum", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].policyholderInsured", is(true)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingWithoutProgramByPremiumSuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(new BigDecimal(101));
        request.setCurrency(1L);
        request.setTerm(50);
        request.setCalendarUnit(CalendarUnit.YEAR);

        request.setInsuredBirthDate(LocalDate.from(LocalDate.parse(LocalDate.now().getYear() - 19 + "-01-01")));
        request.setPolicyHolderBirthDate(LocalDate.from(LocalDate.parse(LocalDate.now().getYear() - 52 + "-01-01")));
        //request.setProgramId(1L);

        request.setType(FindProgramType.PREMIUM);
        request.setProgramKind(ProgramKind.ISJ);

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programName", is("Классический ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].currency", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].premium", is(101)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[2].risks", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[2].risks[0].riskAmount", is(5555.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[2].risks[1].riskAmount", is(232.3)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingWithProgramSuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(BigDecimal.ONE);
        request.setCurrency(1L);
        request.setTerm(50);
        request.setCalendarUnit(CalendarUnit.YEAR);
        request.setPremium(BigDecimal.TEN);
        request.setProgram(1L);

        request.setType(FindProgramType.SUM);
        request.setProgramKind(ProgramKind.ISJ);

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programName", is("Классический ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].currency", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[1].programName", is("Классический ИСЖ")));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения ошибки по причине некорректности запроса.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingWithoutArgProgramBadRequest() throws Exception {


        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(123))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingWithIncorrectArgProgramSuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(BigDecimal.ONE);
        request.setCurrency(3L); //THIS IS INCORRECT ARG (3L IS NOT FOUND)
        request.setTerm(50);
        request.setCalendarUnit(CalendarUnit.DAY);
        request.setPremium(BigDecimal.TEN);
        request.setType(FindProgramType.SUM);
        request.setProgramKind(ProgramKind.ISJ);


        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(0)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа. при указании периодичности
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingWithPeriodicitySuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(new BigDecimal(101));
        request.setCurrency(2L);
        request.setCalendarUnit(CalendarUnit.YEAR);
        request.setTerm(5);
        request.setPeriodicity(PaymentPeriodicity.MONTHLY);
        request.setType(FindProgramType.PREMIUM);
        request.setProgramKind(ProgramKind.ISJ);

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programName", is("Классический ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].currency", is(2)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа. При указании периодичности отсутствуют подходящие программы.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingWithPeriodicityNotFoundSuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(new BigDecimal(101));
        request.setCurrency(2L);
        request.setCalendarUnit(CalendarUnit.YEAR);
        request.setTerm(5);
        request.setPeriodicity(PaymentPeriodicity.TWICE_A_YEAR);
        request.setType(FindProgramType.PREMIUM);
        request.setProgramKind(ProgramKind.ISJ);

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(0)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа. При подборе по премии, проверка вхождения суммы премии.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingByPremiumSuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(BigDecimal.ONE);
        request.setCurrency(2L);
        request.setCalendarUnit(CalendarUnit.YEAR);
        request.setTerm(5);
        request.setPeriodicity(PaymentPeriodicity.MONTHLY);
        request.setType(FindProgramType.PREMIUM);
        request.setProgramKind(ProgramKind.ISJ);

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(0)));

        request.setAmount(new BigDecimal(300));

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(0)));

        request.setAmount(new BigDecimal(150));

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(1)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа. При подборе по премии и стратегии, проверка вхождения суммы премии.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingISJByStrategySuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(BigDecimal.ONE);
        request.setCurrency(1L);
        request.setCalendarUnit(CalendarUnit.YEAR);
        request.setTerm(5);
        request.setPeriodicity(PaymentPeriodicity.ONCE);
        request.setType(FindProgramType.PREMIUM);
        request.setProgramKind(ProgramKind.ISJ);
        request.setStrategy(1L);

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(0)));

        request.setAmount(new BigDecimal(300));

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(0)));

        request.setAmount(new BigDecimal(150));

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(2)));

        request.setStrategy(2L);
        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programName", is("Классический ИСЖ")));
    }


    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа.
     * Выполняется проверка, что в итоговый список параметров программ страхования не попадают параметры, связанные
     * с программами не доступными в текущем (указанном при авторизации) подразделении пользователя.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingBySumSuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(BigDecimal.ONE);
        request.setCurrency(1L);
        request.setCalendarUnit(CalendarUnit.YEAR);
        request.setTerm(5);
        request.setPeriodicity(PaymentPeriodicity.ONCE);
        request.setType(FindProgramType.SUM);
        request.setProgramKind(ProgramKind.NSJ);

        // Данному фильтру удовлетворяют 2 набора параметров программ страхования.
        // Один набор связан с программой "НСЖ 2018", для которой установлена фильтрация EXCLUDE для подразделений 2310, 0001.
        // Второй набор связан с программой "Упрощённый НСЖ", для которой установлена фильтрация INCLUDE для подразделений 2310, 0001.

        // Пользователь при авторизации выбрал подразделение 2310. Список должен содержать только
        // набор параметров, связанных с программой "Упрощённый НСЖ".
        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programName", is("Упрощённый НСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programKind", is("NSJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programNumber", is("003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].policyCode", is("Ы99")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].option", is("01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].coolingPeriod", is(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programSettingId", is(7)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].premiumMethod", is("BY_FORMULA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].periodicity", is("ONCE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].coefficient", is(2.1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].premium", is(0.01)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].sum", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].minimumTerm", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].maximumTerm", is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].calendarUnit", is("YEAR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].currency", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].risks", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].optionalRisks", hasSize(0)));

        // Пользователь при авторизации выбрал подразделение 2310. Список должен содержать только
        // набор параметров, связанных с программой "НСЖ 2018".
        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_2))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programName", is("НСЖ 2018")));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа.
     * Выполняется проверка, что в итоговый список параметров программ страхования не попадают параметры, связанные
     * с программами не доступными для групп пользователей, к которым относится текущий пользователь (пользователь
     * USER_PRINCIPAL_DATA_3 связан с группой "АЛЬФА Страхование").
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeNotFoundBecauseRestrictedGroup() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(BigDecimal.ONE);
        request.setCurrency(1L);
        request.setCalendarUnit(CalendarUnit.YEAR);
        request.setTerm(5);
        request.setPeriodicity(PaymentPeriodicity.ONCE);
        request.setType(FindProgramType.SUM);
        request.setProgramKind(ProgramKind.NSJ);

        // Пользователь при авторизации выбрал подразделение 2310. Этому ограничению удовлетворяет программп "НСЖ 2018".
        // Однако, программа "НСЖ 2018" недоступна пользователям из групп "АЛЬФА Страхование", поэтому итоговый список
        // должен быть пустым.
        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA_3))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(0)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа.
     * Выполняется проверка ограничения максимальной суммы риска
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingWithRiskMaxSum() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(new BigDecimal(50));
        request.setCurrency(1L);
        request.setCalendarUnit(CalendarUnit.YEAR);
        request.setTerm(5);
        request.setPeriodicity(PaymentPeriodicity.ONCE);
        request.setType(FindProgramType.SUM);
        request.setProgramKind(ProgramKind.NSJ);

        // Данному фильтру удовлетворяют 2 набора параметров программ страхования.
        // Один набор связан с программой "НСЖ 2018", для которой установлена фильтрация EXCLUDE для подразделений 2310, 0001.
        // Второй набор связан с программой "Упрощённый НСЖ", для которой установлена фильтрация INCLUDE для подразделений 2310, 0001.

        // Пользователь при авторизации выбрал подразделение 2310. Список должен содержать только
        // набор параметров, связанных с программой "Упрощённый НСЖ".
        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programName", is("Упрощённый НСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programKind", is("NSJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programNumber", is("003")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].policyCode", is("Ы99")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].option", is("01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].coolingPeriod", is(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programSettingId", is(7)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].premiumMethod", is("BY_FORMULA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].periodicity", is("ONCE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].coefficient", is(2.1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].premium", is(0.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].sum", is(50)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].minimumTerm", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].maximumTerm", is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].calendarUnit", is("YEAR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].currency", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].risks", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].risks[0].riskAmount", is(10.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].optionalRisks", hasSize(0)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения сообщения о некорректности запроса из-за отсутствия указания обязательного поля.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingByPremiumBadRequest() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(BigDecimal.ONE);
        request.setCurrency(2L);
        request.setPeriodicity(PaymentPeriodicity.MONTHLY);
        request.setType(FindProgramType.PREMIUM);
        request.setProgramKind(ProgramKind.ISJ);

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа.
     * Подбор КСП без указания суммы и срока
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingWithoutSumAndTermSuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setCurrency(1L);
        request.setCalendarUnit(CalendarUnit.YEAR);
        request.setInsuredBirthDate(LocalDate.from(LocalDate.parse(LocalDate.now().getYear() - 19 + "-01-01")));
        request.setType(FindProgramType.SUM);
        request.setProgramKind(ProgramKind.KSP);

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programName", is("Коробка")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].sum", is(50000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].premium", is(50000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].currency", is(1)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки получения корректного ответа.
     * При подборе ренты без указания срока
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingWithoutTermForRentSuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setCurrency(1L);
        request.setCalendarUnit(CalendarUnit.YEAR);
        request.setInsuredBirthDate(LocalDate.from(LocalDate.parse(LocalDate.now().getYear() - 19 + "-01-01")));
        request.setAmount(new BigDecimal(100));
        request.setType(FindProgramType.SUM);
        request.setProgramKind(ProgramKind.RENT);

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programName", is("Рента")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].currency", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].sum", is(100)));
    }

    /**
     * Тест сервиса POST /insurance-service/v1/dict/find/program-settings
     * для проверки подбора программ на указанную дату.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findProgramSettingByPastDateSuccess() throws Exception {

        FindProgramSettingRq request = new FindProgramSettingRq();
        request.setAmount(BigDecimal.ONE);
        request.setCurrency(1L);
        request.setTerm(50);
        request.setCalendarUnit(CalendarUnit.YEAR);
        request.setProgramDate(LocalDate.of(2000, 2, 1));
        request.setInsuredBirthDate(LocalDate.from(LocalDate.parse(LocalDate.now().getYear() - 19 + "-01-01")));
        request.setPolicyHolderBirthDate(LocalDate.from(LocalDate.parse(LocalDate.now().getYear() - 52 + "-01-01")));

        request.setType(FindProgramType.SUM);
        request.setProgramKind(ProgramKind.ISJ);

        mockMvc.perform(post(FIND_PROGRAMSETTING_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].programName", is("Классический ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].currency", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].sum", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programs[0].policyholderInsured", is(true)));
    }

}
