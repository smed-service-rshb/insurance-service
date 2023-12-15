package ru.softlab.efr.services.insurance.stubs;

import ru.softlab.efr.services.insurance.model.rest.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TestDataExtraModels {

    public static final BaseInsuranceModel INSURANCE_MODEL_COMULATED = new BaseInsuranceModel();

    static {
        INSURANCE_MODEL_COMULATED.setProgramSettingId(12L);
        INSURANCE_MODEL_COMULATED.setStartDate(LocalDate.now());
        INSURANCE_MODEL_COMULATED.setHolderId(1L);
        INSURANCE_MODEL_COMULATED.setInsuredId(1L);
        INSURANCE_MODEL_COMULATED.setHolderEqualsInsured(true);
        INSURANCE_MODEL_COMULATED.setCalendarUnit(CalendarUnit.YEAR);

        List<InsuranceRecipient> recipientListModFour = new ArrayList<>();
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
            recipientListModFour.add(recipient);
        });
        INSURANCE_MODEL_COMULATED.setRecipientList(recipientListModFour);

        INSURANCE_MODEL_COMULATED.setDuration(3);
        INSURANCE_MODEL_COMULATED.setGrowth(80);
        INSURANCE_MODEL_COMULATED.setGuaranteeLevel(new BigDecimal("3.3"));
        INSURANCE_MODEL_COMULATED.setAmount(new BigDecimal(600.00));
        INSURANCE_MODEL_COMULATED.setPremium(new BigDecimal(600.00));
        INSURANCE_MODEL_COMULATED.setRurAmount(new BigDecimal(600.00));
        INSURANCE_MODEL_COMULATED.setRurPremium(new BigDecimal(600.00));
        INSURANCE_MODEL_COMULATED.setCurrencyId(1L);
        INSURANCE_MODEL_COMULATED.setLowerPressure(60);
        INSURANCE_MODEL_COMULATED.setUpperPressure(160);
        INSURANCE_MODEL_COMULATED.setGuaranteeLevel(new BigDecimal(3));
        INSURANCE_MODEL_COMULATED.setRecipientEqualsHolder(false);
        INSURANCE_MODEL_COMULATED.setWeight(80);
        INSURANCE_MODEL_COMULATED.setPeriodicity(PaymentPeriodicity.ONCE);
        INSURANCE_MODEL_COMULATED.setType(FindProgramType.SUM);
    }
}
