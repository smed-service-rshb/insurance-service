package ru.softlab.efr.services.insurance.stubs;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;
import ru.softlab.efr.common.client.DictStatusClient;
import ru.softlab.efr.common.dict.exchange.model.*;
import ru.softlab.efr.common.utilities.rest.client.ClientRestResult;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;

/**
 * Заглушка для клиента для смены статуса проверки по справочникам.
 *
 * @author olshansky
 * @since 26.01.2019
 */
public class DictStatusClientStub extends DictStatusClient {

    public DictStatusClientStub(MicroServiceTemplate microServiceTemplate) {
        super(microServiceTemplate);
    }

    @Override
    public DictStatusRs setDictStatus(DictStatus setDictStatusRq, long timeout) {
        return new DictStatusRs();
    }

    @Override
    public ClientRestResult<FileDateForCurrentState> getFileDate(Long id) throws RestClientException {
        SettableListenableFuture<ResponseEntity<FileDateForCurrentState>> listenableFuture = new SettableListenableFuture<>();
        FileDateForCurrentState state = new FileDateForCurrentState();
        state.setFileDate("12.12.2019");
        listenableFuture.set(ResponseEntity.ok(state));
        return new ClientRestResult<>(listenableFuture, null);
    }

    @Override
    public CurrentDictRs getCurrentDictIdByName(String dictName, long timeout) throws RestClientException {
        return new CurrentDictRs(5L);
    }
}
