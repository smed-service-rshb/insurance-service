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
import ru.softlab.efr.services.insurance.service.ExtractServiceLock;
import ru.softlab.efr.services.insurance.utils.OperationLogServiceStatistics;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.insurance.stubs.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;
import static ru.softlab.efr.services.insurance.stubs.TestData.USER_PRINCIPAL_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class UploadChangeControllerTest {

    private static final String POST_UPLOAD_REPORT_REQUEST = "/insurance-service/v2/contract/import";
    private static final String GET_CONTRACT_BY_ID_REQUEST = "/insurance-service/v2/contracts/{id}";

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();
    private static final Resource INPUT_STREAM = new ClassPathResource("univeralExtractIsj.xlsx");
    @Rule
    @Autowired
    public HibernateCacheInvalidationRule hibernateCacheInvalidationRule;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private OperationLogServiceStatistics operationLogServiceStatistics;

    @Autowired
    private ExtractServiceLock extractServiceLock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    /**
     * Тест сервиса POST /insurance-service/v2/contracts
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postCreateContractSuccess() throws Exception {
        operationLogServiceStatistics.reset();

        mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contractNumber", is("23К010110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("DRAFT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programNumber", is("002")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programSettingId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.surName", is("Иванов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.firstName", is("Иван")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.middleName", is("Иванович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.birthDate", is("30.03.1986")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.birthPlace", is("СССР")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.birthCountry").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.citizenship", is("russian")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.taxResidence", is("russian")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.snils", is("12345678910")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.taxPayerNumber", is("1234567891234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].docType", is("PASSPORT_RF")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].docSeries", is("1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].docNumber", is("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].issuedBy").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].issuedDate").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].isActive", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].isMain", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].divisionCode").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.email", is("ivanov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones[0].number", is("79110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones[0].type", is("mobile")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones[0].main", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].addressType", is("residence")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].country", is("RUSSIA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].region", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].area", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].city", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].locality",  nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].street", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].house", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].construction", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].housing", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].apartment", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].index", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].fullAddress", notNullValue(String.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.publicOfficialStatus", is("none")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.foreignPublicOfficialType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.russianPublicOfficialType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.relations", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.publicOfficialPosition", is("Директор")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.businessRelations",  nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.activitiesGoal", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.businessRelationsGoal", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.businessReputation",nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.financialStability", is("STABLE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.financesSource", is("Бизнес")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderEqualsInsured", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calendarUnit", is("YEAR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.periodicity", is("ONCE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", is(10000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rurAmount", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.premium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rurPremium", is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.guaranteeLevel", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientEqualsHolder", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.riskInfoList", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addRiskInfoList", hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyId", is(1)));


        MockMultipartFile multipartFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(POST_UPLOAD_REPORT_REQUEST)
                .file(multipartFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden())
        ;
        mockMvc.perform(MockMvcRequestBuilders.fileUpload(POST_UPLOAD_REPORT_REQUEST)
                .file(multipartFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").isNotEmpty())
        ;

        mockMvc.perform(get(GET_CONTRACT_BY_ID_REQUEST, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind", is("ISJ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contractNumber", is("23К010110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("PROJECT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programNumber", is("002")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programName", is("Купонный ИСЖ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate", is("12.12.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is("27.11.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programSettingId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.surName", is("Фамилия")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.firstName", is("Имя")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.middleName", is("Отчество")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.birthDate", is("02.02.1984")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.gender", is("MALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.birthPlace", is("Населенный пункт")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.birthCountry", is("Российская Федерация")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.citizenship", is("russian")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.taxResidence", is("russian")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.snils", is("22222222222")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.taxPayerNumber", is("222222222222")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].docType", is("PASSPORT_RF")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].docSeries", is("3333")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].docNumber", is("333333")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].issuedBy", is("ПаспортВыдан")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].issuedDate", is("01.01.2018")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].isActive", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].isMain", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.documents[0].divisionCode", is("333333")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.email", is("holder@test.ru")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones[0].number", is("72222222222")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones[0].type", is("mobile")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.phones[0].main", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].addressType", is("residence")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].country", is("RUSSIA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].region", is("г Москва")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].area", is("РайонРегистрации")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].city", is("ГородРегистрации")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].locality", is("НаселенныйПунктРегистрации")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].street", is("УлицаРегистрации")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].house", is("2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].construction", is("2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].housing", is("2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].apartment", is("2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.addresses[0].index", is("222222")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.publicOfficialStatus", is("none")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.foreignPublicOfficialType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.russianPublicOfficialType", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.relations", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.publicOfficialPosition", is("Директор")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.businessRelations",  is("Долгосрочные")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.activitiesGoal", is("Страхование жизни")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.businessRelationsGoal", is("Страхование жизни")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.riskLevelDesc", is("Низкий. Нет критериев для присвоения иного уровня риска")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.businessReputation", is("Устойчивая")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.financialStability", is("Устойчивое")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.holderData.financesSource", is("Личные накопления")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuredId", is(1)))

                .andExpect(MockMvcResultMatchers.jsonPath("$.holderEqualsInsured", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calendarUnit", is("YEAR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.periodicity", is("ONCE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", is(5000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rurAmount", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.premium", is(2000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rurPremium", is(2000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.guaranteeLevel", nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientEqualsHolder", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.riskInfoList", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[0].surName", is("ВыгодоприобритательПервый")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[0].firstName", is("ВыгодоприобритательПервый")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[0].middleName", is("ВыгодоприобритательПервый")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[0].birthDate", is("02.02.1980")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[0].birthCountry", is("RUSSIA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[0].birthPlace", is("ВыгодоприобритательПервый")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[0].taxResidence", is("russian")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[0].share", is(20.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[1].surName", is("ВыгодоприобритательВторой")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[1].firstName", is("ВыгодоприобритательВторой")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[1].middleName", is("ВыгодоприобритательВторой")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[1].birthDate", is("01.01.2000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[1].birthCountry", is("RUSSIA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[1].birthPlace", is("ВыгодоприобритательВторой")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[1].taxResidence", is("foreign")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipientList[1].share", is(30.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addRiskInfoList", hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.strategyId", is(1)));
    }

}
