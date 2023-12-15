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
public class ContractNumberSequenceHomeService {

    private static final Logger LOGGER = Logger.getLogger(ContractNumberSequenceHomeService.class);

    private static final String CONTRACT_NUMBER_MASK = "AABBCCDDDDDDD";

    /*
Номер договора генерируется автоматически Системой при создании договора. Номер должен соответствовать следующему формату: AABBCCDDDDDDD, где:
•	AA – буквенный код (по умолчанию «ММ» для всех продуктов);
•	BB – номер Правил страхования в соответствии с внутренней классификацией Правил страхования. По умолчанию для страховых продуктов необходимо установить следующие номера:
        o	"Защита Ваших Близких" – «11»;
        o	"Защита в подарок" – «11»;
        o	"Защита Ваших денег на банковской карте" – «20»;
        o	"Страхование квартиры или дома" – «51».
•	СС – код продукта страхования в соответствии с внутренней классификацией продуктов в рамках правил страхования. По умолчанию для страховых продуктов необходимо установить следующие номера:
        o	"Защита Ваших Близких" – «10»;
        o	"Защита в подарок" – «11»;
        o	"Защита Ваших денег на банковской карте" – «02»;
        o	"Страхование квартиры или дома" – «04».
•	DDDDDDD – сквозная нумерация для договоров.
Нумерация по каждому виду программы страхования, начинается со значения 0000001.

* */

    public String generateContractNumber(String charCode, String ruleNumber, String variant, Long nextNumber) {
        return CONTRACT_NUMBER_MASK
                .replaceAll("(AA)", charCode)
                .replaceAll("(BB)", ruleNumber)
                .replaceAll("(CC)", variant)
                .replaceAll("(DDDDDDD)", String.format("%07d", nextNumber));
    }
}
