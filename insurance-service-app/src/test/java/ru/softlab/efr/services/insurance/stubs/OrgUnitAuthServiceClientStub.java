package ru.softlab.efr.services.insurance.stubs;

import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;
import ru.softlab.efr.services.auth.OrgUnitAuthServiceClient;
import ru.softlab.efr.services.auth.exchange.GetOrgUnitFullRs;
import ru.softlab.efr.services.auth.orgunit.OfficeData;
import ru.softlab.efr.services.auth.orgunit.OrgUnitFullData;

import java.util.ArrayList;
import java.util.List;

/**
 * Заглушка для клиента для получения орг.структуры организации.
 *
 * @author Andrey Grigorov
 */
public class OrgUnitAuthServiceClientStub extends OrgUnitAuthServiceClient {

    public OrgUnitAuthServiceClientStub(MicroServiceTemplate microServiceTemplate) {
        super(microServiceTemplate);
    }

    public OrgUnitAuthServiceClientStub(MicroServiceTemplate microServiceTemplate, String prefix) {
        super(microServiceTemplate, prefix);
    }

    @Override
    public GetOrgUnitFullRs getOrgUnitList(long timeout) throws Exception {
        GetOrgUnitFullRs res = new GetOrgUnitFullRs();
        List<OrgUnitFullData> orgUnits = new ArrayList<>();

        OrgUnitFullData branch1 = new OrgUnitFullData();
        branch1.setBranchId(1L);
        branch1.setName("Амурский РФ");
        List<OfficeData> offices = new ArrayList<>();
        OfficeData office1 = new OfficeData();
        office1.setName("2310");
        office1.setOfficeId(2L);
        office1.setCity("Благовещенск");
        offices.add(office1);
        OfficeData office2 = new OfficeData();
        office2.setName("2313");
        office2.setOfficeId(3L);
        office2.setCity("Благовещенск");
        offices.add(office2);

        OfficeData office3 = new OfficeData();
        office3.setName("0001");
        office3.setOfficeId(4L);
        office3.setCity("Севастополь");
        offices.add(office3);

        OfficeData office4 = new OfficeData();
        office4.setName("1111");
        office4.setOfficeId(55L);
        office4.setCity("Симферополь");
        offices.add(office4);

        branch1.setOffices(offices);

        OrgUnitFullData branch2 = new OrgUnitFullData();
        branch1.setBranchId(1L);
        branch1.setName("ДРБ");
        offices = new ArrayList<>();
        office1 = new OfficeData();
        office1.setName("2311");
        office1.setOfficeId(123L);
        office1.setCity("Москва");
        offices.add(office1);
        office2 = new OfficeData();
        office2.setName("2312");
        office2.setOfficeId(3L);
        office2.setCity("Москва");
        offices.add(office2);
        branch2.setOffices(offices);

        orgUnits.add(branch1);
        orgUnits.add(branch2);

        res.setOrgUnits(orgUnits);

        return res;
    }
}
