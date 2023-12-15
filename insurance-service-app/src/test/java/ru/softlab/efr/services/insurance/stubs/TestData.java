package ru.softlab.efr.services.insurance.stubs;

import org.springframework.beans.BeanUtils;
import ru.softlab.efr.common.dict.exchange.model.Currency;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.GenderTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.IdentityDocTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class TestData {

    public static final PrincipalDataImpl CHIEF_ADMIN_PRINCIPAL_DATA;
    public static final PrincipalDataImpl USER_PRINCIPAL_DATA;
    public static final PrincipalDataImpl USER_PRINCIPAL_DATA_2;
    public static final PrincipalDataImpl USER_PRINCIPAL_DATA_3;
    public static final PrincipalDataImpl USER_PRINCIPAL_DATA_4;
    public static final PrincipalDataImpl USER_PRINCIPAL_DATA_5;
    public static final PrincipalDataImpl USER_PRINCIPAL_DATA_6;
    public static final PrincipalDataImpl VSP_HEAD_PRINCIPAL_DATA;
    public static final PrincipalDataImpl PRODUCT_ADMIN_PRINCIPAL_DATA;
    public static final PrincipalDataImpl DEP_HEAD_PRINCIPAL_DATA;
    public static final PrincipalDataImpl COACH_PRINCIPAL_DATA;
    public static final PrincipalDataImpl CALL_CENTER_PRINCIPAL_DATA;
    public static final PrincipalDataImpl CASHIER_PRINCIPAL_DATA;
    public static final PrincipalDataImpl CLIENT_PRINCIPAL_DATA;
    public static final PrincipalDataImpl CLIENT_PRINCIPAL_DATA_2;

    public static final Program PROGRAM_1;
    public static final ProgramSetting PROGRAM_SETTING_1;
    public static final Long HOLDER_ID = 1L;
    public static final Currency RUB_CURRENCY;
    public static final Client CLIENT_1;

    public static final BaseInsuranceModel INSURANCE_MODEL_1 = new BaseInsuranceModel();
    public static final BaseInsuranceModel INSURANCE_MODEL_2 = new BaseInsuranceModel();
    public static final BaseInsuranceModel INSURANCE_MODEL_3 = new BaseInsuranceModel();
    public static final SetStatusInsuranceModel CHANGE_STATUS_INSURANCE_MODEL_1 = new SetStatusInsuranceModel();
    public static final SetStatusInsuranceModel CHANGE_STATUS_REVOKED_REPLACEMENT_MODEL = new SetStatusInsuranceModel();

    public static final ClientEntity CLIENT_ENTITY_1;

    static {
        // Главный администратор
        CHIEF_ADMIN_PRINCIPAL_DATA = new PrincipalDataImpl();
        CHIEF_ADMIN_PRINCIPAL_DATA.setId(1L);
        CHIEF_ADMIN_PRINCIPAL_DATA.setFirstName("Иван");
        CHIEF_ADMIN_PRINCIPAL_DATA.setMiddleName("Иванович");
        CHIEF_ADMIN_PRINCIPAL_DATA.setSecondName("Иванов");
        CHIEF_ADMIN_PRINCIPAL_DATA.setRights(Arrays.asList(
                Right.EDIT_ROLES, Right.VIEW_ROLES, Right.SET_ADMIN_ROLE,
                Right.EDIT_USERS, Right.EDIT_PRODUCT_SETTINGS, Right.EDIT_UNDERWRITING_COEFFICIENTS,
                Right.EDIT_UNDERWRITING_SUMS, Right.VIEW_CLIENT, Right.VIEW_CLIENT_CONTRACTS,
                Right.CREATE_CONTRACT, Right.DELETE_CONTRACT, Right.EDIT_CONTRACT,
                Right.VIEW_CONTRACT_LIST_ALL, Right.VIEW_CONTRACT_LIST_RF_VSP, Right.VIEW_CONTRACT_LIST_VSP,
                Right.VIEW_CONTRACT_LIST_OWNER, Right.VIEW_CONTRACT_REPORT_ALL, Right.VIEW_CONTRACT_REPORT_RF_VSP,
                Right.VIEW_CONTRACT_REPORT_VSP, Right.VIEW_CONTRACT_REPORT_OWNER, Right.VIEW_CONTRACT_REQUIRED_UNDERWRITING,
                Right.CHANGE_STATE_CONTRACT_REQUIRED_UNDERWRITING, Right.EDIT_CONTRACT_REQUIRED_UNDERWRITING,
                Right.UPDATE_FULL_SET_DOCUMENT, Right.MANUAL_CHECK_CLIENT, Right.CHANGE_REPORT, Right.NON_RESIDENT_REPORT,
                Right.UPLOAD_INSURANCE_REPORT, Right.EDIT_CLIENT_REPORT_TOPICS, Right.CLIENT_REQUEST_VIEW, Right.CLIENT_REQUEST_PROCESSING,
                Right.STATEMENT_CONTROL, Right.SET_INDIVIDUAL_CURRENCY_RATE, Right.EDIT_CLIENT_TEMPLATES)
        );
        CHIEF_ADMIN_PRINCIPAL_DATA.setBranch("ДРБ");
        CHIEF_ADMIN_PRINCIPAL_DATA.setOffice("0001");
        CHIEF_ADMIN_PRINCIPAL_DATA.setOffices(Collections.singletonList("0001"));
        CHIEF_ADMIN_PRINCIPAL_DATA.setOfficeId(2L);
        CHIEF_ADMIN_PRINCIPAL_DATA.setPersonnelNumber("1-001");
        CHIEF_ADMIN_PRINCIPAL_DATA.setOffices(Collections.singletonList("0001"));
        CHIEF_ADMIN_PRINCIPAL_DATA.setGroups(Arrays.asList("РСХБ Страхование"));

        //Клиент
        CLIENT_PRINCIPAL_DATA = new PrincipalDataImpl();
        CLIENT_PRINCIPAL_DATA.setId(9L);
        CLIENT_PRINCIPAL_DATA.setFirstName("Иван");
        CLIENT_PRINCIPAL_DATA.setMiddleName("Петрович");
        CLIENT_PRINCIPAL_DATA.setSecondName("Иванов");
        CLIENT_PRINCIPAL_DATA.setMobilePhone("+79999999999");
        CLIENT_PRINCIPAL_DATA.setRights(Arrays.asList(Right.CLIENT_VIEW_CONTRACT, Right.CLIENT_VIEW_CONTRACTS_LIST,
                Right.CLIENT_REQUEST_VIEW, Right.CLIENT_REQUEST_CREATE, Right.VIEW_CLIENT_TEMPLATES_LIST));

        CLIENT_PRINCIPAL_DATA_2 = new PrincipalDataImpl();
        CLIENT_PRINCIPAL_DATA_2.setId(8888L);
        CLIENT_PRINCIPAL_DATA_2.setFirstName("Тыштымбек");
        CLIENT_PRINCIPAL_DATA_2.setMiddleName("Тыштымбекович");
        CLIENT_PRINCIPAL_DATA_2.setSecondName("Джанибекян");
        CLIENT_PRINCIPAL_DATA_2.setMobilePhone("+79999999998");
        CLIENT_PRINCIPAL_DATA_2.setRights(Arrays.asList(Right.CLIENT_VIEW_CONTRACT, Right.CLIENT_VIEW_CONTRACTS_LIST,
                Right.CLIENT_REQUEST_VIEW, Right.CLIENT_REQUEST_CREATE));
        // Пользователь
        USER_PRINCIPAL_DATA = new PrincipalDataImpl();
        USER_PRINCIPAL_DATA.setId(9L);
        USER_PRINCIPAL_DATA.setFirstName("Фёдор");
        USER_PRINCIPAL_DATA.setMiddleName("Фёдорович");
        USER_PRINCIPAL_DATA.setSecondName("Фёдоров");
        USER_PRINCIPAL_DATA.setMobilePhone("+375292149785");
        USER_PRINCIPAL_DATA.setRights(Arrays.asList(Right.VIEW_CLIENT, Right.VIEW_CLIENT_CONTRACTS, Right.CREATE_CONTRACT,
                Right.EDIT_CONTRACT, Right.VIEW_CONTRACT_LIST_OWNER, Right.VIEW_CONTRACT_REPORT_OWNER, Right.DELETE_CONTRACT));
        USER_PRINCIPAL_DATA.setBranch("Амурский РФ");
        USER_PRINCIPAL_DATA.setOffice("2310");
        USER_PRINCIPAL_DATA.setOffices(Collections.singletonList("2310"));
        USER_PRINCIPAL_DATA.setPersonnelNumber("10009");
        USER_PRINCIPAL_DATA.setOfficeId(2L);
        USER_PRINCIPAL_DATA.setGroups(Arrays.asList("РСХБ Страхование", "АЛЬФА Страхование"));

        USER_PRINCIPAL_DATA_2 = new PrincipalDataImpl();
        USER_PRINCIPAL_DATA_2.setId(15L);
        USER_PRINCIPAL_DATA_2.setFirstName("Виктор");
        USER_PRINCIPAL_DATA_2.setMiddleName("Викторович");
        USER_PRINCIPAL_DATA_2.setSecondName("Викторов");
        USER_PRINCIPAL_DATA_2.setRights(Arrays.asList(Right.VIEW_CLIENT, Right.VIEW_CLIENT_CONTRACTS, Right.CREATE_CONTRACT,
                Right.EDIT_CONTRACT, Right.VIEW_CONTRACT_LIST_OWNER, Right.VIEW_CONTRACT_REPORT_OWNER, Right.DELETE_CONTRACT));
        USER_PRINCIPAL_DATA_2.setBranch("Амурский РФ");
        USER_PRINCIPAL_DATA_2.setOffice("2311");
        USER_PRINCIPAL_DATA_2.setOffices(Arrays.asList("2310", "2311"));
        USER_PRINCIPAL_DATA_2.setPersonnelNumber("10009");
        USER_PRINCIPAL_DATA_2.setOfficeId(3L);

        USER_PRINCIPAL_DATA_3 = new PrincipalDataImpl();
        USER_PRINCIPAL_DATA_3.setId(15L);
        USER_PRINCIPAL_DATA_3.setFirstName("Виктор");
        USER_PRINCIPAL_DATA_3.setMiddleName("Викторович");
        USER_PRINCIPAL_DATA_3.setSecondName("Викторов");
        USER_PRINCIPAL_DATA_3.setRights(Arrays.asList(Right.VIEW_CLIENT, Right.VIEW_CLIENT_CONTRACTS, Right.CREATE_CONTRACT,
                Right.EDIT_CONTRACT, Right.VIEW_CONTRACT_LIST_OWNER, Right.VIEW_CONTRACT_REPORT_OWNER, Right.DELETE_CONTRACT));
        USER_PRINCIPAL_DATA_3.setBranch("Амурский РФ");
        USER_PRINCIPAL_DATA_3.setOffice("2311");
        USER_PRINCIPAL_DATA_3.setOffices(Arrays.asList("2310", "2311"));
        USER_PRINCIPAL_DATA_3.setPersonnelNumber("10009");
        USER_PRINCIPAL_DATA_3.setOfficeId(3L);
        USER_PRINCIPAL_DATA_3.setGroups(Arrays.asList("АЛЬФА Страхование"));


        USER_PRINCIPAL_DATA_4 = new PrincipalDataImpl();
        USER_PRINCIPAL_DATA_4.setId(45L);
        USER_PRINCIPAL_DATA_4.setFirstName("Фёдор");
        USER_PRINCIPAL_DATA_4.setMiddleName("Фёдорович");
        USER_PRINCIPAL_DATA_4.setSecondName("Фёдоров");
        USER_PRINCIPAL_DATA_4.setRights(Arrays.asList(Right.VIEW_CLIENT, Right.VIEW_CONTRACT_LIST_OWNER, Right.CREATE_CONTRACT,
                Right.EDIT_CONTRACT, Right.VIEW_CONTRACT_REPORT_OWNER));
        USER_PRINCIPAL_DATA_4.setBranch("Амурский РФ");
        USER_PRINCIPAL_DATA_4.setOffice("1111");
        USER_PRINCIPAL_DATA_4.setOffices(Collections.singletonList("1111"));
        USER_PRINCIPAL_DATA_4.setPersonnelNumber("10011");
        USER_PRINCIPAL_DATA_4.setOfficeId(55L);

        USER_PRINCIPAL_DATA_5 = new PrincipalDataImpl();
        USER_PRINCIPAL_DATA_5.setId(46L);
        USER_PRINCIPAL_DATA_5.setFirstName("Фёдор");
        USER_PRINCIPAL_DATA_5.setMiddleName("Фёдорович");
        USER_PRINCIPAL_DATA_5.setSecondName("Фёдоров");
        USER_PRINCIPAL_DATA_5.setRights(Arrays.asList(Right.VIEW_CONTRACT_LIST_ALL));
        USER_PRINCIPAL_DATA_5.setBranch("Амурский РФ");
        USER_PRINCIPAL_DATA_5.setOffice("1111");
        USER_PRINCIPAL_DATA_5.setOffices(Collections.singletonList("1111"));
        USER_PRINCIPAL_DATA_5.setPersonnelNumber("10011");
        USER_PRINCIPAL_DATA_5.setOfficeId(55L);

        USER_PRINCIPAL_DATA_6 = new PrincipalDataImpl();
        USER_PRINCIPAL_DATA_6.setId(47L);
        USER_PRINCIPAL_DATA_6.setFirstName("Фёдор");
        USER_PRINCIPAL_DATA_6.setMiddleName("Фёдорович");
        USER_PRINCIPAL_DATA_6.setSecondName("Фёдоров");
        USER_PRINCIPAL_DATA_6.setRights(Arrays.asList(Right.VIEW_CONTRACT_LIST_RF_VSP));
        USER_PRINCIPAL_DATA_6.setBranch("Амурский РФ");
        USER_PRINCIPAL_DATA_6.setOffice("1111");
        USER_PRINCIPAL_DATA_6.setOffices(Collections.singletonList("1111"));
        USER_PRINCIPAL_DATA_6.setPersonnelNumber("10011");
        USER_PRINCIPAL_DATA_6.setOfficeId(55L);
        //USER_PRINCIPAL_DATA_4.setGroups(Arrays.asList("РСХБ Страхование", "АЛЬФА Страхование"));

        // Руководитель ВСП
        VSP_HEAD_PRINCIPAL_DATA = new PrincipalDataImpl();
        VSP_HEAD_PRINCIPAL_DATA.setId(8L);
        VSP_HEAD_PRINCIPAL_DATA.setFirstName("Антон");
        VSP_HEAD_PRINCIPAL_DATA.setMiddleName("Антонович");
        VSP_HEAD_PRINCIPAL_DATA.setSecondName("Антонов");
        VSP_HEAD_PRINCIPAL_DATA.setRights(Arrays.asList(Right.VIEW_CLIENT, Right.VIEW_CLIENT_CONTRACTS, Right.CREATE_CONTRACT,
                Right.EDIT_CONTRACT, Right.VIEW_CONTRACT_LIST_VSP, Right.VIEW_CONTRACT_LIST_OWNER, Right.VIEW_CONTRACT_REPORT_OWNER,
                Right.DELETE_CONTRACT));
        VSP_HEAD_PRINCIPAL_DATA.setBranch("Амурский РФ");
        VSP_HEAD_PRINCIPAL_DATA.setOffice("2310");
        VSP_HEAD_PRINCIPAL_DATA.setOffices(Collections.singletonList("2310"));
        VSP_HEAD_PRINCIPAL_DATA.setOfficeId(2L);
        VSP_HEAD_PRINCIPAL_DATA.setPersonnelNumber("10008");

        // Продуктовый администратор
        PRODUCT_ADMIN_PRINCIPAL_DATA = new PrincipalDataImpl();
        PRODUCT_ADMIN_PRINCIPAL_DATA.setId(3L);
        PRODUCT_ADMIN_PRINCIPAL_DATA.setFirstName("Сидор");
        PRODUCT_ADMIN_PRINCIPAL_DATA.setMiddleName("Сидорович");
        PRODUCT_ADMIN_PRINCIPAL_DATA.setSecondName("Сидоров");
        PRODUCT_ADMIN_PRINCIPAL_DATA.setRights(Arrays.asList(Right.EDIT_PRODUCT_SETTINGS,
                Right.EDIT_UNDERWRITING_COEFFICIENTS, Right.EDIT_UNDERWRITING_SUMS));
        PRODUCT_ADMIN_PRINCIPAL_DATA.setBranch("Амурский РФ");
        PRODUCT_ADMIN_PRINCIPAL_DATA.setOffice("2310");
        PRODUCT_ADMIN_PRINCIPAL_DATA.setOffices(Collections.singletonList("2310"));
        PRODUCT_ADMIN_PRINCIPAL_DATA.setOfficeId(2L);
        PRODUCT_ADMIN_PRINCIPAL_DATA.setPersonnelNumber("10003");

        // Начальник отдела розничных продаж
        DEP_HEAD_PRINCIPAL_DATA = new PrincipalDataImpl();
        DEP_HEAD_PRINCIPAL_DATA.setId(7L);
        DEP_HEAD_PRINCIPAL_DATA.setFirstName("Николай");
        DEP_HEAD_PRINCIPAL_DATA.setMiddleName("Николаевич");
        DEP_HEAD_PRINCIPAL_DATA.setSecondName("Николаев");
        DEP_HEAD_PRINCIPAL_DATA.setRights(Arrays.asList(
                Right.VIEW_CONTRACT_LIST_RF_VSP,
                Right.VIEW_CONTRACT_LIST_VSP,
                Right.VIEW_CONTRACT_LIST_OWNER,
                Right.VIEW_CONTRACT_REPORT_RF_VSP,
                Right.VIEW_CONTRACT_REPORT_VSP,
                Right.VIEW_CONTRACT_REPORT_OWNER));
        DEP_HEAD_PRINCIPAL_DATA.setBranch("Амурский РФ");
        DEP_HEAD_PRINCIPAL_DATA.setOffice("2310");
        DEP_HEAD_PRINCIPAL_DATA.setOffices(Collections.singletonList("2310"));
        DEP_HEAD_PRINCIPAL_DATA.setOfficeId(2L);
        DEP_HEAD_PRINCIPAL_DATA.setPersonnelNumber("10003");

        // Коуч
        COACH_PRINCIPAL_DATA = new PrincipalDataImpl();
        COACH_PRINCIPAL_DATA.setId(12L);
        COACH_PRINCIPAL_DATA.setFirstName("Степан");
        COACH_PRINCIPAL_DATA.setMiddleName("Степанович");
        COACH_PRINCIPAL_DATA.setSecondName("Степанов");
        COACH_PRINCIPAL_DATA.setRights(Arrays.asList(
                Right.VIEW_CONTRACT_LIST_RF_VSP,
                Right.VIEW_CONTRACT_LIST_VSP,
                Right.VIEW_CONTRACT_LIST_OWNER,
                Right.VIEW_CONTRACT_REPORT_RF_VSP,
                Right.VIEW_CONTRACT_REPORT_VSP,
                Right.VIEW_CONTRACT_REPORT_OWNER));
        COACH_PRINCIPAL_DATA.setBranch("Амурский РФ");
        COACH_PRINCIPAL_DATA.setOffice("2310");
        COACH_PRINCIPAL_DATA.setOffices(Collections.singletonList("2310"));
        COACH_PRINCIPAL_DATA.setOfficeId(2L);
        COACH_PRINCIPAL_DATA.setPersonnelNumber("10003");

        // Сотрудник кол.центра
        CALL_CENTER_PRINCIPAL_DATA = new PrincipalDataImpl();
        CALL_CENTER_PRINCIPAL_DATA.setId(10L);
        CALL_CENTER_PRINCIPAL_DATA.setFirstName("Николай");
        CALL_CENTER_PRINCIPAL_DATA.setMiddleName("Николаевич");
        CALL_CENTER_PRINCIPAL_DATA.setSecondName("Николаев");
        CALL_CENTER_PRINCIPAL_DATA.setRights(Arrays.asList(
                Right.VIEW_CLIENT,
                Right.VIEW_CLIENT_CONTRACTS,
                Right.CREATE_CONTRACT_IN_CALL_CENTER,
                Right.VIEW_CONTRACT_LIST_OWNER));
        CALL_CENTER_PRINCIPAL_DATA.setBranch("Амурский РФ");
        CALL_CENTER_PRINCIPAL_DATA.setOffice("2310");
        CALL_CENTER_PRINCIPAL_DATA.setOfficeId(2L);
        CALL_CENTER_PRINCIPAL_DATA.setPersonnelNumber("10019");
        CALL_CENTER_PRINCIPAL_DATA.setOfficeId(2L);

        // Сотрудник кассы
        CASHIER_PRINCIPAL_DATA = new PrincipalDataImpl();
        CASHIER_PRINCIPAL_DATA.setId(11L);
        CASHIER_PRINCIPAL_DATA.setFirstName("Николай");
        CASHIER_PRINCIPAL_DATA.setMiddleName("Николаевич");
        CASHIER_PRINCIPAL_DATA.setSecondName("Николаев");
        CASHIER_PRINCIPAL_DATA.setRights(Arrays.asList(
                Right.CREATE_CONTRACT,
                Right.VIEW_CLIENT,
                Right.VIEW_CLIENT_CONTRACTS,
                Right.VIEW_CONTRACT_EXECUTED_IN_CALL_CENTER,
                Right.EDIT_CONTRACT));
        CASHIER_PRINCIPAL_DATA.setBranch("Амурский РФ");
        CASHIER_PRINCIPAL_DATA.setOffice("2310");
        CASHIER_PRINCIPAL_DATA.setOfficeId(2L);
        CASHIER_PRINCIPAL_DATA.setPersonnelNumber("100106");


        PROGRAM_1 = new Program();
        PROGRAM_1.setId(1L);
        PROGRAM_1.setName("Выбери здоровье плюс");
        PROGRAM_1.setType(ProgramKind.ISJ);
        PROGRAM_1.setNumber("01");
        PROGRAM_1.setDeleted(false);

        PROGRAM_SETTING_1 = new ProgramSetting();
        PROGRAM_SETTING_1.setId(2L);
        PROGRAM_SETTING_1.setProgram(PROGRAM_1);
        PROGRAM_SETTING_1.setStartDate(new Timestamp(new Date().getTime()));

        RUB_CURRENCY = new Currency();
        RUB_CURRENCY.setId(1L);
        RUB_CURRENCY.setDigitalISO(643L);
        RUB_CURRENCY.setLiteralISO("RUB");
        RUB_CURRENCY.setCurrencyName("Рубль");
        RUB_CURRENCY.setFullCurrencyName("Рубль РФ");
        RUB_CURRENCY.setCountryHolder("Россия");

        // Тестовый клиент; возвращается ru.softlab.efr.services.insurance.stubs.ClientsDataClientStub.getClient
        CLIENT_1 = new Client();
        CLIENT_1.setId("1");
        CLIENT_1.setSurName("Иванов");
        CLIENT_1.setFirstName("Иван");
        CLIENT_1.setMiddleName("Иванович");
        CLIENT_1.setGender(Gender.MALE);
        CLIENT_1.setEmail("ivanov@example.org");
        CLIENT_1.setBirthDate(LocalDate.parse("1986-03-30", DateTimeFormatter.ISO_DATE));
        Document document = new Document();
        document.setDocSeries("1234");
        document.setDocNumber("123456");
        document.setIsMain(Boolean.TRUE);
        document.setIsActive(Boolean.TRUE);
        document.setDocType(DocumentType.PASSPORT_RF);
        CLIENT_1.setDocuments(Collections.singletonList(document));
        CLIENT_1.setPhones(Arrays.asList(
                new Phone("+791100000000", PhoneType.WORK, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, null),
                new Phone("79110000001", PhoneType.MOBILE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, null)
        ));

        // Тестовый договор страхования. Набор параметров страхования с id=1.
        INSURANCE_MODEL_1.setProgramSettingId(PROGRAM_SETTING_1.getId());
        INSURANCE_MODEL_1.setStartDate(LocalDate.now());
        INSURANCE_MODEL_1.setHolderId(1L);
        INSURANCE_MODEL_1.setInsuredId(1L);
        INSURANCE_MODEL_1.setHolderEqualsInsured(true);
        INSURANCE_MODEL_1.setCalendarUnit(CalendarUnit.YEAR);

        List<InsuranceRecipient> recipientList = new ArrayList<>();
        Stream.of(1, 2).forEach(i -> {
            InsuranceRecipient recipient = new InsuranceRecipient();
            recipient.setBirthDate(LocalDate.of(1980, i, i));
            recipient.setBirthPlace("Москва" + i);
            recipient.setFirstName("Иван" + i);
            recipient.setSurName("Иванов" + i);
            recipient.setMiddleName("Иванович" + i);
            recipient.setRelationship("Брат" + i);
            recipient.setTaxResidence(TaxResidenceType.RUSSIAN);
            recipient.setShare(new BigDecimal(50));
            recipient.setBirthCountry("Россия");
            recipientList.add(recipient);
        });
        INSURANCE_MODEL_1.setRecipientList(recipientList);

        INSURANCE_MODEL_1.setDuration(3);
        INSURANCE_MODEL_1.setGrowth(80);
        INSURANCE_MODEL_1.setGuaranteeLevel(new BigDecimal("3.3"));
        INSURANCE_MODEL_1.setAmount(new BigDecimal(200000.0));
        INSURANCE_MODEL_1.setPremium(new BigDecimal(20000.0));
        INSURANCE_MODEL_1.setRurAmount(new BigDecimal(200000.0));
        INSURANCE_MODEL_1.setRurPremium(new BigDecimal(20000.0));
        INSURANCE_MODEL_1.setCurrencyId(1L);
        INSURANCE_MODEL_1.setLowerPressure(60);
        INSURANCE_MODEL_1.setUpperPressure(160);
        INSURANCE_MODEL_1.setGuaranteeLevel(new BigDecimal(3));
        INSURANCE_MODEL_1.setRecipientEqualsHolder(false);
        INSURANCE_MODEL_1.setWeight(80);
        INSURANCE_MODEL_1.setPeriodicity(PaymentPeriodicity.ONCE);
        INSURANCE_MODEL_1.setType(FindProgramType.SUM);

        BeanUtils.copyProperties(INSURANCE_MODEL_1, CHANGE_STATUS_INSURANCE_MODEL_1);
        CHANGE_STATUS_INSURANCE_MODEL_1.setNewStatus(InsuranceStatusType.PROJECT);

        BeanUtils.copyProperties(INSURANCE_MODEL_1, CHANGE_STATUS_REVOKED_REPLACEMENT_MODEL);
        CHANGE_STATUS_REVOKED_REPLACEMENT_MODEL.setNewStatus(InsuranceStatusType.REVOKED_REPLACEMENT);

        // Тестовый договор страхования. Набор параметров страхования с id=2. Договор без рисков и выгодоприобретателей.
        INSURANCE_MODEL_2.setProgramSettingId(2L);
        INSURANCE_MODEL_2.setStartDate(LocalDate.now());
        INSURANCE_MODEL_2.setHolderId(1L);
        INSURANCE_MODEL_2.setInsuredId(1L);
        INSURANCE_MODEL_2.setHolderEqualsInsured(true);
        INSURANCE_MODEL_2.setCalendarUnit(CalendarUnit.YEAR);

        INSURANCE_MODEL_2.setRiskInfoList(null);
        INSURANCE_MODEL_2.setAddRiskInfoList(null);
        INSURANCE_MODEL_2.setRecipientList(null);

        INSURANCE_MODEL_2.setDuration(5);
        INSURANCE_MODEL_2.setGrowth(80);
        INSURANCE_MODEL_2.setGuaranteeLevel(new BigDecimal("3.3"));
        INSURANCE_MODEL_2.setAmount(new BigDecimal(200000.0));
        INSURANCE_MODEL_2.setPremium(new BigDecimal(20000.0));
        INSURANCE_MODEL_2.setRurAmount(new BigDecimal(200000.0));
        INSURANCE_MODEL_2.setRurPremium(new BigDecimal(20000.0));
        INSURANCE_MODEL_2.setCurrencyId(1L);
        INSURANCE_MODEL_2.setLowerPressure(60);
        INSURANCE_MODEL_2.setUpperPressure(160);
        INSURANCE_MODEL_2.setGuaranteeLevel(new BigDecimal(3));
        INSURANCE_MODEL_2.setRecipientEqualsHolder(false);
        INSURANCE_MODEL_2.setWeight(80);
        INSURANCE_MODEL_2.setPeriodicity(PaymentPeriodicity.MONTHLY);
        INSURANCE_MODEL_2.setType(FindProgramType.SUM);

        // Тестовый договор страхования. Набор параметров страхования с id=1. Договор с HolderData.
        INSURANCE_MODEL_3.setProgramSettingId(PROGRAM_SETTING_1.getId());
        INSURANCE_MODEL_3.setStartDate(LocalDate.now());
        INSURANCE_MODEL_3.setHolderId(1L);
        Client holderData = new Client();
        holderData.setId("1");
        holderData.setSurName("Лазарев");
        holderData.setFirstName("Виталий");
        holderData.setMiddleName("Батькович");
        holderData.setBirthDate(LocalDate.of(1989, 1, 1));
        holderData.setGender(Gender.MALE);
        holderData.setBirthPlace("Россия, г. Челябинск");
        holderData.setBirthCountry("Россия");
        holderData.setCitizenship(CitizenshipType.RUSSIAN);
        holderData.setTaxResidence(TaxResidenceType.RUSSIAN);
        holderData.setSnils("12345678901");
        holderData.setTaxPayerNumber("123456789012");

        Document doc = new Document();
        doc.setDocSeries("1234");
        doc.setDocNumber("123456");
        doc.setIsMain(Boolean.TRUE);
        doc.setIsActive(Boolean.TRUE);
        doc.setDocType(DocumentType.PASSPORT_RF);
        doc.setDivisionCode("123-123");
        doc.setIssuedBy("УВД гор. Череповца Вологодской области");
        doc.setIssuedDate(LocalDate.of(2009, 1, 30));
        holderData.setDocuments(Collections.singletonList(doc));

        holderData.setEmail("lazarev@example.org");
        holderData.setPhones(Collections.singletonList(new Phone("+79123456789", PhoneType.MOBILE, true, true, true, null)));

        List<Address> addresses = new ArrayList<>();
        Address resAddress = new Address();
        resAddress.setAddressType(AddressType.RESIDENCE);
        resAddress.setCountry("Россия");
        resAddress.setIndex("162602");
        resAddress.setRegion("Вологодская область");
        resAddress.setLocality("Череповец");
        resAddress.setStreet("пр. Московский");
        resAddress.setHouse("49а");
        resAddress.setApartment("415");
        addresses.add(resAddress);
        Address regAddress = new Address();
        regAddress.setAddressType(AddressType.REGISTRATION);
        regAddress.setCountry("Россия");
        regAddress.setIndex("162612");
        regAddress.setRegion("Вологодская область");
        regAddress.setLocality("Череповец");
        regAddress.setStreet("ул. Химиков");
        regAddress.setHouse("14");
        regAddress.setApartment("10");
        addresses.add(regAddress);
        holderData.setAddresses(addresses);

        holderData.setPublicOfficialStatus(PublicOfficialType.NONE);
        holderData.setBusinessRelations("Долгосрочные");
        holderData.setActivitiesGoal("Страхование жизни");
        holderData.setBusinessRelationsGoal("Страхование жизни");
        holderData.setBusinessReputation("Устойчивая");
        holderData.setFinancialStability("Устойчивое");
        holderData.setFinancesSource("Личные накопления");
        holderData.setPersonalDataConsent(true);
        INSURANCE_MODEL_3.setHolderData(holderData);

        INSURANCE_MODEL_3.setInsuredId(2L);
        Client insuredData = new Client();
        insuredData.setId("2");
        insuredData.setSurName("Войтюк");
        insuredData.setFirstName("Александр");
        insuredData.setMiddleName("Батькович");
        insuredData.setGender(Gender.MALE);
        insuredData.setBirthDate(LocalDate.of(1988, 3, 11));
        insuredData.setBirthPlace("Россия, г. Севастополь");
        insuredData.setBirthCountry("Россия");
        insuredData.setCitizenship(CitizenshipType.RUSSIAN);
        insuredData.setTaxResidence(TaxResidenceType.FOREIGN);
        insuredData.setSnils("10987654321");
        insuredData.setTaxPayerNumber("210987654321");

        doc = new Document();
        doc.setDocSeries("4321");
        doc.setDocNumber("654321");
        doc.setIsMain(Boolean.TRUE);
        doc.setIsActive(Boolean.TRUE);
        doc.setDocType(DocumentType.PASSPORT_RF);
        doc.setDivisionCode("321-321");
        doc.setIssuedDate(LocalDate.of(2008, 3, 30));
        insuredData.setDocuments(Collections.singletonList(doc));

        insuredData.setEmail("voytyuk@example.org");
        insuredData.setPhones(Arrays.asList(
                new Phone("+78202555555", PhoneType.HOME, false, null, null, null),
                new Phone("+79115555555", PhoneType.MOBILE, true, null, null, null),
                new Phone("+78202666666", PhoneType.WORK, false, null, null, null),
                new Phone("+78202777777", PhoneType.FAX, false, null, null, null)
        ));

        List<Address> insuredClientAddresses = new ArrayList<>();
        Address insuredClientResAddress = new Address();
        insuredClientResAddress.setAddressType(AddressType.RESIDENCE);
        insuredClientResAddress.setCountry("Россия");
        insuredClientResAddress.setIndex("162602");
        insuredClientResAddress.setRegion("Вологодская область");
        insuredClientResAddress.setLocality("Череповец");
        insuredClientResAddress.setStreet("ул. Металлургов");
        insuredClientResAddress.setHouse("2");
        insuredClientResAddress.setApartment("215");
        insuredClientAddresses.add(insuredClientResAddress);
        Address insuredClientRegAddress = new Address();
        insuredClientRegAddress.setAddressType(AddressType.REGISTRATION);
        insuredClientRegAddress.setCountry("Россия");
        insuredClientRegAddress.setIndex("160012");
        insuredClientRegAddress.setRegion("Вологодская область");
        insuredClientRegAddress.setLocality("Вологда");
        insuredClientRegAddress.setStreet("ул. Ленина");
        insuredClientRegAddress.setHouse("7");
        insuredClientRegAddress.setApartment("12");
        addresses.add(insuredClientRegAddress);
        insuredData.setAddresses(insuredClientAddresses);

        insuredData.setPublicOfficialStatus(PublicOfficialType.FOREIGNRELATIVE);
        insuredData.setRelations(RelationType.CHILD);
        insuredData.setForeignPublicOfficialType(ForeignPublicOfficialType.MINISTERS);
        insuredData.setPublicOfficialNameAndPosition("Министр иностранных дел Италии");
        insuredData.setBusinessRelations("Долгосрочные");
        insuredData.setActivitiesGoal("Страхование жизни и здоровья");
        insuredData.setBusinessRelationsGoal("Страхование жизни и здоровья");
        insuredData.setBusinessReputation("Негативная");
        insuredData.setFinancialStability("Неустойчивое");
        insuredData.setFinancesSource("Заёмные средства");
        insuredData.setPersonalDataConsent(true);

        INSURANCE_MODEL_3.setInsuredData(insuredData);

        INSURANCE_MODEL_3.setHolderEqualsInsured(false);
        INSURANCE_MODEL_3.setCalendarUnit(CalendarUnit.YEAR);
        INSURANCE_MODEL_3.setPeriodicity(PaymentPeriodicity.ONCE);
        INSURANCE_MODEL_3.setType(FindProgramType.SUM);
        INSURANCE_MODEL_3.setRecipientList(recipientList);
        INSURANCE_MODEL_3.setDuration(4);
        INSURANCE_MODEL_3.setGrowth(180);
        INSURANCE_MODEL_3.setGuaranteeLevel(new BigDecimal("2.3"));
        INSURANCE_MODEL_3.setAmount(new BigDecimal("300000.0"));
        INSURANCE_MODEL_3.setPremium(new BigDecimal("30000.0"));
        INSURANCE_MODEL_3.setRurAmount(new BigDecimal("300000.0"));
        INSURANCE_MODEL_3.setRurPremium(new BigDecimal("30000.0"));
        INSURANCE_MODEL_3.setCurrencyId(1L);
        INSURANCE_MODEL_3.setLowerPressure(80);
        INSURANCE_MODEL_3.setUpperPressure(120);
        INSURANCE_MODEL_3.setRecipientEqualsHolder(false);
        INSURANCE_MODEL_3.setWeight(90);
        INSURANCE_MODEL_3.setStrategyId(1L);

        CLIENT_ENTITY_1 = new ClientEntity();
        CLIENT_ENTITY_1.setId(1L);
        CLIENT_ENTITY_1.setSurName("Иванов");
        CLIENT_ENTITY_1.setFirstName("Иван");
        CLIENT_ENTITY_1.setMiddleName("Иванович");
        CLIENT_ENTITY_1.setGender(GenderTypeEnum.MALE);
        CLIENT_ENTITY_1.setEmail("ivanov@example.org");
        CLIENT_ENTITY_1.setBirthDate(LocalDate.parse("1986-03-30", DateTimeFormatter.ISO_DATE));
        DocumentForClientEntity clientDocument = new DocumentForClientEntity();
        clientDocument.setDocSeries("1234");
        clientDocument.setDocNumber("123456");
        clientDocument.setMain(Boolean.TRUE);
        clientDocument.setActive(Boolean.TRUE);
        clientDocument.setDocType(IdentityDocTypeEnum.PASSPORT_RF);
        CLIENT_ENTITY_1.getDocuments().add(clientDocument);
        PhoneForClaimEntity phone = new PhoneForClaimEntity();
        phone.setPhoneType(PhoneType.MOBILE);
        phone.setNumber("+791100000000");
        phone.setMain(true);
        CLIENT_ENTITY_1.getPhones().add(phone);

    }

}
