package ru.softlab.efr.services.insurance.stubs;

import org.springframework.beans.BeanUtils;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class SmsTestData {

    public static final Program PROGRAM_SMS_1;
    public static final ProgramSetting PROGRAM_SETTING_SMS_1;
    public static final BaseInsuranceModel INSURANCE_SMS_MODEL_1 = new BaseInsuranceModel();
    public static final SetStatusInsuranceModel CHANGE_STATUS_INSURANCE_SMS_MODEL_1 = new SetStatusInsuranceModel();

    static {
        PROGRAM_SMS_1 = new Program();
        PROGRAM_SMS_1.setId(1L);
        PROGRAM_SMS_1.setName("Медсоветник. Индивидуальный");
        PROGRAM_SMS_1.setType(ProgramKind.SMS);
        PROGRAM_SMS_1.setNumber("001");
        PROGRAM_SMS_1.setDeleted(false);

        PROGRAM_SETTING_SMS_1 = new ProgramSetting();
        PROGRAM_SETTING_SMS_1.setId(1L);
        PROGRAM_SETTING_SMS_1.setProgram(PROGRAM_SMS_1);
        PROGRAM_SETTING_SMS_1.setStartDate(new Timestamp(new Date().getTime()));


        // Тестовый договор страхования. Набор параметров страхования с id=1.
        INSURANCE_SMS_MODEL_1.setProgramSettingId(PROGRAM_SETTING_SMS_1.getId());
        INSURANCE_SMS_MODEL_1.setStartDate(LocalDate.now());
        INSURANCE_SMS_MODEL_1.setHolderId(1L);
        INSURANCE_SMS_MODEL_1.setInsuredId(1L);
        INSURANCE_SMS_MODEL_1.setHolderEqualsInsured(true);
        INSURANCE_SMS_MODEL_1.setCalendarUnit(CalendarUnit.MONTH);

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
        INSURANCE_SMS_MODEL_1.setRecipientList(recipientList);

        INSURANCE_SMS_MODEL_1.setDuration(3);
        INSURANCE_SMS_MODEL_1.setGrowth(80);
        INSURANCE_SMS_MODEL_1.setGuaranteeLevel(new BigDecimal("3.3"));
        INSURANCE_SMS_MODEL_1.setAmount(new BigDecimal(200000.0));
        INSURANCE_SMS_MODEL_1.setPremium(new BigDecimal(20000.0));
        INSURANCE_SMS_MODEL_1.setRurAmount(new BigDecimal(200000.0));
        INSURANCE_SMS_MODEL_1.setRurPremium(new BigDecimal(20000.0));
        INSURANCE_SMS_MODEL_1.setCurrencyId(1L);
        INSURANCE_SMS_MODEL_1.setLowerPressure(60);
        INSURANCE_SMS_MODEL_1.setUpperPressure(160);
        INSURANCE_SMS_MODEL_1.setGuaranteeLevel(new BigDecimal(3));
        INSURANCE_SMS_MODEL_1.setRecipientEqualsHolder(false);
        INSURANCE_SMS_MODEL_1.setWeight(80);
        INSURANCE_SMS_MODEL_1.setPeriodicity(PaymentPeriodicity.ONCE);
        INSURANCE_SMS_MODEL_1.setType(FindProgramType.SUM);

        BeanUtils.copyProperties(INSURANCE_SMS_MODEL_1, CHANGE_STATUS_INSURANCE_SMS_MODEL_1);
        CHANGE_STATUS_INSURANCE_SMS_MODEL_1.setNewStatus(InsuranceStatusType.CRM_IMPORTED);
    }
}
