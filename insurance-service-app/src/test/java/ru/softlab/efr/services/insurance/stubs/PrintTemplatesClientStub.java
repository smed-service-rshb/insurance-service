package ru.softlab.efr.services.insurance.stubs;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.SettableListenableFuture;
import ru.softlab.efr.common.client.PrintTemplatesClient;
import ru.softlab.efr.common.dict.exchange.model.PrintTemplate;
import ru.softlab.efr.common.utilities.rest.client.ClientRestResult;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;

/**
 * Заглушка для клиента для получения данных по печатным формам (common-dict).
 *
 * @author Andrey Grigorov
 */
public class PrintTemplatesClientStub extends PrintTemplatesClient {

    public PrintTemplatesClientStub(MicroServiceTemplate microServiceTemplate) {
        super(microServiceTemplate);
    }

    @Override
    public Resource getContent(String type, long timeout) {
        return new ClassPathResource("templates/templatesInRelease/" + type + ".jrxml");
    }

    @Override
    public PrintTemplate get(String type, long timeout) {
        return new PrintTemplate(type, 0, type);
    }

    @Override
    public ClientRestResult<PrintTemplate> get(String type) {
        SettableListenableFuture<ResponseEntity<PrintTemplate>> listenableFuture = new SettableListenableFuture<>();
        listenableFuture.set(ResponseEntity.ok(new PrintTemplate(type, 0, type)));
        return new ClientRestResult<>(listenableFuture, null);
    }

    @Override
    public PrintTemplate create(Resource content, long timeout) {
        PrintTemplate result = new PrintTemplate();
        result.setType("test-template");
        return result;
    }
}
