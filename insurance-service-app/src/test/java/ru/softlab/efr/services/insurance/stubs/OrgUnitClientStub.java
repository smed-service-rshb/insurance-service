package ru.softlab.efr.services.insurance.stubs;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.SettableListenableFuture;
import ru.softlab.efr.common.utilities.rest.client.ClientRestResult;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;
import ru.softlab.efr.services.auth.OrgUnitClient;
import ru.softlab.efr.services.auth.exchange.model.OfficeData;
import ru.softlab.efr.services.auth.exchange.model.OrgUnitData;
import ru.softlab.efr.services.auth.exchange.model.OrgUnitsData;

import java.util.Collections;

public class OrgUnitClientStub extends OrgUnitClient {

    public OrgUnitClientStub(MicroServiceTemplate microServiceTemplate) {
        super(microServiceTemplate);
    }

    @Override
    public ClientRestResult<OfficeData> getOrgUnit(Long id) throws RestClientException {
        SettableListenableFuture<ResponseEntity<OfficeData>> listenableFuture = new SettableListenableFuture<>();
        listenableFuture.set(ResponseEntity.ok().build());
        return new ClientRestResult<>(listenableFuture, null);
    }

    @Override
    public OrgUnitsData listOrgUnitByType(Long orgUnitType, long timeout) throws RestClientException {
        OrgUnitsData orgUnitsData = new OrgUnitsData();
        orgUnitsData.setOrgUnits(Collections.singletonList(new OrgUnitData(99L, "Системный", "Системный")));
        return orgUnitsData;
    }
}
