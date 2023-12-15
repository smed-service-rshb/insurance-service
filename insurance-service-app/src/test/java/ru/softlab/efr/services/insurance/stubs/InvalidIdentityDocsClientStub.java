package ru.softlab.efr.services.insurance.stubs;

import org.apache.commons.lang3.StringUtils;
import ru.softlab.efr.common.client.InvalidIdentityDocsClient;
import ru.softlab.efr.common.dict.exchange.model.CheckInvalidIdentityDocRq;
import ru.softlab.efr.common.dict.exchange.model.CheckInvalidIdentityDocRs;
import ru.softlab.efr.common.dict.exchange.model.IdentityDocCheckResult;
import ru.softlab.efr.common.dict.exchange.model.IdentityDocRq;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;

import java.util.ArrayList;

/**
 * Заглушка для клиента для получениия результатов проверки по справочнику недействительных паспортов.
 *
 * @author olshansky
 * @since 26.01.2019
 */
public class InvalidIdentityDocsClientStub extends InvalidIdentityDocsClient {

    public InvalidIdentityDocsClientStub(MicroServiceTemplate microServiceTemplate) {
        super(microServiceTemplate);
    }

    @Override
    public CheckInvalidIdentityDocRs checkInvalidIdentityDoc(CheckInvalidIdentityDocRq checkInvalidIdentityDocRq, long timeout) {
        CheckInvalidIdentityDocRs response = new CheckInvalidIdentityDocRs("1", new ArrayList<>());
        checkInvalidIdentityDocRq.getIdentityDocs().forEach(element ->
                response.addResultListItem(getCheckResult(element)));
        return response;
    }

    private IdentityDocCheckResult getCheckResult(IdentityDocRq request) {
        // 765432 - номер второго паспорта тестового клиента №1 (Иванов Иван Иванович);
        // Заглушка один паспорт этого тестового пользователя признает прошедшим проверку, а второй - нет.
        return new IdentityDocCheckResult(
                request.getId(),
                (StringUtils.isNumeric(request.getId()) && Integer.parseInt(request.getId()) % 2 == 0) || request.getPassportNumber().equals(765432)
        );
    }
}
