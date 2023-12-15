package ru.softlab.efr.services.insurance.controllers;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.services.insurance.exception.InternalException;
import ru.softlab.efr.services.insurance.model.client.FoundClient;
import ru.softlab.efr.services.insurance.model.client.ListClientsResponse;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.PhoneForClaimEntity;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.services.ClientService;
import ru.softlab.efr.services.insurance.services.InsuranceService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.softlab.efr.services.insurance.controllers.Constants.MAX_FOUND_CLIENT_COUNT_SETTING;

/**
 * Контроллер предоставляющий точки доступа для работы с клиентскими данными
 *
 * @author khudyakov
 * @since 17.11.2018
 */
@RestController
public class PrivateClientsDataController implements PrivateClientsDataApi {

    private static final String MAX_FOUND_CLIENT_COUNT_SETTING_NOT_FOUND = "Не задана настройка maxFoundClientCount";
    private static final Log LOG = LogFactory.getLog(PrivateClientsDataController.class);

    private final ClientService clientService;
    private final SettingsService settingsService;
    private final InsuranceService insuranceService;

    /**
     * Конструктор
     *
     * @param clientService    скервис работы с клиентами
     * @param settingsService  сервис работы с настройками
     * @param insuranceService сервис работы с договорами
     */
    public PrivateClientsDataController(ClientService clientService, SettingsService settingsService, InsuranceService insuranceService) {
        this.clientService = clientService;
        this.settingsService = settingsService;
        this.insuranceService = insuranceService;
    }

    @Override
    public ResponseEntity<ListClientsResponse> clientsSearchWithOutPermissions(
            @RequestParam(value = "surName", required = false) String surName,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "middleName", required = false) String middleName,
            @RequestParam(value = "birthDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber) throws Exception {
        try {
            List<ClientEntity> clients = clientService.findClient(surName, firstName, middleName, birthDate, phoneNumber, null);
            List<FoundClient> foundClients = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(clients)) {
                for (ClientEntity client : clients) {
                    // Клиенту без договоров отказываем в регистрации
                    List<Insurance> insurances = insuranceService.findAllByHolder(client);
                    if (CollectionUtils.isEmpty(insurances)) {
                        continue;
                    }

                    boolean skipClient = true;
                    for (Insurance insurance : insurances) {
                        InsuranceStatusCode statusCode = insurance.getStatus().getCode();
                        if (InsuranceStatusCode.DRAFT == statusCode || InsuranceStatusCode.PROJECT == statusCode
                                || InsuranceStatusCode.REVOKED == statusCode) {
                            // Если договора только в этих статусах, то в регистрации то же отказываем
                            continue;
                        }
                        skipClient = false;
                    }

                    if (!skipClient) {
                        foundClients.add(toFoundClient(client, insurances.get(0)));
                    }
                }
            }

            return ResponseEntity.ok(new ListClientsResponse(limitList(foundClients), foundClients.size()));
        } catch (Exception e) {
            LOG.error("При поиске клиента произошла ошибка. surName=" + surName + " firstName=" + firstName
                    + " middleName=" + middleName + " birthDate=" + birthDate + " phoneNumber=" + phoneNumber, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private FoundClient toFoundClient(ClientEntity clientEntity, Insurance insurance) {
        FoundClient foundClient = new FoundClient();
        foundClient.setId(String.valueOf(clientEntity.getId()));
        foundClient.setFirstName(clientEntity.getFirstName());
        foundClient.setSurName(clientEntity.getSurName());
        foundClient.setMiddleName(clientEntity.getMiddleName());
        foundClient.setBirthDate(clientEntity.getBirthDate());
        foundClient.setOrgUnitId(insurance.getSubdivisionId());
        foundClient.setEmail(clientEntity.getEmail());
        foundClient.setPhoneNumber(
                clientEntity.getPhones()
                        .stream()
                        .filter(PhoneForClaimEntity::isMain)
                        .map(PhoneForClaimEntity::getNumber)
                        .findFirst()
                        .orElse(null));

        return foundClient;
    }

    private List<FoundClient> limitList(List<FoundClient> foundClients) throws InternalException {
        if (CollectionUtils.isEmpty(foundClients)) {
            return foundClients;
        }

        SettingEntity maxFoundClientCount = settingsService.get(MAX_FOUND_CLIENT_COUNT_SETTING);
        if (maxFoundClientCount == null) {
            throw new InternalException(MAX_FOUND_CLIENT_COUNT_SETTING_NOT_FOUND);
        }

        int maxCount = Integer.parseInt(maxFoundClientCount.getValue());
        return foundClients.size() <= maxCount ? foundClients : foundClients.subList(0, maxCount);
    }
}
