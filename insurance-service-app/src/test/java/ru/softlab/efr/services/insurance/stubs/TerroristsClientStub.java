package ru.softlab.efr.services.insurance.stubs;

import org.apache.commons.lang3.StringUtils;
import ru.softlab.efr.common.client.TerroristsClient;
import ru.softlab.efr.common.dict.exchange.model.CheckTerroristResult;
import ru.softlab.efr.common.dict.exchange.model.CheckTerroristRq;
import ru.softlab.efr.common.dict.exchange.model.CheckTerroristRs;
import ru.softlab.efr.common.dict.exchange.model.PersonnelData;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;

import java.util.ArrayList;

/**
 * Заглушка для клиента для получениия результатов проверки по справочнику террористов.
 *
 * @author olshansky
 * @since 26.01.2019
 */
public class TerroristsClientStub extends TerroristsClient {

    public TerroristsClientStub(MicroServiceTemplate microServiceTemplate) {
        super(microServiceTemplate);
    }

    @Override
    public CheckTerroristRs checkTerrorist(CheckTerroristRq checkTerroristRq, long timeout) {
        CheckTerroristRs response = new CheckTerroristRs("1", new ArrayList<>());
        checkTerroristRq.getCitizens().forEach(element ->
                response.addCitizensItem(getCheckTerroristResult(element)));
        return response;
    }

    private CheckTerroristResult getCheckTerroristResult(PersonnelData request) {
        // 765432 - номер второго паспорта тестового клиента №1 (Иванов Иван Иванович);
        // Заглушка один паспорт этого тестового пользователя признает прошедшим проверку, а второй - нет.
        return new CheckTerroristResult(
                request.getId(),
                (StringUtils.isNumeric(request.getId()) && Integer.parseInt(request.getId()) % 2 != 0),
                ""
        );
    }
}
