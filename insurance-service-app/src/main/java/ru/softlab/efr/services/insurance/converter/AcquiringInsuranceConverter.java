package ru.softlab.efr.services.insurance.converter;

import org.springframework.stereotype.Service;
import ru.softlab.efr.common.dict.exchange.model.Currency;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.insurance.model.db.AcquiringProgram;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.CalendarUnitEnum;
import ru.softlab.efr.services.insurance.model.enums.GenderTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.IdentityDocTypeEnum;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.services.AcquiringProgramService;
import ru.softlab.efr.services.insurance.services.CurrencyCachedClientService;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Конвертер преобразования представлений сущности договора оформленного клиентом
 */
@Service
public class AcquiringInsuranceConverter {

    private final AcquiringProgramService acquiringProgramService;
    private final CurrencyCachedClientService currencyCachedService;

    public AcquiringInsuranceConverter(AcquiringProgramService acquiringProgramService, CurrencyCachedClientService currencyCachedService) {
        this.acquiringProgramService = acquiringProgramService;
        this.currencyCachedService = currencyCachedService;
    }

    /**
     * Преобразование ответа с данными о программе для договора
     *
     * @param info данные о процессе оформления
     * @return ответ с данными о программе в представлении API
     */
    public AcquiringInsuranceRs convert(AcquiringInfo info) throws RestClientException {
        AcquiringProgram acquiringProgram = info.getAcquiringProgram();
        ProgramSetting programSetting = acquiringProgram != null ?
                acquiringProgram.getProgram() : info.getInsurance().getProgramSetting();
        AcquiringInsuranceRs rs = new AcquiringInsuranceRs();
        rs.setProgramName(programSetting.getProgram().getNameForPrint());
        rs.setProgramKind(ProgramKind.valueOf(programSetting.getProgram().getType().name()));
        Currency currency = currencyCachedService.getById(programSetting.getCurrency());
        if (currency == null) {
            throw new EntityNotFoundException();
        }
        if (acquiringProgram != null) {
            rs.setProgramId(info.getAcquiringProgram().getId());
            rs.setImage(info.getAcquiringProgram().getImage().getId());
        }
        rs.setCurrencyIso(currency.getLiteralISO());
        rs.setPaymentAmount(programSetting.getBonusAmount());
        rs.setInsuranceAmount(programSetting.getInsuranceAmount());
        rs.setInsurancePremium(programSetting.getBonusAmount());
        rs.setDateCreate(LocalDate.now());
        rs.setStartDate(LocalDate.now().plusDays(1).plusDays(programSetting.getProgram().getCoolingPeriod()));
        rs.setEndDate(calculateEndDate(programSetting));
        rs.setRisks(programSetting.getRequiredRiskSettingList().stream().map(riskSetting -> riskSetting.getRisk().getName()).collect(Collectors.toList()));
        ShortClientData clientData = new ShortClientData();
        clientData.setId(info.getClientId());
        clientData.setSurName(info.getSurName());
        clientData.setFirstName(info.getFirstName());
        clientData.setMiddleName(info.getMiddleName());
        clientData.setGender(Gender.valueOf(info.getGender().name()));
        clientData.setDocNumber(info.getDocNumber());
        clientData.setDocSeries(info.getDocSeries());
        clientData.setPhoneNumber(info.getPhone());
        clientData.setEmail(info.getEmail());
        clientData.setBirthDate(info.getBirthDate());
        rs.setInsured(clientData);
        rs.setAddress(info.getAddress());
        rs.setUuid(info.getUuid());
        if (info.getInsurance() != null) {
            rs.setInsuranceId(info.getInsurance().getId());
            rs.setInsuranceNumber(info.getInsurance().getContractNumber());
            rs.setInsuranceStatus(InsuranceStatusType.valueOf(info.getInsurance().getStatus().getCode().name()));
            rs.setPaymentAmount(info.getInsurance().getPremium());
            rs.setInsuranceAmount(info.getInsurance().getAmount());
            rs.setInsurancePremium(info.getInsurance().getPremium());
        }
        return rs;
    }

    private LocalDate calculateEndDate(ProgramSetting programSetting) {
//        •Дата окончания действия, которая рассчитывается как текущая дата плюс период охлаждения, заданный для данной
//        программы страхования в настройках Системы, плюс срок действия программы страхования (задан в настройках Системы).

        LocalDate date = LocalDate.now().plusDays(programSetting.getProgram().getCoolingPeriod());
        CalendarUnitEnum calendarUnit = programSetting.getCalendarUnit();
        Integer duration = programSetting.getMinimumTerm();
        if (duration == null) return date;
        switch (calendarUnit) {
            case DAY:
                return date.plusDays(duration);
            case MONTH:
                return date.plusMonths(duration);
            case YEAR:
                return date.plusYears(duration);
            default:
                return date;
        }
    }

    /**
     * Преобразование запроса на создание договора в ЛК в обычный запрос создания страховки
     *
     * @param request запрос
     * @param info    информация о процессе
     * @return базовая модель
     */
    public BaseInsuranceModel convertToBaseInsuranceModel(AcquiringInsuranceRq request, AcquiringInfo info) {
        ProgramSetting programSetting = info.getAcquiringProgram().getProgram();
        BaseInsuranceModel model = new BaseInsuranceModel();
        model.setPeriodicity(PaymentPeriodicity.valueOf(programSetting.getPeriodicity().name()));
        model.setCalendarUnit(CalendarUnit.valueOf(programSetting.getCalendarUnit().name()));
        model.setDuration(programSetting.getMinimumTerm());
        model.setCurrencyId(programSetting.getCurrency());
        model.setType(FindProgramType.SUM);
        model.setAmount(programSetting.getMinSum());
        model.setPremium(programSetting.getMinPremium());
        model.setProgramSettingId(programSetting.getId());
        model.setHolderEqualsInsured(true);
        model.setStartDate(LocalDate.now().plusDays(1).plusDays(programSetting.getProgram().getCoolingPeriod()));
        InsuranceRecipient recipient = new InsuranceRecipient();
        model.setRecipientList(Collections.singletonList(recipient));
        recipient.setSurName(request.getSurName().trim());
        recipient.setFirstName(request.getFirstName().trim());
        recipient.setMiddleName(request.getMiddleName().trim());
        recipient.setBirthDate(request.getBirthDate());
        recipient.setShare(BigDecimal.valueOf(100));
        Client holderData = new Client();
        model.setHolderId(request.getId());
        holderData.setSurName(request.getSurName());
        holderData.setFirstName(request.getFirstName());
        holderData.setMiddleName(request.getMiddleName());
        holderData.setBirthDate(request.getBirthDate());
        holderData.setEmail(request.getEmail());
        holderData.setGender(Gender.valueOf(info.getGender().name()));
        holderData.setCitizenship(CitizenshipType.RUSSIAN);
        holderData.setTaxResidence(TaxResidenceType.RUSSIAN);
        holderData.setPublicOfficialStatus(PublicOfficialType.NONE);
        holderData.setBusinessRelations("Долгосрочные");
        holderData.setActivitiesGoal("Страхование жизни");
        holderData.setBusinessRelationsGoal("Страхование жизни");
        holderData.setRiskLevelDesc("Низкий. Нет критериев для присвоения иного уровня риска");
        holderData.setBusinessReputation("Устойчивая");
        holderData.setFinancialStability("Устойчивая");
        holderData.setFinancesSource("Личные накопления");
        List<Document> documents = new ArrayList<>();
        holderData.setDocuments(documents);
        Phone phone = new Phone();
        phone.setMain(true);
        phone.setNumber(request.getPhoneNumber());
        phone.setType(PhoneType.MOBILE);
        holderData.setPhones(Collections.singletonList(phone));
        holderData.setBeneficialOwner(String.format("%s %s %s", request.getSurName().trim(), request.getFirstName().trim(), request.getMiddleName()).trim());
        model.setHolderData(holderData);

        return model;
    }

    public AcquiringInfo convertInfo(AcquiringProgram acquiringProgram, AcquiringInsuranceRq rq, Long clientId) {
        AcquiringInfo info = new AcquiringInfo();
        info.setAcquiringProgram(acquiringProgram);
        info.setSurName(rq.getSurName());
        info.setClientId(clientId);
        info.setFirstName(rq.getFirstName());
        info.setMiddleName(rq.getMiddleName());
        info.setGender(GenderTypeEnum.valueOf(rq.getGender().name()));
        info.setDocNumber(rq.getDocNumber());
        info.setDocSeries(rq.getDocSeries());
        info.setPhone(rq.getPhoneNumber());
        info.setEmail(rq.getEmail());
        info.setBirthDate(rq.getBirthDate());
        info.setAddress(rq.getAddress());
        return info;
    }

    public AcquiringInfo convertInfo(AcquiringProgram acquiringProgram, ClientEntity clientEntity) {
        AcquiringInfo info = new AcquiringInfo();
        info.setAcquiringProgram(acquiringProgram);
        info.setSurName(clientEntity.getSurName());
        info.setClientId(clientEntity.getId());
        info.setFirstName(clientEntity.getFirstName());
        info.setMiddleName(clientEntity.getMiddleName());
        info.setGender(clientEntity.getGender());
        if (clientEntity.getDocuments() != null) {
            clientEntity.getDocuments().stream()
                    .filter(document -> document.getDocType() == IdentityDocTypeEnum.PASSPORT_RF)
                    .forEach(document -> {
                        info.setDocNumber(document.getDocNumber());
                        info.setDocSeries(document.getDocSeries());
                    });
        }
        clientEntity.getPhones().stream()
                .filter(PhoneForClaimEntity::isMain)
                .forEach(phone -> info.setPhone(phone.getNumber()));
        info.setEmail(clientEntity.getEmail());
        info.setBirthDate(clientEntity.getBirthDate());
        return info;
    }

    public AcquiringInfo convertInfo(Insurance insurance, boolean isAuthorized) {
        ClientEntity clientEntity = insurance.getHolder();
        List<AcquiringProgram> programs = acquiringProgramService.findByProgram(insurance.getProgramSetting(), isAuthorized);

        AcquiringInfo info = new AcquiringInfo();
        if (!programs.isEmpty()) {
            info.setAcquiringProgram(programs.get(0));
        }

        info.setSurName(clientEntity.getSurName());
        info.setClientId(clientEntity.getId());
        info.setFirstName(clientEntity.getFirstName());
        info.setMiddleName(clientEntity.getMiddleName());
        info.setGender(clientEntity.getGender());
        info.setInsurance(insurance);
        if (clientEntity.getDocuments() != null) {
            clientEntity.getDocuments().stream()
                    .filter(document -> document.getDocType() == IdentityDocTypeEnum.PASSPORT_RF)
                    .forEach(document -> {
                        info.setDocNumber(document.getDocNumber());
                        info.setDocSeries(document.getDocSeries());
                    });
        }
        clientEntity.getPhones().stream()
                .filter(PhoneForClaimEntity::isMain)
                .forEach(phone -> info.setPhone(phone.getNumber()));
        info.setEmail(clientEntity.getEmail());
        info.setBirthDate(clientEntity.getBirthDate());
        return info;
    }
}
