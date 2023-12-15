package ru.softlab.efr.services.insurance.model.reportable;

import com.ibm.icu.text.RuleBasedNumberFormat;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.clients.model.AddressType;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.*;
import ru.softlab.ib6.reporting.model.Reportable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportableContract implements Reportable {

    private static final String LOCALE_LANGUAGE = "ru";
    private static final String REGEX_PHONE_FIND_PATTERN = "(\\d)(\\d{3})(\\d{3})(\\d{2})(\\d{2})";
    private static final String REGEX_PHONE_CONVERT_PATTERN = "\\+$1 \\($2\\) $3 $4 $5";
    private static final String REGEX_DIVISION_CODE_FIND_PATTERN = "(\\d{3})(\\d{3})";
    private static final String REGEX_DIVISION_CODE_CONVERT_PATTERN = "$1-$2";

    private static final String NOT_SPECIFIED = "(не указано)";
    private static final String NOT_A_PDL = "(Не является ПДЛ)";

    private static final String type1 = NecessaryRisksEnum.SURVIVAL.getDeathName();
    private static final String type2 = NecessaryRisksEnum.SURVIVAL_TILL.getDeathName();
    private static final String type3 = NecessaryRisksEnum.SURVIVAL_INSURED_TILL.getDeathName();
    private static final Set<InsuranceStatusCode> INSURANCE_STATUS_CODES = Collections.unmodifiableSet(EnumSet.of(InsuranceStatusCode.DRAFT, InsuranceStatusCode.PROJECT, InsuranceStatusCode.CLIENT_REFUSED));
    private Map<String, Object> fieldValues;

    public static ReportableContract construct(Insurance contract, ClientEntity client, Boolean isTestInstance, String currency, BigDecimal amount, BigDecimal premium, List<ReportableRedemption> reportableRedemptionList, String paysDateString) {

        boolean stamp = true;
        boolean sign = true;
        boolean showBackgroundImageIntoOther = false;
        Boolean companyLogo = true;

        if (isTestInstance) {
            stamp = false;
            sign = false;
            showBackgroundImageIntoOther = true;
        }
        if (INSURANCE_STATUS_CODES.contains(contract.getStatus().getCode()) && !isTestInstance) {
            stamp = false;
            sign = false;
            isTestInstance = true;
        }
        ReportableContract reportableContract = new ReportableContract();
        reportableContract.fieldValues = new LinkedHashMap<>();

        reportableContract.fieldValues.put("companyLogo", companyLogo);
        reportableContract.fieldValues.put("sign", sign);
        reportableContract.fieldValues.put("stamp", stamp);
        reportableContract.fieldValues.put("isTestInstance", isTestInstance);
        reportableContract.fieldValues.put("showBackgroundImageIntoOther", showBackgroundImageIntoOther);


        reportableContract.fieldValues.put("contractNumber", contract.getContractNumber() != null ? contract.getContractNumber() : "");
        reportableContract.fieldValues.put("surname", client.getSurName());
        reportableContract.fieldValues.put("name", client.getFirstName());
        reportableContract.fieldValues.put("patronymic", ensureNotNull(client.getMiddleName()));
        reportableContract.fieldValues.put("birthDate", presentLocalDate(client.getBirthDate()));
        reportableContract.fieldValues.put("contractStartDate", presentLocalDate(contract.getStartDate()));
        reportableContract.fieldValues.put("contractSignDate", presentLocalDate(contract.getStartDate()));

        LocalDate contractConclusionDate = getConclusionDate(contract);
        reportableContract.fieldValues.put("contractConclusionDate", presentLocalDate(contractConclusionDate));

        LocalDate cumulativePeriodStart = contractConclusionDate.plusDays(getCoolingPeriod(contract));
        reportableContract.fieldValues.put("cumulativePeriodStart", presentLocalDate(cumulativePeriodStart.plusDays(1)));

        LocalDate cumulativePeriodEnd = getEndDateByCalendarUnit(cumulativePeriodStart, contract.getCalendarUnit(), contract.getDuration());
        reportableContract.fieldValues.put("cumulativePeriodEnd", presentLocalDate(cumulativePeriodEnd));


        String holderResidenceAddress = getAddressNameByType(contract.getHolder().getAddresses(), AddressType.RESIDENCE);
        String holderRegistrationAddress = getAddressNameByType(contract.getHolder().getAddresses(), AddressType.REGISTRATION);

        reportableContract.fieldValues.put("policyHolderIdDocResidenceAddress", ensureNotNull(holderRegistrationAddress));

        ClientEntity clientHolder = Optional.ofNullable(contract.getHolder())
                .orElseGet(ClientEntity::new);

        ReportableDocumentData holderDocumentData = getDocumentData(clientHolder.getDocuments());

        reportableContract.fieldValues.put("policyHolderBirthDate", presentLocalDate(clientHolder.getBirthDate()));
        reportableContract.fieldValues.put("policyHolderGender", Optional.ofNullable(clientHolder.getGender())
                .map(Object::toString)
                .orElse(NOT_SPECIFIED));
        reportableContract.fieldValues.put("policyHolderFullName", getClientFullName(clientHolder));

        reportableContract.fieldValues.put("policyHolderIdDocName", getMainDocNameByDocEntity(clientHolder.getMainDocument()));
        reportableContract.fieldValues.put("policyHolderIdDocSeries", holderDocumentData.getPolicyHolderIdDocSeries());
        reportableContract.fieldValues.put("policyHolderIdDocNumber", holderDocumentData.getPolicyHolderIdDocNumber());
        reportableContract.fieldValues.put("policyHolderIdDocIssuedDate", holderDocumentData.getPolicyHolderIdDocIssuedDate());
        reportableContract.fieldValues.put("policyHolderIdDocIssuedBy", holderDocumentData.getPolicyHolderIdDocIssuedBy());
        reportableContract.fieldValues.put("policyHolderIdDivisionCode", holderDocumentData.getPolicyHolderIdDivisionCode());

        reportableContract.fieldValues.put("policyHolderAddress", isEqualsAndNotEmpty(holderResidenceAddress, holderRegistrationAddress) ? "Соответствует адресу регистрации" : ensureNotNull(holderResidenceAddress));

        reportableContract.fieldValues.put("currency", getCurrencyNameByCode(ensureNotNull(currency)));
        reportableContract.fieldValues.put("currencyForGrids", getCurrencyNameByCodeForGrids(ensureNotNull(currency)));
        reportableContract.fieldValues.put("contractAmount", amount != null ? presentBigDecimal(amount) : NOT_SPECIFIED);
        reportableContract.fieldValues.put("contractAmountString", amount != null ? getRurStringAmount(amount) : NOT_SPECIFIED);
        reportableContract.fieldValues.put("multipliedContractAmount", amount != null ?
                presentBigDecimal(amount.multiply(BigDecimal.valueOf(contract.getDuration()))) : NOT_SPECIFIED);
        reportableContract.fieldValues.put("multipliedContractAmountString", amount != null ?
                getRurStringAmountWithDoubleZero(amount.multiply(BigDecimal.valueOf(contract.getDuration()))) : NOT_SPECIFIED);
        reportableContract.fieldValues.put("getRurStringAmountWithDoubleZero", amount != null ? getRurStringAmountWithDoubleZero(amount) : NOT_SPECIFIED);
        reportableContract.fieldValues.put("contractAmountKSP", amount != null ? presentBigDecimal(contract.getAmount()) : NOT_SPECIFIED);
        reportableContract.fieldValues.put("contractAmountStringKSP", amount != null ? getRurStringAmount(contract.getAmount()) : NOT_SPECIFIED);
        reportableContract.fieldValues.put("contractAmountNewFormatString", amount != null ? getRurStringAmountWithDoubleZero(contract.getAmount()) : NOT_SPECIFIED);
        String periodicity = contract.getPeriodicity() != null ? contract.getPeriodicity().toString() : NOT_SPECIFIED;
        reportableContract.fieldValues.put("periodicity", periodicity);
        reportableContract.fieldValues.put("contractPremium", premium != null ? presentBigDecimal(premium) : NOT_SPECIFIED);
        reportableContract.fieldValues.put("contractPremiumString", premium != null ? getRurStringAmount(premium) : NOT_SPECIFIED);
        reportableContract.fieldValues.put("contractPremiumStringForKSP", premium != null ? getRurStringAmountWithDoubleZero(premium) : NOT_SPECIFIED);

        /*
         * payPeriodDate -
         * Акимов Владислав Николаевич
         * Число и месяц, дата определяется как дата перевода в статус оформлен
         * + период охлаждения + 1 день (например, перевели в статус оформлен 01.01. тогда печатаем дату 16.01 (01 + 14 + 1))
         * */
        LocalDate payPeriodDate = contract.getCreationDate().plusDays(getCoolingPeriod(contract)).plusDays(1).toLocalDate();
        String payPeriodDateString = presentLocalDate(payPeriodDate, "dd.MM");
        String payPeriodStartDate = presentLocalDate(payPeriodDate);

        reportableContract.fieldValues.put("payPeriodDate", payPeriodDateString);
        reportableContract.fieldValues.put("payPeriodStartDate", payPeriodStartDate);
        reportableContract.fieldValues.put("payPeriodEndDate", /*TODO: уточнить дату последнего платежа */NOT_SPECIFIED);
        reportableContract.fieldValues.put("contractEndDate", getContractEndDate(contract));
        String insuranceTerm = contract.getDuration() != null ? contract.getDuration().toString() : NOT_SPECIFIED;
        reportableContract.fieldValues.put("insuranceTerm", insuranceTerm);
        String calendarUnit = contract.getCalendarUnit() != null ? contract.getCalendarUnit().toString() : NOT_SPECIFIED;
        reportableContract.fieldValues.put("calendarUnit", calendarUnit);
        reportableContract.fieldValues.put("phoneNumber", getClientMainPhone(contract.getHolder()));

        Strategy strategy = getStrategy(contract);
        reportableContract.fieldValues.put("email", ensureNotNull(client.getEmail()));
        reportableContract.fieldValues.put("contractCurrency", getCurrencyNameByCode(ensureNotNull(currency)));
        reportableContract.fieldValues.put("contractCurrencyIsUSD", currency != null && currency.equalsIgnoreCase("usd"));
        reportableContract.fieldValues.put("coolingPeriodString", getCoolingPeriodString(getCoolingPeriod(contract)));
        reportableContract.fieldValues.put("expirationDate", getContractEndDate(contract));
        ClientEntity clientInsured = contract.getHolderEqualsInsured() ? contract.getHolder() : contract.getInsured();
        reportableContract.fieldValues.put("insuranceYearCountString", getYearCountString(contract.getDuration()));
        reportableContract.fieldValues.put("insuranceDurationCountString", getDurationCountString(contract.getDuration()));


        String programName = contract.getProgramSetting() != null
                && contract.getProgramSetting().getProgram() != null
                && contract.getProgramSetting().getProgram().getName() != null
                ? contract.getProgramSetting().getProgram().getName()
                : NOT_SPECIFIED;
        reportableContract.fieldValues.put("programName", programName);

        if (programName.contains(".")) {
            int index = programName.indexOf('.');
            reportableContract.fieldValues.put("shortProgramName", programName.substring(index + 1).trim());
        }
        reportableContract.fieldValues.put("holderEqualsInsured", contract.getHolderEqualsInsured());
        reportableContract.fieldValues.put("policyHolderFullName", getClientFullName(contract.getHolder()));
        reportableContract.fieldValues.put("policyHolderShortName", getClientShortName(clientHolder));
        reportableContract.fieldValues.put("insuredShortName", getClientShortName(clientInsured));

        reportableContract.fieldValues.put("strategyName", strategy == null ? NOT_SPECIFIED : strategy.getName());
        reportableContract.fieldValues.put("strategyDescription", strategy == null ? NOT_SPECIFIED : ensureNotNull(strategy.getDescription()));


        reportableContract.fieldValues.put("currentDate", presentLocalDate(LocalDate.now(), "«dd» MMMM yyyy"));
        reportableContract.fieldValues.put("serviceName", ensureNotNull(getServiceNameForContractType(contract.getProgramSetting().getProgram().getType())));

        reportableContract.fieldValues.put("formattedConclusionDate", presentLocalDate(contractConclusionDate, "«dd» MMMM yyyy"));

        return reportableContract;
    }

    private static ReportableDocumentData getDocumentData(List<DocumentForClientEntity> documentForClientEntities) {
        ReportableDocumentData response = new ReportableDocumentData();

        Optional<DocumentForClientEntity> idDocClient = documentForClientEntities.stream()
                .filter(f -> f.isActive() && f.isMain())
                .findFirst();

        response.setPolicyHolderIdDocSeries(idDocClient
                .map(DocumentForClientEntity::getDocSeries)
                .orElse(NOT_SPECIFIED));
        response.setPolicyHolderIdDocNumber(idDocClient
                .map(DocumentForClientEntity::getDocNumber)
                .orElse(NOT_SPECIFIED));
        response.setPolicyHolderIdDocIssuedDate(idDocClient
                .map(DocumentForClientEntity::getIssuedDate)
                .map(ReportableContract::presentLocalDate)
                .orElse(NOT_SPECIFIED));
        response.setPolicyHolderIdDocIssuedBy(idDocClient
                .map(DocumentForClientEntity::getIssuedBy)
                .orElse(NOT_SPECIFIED));
        response.setPolicyHolderIdDivisionCode(idDocClient
                .map(DocumentForClientEntity::getDivisionCode)
                .map(ReportableContract::presentDivisionCode)
                .orElse(NOT_SPECIFIED));

        return response;
    }

    public static String getMainDocNameByDocEntity(DocumentForClientEntity mainDocument) {
        return Optional.ofNullable(mainDocument.getDocType())
                .map(IdentityDocTypeEnum::toString)
                .orElse(NOT_SPECIFIED);
    }

    private static String getDurationCountString(Integer duration) {
        return duration.toString().concat(" ".concat(getMonthString(duration)));
    }

    private static String getServiceNameForContractType(ProgramKind type) {
        if (type == ProgramKind.ISJ) {
            return "Заключение Договора страхования по программе инвестиционного страхования жизни";
        }
        if (type == ProgramKind.RENT) {
            return "Заключение Договора страхования жизни «Накопи на пенсию»";
        }
        return NOT_SPECIFIED;
    }


    public static ReportableRisk getReportableRisk(Risk risk, BigDecimal amount) {
        return new ReportableRisk(risk.getName().trim(),
                String.format("%s (риск «%s»)", risk.getFullName().trim(),
                        risk.getName().trim()),
                presentBigDecimal(amount),
                getRurStringAmount(amount),
                isBenefitsInsured(risk));
    }

    private static Integer getPayTermByContract(Insurance insuranceContract) {
        return insuranceContract.getPaymentTerm() != null ? insuranceContract.getPaymentTerm() : insuranceContract.getProgramSetting().getPaymentTerm();
    }

    public static String getMainDocNameByClient(DocumentForClientEntity mainDocument) {
        return mainDocument.getDocType() != null ?
                Arrays.stream(IdentityDocTypeEnum.values()).anyMatch(m -> m.name().equalsIgnoreCase(String.valueOf(mainDocument.getDocType()))) ?
                        mainDocument.getDocType().toString().trim() : String.valueOf(mainDocument.getDocType()).trim() : NOT_SPECIFIED;
    }

    public static String getCountryNameByCode(String code) {
        if (code != null && !code.isEmpty()
                && (code.equalsIgnoreCase("russia") || code.equalsIgnoreCase("russian"))) {
            return "Российская Федерация";
        }
        return code;
    }

    private static RiskSetting getRiskSettingByRiskName(List<RiskSetting> riskSettingList, String name) {
        if (CollectionUtils.isEmpty(riskSettingList)) {
            return null;
        }
        return riskSettingList.stream()
                .filter(f -> f.getRisk().getName().contains(name)).findFirst().orElse(null);
    }

    private static boolean isMadeRent(Insurance insuranceContract) {
        return insuranceContract != null
                && insuranceContract.getProgramSetting() != null
                && insuranceContract.getProgramSetting().getProgram() != null
                && insuranceContract.getProgramSetting().getProgram().getType() != null
                && insuranceContract.getProgramSetting().getProgram().getType().equals(ProgramKind.RENT)
                && insuranceContract.getStatus() != null
                && insuranceContract.getStatus().getCode() != null
                && insuranceContract.getStatus().getCode().equals(InsuranceStatusCode.MADE);
    }

    private static boolean isBenefitsInsured(Risk riskEntity) {
        return riskEntity != null && riskEntity.getBenefitsInsured() != null && riskEntity.getBenefitsInsured();
    }

    public static String ensureNotNull(String inputString) {
        return isNotEmpty(inputString) ? inputString : NOT_SPECIFIED;
    }

    private static Boolean isEqualsAndNotEmpty(String residenceAddress, String registrationAddress) {
        return isNotEmpty(residenceAddress) && residenceAddress.equals(registrationAddress);
    }

    private static Boolean isNotEmpty(String inputString) {
        return inputString != null && !inputString.trim().isEmpty();
    }

    private static Strategy getStrategy(Insurance contract) {
        if (contract.getStrategy() == null || contract.getProgramSetting() == null || CollectionUtils.isEmpty(contract.getProgramSetting().getStrategyList())) {
            return null;
        }
        return contract.getStrategy();
    }


    public static Boolean isInternalCitizen(ClientEntity client) {
        if (client.getResident() == null) {
            return false;
        }
        return client.getResident().equalsIgnoreCase("russian")
                || client.getResident().equalsIgnoreCase("russia")
                || client.getResident().equalsIgnoreCase("российская федерация")
                || client.getResident().equalsIgnoreCase("россия");
    }


    private static String getCurrencyNameByCodeForGrids(String currency) {
        if (currency.equalsIgnoreCase("rub")) {
            return "рубли РФ";
        }
        if (currency.equalsIgnoreCase("usd")) {
            return "доллары США";
        }
        return currency;
    }

    private static String getCurrencyNameByCode(String currency) {
        if (currency.equalsIgnoreCase("rub")) {
            return "РУБ.";
        }
        return currency;
    }

    public static boolean isEmptyMainDoc(ClientEntity client) {
        return client.getDocuments().stream().noneMatch(f -> f.isActive() && f.isMain() && f.isIdentity());
    }


    private static String getStringByDoc(DocumentForClientEntity document) {
        if (document == null) {
            return NOT_SPECIFIED;
        }
        StringJoiner joiner = new StringJoiner(", ");
        joiner.add("Серия: ".concat(ensureNotNull(document.getDocSeries())));
        joiner.add("№: ".concat(ensureNotNull(document.getDocSeries())));
        joiner.add("дата выдачи: ".concat(presentLocalDate(document.getIssuedDate())));
        joiner.add("срок действия: ".concat(presentLocalDate(document.getIssuedEndDate())));
        joiner.add("кем выдан: ".concat(ensureNotNull(document.getIssuedBy())));
        return joiner.toString();
    }

    public static DocumentForClientEntity getDocByType(ClientEntity client, IdentityDocTypeEnum type) {
        return client.getDocuments().stream()
                .filter(item -> item.isActive()
                        && item.getDocType().name().equalsIgnoreCase(type.name()))
                .findFirst().orElse(null);
    }

    public static DocumentForClientEntity getDocByTypeWithoutActiveCheck(ClientEntity client, IdentityDocTypeEnum type) {
        return client.getDocuments().stream()
                .filter(item -> item.getDocType().equals(type))
                .findFirst().orElse(null);
    }

    private static String getClientMainPhone(ClientEntity client) {
        String phoneString = "";
        if (!CollectionUtils.isEmpty(client.getPhones())) {
            PhoneForClaimEntity phone = client.getPhones()
                    .stream()
                    .filter(PhoneForClaimEntity::isMain)
                    .findFirst()
                    .orElse(client.getPhones()
                            .stream()
                            .findFirst()
                            .get());
            phoneString = phone.getNumber();
        } else {
            phoneString = NOT_SPECIFIED;
        }
        return presentInternationalPhone(phoneString);
    }

    private static String presentInternationalPhone(String phone) {
        return phone.replaceAll(REGEX_PHONE_FIND_PATTERN, REGEX_PHONE_CONVERT_PATTERN).replaceAll("\\+{2,9}", "+");
    }

    public static String presentDivisionCode(String divisionCode) {
        return divisionCode.replaceAll(REGEX_DIVISION_CODE_FIND_PATTERN, REGEX_DIVISION_CODE_CONVERT_PATTERN);
    }

    public static String getClientFullName(ClientEntity client) {
        return getClientFullName(client.getSurName(), client.getFirstName(), client.getMiddleName());
    }

    public static String getClientFullName(String surName, String firstName, String middleName) {
        if (StringUtils.isBlank(surName)) {
            surName = "";
        }
        if (StringUtils.isBlank(firstName)) {
            firstName = "";
        }
        return surName.concat(" ")
                .concat(firstName)
                .concat(StringUtils.isNotBlank(middleName) ? " ".concat(middleName) : "");
    }

    private static String getClientShortName(ClientEntity client) {
        return client.getSurName().concat(" ")
                .concat(client.getFirstName().substring(0, 1).concat("."))
                .concat(client.getMiddleName() != null && !client.getMiddleName().trim().isEmpty() ? client.getMiddleName().substring(0, 1).concat(".") : "");
    }

    public static AddressForClientEntity getAddressByType(List<AddressForClientEntity> addresses, AddressType addressType) {
        if (!CollectionUtils.isEmpty(addresses)) {
            Collections.reverse(addresses);
            Optional<AddressForClientEntity> address = addresses.stream().filter(f -> addressType.name().equals(f.getAddressType().name()) &&
                    (f.getRegistrationPeriodEnd() == null || f.getRegistrationPeriodEnd().isAfter(LocalDate.now()))).findFirst();
            if (address.isPresent()) {
                return address.get();
            } else {
                return null;
            }
        }
        return null;
    }

    public static String getAddressNameByType(List<AddressForClientEntity> addresses, AddressType addressType) {
        AddressForClientEntity clientAddress = getAddressByType(addresses, addressType);
        return clientAddress != null ? clientAddress.getAddress() : NOT_SPECIFIED;
    }

    private static String getYearCountString(Integer contractTerm) {
        return contractTerm.toString().concat(" (".concat(getStringAmount(BigDecimal.valueOf(contractTerm))).concat(")")).concat(" ".concat(getYearString(contractTerm)));
    }

    private static String getCoolingPeriodString(Integer coolingPeriod) {
        return coolingPeriod.toString()
                .concat(String.format(" (%s) %s %s",
                        getStringAmount(BigDecimal.valueOf(coolingPeriod)),
                        getTermKindString(coolingPeriod),
                        getDayString(coolingPeriod)
                ));
    }

    private static String getTermKindString(int digit) {
        return getIntString(digit, Arrays.asList("календарных", "календарный", "календарных", "календарных"));
    }

    private static String getYearString(int digit) {
        return getIntString(digit, Arrays.asList("лет", "год", "года", "лет"));
    }

    private static String getMonthString(int digit) {
        return getIntString(digit, Arrays.asList("месяцев", "месяц", "месяца", "месяцев"));
    }

    private static String getDayString(int digit) {
        return getIntString(digit, Arrays.asList("дней", "день", "дня", "дней"));
    }

    private static String getRubString(int digit) {
        return getIntString(digit, Arrays.asList("рублей", "рубль", "рубля", "рублей"));
    }

    private static String getCopString(int digit) {
        return getIntString(digit, Arrays.asList("копеек", "копейка", "копейки", "копеек"));
    }

    private static String getIntString(int digit, List<String> strings) {
        int preLastDigit = digit % 100 / 10;
        if (preLastDigit == 1) {
            return strings.get(0);
        }
        switch (digit % 10) {
            case 1:
                return strings.get(1);
            case 2:
            case 3:
            case 4:
                return strings.get(2);
            default:
                return strings.get(3);
        }
    }

    public static ReportableContract emptyContract() {
        ReportableContract reportableContract = new ReportableContract();
        reportableContract.fieldValues = new LinkedHashMap<>();
        reportableContract.fieldValues.put("contractNumber", "");
        reportableContract.fieldValues.put("isTestInstance", true);
        reportableContract.fieldValues.put("surname", "");
        reportableContract.fieldValues.put("name", "");
        reportableContract.fieldValues.put("patronymic", "");
        reportableContract.fieldValues.put("birthDate", "");
        reportableContract.fieldValues.put("contractStartDate", "");
        reportableContract.fieldValues.put("phoneNumber", "");
        reportableContract.fieldValues.put("email", "");
        return reportableContract;
    }

    private static String getBirthAddress(String country, String region, String area, String place) {
        StringJoiner resultAddress = new StringJoiner(", ");
        if (isNotEmpty(country)) {
            resultAddress.add(getCountryNameByCode(country));
        }
        if (isNotEmpty(region)) {
            resultAddress.add(region);
        }
        if (isNotEmpty(area)) {
            resultAddress.add(area);
        }
        if (isNotEmpty(place)) {
            resultAddress.add(place);
        }
        return resultAddress.toString();
    }

    public static String presentBigDecimal(BigDecimal amount) {
        if (amount == null) {
            return NOT_SPECIFIED;
        }
        return String.format("%.2f", amount.doubleValue());
    }

    private static int getCoolingPeriod(Insurance contract) {
        if (contract != null
                && contract.getProgramSetting() != null
                && contract.getProgramSetting().getProgram() != null
                && contract.getProgramSetting().getProgram().getCoolingPeriod() != null) {
            return contract.getProgramSetting().getProgram().getCoolingPeriod();
        }
        return 0;
    }

    public static String presentLocalDate(LocalDate localDate) {
        return presentLocalDate(localDate, "dd.MM.yyyy");
    }

    public static String presentLocalDate(LocalDate localDate, String pattern) {
        if (localDate == null) {
            return NOT_SPECIFIED;
        }
        return localDate.format(DateTimeFormatter.ofPattern(pattern).withLocale(new Locale(LOCALE_LANGUAGE)));
    }

    public static String presentLocalDateTime(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) {
            return NOT_SPECIFIED;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern).withLocale(new Locale(LOCALE_LANGUAGE)));
    }

    public static String getRurStringAmount(BigDecimal amount) {
        if (amount == null) {
            return NOT_SPECIFIED;
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal integerPart = amount.setScale(0, RoundingMode.FLOOR);
        String rub = getStringAmount(integerPart).concat(" ").concat(getRubString(integerPart.intValue()));
        BigDecimal fractionalPart = amount.subtract(integerPart).multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.FLOOR);
        String cop = getStringAmount(fractionalPart).concat(" ").concat(getCopString(fractionalPart.intValue()));
        return rub.concat(" ").concat(cop);
    }

    public static String getRurStringAmountWithDoubleZero(BigDecimal amount) {
        String cop;
        if (amount == null) {
            return NOT_SPECIFIED;
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal integerPart = amount.setScale(0, RoundingMode.FLOOR);
        String amountString = getStringAmount(integerPart);
        String rub = amountString.substring(0, 1).toUpperCase() + amountString.substring(1).concat(" ").concat(getRubString(integerPart.intValue()));
        BigDecimal fractionalPart = amount.subtract(integerPart).multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.FLOOR);
        cop = fractionalPart.intValue() == 0 ? "00".concat(" ").concat(getCopString(fractionalPart.intValue()))
                : getStringAmount(fractionalPart).concat(" ").concat(getCopString(fractionalPart.intValue()));
        return rub.concat(" ").concat(cop);
    }


    public static String getStringAmount(BigDecimal amount) {
        if (amount == null) {
            return NOT_SPECIFIED;
        }
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag(LOCALE_LANGUAGE), RuleBasedNumberFormat.SPELLOUT);
        return nf.format(amount);
    }

    public static LocalDate getPayStart(Insurance insuranceContract) {
        /*
        Если ежегодный, то дата и месяц рассчитываются как:  дата заключения плюс период охлаждения плюс 1 день.
        Если раз в пол года, то: первая дата: дата заключения + период охлаждения + 1 день + 6 месяцев, вторая дата: первая дата + 6 месяцев.
    * */
        LocalDate payStartDate = getConclusionDate(insuranceContract);

        if (insuranceContract.getPeriodicity().equals(PeriodicityEnum.YEARLY)) {
            payStartDate = payStartDate.plusDays(getCoolingPeriod(insuranceContract)).plusDays(1);
        }
        if (insuranceContract.getPeriodicity().equals(PeriodicityEnum.TWICE_A_YEAR)) {
            payStartDate = payStartDate.plusDays(getCoolingPeriod(insuranceContract)).plusDays(1).plusMonths(6);
        }
        return payStartDate;
    }

    private static LocalDate getConclusionDate(Insurance insuranceContract) {
        return insuranceContract.getConclusionDate() != null ? insuranceContract.getConclusionDate() : LocalDate.now();
    }

    private static String getContractEndDate(Insurance insuranceContract) {
        return presentLocalDate(getEndDateByCalendarUnit(
                getConclusionDate(insuranceContract).plusDays(getCoolingPeriod(insuranceContract)),
                insuranceContract.getCalendarUnit(),
                insuranceContract.getDuration()));
    }


    private static LocalDate getEndDateByCalendarUnit(LocalDate startDate, CalendarUnitEnum calendarUnitEnum, int duration) {
        if (startDate == null) {
            return null;
        }
        if (calendarUnitEnum == null || calendarUnitEnum.equals(CalendarUnitEnum.YEAR)) {
            return startDate.plusYears(duration);
        } else if (calendarUnitEnum.equals(CalendarUnitEnum.MONTH)) {
            return startDate.plusMonths(duration);
        } else if (calendarUnitEnum.equals(CalendarUnitEnum.DAY)) {
            return startDate.plusDays(duration);
        }
        return null;
    }

    @Override
    public Map<String, Object> getFieldValues() {
        return fieldValues;
    }

}
