package ru.softlab.efr.services.insurance.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.authorization.PrincipalDataStore;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.converter.InsuranceConverter;
import ru.softlab.efr.services.insurance.listener.UserRevisionListener;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.*;
import ru.softlab.efr.services.insurance.model.rest.PhoneType;
import ru.softlab.efr.services.insurance.repositories.*;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.services.RiskService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static ru.softlab.efr.services.insurance.stubs.TestData.INSURANCE_MODEL_3;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
@Transactional
public class AuditedEntityTest {

    private static final Long AUDIT_TEST_USER_ID = 1L;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InsuranceConverter insuranceConverter;

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private RiskService riskService;

    @Autowired
    private RiskRepository riskRepository;

    @Autowired
    private ProgramSettingRepository programSettingRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private PrincipalDataStore principalDataStore;

    @Autowired
    private UserRevisionListener userRevisionListener;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Данный метод перед каждым выполнением теста из данного класса будет подкладывать в текущий контекст информацию
     * о тестовом пользователе.
     * Кроме этого в UserRevisionListener будет передаваться ссылка на текущий Spring-контекст.
     * Необходимость этих действий обусловленна особенностью передачи в экземпляры класса UserRevisionListener
     * ссылки на Spring-контекст. Экземпляры UserRevisionListener создаются Hibernate Envers минуя Spring,
     * поэтому ссылка на Spring-контекст хранится в статическом поле, значение которого доступно из каждого экземпляра
     * UserRevisionListener.
     *
     * При инициализации Spring-контекста создаётся бин UserRevisionListener (фактически он нигде не используется), в
     * метод setApplicationContext которого передаётся ссылка на создаваемый Spring-контекст.
     * При выполнении тестов Spring-контекстов создаётся несколько (по мере запуска тестов из разных классов).
     * Соответственно, какой контекст создаётся последним, то значение и будет установлено в UserRevisionListener.
     *
     * Если несколько классов с тестами имеют @ContextConfiguration, в котором указан класс с одной и той же
     * конфигурацией, то контекст создаётся один раз (а следовательно и вызывается
     * UserRevisionListener.setApplicationContext) и переиспользуется для всех классов с тестами.
     * Поэтому в UserRevisionListener может оказаться не тот Spring-контекст, в рамках которого выполняется тест.
     * Такая ситуация может произойти, если сначала запускаются тесты с контекстом A, потом B, а затем снова с
     * контекстом A. Вот для обхода данной проблемы и используется данный метод.
     */
    @Before
    public void setActualApplicationContext() {
        PrincipalDataImpl principalData = new PrincipalDataImpl();
        principalData.setId(AUDIT_TEST_USER_ID);
        principalDataStore.setPrincipalData(principalData);
        userRevisionListener.setApplicationContext(applicationContext);
    }

    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveAuditedClient() {
        ClientEntity client = new ClientEntity();
        client.setSurName("Иванов");
        client.setFirstName("firstName1");
        client.setMiddleName("Иванович");
        client.setGender(GenderTypeEnum.MALE);
        client.setEmail("ivanov@example.org");
        client.setBirthDate(LocalDate.parse("1986-03-30", DateTimeFormatter.ISO_DATE));
        DocumentForClientEntity document = new DocumentForClientEntity();
        document.setDocSeries("1111");
        document.setDocNumber("123456");
        document.setActive(Boolean.TRUE);
        document.setMain(Boolean.TRUE);
        document.setDocType(IdentityDocTypeEnum.PASSPORT_RF);
        document.setClient(client);
        client.getDocuments().add(document);

        client = clientRepository.save(client);
        Long clientId = client.getId();
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        Revision<Integer, ClientEntity> last = clientRepository.findLastChangeRevision(client.getId());
        TestTransaction.end();
        assertNotNull(last);
        Integer revNumber = last.getRevisionNumber();

        //Изменяем существующий документ
        client.getDocuments().get(0).setDocSeries("2222");
        //Изменяем свойство клиента
        client.setFirstName("firstName2");
        //Добавляем документ
        DocumentForClientEntity document2 = new DocumentForClientEntity();
        document2.setDocSeries("8888");
        document2.setDocNumber("999999");
        document2.setActive(Boolean.TRUE);
        document2.setMain(Boolean.FALSE);
        document2.setDocType(IdentityDocTypeEnum.PASSPORT_RF);
        document2.setClient(client);
        client.getDocuments().add(document2);

        TestTransaction.start();
        client = clientRepository.save(client);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //System.out.println("---get last rev info---");
        TestTransaction.start();
        last = clientRepository.findLastChangeRevision(client.getId());
        assertEquals(revNumber + 1, last.getRevisionNumber().intValue());
        assertEquals("firstName2", last.getEntity().getFirstName());
        assertEquals(2, last.getEntity().getDocuments().size());
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        //System.out.println("---get first rev info---");
        Revision<Integer, ClientEntity> first = clientRepository.findRevision(clientId, revNumber);
        assertEquals("firstName1", first.getEntity().getFirstName());
        assertEquals(1, first.getEntity().getDocuments().size());
        assertEquals("1111", first.getEntity().getDocuments().get(0).getDocSeries());
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAuditSingleClientProperty() {
        ClientEntity client = new ClientEntity();
        client.setSurName("Иванов");
        client.setFirstName("firstName1");
        client.setMiddleName("MiddleName1");
        client.setGender(GenderTypeEnum.MALE);
        client.setEmail("ivanov@example.org");
        client.setBirthDate(LocalDate.parse("1986-03-30", DateTimeFormatter.ISO_DATE));

        client = clientRepository.save(client);
        Long clientId = client.getId();
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        client = clientRepository.findOne(clientId);
        client.setFirstName("firstName2");
        client = clientRepository.save(client);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        Revisions<Integer, ClientEntity> revisions = clientRepository.findRevisions(clientId);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        assertEquals(2, revisions.getContent().size());
        assertEquals("firstName1", revisions.getContent().get(0).getEntity().getFirstName());
        assertEquals("firstName2", revisions.getContent().get(1).getEntity().getFirstName());
    }


    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAuditClientDocCollection() {
        ClientEntity client = new ClientEntity();
        client.setSurName("Иванов");
        client.setFirstName("firstName1");
        client.setMiddleName("Иванович");
        client.setGender(GenderTypeEnum.MALE);
        client.setEmail("ivanov@example.org");
        client.setBirthDate(LocalDate.parse("1986-03-30", DateTimeFormatter.ISO_DATE));
        DocumentForClientEntity document = new DocumentForClientEntity();
        document.setDocSeries("1111");
        document.setDocNumber("123456");
        document.setActive(Boolean.TRUE);
        document.setMain(Boolean.TRUE);
        document.setDocType(IdentityDocTypeEnum.PASSPORT_RF);
        document.setClient(client);
        client.getDocuments().add(document);

        client = clientRepository.save(client);
        Long clientId = client.getId();
        TestTransaction.flagForCommit();
        TestTransaction.end();

        Revision<Integer, ClientEntity> revision = clientRepository.findLastChangeRevision(client.getId());
        Integer firstRevisionNumber = revision.getRevisionNumber();

        //Изменяем существующий документ
        client.getDocuments().get(0).setDocSeries("2222");
        //Добавляем документ
        DocumentForClientEntity document2 = new DocumentForClientEntity();
        document2.setDocSeries("8888");
        document2.setDocNumber("999999");
        document2.setActive(Boolean.TRUE);
        document2.setMain(Boolean.FALSE);
        document2.setDocType(IdentityDocTypeEnum.PASSPORT_RF);
        document2.setClient(client);
        client.getDocuments().add(document2);

        TestTransaction.start();
        client = clientRepository.save(client);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //System.out.println("---get last rev info---");
        TestTransaction.start();
        Revision<Integer, ClientEntity> last = clientRepository.findLastChangeRevision(client.getId());
        assertEquals(firstRevisionNumber + 1, last.getRevisionNumber().intValue());
        assertEquals(2, last.getEntity().getDocuments().size());
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        //System.out.println("---get first rev info---");
        Revision<Integer, ClientEntity> first = clientRepository.findRevision(clientId, firstRevisionNumber);
        assertEquals(1, first.getEntity().getDocuments().size());
        assertEquals("1111", first.getEntity().getDocuments().get(0).getDocSeries());
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAuditClientAddressCollection() {
        ClientEntity client = new ClientEntity();
        client.setSurName("Иванов");
        client.setFirstName("firstName1");
        client.setMiddleName("Иванович");
        client.setGender(GenderTypeEnum.MALE);
        client.setEmail("ivanov@example.org");
        client.setBirthDate(LocalDate.parse("1986-03-30", DateTimeFormatter.ISO_DATE));
        AddressForClientEntity address = new AddressForClientEntity();
        address.setAddressType(AddressTypeEnum.RESIDENCE);
        address.setArea("area1");
        address.setClient(client);
        client.getAddresses().add(address);

        client = clientRepository.save(client);
        Long clientId = client.getId();
        TestTransaction.flagForCommit();
        TestTransaction.end();

        Revision<Integer, ClientEntity> revision = clientRepository.findLastChangeRevision(client.getId());
        Integer firstRevisionNumber = revision.getRevisionNumber();

        //Изменяем существующий адрес
        client.getAddresses().get(0).setArea("area2");
        //Добавляем адрес
        AddressForClientEntity address2 = new AddressForClientEntity();
        address2.setAddressType(AddressTypeEnum.REGISTRATION);
        address2.setArea("area3");
        address2.setClient(client);
        client.getAddresses().add(address2);

        TestTransaction.start();
        client = clientRepository.save(client);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        Revision<Integer, ClientEntity> last = clientRepository.findLastChangeRevision(client.getId());
        assertEquals(firstRevisionNumber + 1, last.getRevisionNumber().intValue());
        assertEquals(2, last.getEntity().getAddresses().size());
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        Revision<Integer, ClientEntity> first = clientRepository.findRevision(clientId, firstRevisionNumber);
        assertEquals(1, first.getEntity().getAddresses().size());
        assertEquals("area1", first.getEntity().getAddresses().get(0).getArea());
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAuditClientPhoneCollection() {
        ClientEntity client = new ClientEntity();
        client.setSurName("Иванов");
        client.setFirstName("firstName1");
        client.setMiddleName("Иванович");
        client.setGender(GenderTypeEnum.MALE);
        client.setEmail("ivanov@example.org");
        client.setBirthDate(LocalDate.parse("1986-03-30", DateTimeFormatter.ISO_DATE));
        PhoneForClaimEntity phone = new PhoneForClaimEntity();
        phone.setMain(true);
        phone.setPhoneType(PhoneType.MOBILE);
        phone.setNumber("1");
        phone.setClient(client);
        client.getPhones().add(phone);

        client = clientRepository.save(client);
        Long clientId = client.getId();
        TestTransaction.flagForCommit();
        TestTransaction.end();

        Revision<Integer, ClientEntity> revision = clientRepository.findLastChangeRevision(client.getId());
        Integer firstRevisionNumber = revision.getRevisionNumber();

        //Изменяем существующий телефон
        client.getPhones().get(0).setNumber("2");
        //Добавляем телефон
        PhoneForClaimEntity phone2 = new PhoneForClaimEntity();
        phone2.setMain(false);
        phone2.setPhoneType(PhoneType.HOME);
        phone2.setNumber("3");
        phone2.setClient(client);
        client.getPhones().add(phone2);

        TestTransaction.start();
        client = clientRepository.save(client);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        Revision<Integer, ClientEntity> last = clientRepository.findLastChangeRevision(client.getId());
        assertEquals(firstRevisionNumber + 1, last.getRevisionNumber().intValue());
        assertEquals(2, last.getEntity().getPhones().size());
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        Revision<Integer, ClientEntity> first = clientRepository.findRevision(clientId, firstRevisionNumber);
        assertEquals(1, first.getEntity().getPhones().size());
        assertEquals("1", first.getEntity().getPhones().get(0).getNumber());
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createContractSuccessProgramSettingWithHolderData() {
        Insurance insurance = new Insurance();
        insuranceConverter.updateInsuranceFromBaseModel(INSURANCE_MODEL_3, insurance, null, null, null, null);
        String firstName1 = insurance.getHolder().getFirstName();
        Long strategyId1 = insurance.getStrategy().getId();
        int recipientListSize1 = insurance.getRecipientList().size();
        insurance.setId(null);
        insurance = insuranceRepository.save(insurance);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        Revision<Integer, Insurance> revision = insuranceRepository.findLastChangeRevision(insurance.getId());
        Integer firstRevisionNumber = revision.getRevisionNumber();

        //Изменяем статус
        insurance.setStrategy(strategyRepository.findOne(2L));
        //Изменяем стратегию
        insurance.setStatus(statusRepository.findOne(3L));
        //Изменяем настройки параметра программы
        Long psId2 = 3L;
        insurance.setProgramSetting(programSettingRepository.findOne(psId2));
        //Изменяем фамилию застрахованного
        insurance.getHolder().setFirstName("Тестовый");
        //Удаляем выгодоприобретателя
        insurance.getRecipientList().remove(0);
        //Сохраняем
        TestTransaction.start();
        insurance = insuranceRepository.save(insurance);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //Находим данные первой и второй версии
        TestTransaction.start();
        Revision<Integer, Insurance> first = insuranceService.findRevision(insurance.getId(), firstRevisionNumber);
        Revision<Integer, Insurance> second = insuranceService.findRevision(insurance.getId(), firstRevisionNumber + 1);
        //asserts for first
        assertEquals(strategyId1, first.getEntity().getStrategy().getId());
        assertEquals(firstName1, first.getEntity().getHolder().getFirstName());
        //Настройки параметров программы не историчны
        assertEquals(psId2, first.getEntity().getProgramSetting().getId());
        assertEquals(recipientListSize1, first.getEntity().getRecipientList().size());
        //asserts for second
        assertEquals(3L, second.getEntity().getStatus().getId().longValue());
        assertEquals(2L, second.getEntity().getStrategy().getId().longValue());
        assertEquals("Тестовый", second.getEntity().getHolder().getFirstName());
        assertEquals(psId2, second.getEntity().getProgramSetting().getId());
        assertEquals(recipientListSize1 - 1, second.getEntity().getRecipientList().size());

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }


    @Test
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveFirstRiskVersion() {
        Risk risk = new Risk();
        risk.setBenefitsInsured(false);
        risk.setProgramKind(ProgramKind.KSP);
        risk.setEndDate(LocalDate.now().plusDays(100));
        risk.setFullName("test");
        risk.setName("test");
        risk.setPaymentMethod(PaymentMethod.ONCE);
        risk.setStartDate(LocalDate.now());
        risk.setDeleted(false);
        Risk savedRisk = riskService.create(risk);
        assertNotNull(savedRisk.getId());
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        Revision<Integer, Risk> lastChangeRevision = riskRepository.findLastChangeRevision(savedRisk.getId());
        Integer firstRevisionNumber = lastChangeRevision.getRevisionNumber();
        assertNotNull(lastChangeRevision);
        //assertEquals(1, lastChangeRevision.getRevisionNumber().intValue());
        Long userId = ((AuditEnversInfo) lastChangeRevision.getMetadata().getDelegate()).getUserId();
        assertEquals(AUDIT_TEST_USER_ID, userId);
        savedRisk.setFullName("test2");
        savedRisk = riskService.create(savedRisk);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        lastChangeRevision = riskRepository.findLastChangeRevision(savedRisk.getId());
        assertEquals(firstRevisionNumber + 1, lastChangeRevision.getRevisionNumber().intValue());
        Revision<Integer, Risk> firstRevision = riskRepository.findRevision(savedRisk.getId(), firstRevisionNumber);
        assertEquals("test", firstRevision.getEntity().getFullName());
        TestTransaction.end();
    }
}
