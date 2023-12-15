package ru.softlab.efr.services.insurance.services;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xml.sax.SAXException;
import ru.softlab.efr.clients.model.AddressType;
import ru.softlab.efr.services.auth.exceptions.EntityNotFoundException;
import ru.softlab.efr.services.insurance.exception.ValidationException;
import ru.softlab.efr.services.insurance.model.client.unload.*;
import ru.softlab.efr.services.insurance.model.db.AddressForClientEntity;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.DocumentForClientEntity;
import ru.softlab.efr.services.insurance.model.db.PhoneForClaimEntity;
import ru.softlab.efr.services.insurance.model.enums.IdentityDocTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.PublicOfficialTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.RelationTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.RiskLevelEnum;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис, осуществляющий подготовку сущности клиента к выгрузке в XML формат
 *
 * @author olshansky
 * @since 10.01.2019
 */
@Service
public class ClientUnloadService {


    private static final Logger LOGGER = Logger.getLogger(ClientUnloadService.class);

    private static final String REGEX_SNILS_FIND_PATTERN = "(\\d{3})-?(\\d{3})-?(\\d{3})-? ?(\\d{2})";
    private static final String REGEX_SNILS_CONVERT_PATTERN = "$1-$2-$3 $4";

    private static final List<String> pointNames = Arrays.asList(
            "аллея", "авеню", "бульвар", "вал", "взвоз", "въезд", "дорога", "заезд", "кольцо", "линнея",
            "линия", "луч", "магистраль", "набережная", "переулок", "перспектива", "першпектива",
            "площадь", "проезд", "проспект", "проулок", "разъезд", "спуск", "съезд", "территория",
            "тракт", "тупик", "улица", "шоссе", "эспланада", "сквер", "город", "страна", "республика", "область",
            "район", "поселок", "посёлок", "дом", "квартира", "офис", "строение", "корпус", "этаж", "село", "деревня"
    );
    private static final List<String> abbreviations = Arrays.asList(
            "ал", "бул", "б-р", "маг", "наб", "пер", "пл", "просп", "дер",
            "пр-кт", "туп", "ул", "ш", "обл", "пос", "г", "эт", "р-н",
            "пр-т", "пр", "пер", "наб", "корп", "стр", "д", "э", "гор",
            "кв", "к", "р", "с", "ст", "м", "оф", "респ", "т", "п", "у"
    );

    private final CountryService countryService;
    private final InsuranceService insuranceService;
    private final ClientService clientService;

    @Autowired
    public ClientUnloadService(CountryService countryService, InsuranceService insuranceService, ClientService clientService) {
        this.countryService = countryService;
        this.insuranceService = insuranceService;
        this.clientService = clientService;
    }

    public byte[] getClientXmlBytesById(String clientId) throws JAXBException, EntityNotFoundException {
        ClientEntity clientBack = clientService.get(Long.valueOf(clientId));
        if (clientBack == null) {
            throw new EntityNotFoundException();
        }
        return getXmlFromObject(getClientXml(clientBack, null));
    }

    public byte[] getClientXmlBytesByContractIds(List<Long> contractIds) throws JAXBException, EntityNotFoundException {
        List<ClientEntity> clients = clientService.findAllClientByContractIdIn(contractIds);
        if (clients == null || clients.isEmpty()) {
            throw new EntityNotFoundException();
        }
        ClientXml clientXml = null;
        for (ClientEntity client : clients) {
            clientXml = getClientXml(client, clientXml);
        }
        return getXmlFromObject(clientXml);
    }

    private ClientXml getClientXml(ClientEntity clientBack, ClientXml clientXml) {
        ObjectFactory oFactory = new ObjectFactory();

        clientXml = clientXml == null ? oFactory.createClientXml() : clientXml;
        IndividualCustomerInfo individualCustomerInfo = oFactory.createIndividualCustomerInfo();

        ClientXml.ClientInfo clientInfo = oFactory.createClientXmlClientInfo();
        clientInfo.setIsActive("1"); // 1 - активный клиент
        clientInfo.setClientType("2"); // 2- физическое лицо;
        clientInfo.setIsValidPassport("1");
        clientInfo.setIsContractor("0"); // <0> – клиент не является контрагентом;
        individualCustomerInfo.setIdentityKind("2"); //<2> - стандартный

        /*[Михайлец Илона] указать именно эти данные для всех выгружаемых данных*/
        FullName employeeName = new FullName();
        employeeName.setLastName("Королев");
        employeeName.setFirstName("Сергей");
        employeeName.setMiddleName("Геннадьевич");
        clientInfo.setEmployeeName(employeeName);
        clientInfo.setEmployeePosition("Главный специалист УОП");

        /*[Михайлец Илона] В поле "Код страны гражданства" необходимо указывать 643, если клиент - Гражданин РФ, для отсальных - ничего. */
        if (ReportableContract.isInternalCitizen(clientBack)) {
            individualCustomerInfo.setOksmCode("643");
        }

        clientInfo.setReputationInfo("Устойчивая");
        clientInfo.setRelationshipCharacterInfo("Долгосрочные");
        clientInfo.setRiskDegreeInfo("Нет критериев для присвоения иного уровня риска");


        clientInfo.setFinanceInfo(StringUtils.isNotBlank(clientBack.getFinancialStability()) ? clientBack.getFinancialStability() : "Устойчивая");
        clientInfo.setFinancesSourceInfo(StringUtils.isNotBlank(clientBack.getFinancesSource()) ? clientBack.getFinancesSource() : "Устойчивое");
        clientInfo.setIsResident(ReportableContract.isInternalCitizen(clientBack) ? "1" : "0");
        String activitiesGoal = StringUtils.isNotBlank(clientBack.getActivitiesGoal()) ? clientBack.getActivitiesGoal() : "Страхование жизни";
        clientInfo.setActivitiesGoalInfo(activitiesGoal);
        clientInfo.setRelationshipGoalInfo(activitiesGoal);

        /*<1> - стандартная;<2> - высокая;*/
        String riskDegree = clientBack.getRiskLevel() != null && clientBack.getRiskLevel().equals(RiskLevelEnum.HIGH) ? "2" : "1";
        clientInfo.setRiskDegree(riskDegree);

        ClientXml.ClientInfo.OrgInfo orgInfo = oFactory.createClientXmlClientInfoOrgInfo();

        FullName fullName = oFactory.createFullName();
        fullName.setLastName(clientBack.getSurName());
        fullName.setFirstName(clientBack.getFirstName());
        fullName.setMiddleName(clientBack.getMiddleName());
        individualCustomerInfo.setIndividualCustomerFullName(fullName);

        if (StringUtils.isNotBlank(clientBack.getInn())) {
            individualCustomerInfo.setInn(clientBack.getInn());
        }
        if (StringUtils.isNotBlank(clientBack.getSnils())) {
            individualCustomerInfo.setSnlis(presentSnils(clientBack.getSnils()));
        }
        individualCustomerInfo.setBirthDate(presentLocalDate(clientBack.getBirthDate()));
        individualCustomerInfo.setCitizenshipKind(getCitizenShipKind(clientBack));
        individualCustomerInfo.setIsPublicOfficial(getPublicOfficialFlag(clientBack));
        String relations = getRelations(clientBack);
        if (StringUtils.isNotBlank(relations)) {
            individualCustomerInfo.setRelations(relations);
        }
        if (StringUtils.isNotBlank(clientBack.getPublicOfficialPosition())) {
            individualCustomerInfo.setPosition(clientBack.getPublicOfficialPosition());
        }
        if (StringUtils.isNotBlank(clientBack.getCitizenshipCountry())) {
            individualCustomerInfo.setCountryName(clientBack.getCitizenshipCountry());
        }

        BirthPlace birthPlace = oFactory.createBirthPlace();

        String oksmCode = countryService.getOKSMCodeByCountryName(clientBack.getBirthCountry());

        if (oksmCode != null) {
            birthPlace.setOksmCode(formatOKSM(oksmCode));
        }

        birthPlace.setEntityOkatoCode(formatOkato(clientBack.getBirthRegionOkato()));


        birthPlace.setArea(addDescriptionIfNeed(replaceFirstWordOnBack(clientBack.getBirthArea()), "р-н"));
        birthPlace.setPoint(addDescriptionIfNeed(replaceFirstWordOnBack(clientBack.getBirthPlace()), "г."));
        individualCustomerInfo.setBirthPlace(birthPlace);

        DocumentForClientEntity migrationCardBack = ReportableContract.getDocByType(clientBack, IdentityDocTypeEnum.MIGRATION_CARD);
        if (migrationCardBack != null) {
            MigrationCardInfo migrationCardXml = oFactory.createMigrationCardInfo();
            migrationCardXml.setSeries(migrationCardBack.getDocSeries());
            migrationCardXml.setNumber(migrationCardBack.getDocNumber());
            migrationCardXml.setStartDate(presentLocalDate(migrationCardBack.getStayStartDate()));
            migrationCardXml.setEndDate(presentLocalDate(migrationCardBack.getStayEndDate()));
            individualCustomerInfo.setMigrationCardInfo(migrationCardXml);
        }

        DocumentForClientEntity foreignDocBack = ReportableContract.getDocByType(clientBack, IdentityDocTypeEnum.MIGRATION_CARD);

        if (foreignDocBack != null) {
            ForeignDocInfo foreignDocInfo = oFactory.createForeignDocInfo();
            foreignDocInfo.setSeries(foreignDocBack.getDocSeries());
            foreignDocInfo.setNumber(foreignDocBack.getDocNumber());
            foreignDocInfo.setDocKindCode(foreignDocBack.getDocType() != null ? foreignDocBack.getDocType().name() : null);
            foreignDocInfo.setStartDate(presentLocalDate(foreignDocBack.getStayStartDate()));
            foreignDocInfo.setEndDate(presentLocalDate(foreignDocBack.getStayEndDate()));
            individualCustomerInfo.setForeignDocInfo(foreignDocInfo);
        }

        DocumentForClientEntity identityDocBack = clientBack.getMainDocument();
        IdentityDocInfo identityDocInfo = oFactory.createIdentityDocInfo();
        if (identityDocBack != null) {
            identityDocInfo.setIdentityDocSeries(identityDocBack.getDocSeries());
            identityDocInfo.setIdentityDocNumber(identityDocBack.getDocNumber());

            identityDocInfo.setIdentityDocIssuedDate(presentLocalDate(identityDocBack.getIssuedDate()));
            identityDocInfo.setIdentityDocIssuedBy(identityDocBack.getIssuedBy());
            if (identityDocBack.getDocType() != null) {
                identityDocInfo.setIdentityDocKindCode(identityDocBack.getDocType().getCode());
                identityDocInfo.setIdentityDocAnotherName(identityDocBack.getDocName());
            }
            if (StringUtils.isNotBlank(identityDocBack.getDivisionCode())) {
                identityDocInfo.setIdentityDocDivisionCode(ReportableContract.presentDivisionCode(identityDocBack.getDivisionCode()));
            } else {
                LOGGER.error("При формировании XML произошла ошибка - отсутствует значение в поле Код подразделения в ДУЛ.");
            }
        }
        individualCustomerInfo.setIdentityDoc(oFactory.createIndividualCustomerInfoIdentityDoc(identityDocInfo));

        orgInfo.setIndividualCustomerInfo(individualCustomerInfo);

        AddressForClientEntity regAddressBack = ReportableContract.getAddressByType(clientBack.getAddresses(), AddressType.REGISTRATION);
        if (regAddressBack != null) {
            Address addressXml = getAddressXml(oFactory, regAddressBack);
            clientInfo.setRegAddress(addressXml);
        }
        clientInfo.setOrgInfo(orgInfo);
        AddressForClientEntity residenceAddressBack = ReportableContract.getAddressByType(clientBack.getAddresses(), AddressType.RESIDENCE);

        if (residenceAddressBack != null) {
            Address addressXml = getAddressXml(oFactory, residenceAddressBack);
            clientInfo.setResidenceAddress(addressXml);
        }

        clientInfo.setPhone(CollectionUtils.isEmpty(clientBack.getPhones()) ? "-" : clientBack.getPhones().stream().map(PhoneForClaimEntity::getNumber).collect(Collectors.joining(",")));

        LocalDate firstContractDate = getFirstContractDateByClient(clientBack);

        clientInfo.setRelStartDate(presentLocalDate(firstContractDate));
        clientInfo.setCompletionDate(presentLocalDate(firstContractDate));
        clientInfo.setDataIdent(presentLocalDate(firstContractDate));

        clientXml.getClientInfo().add(clientInfo);

        return clientXml;
    }

    private LocalDate getFirstContractDateByClient(ClientEntity clientBack) {
        LocalDate contractDate = insuranceService.getFirstContractDateByClient(clientBack.getId());
        if (contractDate != null) {
            return contractDate;
        }
        return clientBack.getRegistrationDate() != null ? clientBack.getRegistrationDate() : clientBack.getLastUpdateDate();
    }

    static boolean isPointName(String word) {
        if (StringUtils.isBlank(word)) {
            return false;
        }
        String handledWord = word.replaceAll("\\.+", "").toLowerCase();
        return pointNames.contains(handledWord) || abbreviations.contains(handledWord);
    }

    private static String[] splitWord(String inputString, String regexPatternSplit) {
        if (StringUtils.isNotBlank(inputString) && (inputString.matches(".*" + regexPatternSplit + ".*"))) {
            return inputString.split(regexPatternSplit);
        }
        return new String[]{inputString};
    }

    static String addDescriptionIfNeed(String source, String description) {
        if (StringUtils.isNotBlank(source) && (splitWord(source.trim(), "\\s").length == 1
                && splitWord(source.trim(), "\\.").length == 1)) {
            return source.trim().concat(" ").concat(description.trim());
        }
        return source;
    }

    static String replaceFirstWordOnBack(String inputString) {
        String[] words = splitWord(inputString, "\\s");
        if (words.length > 1 && isPointName(words[0])) {
            return inputString.replaceFirst(words[0].concat(" "), "").concat(" ".concat(words[0]));
        } else {
            words = splitWord(inputString, "\\.");
            if (words.length > 1 && isPointName(words[0])) {
                return inputString.replaceFirst(words[0].concat("."), "").concat(" ".concat(words[0].concat(".")));
            }
        }
        return getStringOrNull(inputString);
    }

    private Address getAddressXml(ObjectFactory oFactory, AddressForClientEntity addressBack) {
        Address addressXml = oFactory.createAddress();
        String oksmCode = addressBack.getCountryCode() != null ? addressBack.getCountryCode() :
                countryService.getOKSMCodeByCountryName(addressBack.getCountry());
        if (oksmCode != null) {
            addressXml.setOksmCode(formatOKSM(oksmCode));
        }
        addressXml.setCountryName(getStringOrNull(addressBack.getCountry()));
        addressXml.setArea(addDescriptionIfNeed(replaceFirstWordOnBack(addressBack.getArea()), "р-н"));
        /*[Михайлец Илона] Значение из поля Город добавляется к населенному пункту. В начале переставленное значение из населенного пункта, потом переставленное из города.*/
        String point = getAddressWithCity(addressBack.getLocality(),addressBack.getCity());
        if (StringUtils.isNotBlank(point)) {
            addressXml.setPoint(point);
        }
        addressXml.setEntityOkatoCode(formatOkato(addressBack.getOkato()));
        addressXml.setPostCode(replaceFirstWordOnBack(addressBack.getIndex()));
        addressXml.setStreet(addDescriptionIfNeed(replaceFirstWordOnBack(addressBack.getStreet()), "ул."));
        addressXml.setHouse(replaceFirstWordOnBack(addressBack.getHouse()));
        addressXml.setBuilding(replaceFirstWordOnBack(addressBack.getHousing()));
        addressXml.setOffice(replaceFirstWordOnBack(addressBack.getApartment()));
        return addressXml;
    }

    public static String getAddressWithCity(String locality, String city) {
        return (StringUtils.isNotBlank(locality) && !locality.trim().equalsIgnoreCase("-") ?
                addDescriptionIfNeed(replaceFirstWordOnBack(locality), "г.") : "")
                .concat(StringUtils.isNotBlank(city) ?
                        (StringUtils.isNotBlank(locality) && !locality.trim().equalsIgnoreCase("-") ? ", " : "")
                                .concat(addDescriptionIfNeed(replaceFirstWordOnBack(city), "г.")) : "");
    }
    private byte[] getXmlFromObject(ClientXml clientXml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ClientXml.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(clientXml, sw);
        return sw.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String presentLocalDate(LocalDate localDate) {
        if (localDate == null) {
            //TODO: Что делать, если не указано? По XSD схеме значение должно соответствовать регулярному выражению "[0-9]{2}/[0-9]{2}/[0-9]{4}"
            return "01/01/1900";
        }
        return ReportableContract.presentLocalDate(localDate, "dd/MM/yyyy");
    }

    String presentSnils(String snils) {
        if (StringUtils.isBlank(snils)) {
            return "";
        }
        return snils.replaceAll(REGEX_SNILS_FIND_PATTERN, REGEX_SNILS_CONVERT_PATTERN)
                .replaceAll("-{2,9}", "-")
                .replaceAll(" {2,9}", " ");
    }


    private String getRelations(ClientEntity clientBack) {
        if (clientBack.getRelations() != null) {
            if (clientBack.getRelations().equals(RelationTypeEnum.SPOUSE)) {
                return "1";
            } else if (clientBack.getRelations().equals(RelationTypeEnum.PARENT)) {
                return "2";
            } else if (clientBack.getRelations().equals(RelationTypeEnum.CHILD)) {
                return "3";
            } else if (clientBack.getRelations().equals(RelationTypeEnum.GRANDPARENT)) {
                return "4";
            } else if (clientBack.getRelations().equals(RelationTypeEnum.GRANDCHILD)) {
                return "5";
            } else if (clientBack.getRelations().equals(RelationTypeEnum.SIBLING)) {
                return "6";
            } else if (clientBack.getRelations().equals(RelationTypeEnum.STEPPARENT)) {
                return "7";
            }
        }

        return "";
    }

    private String getPublicOfficialFlag(ClientEntity clientBack) {
        if (clientBack.getPublicOfficialStatus() != null) {
            if (clientBack.getPublicOfficialStatus().equals(PublicOfficialTypeEnum.FOREIGN)) {
                return "1";
            } else if (clientBack.getPublicOfficialStatus().equals(PublicOfficialTypeEnum.FOREIGNRELATIVE)) {
                return "2";
            } else if (clientBack.getPublicOfficialStatus().equals(PublicOfficialTypeEnum.RUSSIAN)) {
                return "3";
            } else if (clientBack.getPublicOfficialStatus().equals(PublicOfficialTypeEnum.RUSSIANRELATIVE)) {
                return "4";
            } else if (clientBack.getPublicOfficialStatus().equals(PublicOfficialTypeEnum.INTERNATIONAL)
                    || clientBack.getPublicOfficialStatus().equals(PublicOfficialTypeEnum.INTERNATIONALRELATIVE)) {
                return "5";
            }
        }
        return "0";
    }

    String getCitizenShipKind(ClientEntity clientBack) {
        String residentCode = countryService.getOKSMCodeByCountryName(clientBack.getResident());
        return StringUtils.isNotBlank(residentCode) && (residentCode.equalsIgnoreCase("643") //Российская Федерация
                || residentCode.equalsIgnoreCase("398") // Республика Казахстан
                || residentCode.equalsIgnoreCase("112") //Республика Беларусь
                || residentCode.equalsIgnoreCase("51") //Республика Армения
        ) ? "1" : "2"; // 1 - Гражданин РФ, Беларусии, Казахстана или Армении; 2 - Иностранный гражданин
    }

    public void checkRequest(byte[] xmlFile, InputStream xsdSchema) {
        String errors = getClientXmlErrors(xmlFile, xsdSchema);
        if (!errors.isEmpty()) {
            List<String> errorMessages = new ArrayList<>();
            errorMessages.add(String.format("Произошла ошибка при проверке сформированного XML-документа. Подробности: %s", errors));
            throw new ValidationException(errorMessages);
        }
    }

    public String getClientXmlErrors(byte[] xmlFile, InputStream xsdSchema) {
        JAXBContext jaxbContext;
        String result = "";

        try {
            jaxbContext = JAXBContext.newInstance(ClientXml.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Schema employeeSchema = sf.newSchema(new StreamSource(xsdSchema));
            jaxbUnmarshaller.setSchema(employeeSchema);
            InputStream targetStream = new ByteArrayInputStream(xmlFile);
            jaxbUnmarshaller.unmarshal(targetStream);
        } catch (UnmarshalException e) {
            LOGGER.error("Во время выгрузки анкеты клиента в XML произошла ошибка, причина: ", e);
            LOGGER.info(new String(xmlFile, StandardCharsets.UTF_8));
            result = e.getLinkedException().getMessage();
        } catch (JAXBException | SAXException e) {
            LOGGER.error("Во время выгрузки анкеты клиента в XML произошла ошибка, причина: ", e);
            LOGGER.info(new String(xmlFile, StandardCharsets.UTF_8));
        }
        return result;
    }

    private String formatOkato(String okato) {
        if (okato != null && okato.length() == 11) {
            return okato.substring(0, 2);
        }
        return null;
    }

    private String formatOKSM(String oksm) {
        if (oksm != null && oksm.length() == 1) {
            return "00" + oksm;
        } else if (oksm != null && oksm.length() == 2) {
            return "0" + oksm;
        }
        return oksm;
    }

    private static String getStringOrNull(String value) {
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        return null;
    }
}
