package ru.softlab.efr.services.insurance.stubs;

import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.exchange.model.EmploeeDataWithOrgUnits;
import ru.softlab.efr.services.auth.exchange.model.GetEmployeeRs;
import ru.softlab.efr.services.auth.exchange.model.OrgUnitData;

import java.time.LocalDate;
import java.util.Collections;

/**
 * Заглушка для клиента для получения учётных записей работников организации.
 *
 * @author Andrey Grigorov
 */
public class EmployeesClientStub extends EmployeesClient {

    public EmployeesClientStub(MicroServiceTemplate microServiceTemplate) {
        super(microServiceTemplate);
    }

    @Override
    public GetEmployeeRs getEmployeeById(Long id, long timeout) {
        GetEmployeeRs rs = new GetEmployeeRs();

        rs.setId(id);
        rs.setSecondName("Иванов");
        rs.setFirstName("Иван");
        rs.setMiddleName("Иванович");
        rs.setEmail("ivanov@example.org");
        rs.setPersonnelNumber("1-001");
        rs.setBirthDate(LocalDate.of(1986, 3, 30));

        return rs;
    }

    @Override
    public EmploeeDataWithOrgUnits getEmployeeByIdWithOutPermission(Long id, long timeout) throws RestClientException {
        EmploeeDataWithOrgUnits rs = new EmploeeDataWithOrgUnits();
        rs.setId(id);
        rs.setSecondName("System");
        rs.setFirstName("System");
        rs.setOrgUnits(Collections.singletonList(new OrgUnitData(99L, "Системный", "Системный")));
        rs.setBirthDate(LocalDate.of(1986, 3, 30));
        return rs;
    }
}
