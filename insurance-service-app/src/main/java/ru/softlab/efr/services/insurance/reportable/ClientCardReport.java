package ru.softlab.efr.services.insurance.reportable;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.clients.model.AddressType;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.*;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;
import ru.softlab.efr.services.insurance.service.CacheableDictStatusService;
import ru.softlab.efr.services.insurance.services.ClientUnloadService;
import ru.softlab.ib6.reporting.model.Reportable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum.INVALID_IDENTITY_DOC;
import static ru.softlab.efr.services.insurance.model.reportable.ReportableContract.*;

public class ClientCardReport implements Reportable {

    private static final Logger LOGGER = Logger.getLogger(ClientCardReport.class);
    private static final String NOT_SPECIFIED = "(не указано)";
    private static final String NOT_DATA = "(нет данных о проверках)";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String LOCALE_LANGUAGE = "ru";
    private static final Long ELEVEN_MONTHS = 11L;
    private Map<String, Object> fieldValues;

    /**
     * Функция расчета дат обновлений карточки клиента.
     * <p>
     * Так как пока у нас нет поддержки версионности для анкеты клиента, текущий список дат формируется след. образом:
     * <p>
     * Расчитывается разница (в месяцах) между текущей датой и датой создания карточки клиента.
     * берется дата формирование карточки клиента + 11 месяцев. В список "дат обновлений" добавляется ближайшая дата проверки по справочнику
     * недействительных паспортов. Цикл выполняется пока полученная разница в месяцах не окажется меньше 0
     *
     * @param dates      список дат успешных проверок по справочнику недействительных паспортов.
     * @param targetDate дата создания карточки клиента = дате создание первого контракта.
     * @return список "дат обновлений карточки клиента"
     */
    public static List<LocalDate> getModifiedDate(List<LocalDate> dates, LocalDate targetDate) {
        NavigableSet<LocalDate> treeSet = new TreeSet<>(dates);
        List<LocalDate> list = new ArrayList<>();
        //расчитывается разница (в месяцах) между текущей датой и датой создания карточки клиента
        long difference = Math.abs(ChronoUnit.MONTHS.between(LocalDate.now().withDayOfMonth(1), targetDate.withDayOfMonth(1)));
        //кол-во требуемых месяцев
        long months = ELEVEN_MONTHS;
        long differenceForLoop = difference;
        //пока разница > 0 и кол-во добавляемых месяцев в итерации < чем вычисляемая разница
        while (differenceForLoop >= 0 && months <= difference) {
            //увеличиваем дату создания на months, где months кол-во месяцев увеличивыемое с каждой итерацией на 11 месяцев.
            LocalDate newDate = targetDate.plusMonths(months);
            //получаем ближайшую дату, но >= чем newDate, возвращаем null если не найдено
            LocalDate localAbove = treeSet.ceiling(newDate);
            //получаем ближайшую дату, но <= чем newDate, возвращаем null если не найдено
            LocalDate localBelow = treeSet.floor(newDate);
            try {
                if (localAbove != null) {
                    long diffAboveDate = Math.abs(ChronoUnit.DAYS.between(localAbove, newDate));
                    if (localBelow != null) {
                        long diffBelowDate = Math.abs(ChronoUnit.DAYS.between(localBelow, newDate));
                        //добавляем дату которая находится ближе всего к newDate
                        list.add(diffAboveDate <= diffBelowDate ? localAbove : localBelow);
                    } else {
                        list.add(localAbove);
                    }
                }
            } catch (NullPointerException e) {
                LOGGER.error("Не удалось найти ближайшую дату проверки, причина: " + e, e);
            }
            months += ELEVEN_MONTHS;
            differenceForLoop -= ELEVEN_MONTHS;
        }
        if (list.isEmpty()) {
            list.add(targetDate);
        }
        return list.stream().distinct().collect(Collectors.toList());
    }

    private static String resultCheck(CheckStateEnum checkResult) {
        if (checkResult == CheckStateEnum.TRUE) {
            return "Недействительный";
        }
        if (checkResult == CheckStateEnum.FALSE) {
            return "Действительный";
        }
        return NOT_SPECIFIED;
    }

    private static boolean checkEnum(ClientEntity client, PublicOfficialTypeEnum typeEnum) {
        return client.getPublicOfficialStatus() == typeEnum;
    }

    private static String relationTypeEnumSearch(RelationTypeEnum relations) {
        if (relations == RelationTypeEnum.SPOUSE) {
            return "Супруг/супруга";
        }
        if (relations == RelationTypeEnum.PARENT) {
            return "Отец/мать";
        }
        if (relations == RelationTypeEnum.CHILD) {
            return "Сын/дочь";
        }
        if (relations == RelationTypeEnum.GRANDPARENT) {
            return "Дедушка/бабушка";
        }
        if (relations == RelationTypeEnum.GRANDCHILD) {
            return "Внук/внучка";
        }
        if (relations == RelationTypeEnum.SIBLING) {
            return "Брат/сестра (в том числе неполнородные)";
        }
        if (relations == RelationTypeEnum.STEPPARENT) {
            return "Отчим/мачеха";
        }
        return NOT_SPECIFIED;
    }

    private static String foreignOfficialTypeSearch(ForeignPublicOfficialTypeEnum typeEnum) {
        if (typeEnum == ForeignPublicOfficialTypeEnum.STATELEADERS) {
            return "Главы государств или правительств (независимо от формы государственного устройства)";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.MINISTERS) {
            return "Министры, их заместители и помощники";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.COURTOFFICIALS) {
            return "Должностные лица судебных органов власти последней инстанции (Верховный, Конституционный суд)";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.HIGHOFFICIALS) {
            return "Высшие правительственные чиновники";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.PROSECUTORS) {
            return "Государственный прокурор и его заместители";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.HIGHMILITARYOFFICIALS) {
            return "Высшие военные чиновники";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.NATIONALBANKLEADERS) {
            return "Руководители и члены Советов директоров Национальных Банков";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.POLITICALLEADERS) {
            return "Лидер официально зарегистрированной политической партии движения, его заместитель";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.NATIONALCORPORATIONLEADERS) {
            return "Руководители государственных корпораций";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.RELIGIOUSLEADERS) {
            return "Глава религиозной организации (осуществляющей государственные управленческие функции), его заместитель";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.AMBASSADORS) {
            return "Послы";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.INTERNATIONALORGANIZATIONLEADERS) {
            return "Руководители, заместители руководителей международных организаций (ООН, ОЭСР, ОПЕК, Олимпийский комитет, Гаагский трибунал)";
        }
        if (typeEnum == ForeignPublicOfficialTypeEnum.INTERNATIONALCOURTLEADERS) {
            return "Руководители и члены международных судебных Организаций (Суд по правам человека, Гаагский трибунал)";
        }
        return NOT_SPECIFIED;
    }

    private static String russianOfficialTypeSearch(RussianPublicOfficialTypeEnum typeEnum) {
        if (typeEnum == RussianPublicOfficialTypeEnum.OTHER) {
            return "Должности в иных организациях, созданных РФ на основании федеральных законов, включенных в перечни должностей, определяемых Президентом РФ";
        }
        if (typeEnum == RussianPublicOfficialTypeEnum.STATEOFFICIALS) {
            return "Государственные должности РФ";
        }
        if (typeEnum == RussianPublicOfficialTypeEnum.CENTRALBANKEXECUTIVES) {
            return "Должности членов Совета директоров ЦБ РФ";
        }
        if (typeEnum == RussianPublicOfficialTypeEnum.FEDERALSTATEOFFICIALS) {
            return "Должности федеральной государственной службы, назначение на которые и освобождение от которых осуществляется Президентом РФ и Правительством РФ";
        }
        if (typeEnum == RussianPublicOfficialTypeEnum.CENTRALBANKOFFICIALS) {
            return "Должности в ЦБ РФ, включенные в перечни должностей, определяемые Президентом РФ";
        }
        if (typeEnum == RussianPublicOfficialTypeEnum.NATIONALCORPORATIONOFFICIALS) {
            return "Должности в государственных корпорациях, созданных РФ на основании федеральных законов, включенные в перечни должностей, определяемых Президентом РФ";
        }
        return NOT_SPECIFIED;
    }

    private static String ensureNotNull(String inputString) {
        return isNotEmpty(inputString) ? inputString : NOT_SPECIFIED;
    }

    private static Boolean isNotEmpty(String inputString) {
        return inputString != null && !inputString.trim().isEmpty();
    }

    private static String presentLocalDate(LocalDateTime localDate) {
        if (localDate == null) {
            return NOT_SPECIFIED;
        }
        return localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(new Locale(LOCALE_LANGUAGE)));
    }

    private static DocumentForClientEntity getDocByType(ClientEntity client, IdentityDocTypeEnum type) {
        return client.getDocuments().stream()
                .filter(f -> type.equals(f.getDocType()))
                .findFirst().orElse(null);
    }

    public ClientCardReport construct(List<Insurance> allContracts, ClientEntity client, List<ClientCheck> checkList, CacheableDictStatusService dictStatusService) {
        ClientCardReport clientCardReport = new ClientCardReport();
        clientCardReport.fieldValues = new LinkedHashMap<>();

        clientCardReport.fieldValues.put("fullName", getClientFullName(client.getSurName(), client.getFirstName(), client.getMiddleName()));
        clientCardReport.fieldValues.put("INN", ensureNotNull(client.getTaxPayerNumber()));
        clientCardReport.fieldValues.put("snils", ensureNotNull(client.getSnils()));

        clientCardReport.fieldValues.put("birthDate", ReportableContract.presentLocalDate(client.getBirthDate()));
        clientCardReport.fieldValues.put("birthCountry", ensureNotNull(getCountryNameByCode(client.getBirthCountry())));
        clientCardReport.fieldValues.put("birthRegion", ensureNotNull(client.getBirthRegion()));
        clientCardReport.fieldValues.put("birthArea", ensureNotNull(client.getBirthArea()));
        clientCardReport.fieldValues.put("birthPlace", ensureNotNull(client.getBirthPlace()));

        clientCardReport.fieldValues.put("riskLevelDesc", ensureNotNull(client.getRiskLevelDesc()));

        clientCardReport.fieldValues.put("insuredIdDocName", isEmptyMainDoc(client) ? NOT_SPECIFIED : getMainDocNameByClient(client.getMainDocument()));
        clientCardReport.fieldValues.put("insuredIdDocNumber", isEmptyMainDoc(client) ? NOT_SPECIFIED : ensureNotNull(client.getMainDocument().getDocNumber()));
        clientCardReport.fieldValues.put("insuredIdDocSeries", isEmptyMainDoc(client) ? NOT_SPECIFIED : ensureNotNull(client.getMainDocument().getDocSeries()));
        clientCardReport.fieldValues.put("insuredCitizenship", client.getResident() != null ? CitizenshipTypeEnum.valueOf(client.getResident().toUpperCase()).toString() : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("insuredIdDocIssuedBy", isEmptyMainDoc(client) ? NOT_SPECIFIED : ensureNotNull(client.getMainDocument().getIssuedBy()));
        clientCardReport.fieldValues.put("insuredIdDivisionCode", isEmptyMainDoc(client) ? NOT_SPECIFIED : client.getMainDocument().getDivisionCode() != null ?
                presentDivisionCode(client.getMainDocument().getDivisionCode()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("insuredIdDocIssuedDate", isEmptyMainDoc(client) ? NOT_SPECIFIED : ReportableContract.presentLocalDate(client.getMainDocument().getIssuedDate()));

        DocumentForClientEntity migrationCard = getDocByType(client, IdentityDocTypeEnum.MIGRATION_CARD);
        DocumentForClientEntity residencePermitCard = getDocByType(client, IdentityDocTypeEnum.TEMPORARY_RESIDENCE_PERMIT);

        clientCardReport.fieldValues.put("migrationCardSeries", migrationCard != null ? ensureNotNull(migrationCard.getDocSeries()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("migrationCardNumber", migrationCard != null ? ensureNotNull(migrationCard.getDocNumber()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("migrationCardStartDate", migrationCard != null ? ReportableContract.presentLocalDate(migrationCard.getStayStartDate()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("migrationCardEndDate", migrationCard != null ? ReportableContract.presentLocalDate(migrationCard.getStayEndDate()) : NOT_SPECIFIED);

        clientCardReport.fieldValues.put("residencePermitCardName", residencePermitCard != null ? ensureNotNull(residencePermitCard.getDocName()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residencePermitCardSeries", residencePermitCard != null ? ensureNotNull(residencePermitCard.getDocSeries()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residencePermitCardNumber", residencePermitCard != null ? ensureNotNull(residencePermitCard.getDocNumber()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residencePermitCardStartDate", residencePermitCard != null ? ReportableContract.presentLocalDate(residencePermitCard.getStayStartDate()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residencePermitCardEndDate", residencePermitCard != null ? ReportableContract.presentLocalDate(residencePermitCard.getStayEndDate()) : NOT_SPECIFIED);

        AddressForClientEntity residenceAddress = getAddressByType(client.getAddresses(), AddressType.RESIDENCE);
        AddressForClientEntity registrationAddress = getAddressByType(client.getAddresses(), AddressType.REGISTRATION);

        clientCardReport.fieldValues.put("residenceIndex", residenceAddress != null ? ensureNotNull(residenceAddress.getIndex()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residenceCode", residenceAddress != null ? ensureNotNull(Reportable.ensureNotNull(residenceAddress.getCountryCode())) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residenceNameCountry", residenceAddress != null ? ensureNotNull(getCountryNameByCode(Reportable.ensureNotNull(residenceAddress.getCountry()))) : NOT_SPECIFIED);

        clientCardReport.fieldValues.put("residenceOkato", residenceAddress != null && residenceAddress.getOkato() != null ? residenceAddress.getOkato().substring(0, 2) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residenceRegion", residenceAddress != null ? ensureNotNull(residenceAddress.getRegion()) : NOT_SPECIFIED);

        String residenceLocality = residenceAddress != null ? residenceAddress.getLocality() : "";
        String residenceCity = residenceAddress != null ? residenceAddress.getCity() : "";
        String getResidenceLocality = ClientUnloadService.getAddressWithCity(residenceLocality, residenceCity);

        clientCardReport.fieldValues.put("residenceLocality", residenceAddress != null ? (StringUtils.isNoneBlank(getResidenceLocality) ? getResidenceLocality : NOT_SPECIFIED) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residenceArea", residenceAddress != null ? ensureNotNull(residenceAddress.getArea()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residenceStreet", residenceAddress != null ? ensureNotNull(residenceAddress.getStreet()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residenceHouse", residenceAddress != null ? ensureNotNull(residenceAddress.getHouse()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residenceConstruction", residenceAddress != null ? ensureNotNull(residenceAddress.getConstruction()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("residenceApartment", residenceAddress != null ? ensureNotNull(residenceAddress.getApartment()) : NOT_SPECIFIED);


        clientCardReport.fieldValues.put("registrationIndex", registrationAddress != null ? ensureNotNull(registrationAddress.getIndex()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("registrationCode", registrationAddress != null ? ensureNotNull(Reportable.ensureNotNull(registrationAddress.getCountryCode())) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("registrationNameCountry", registrationAddress != null ? ensureNotNull(getCountryNameByCode(ensureNotNull(registrationAddress.getCountry()))) : NOT_SPECIFIED);

        clientCardReport.fieldValues.put("registrationOkato", registrationAddress != null && registrationAddress.getOkato() != null ? ensureNotNull(registrationAddress.getOkato().substring(0, 2)) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("registrationRegion", registrationAddress != null ? ensureNotNull(registrationAddress.getRegion()) : NOT_SPECIFIED);

        clientCardReport.fieldValues.put("registrationArea", registrationAddress != null ? ensureNotNull(registrationAddress.getArea()) : NOT_SPECIFIED);


        String registrationLocality = registrationAddress != null ? registrationAddress.getLocality() : "";
        String registrationCity = registrationAddress != null ? registrationAddress.getCity() : "";
        String getRegistrationLocality = ClientUnloadService.getAddressWithCity(registrationLocality, registrationCity);
        clientCardReport.fieldValues.put("registrationLocality", registrationAddress != null ? (StringUtils.isNoneBlank(getRegistrationLocality) ? getRegistrationLocality : NOT_SPECIFIED) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("registrationStreet", registrationAddress != null ? ensureNotNull(registrationAddress.getStreet()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("registrationHouse", registrationAddress != null ? ensureNotNull(registrationAddress.getHouse()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("registrationConstruction", registrationAddress != null ? ensureNotNull(registrationAddress.getConstruction()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("registrationApartment", registrationAddress != null ? ensureNotNull(registrationAddress.getApartment()) : NOT_SPECIFIED);


        List<String> list = client.getPhones().stream().map(PhoneForClaimEntity::getNumber).collect(Collectors.toList());
        clientCardReport.fieldValues.put("phoneNumbers", !client.getPhones().isEmpty() ? StringUtils.join(list, ", ") : NOT_SPECIFIED);

        //ИПДЛ
        clientCardReport.fieldValues.put("foreign", client.getPublicOfficialStatus() == PublicOfficialTypeEnum.FOREIGN ? "ДА" : "НЕТ");
        clientCardReport.fieldValues.put("foreignOfficialType", checkEnum(client, PublicOfficialTypeEnum.FOREIGN) ? foreignOfficialTypeSearch(client.getForeignPublicOfficialType()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("foreignOfficialPosition", checkEnum(client, PublicOfficialTypeEnum.FOREIGN) ? client.getPublicOfficialPosition() : NOT_SPECIFIED);

        //Ближайшее окружение иностранного публичного должностного лица
        clientCardReport.fieldValues.put("foreignRelative", client.getPublicOfficialStatus() == PublicOfficialTypeEnum.FOREIGNRELATIVE ? "ДА" : "НЕТ");
        clientCardReport.fieldValues.put("foreignRelativeRelationType", checkEnum(client, PublicOfficialTypeEnum.FOREIGNRELATIVE) ? relationTypeEnumSearch(client.getRelations()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("foreignRelativeOfficialType", checkEnum(client, PublicOfficialTypeEnum.FOREIGNRELATIVE) ? foreignOfficialTypeSearch(client.getForeignPublicOfficialType()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("foreignRelativeNameAndPosition", checkEnum(client, PublicOfficialTypeEnum.FOREIGNRELATIVE) ? client.getPublicOfficialNameAndPosition() : NOT_SPECIFIED);

        //РПДЛ
        clientCardReport.fieldValues.put("russian", client.getPublicOfficialStatus() == PublicOfficialTypeEnum.RUSSIAN ? "ДА" : "НЕТ");
        clientCardReport.fieldValues.put("russianOfficialType", checkEnum(client, PublicOfficialTypeEnum.RUSSIAN) ? russianOfficialTypeSearch(client.getRussianPublicOfficialType()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("russianOfficialPosition", checkEnum(client, PublicOfficialTypeEnum.RUSSIAN) ? client.getPublicOfficialPosition() : NOT_SPECIFIED);

        //Ближайшее окружение РПДЛ
        clientCardReport.fieldValues.put("russianRelative", client.getPublicOfficialStatus() == PublicOfficialTypeEnum.RUSSIANRELATIVE ? "ДА" : "НЕТ");
        clientCardReport.fieldValues.put("russianRelativeRelationType", checkEnum(client, PublicOfficialTypeEnum.RUSSIANRELATIVE) ? relationTypeEnumSearch(client.getRelations()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("russianRelativeOfficialType", checkEnum(client, PublicOfficialTypeEnum.RUSSIANRELATIVE) ? russianOfficialTypeSearch(client.getRussianPublicOfficialType()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("russianRelativeNameAndPosition", checkEnum(client, PublicOfficialTypeEnum.RUSSIANRELATIVE) ? client.getPublicOfficialNameAndPosition() : NOT_SPECIFIED);

        //МПДЛ
        clientCardReport.fieldValues.put("international", client.getPublicOfficialStatus() == PublicOfficialTypeEnum.INTERNATIONAL ? "ДА" : "НЕТ");
        clientCardReport.fieldValues.put("internationalOfficialPosition", checkEnum(client, PublicOfficialTypeEnum.INTERNATIONAL) ? client.getPublicOfficialPosition() : NOT_SPECIFIED);

        //Ближайшее окружение МПДЛ
        clientCardReport.fieldValues.put("internationalRelative", client.getPublicOfficialStatus() == PublicOfficialTypeEnum.INTERNATIONALRELATIVE ? "ДА" : "НЕТ");
        clientCardReport.fieldValues.put("internationalRelativeRelationType", checkEnum(client, PublicOfficialTypeEnum.INTERNATIONALRELATIVE) ? relationTypeEnumSearch(client.getRelations()) : NOT_SPECIFIED);
        clientCardReport.fieldValues.put("internationalRelativeNameAndPosition", checkEnum(client, PublicOfficialTypeEnum.INTERNATIONALRELATIVE) ? client.getPublicOfficialNameAndPosition() : NOT_SPECIFIED);


        List<ReportableRecipient> reportableRecipientList = new ArrayList<>();
        for (Insurance contract : allContracts) {
            if (!CollectionUtils.isEmpty(contract.getRecipientList())) {
                List<ReportableRisk> reportableRiskList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(contract.getRiskInfoList())) {
                    reportableRiskList.addAll(contract.getRiskInfoList().stream()
                            .map(m -> getReportableRisk(m.getRisk(), m.getAmount())).collect(Collectors.toList()));
                }
                String recipientRisks = NOT_SPECIFIED;
                if (!CollectionUtils.isEmpty(reportableRiskList)) {
                    recipientRisks = reportableRiskList.stream().
                            filter(ReportableRisk::isBenefitsInsured).
                            map(ReportableRisk::getName).
                            collect(Collectors.joining("; "));
                }
                Collections.reverse(contract.getRecipientList());

                String finalRecipientRisks = recipientRisks;
                contract.getRecipientList().stream()
                        .sorted((r1, r2) -> Double.compare(r2.getShare().doubleValue(), r1.getShare().doubleValue()))
                        .map(m -> new ReportableRecipient(
                                ReportableContract.getClientFullName(m.getSurName(), m.getFirstName(), m.getMiddleName()),
                                ReportableContract.presentLocalDate(m.getBirthDate()).concat(", ".concat(ensureNotNull(m.getBirthPlace())).concat(", ".concat(getCountryNameByCode(ensureNotNull(m.getBirthCountry()))))),
                                NOT_SPECIFIED,
                                m.getTaxResidence() != null ? m.getTaxResidence().toString() : contract.getHolder().getTaxResidence().toString(),
                                finalRecipientRisks,
                                String.format("%.2f", m.getShare().doubleValue()))).forEach(reportableRecipientList::add);
            }
        }

        clientCardReport.fieldValues.put("RecipientSubreportDataSource", new JRBeanCollectionDataSource(reportableRecipientList));

        List<Insurance> insurances = new ArrayList<>();
        for (Insurance insurance : allContracts) {
            InsuranceStatusCode type = insurance.getStatus().getCode();
            if (type != InsuranceStatusCode.DRAFT && type != InsuranceStatusCode.PROJECT && type != InsuranceStatusCode.REVOKED &&
                    type != InsuranceStatusCode.REVOKED_REPLACEMENT && type != InsuranceStatusCode.CANCELED && type != InsuranceStatusCode.CANCELED_IN_HOLD_PERIOD &&
                    type != InsuranceStatusCode.CLIENT_REFUSED && type != InsuranceStatusCode.FINISHED) {
                insurances.add(insurance);
            }
        }
        if (insurances.isEmpty()) {
            clientCardReport.fieldValues.put("firstContractDate", "Нет данных о действующих контрактах");
        } else {
            clientCardReport.fieldValues.put("firstContractDate", presentLocalDate(Collections.min(insurances, Comparator.comparing(Insurance::getCreationDate)).getCreationDate()));
        }

        String terroristsListString = NOT_DATA;
        String blockagesListString = NOT_DATA;

        List<ClientCheck> terroristsList = new ArrayList<>();
        List<ClientCheck> blockagesList = new ArrayList<>();
        List<ClientCheck> invalidsDocs = new ArrayList<>();
        checkList.forEach(clientCheck -> {
            if (clientCheck.getCheckType() == CheckUnitTypeEnum.TERRORIST) {
                terroristsList.add(clientCheck);
            }
            if (clientCheck.getCheckType() == CheckUnitTypeEnum.BLOCKAGE) {
                blockagesList.add(clientCheck);
            }
            if (clientCheck.getCheckType() == INVALID_IDENTITY_DOC) {
                invalidsDocs.add(clientCheck);
            }
        });
        if (!CollectionUtils.isEmpty(terroristsList)) {
            int[] idx = {0};
            terroristsList.sort((o1, o2) -> {
                if (o1.getCreationDate() == null || o2.getCreationDate() == null)
                    return 0;
                return o1.getCreationDate().compareTo(o2.getCreationDate());
            });
            terroristsListString = terroristsList.stream().
                    map(m -> String.valueOf(++idx[0]).concat(". Результат проверки: ".concat(resultCheckForTerrorists(m.getCheckResult(), m.getUpdateId(), dictStatusService)
                            .concat(". Дата по результатам проверки : ").concat(presentLocalDate(m.getCreationDate()))))).
                    collect(Collectors.joining(";\n"));
        }
        if (!CollectionUtils.isEmpty(blockagesList)) {
            int[] idx = {0};
            blockagesList.sort((o1, o2) -> {
                if (o1.getCreationDate() == null || o2.getCreationDate() == null)
                    return 0;
                return o1.getCreationDate().compareTo(o2.getCreationDate());
            });
            blockagesListString = blockagesList.stream().
                    map(m -> String.valueOf(++idx[0]).concat(". Результат проверки: ".concat(resultCheckForTerrorists(m.getCheckResult(), m.getUpdateId(), dictStatusService)
                            .concat(". Дата по результатам проверки : ").concat(presentLocalDate(m.getCreationDate()))))).
                    collect(Collectors.joining(";\n"));
        }

        clientCardReport.fieldValues.put("terroristsListString", terroristsListString);
        clientCardReport.fieldValues.put("blockagesListString", blockagesListString);
        clientCardReport.fieldValues.put("invalidDocsString", !invalidsDocs.isEmpty() ? resultCheck(Collections.max(invalidsDocs, Comparator.comparing(ClientCheck::getCheckResult)).getCheckResult()) : NOT_DATA);


        List<LocalDate> checkTime = checkList.stream().filter(clientCheck -> clientCheck.getCheckType() == INVALID_IDENTITY_DOC
                && clientCheck.getCheckResult() == CheckStateEnum.FALSE)
                .map(clientCheck -> clientCheck.getCreationDate().toLocalDate()).collect(Collectors.toList());

        String resultChecksListString = NOT_DATA;
        if (insurances.isEmpty()) {
            clientCardReport.fieldValues.put("resultChecksListString", resultChecksListString);
        } else {
            LocalDate firstDate = Collections.min(insurances, Comparator.comparing(Insurance::getCreationDate)).getCreationDate().toLocalDate();
            List<LocalDate> listModifiedDates = getModifiedDate(checkTime, firstDate);
            if (!CollectionUtils.isEmpty(listModifiedDates)) {
                int[] idx = {0};
                resultChecksListString = listModifiedDates.stream().map(item -> String.valueOf(++idx[0]).concat(". ").concat(item.format(DATE_FORMATTER)))
                        .collect(Collectors.joining("\n"));
            }
            clientCardReport.fieldValues.put("resultChecksListString", resultChecksListString);
        }

        return clientCardReport;
    }

    private String resultCheckForTerrorists(CheckStateEnum checkResult, Long updateId, CacheableDictStatusService dictStatusService) {

        String fileDate = NOT_SPECIFIED;
        try {
            fileDate = dictStatusService.dictDate(updateId);
        } catch (RestClientException | NullPointerException e) {
            LOGGER.error(String.format("Во время получения даты справочника c ID='%s' из сервиса common-dict произошла ошибка, причина: %s", updateId, e), e);
        }

        if (checkResult == CheckStateEnum.TRUE || checkResult == CheckStateEnum.EMPLOYEE_TRUE) {
            return "Идентифицирован по перечню ".concat(fileDate);
        }
        if (checkResult == CheckStateEnum.FALSE || checkResult == CheckStateEnum.EMPLOYEE_FALSE) {
            return "Не идентифицирован по перечню ".concat(fileDate);
        }
        if (checkResult == CheckStateEnum.NEED_EMPLOYEE_DECISION) {
            return "Требуется перепроверка сотрудником";
        }
        return NOT_SPECIFIED;
    }

    @Override
    public Map<String, Object> getFieldValues() {
        return fieldValues;
    }
}
