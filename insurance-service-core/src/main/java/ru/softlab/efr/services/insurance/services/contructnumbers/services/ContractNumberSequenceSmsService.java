package ru.softlab.efr.services.insurance.services.contructnumbers.services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Сервис счётчика номера договора для страховых проектов недвижимости и средств
 *
 * @author olshansky
 * @since 19.12.2018
 */
@Service
public class ContractNumberSequenceSmsService {

    private static final Logger LOGGER = Logger.getLogger(ContractNumberSequenceSmsService.class);

    private static final String CONTRACT_NUMBER_MASK = "КТДДНННННН";

    /*
    генерирует уникальный код клиента по следующему шаблону XMDDNNNNNN. Код должен генерироваться по следующему принципу:
•	X – кодировка программы. Латинская буква из настроек программы.
•	M – тарифа. Латинская буква из настроек программы.
•	DD – текущее число месяца.
•	NNNNNN – уникальный неповторяющийся номер.

* */

    public String generateContractNumber(String programCode, String tarif, Integer date, Long nextNumber) {
        return CONTRACT_NUMBER_MASK
                .replaceAll("(К)", programCode)
                .replaceAll("(Т)", tarif)
                .replaceAll("(ДД)", String.format("%02d", date))
                .replaceAll("(НННННН)", String.format("%06d", nextNumber));
    }
}
