package ru.softlab.efr.services.insurance.stubs;

import org.apache.commons.lang3.StringUtils;
import ru.softlab.efr.common.client.BlockagesClient;
import ru.softlab.efr.common.dict.exchange.model.CheckBlockagesResult;
import ru.softlab.efr.common.dict.exchange.model.CheckBlockagesRq;
import ru.softlab.efr.common.dict.exchange.model.CheckBlockagesRs;
import ru.softlab.efr.common.dict.exchange.model.PersonnelData;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;

import java.util.ArrayList;

/**
 * Заглушка для клиента для получениия результатов проверки по справочнику блокировок.
 *
 * @author olshansky
 * @since 26.01.2019
 */
public class BlockagesClientStub extends BlockagesClient {

    public BlockagesClientStub(MicroServiceTemplate microServiceTemplate) {
        super(microServiceTemplate);
    }

    @Override
    public CheckBlockagesRs checkBlockages(CheckBlockagesRq checkBlockagesRq, long timeout) {
        CheckBlockagesRs response = new CheckBlockagesRs("1", new ArrayList<>());
        checkBlockagesRq.getCitizens().forEach(element ->
                response.addCitizensItem(getCheckResult(element)));
        return response;
    }

    private CheckBlockagesResult getCheckResult(PersonnelData request) {
        // 765432 - номер второго паспорта тестового клиента №1 (Иванов Иван Иванович);
        // Заглушка один паспорт этого тестового пользователя признает прошедшим проверку, а второй - нет.
        return new CheckBlockagesResult(
                request.getId(),
                (StringUtils.isNumeric(request.getId()) && Integer.parseInt(request.getId()) % 2 == 0) || "765432".equals(request.getPassportNumber()),
                ""
        );
    }
}
