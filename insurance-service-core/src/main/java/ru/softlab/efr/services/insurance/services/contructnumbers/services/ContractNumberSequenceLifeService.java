package ru.softlab.efr.services.insurance.services.contructnumbers.services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Сервис счётчика номера договора для страховых проектов жизни
 *
 * @author olshansky
 * @since 19.12.2018
 */
@Service
public class ContractNumberSequenceLifeService {

    private static final Logger LOGGER = Logger.getLogger(ContractNumberSequenceLifeService.class);

    private static final String CONTRACT_NUMBER_MASK = "AABBBCCDEEEEEEE";

    /*
Номер договора генерируется автоматически Системой при создании договора. Номер должен соответствовать следующему формату: AABBBCCDEEEEEEE, где:
•	AA – код РФ.
•	BBB – программа страхования. Кодировка программы для (ИСЖ/НСЖ).
•	СС – вариант программы страхования.
•	D – код валюты договора:
    o	1 – Рубль РФ.
    o	2 – Доллар США.
    o	3 – Евро.
•	EEEEEEE – сквозная нумерация для договоров в разрезе вида программы: НСЖ, ИСЖ, КСП.
Нумерация по каждому виду программы страхования, начинается со значения 0000001.

* */

    public String generateContractNumber(String branchCode, String policyCode, String variant,
                                         String currencyContractType, String contractNumber, Long nextNumber) {

        if (contractNumber != null && contractNumber.length() > 8) {
            return contractNumber.substring(0, 2) + policyCode + variant + currencyContractType + contractNumber.substring(8);
        } else {
            return CONTRACT_NUMBER_MASK
                    .replaceAll("(AA)", branchCode)
                    .replaceAll("(BBB)", policyCode)
                    .replaceAll("(CC)", variant)
                    .replaceAll("(D)", currencyContractType)
                    .replaceAll("(EEEEEEE)", String.format("%07d", nextNumber));
        }
    }
}
