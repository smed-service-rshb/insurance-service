package ru.softlab.efr.services.insurance.stubs;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.SettableListenableFuture;
import ru.softlab.efr.common.client.CurrenciesClient;
import ru.softlab.efr.common.dict.exchange.model.Currency;
import ru.softlab.efr.common.utilities.rest.RestPageImpl;
import ru.softlab.efr.common.utilities.rest.client.ClientRestResult;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;

import java.util.Arrays;

/**
 * Заглушка для клиента для получения справочника валют из микросервиса общих справочников (common-dict).
 *
 * @author Andrey Grigorov
 */
public class CurrenciesClientStub extends CurrenciesClient {

    public CurrenciesClientStub(MicroServiceTemplate microServiceTemplate) {
        super(microServiceTemplate);
    }

    @Override
    public ClientRestResult<Currency> getCurrency(Long id) {
        SettableListenableFuture<ResponseEntity<Currency>> listenableFuture = new SettableListenableFuture<>();
        listenableFuture.set(ResponseEntity.ok(TestData.RUB_CURRENCY));
        return new ClientRestResult<>(listenableFuture, null);
    }

    @Override
    public RestPageImpl<Currency> listCurrencies(Pageable pageable, long timeout) {

        Currency rub = new Currency();
        rub.setId(1L);
        rub.setCurrencyName("Рубль");
        rub.setLiteralISO("RUB");

        Currency usd = new Currency();
        usd.setId(2L);
        usd.setCurrencyName("Доллар");
        usd.setLiteralISO("USD");

        Currency eur = new Currency();
        eur.setId(3L);
        eur.setCurrencyName("Евро");
        eur.setLiteralISO("EUR");

        return new RestPageImpl<>(Arrays.asList(rub, usd, eur));
    }
}
