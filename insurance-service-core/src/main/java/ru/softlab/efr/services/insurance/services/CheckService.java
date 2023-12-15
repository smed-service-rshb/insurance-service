package ru.softlab.efr.services.insurance.services;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.clients.model.Client;
import ru.softlab.efr.clients.model.Document;
import ru.softlab.efr.common.client.BlockagesClient;
import ru.softlab.efr.common.client.InvalidIdentityDocsClient;
import ru.softlab.efr.common.client.TerroristsClient;
import ru.softlab.efr.common.dict.exchange.model.*;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.insurance.model.db.ClientCheck;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.ClientShortData;
import ru.softlab.efr.services.insurance.model.enums.CheckStateEnum;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.IdentityDocTypeEnum;
import ru.softlab.efr.services.insurance.pojo.CheckResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис проверки клиентов
 */
@Service
public class CheckService {

    private static final Logger LOGGER = Logger.getLogger(CheckService.class);
    @Autowired
    private BlockagesClient blockagesClientChecker;
    @Autowired
    private ClientShortDataService clientShortDataService;
    @Autowired
    private ClientCheckService clientCheckService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TerroristsClient terroristChecker;
    @Autowired
    private InvalidIdentityDocsClient passportChecker;

    private static final long TIMEOUT_IN_SECONDS = 10L;

    /**
     * Сервис проверки клиента по справочнику заблокированных пользователей
     *
     * @param client клиент для проверки
     * @return результат проверки
     * @throws RestClientException исключение взаимодействия с другими сервисами
     */
    public CheckResult checkClientByBlockage(Client client) throws RestClientException {
        PersonnelData blockedPersonnelData = null;
        Long cliId = StringUtils.isNumeric(client.getId()) ? Long.parseLong(client.getId()) : null;
        ClientEntity clientEntity = cliId != null ? clientService.get(cliId) : null;
        ClientCheck clientCheck = null;
        Long updateId = 0L;
        try {
            List<PersonnelData> dataList = new ArrayList<>();
            String clientId = null;
            if (CollectionUtils.isEmpty(client.getDocuments())) {
                clientId = getClientId();
                dataList.add(getPersonnelData(clientId, client, null, null));
            } else {
                for (Document d : client.getDocuments()) {
                    clientId = getClientId();
                    dataList.add(getPersonnelData(clientId, client, d.getDocSeries(), d.getDocNumber()));
                }
            }

            //Выполняем проверку по списку замороженных лиц
            CheckBlockagesRs checkResult = blockagesClientChecker.checkBlockages(new CheckBlockagesRq(dataList), TIMEOUT_IN_SECONDS);
            if (checkResult == null) {
                return new CheckResult(false, null, null);
            }
            updateId = Long.valueOf(checkResult.getUpdateId());

            Optional<CheckBlockagesResult> isBlocked = checkResult.getCitizens()
                    .stream().filter(responseItem ->
                            dataList.stream().anyMatch(requestItem -> requestItem.getId().equalsIgnoreCase(responseItem.getId())
                                    && Boolean.TRUE.equals(responseItem.isIsBlock()))).findAny();

            if (isBlocked.isPresent()) {
                //noinspection OptionalGetWithoutIsPresent
                blockedPersonnelData = dataList.stream().filter(f -> f.getId().equalsIgnoreCase(isBlocked.get().getId())).findFirst().get();
            }
        } catch (Exception e) {
            LOGGER.error(e);
            throw e;
        }
        ClientShortData clientShortData = (clientEntity != null) ? clientShortDataService.findById(clientEntity.getId()) : null;
        if (blockedPersonnelData != null) {
            String message = "Имеется решение комиссии о замораживании (блокировании) денежных средств клиента.";
            message = message + String.format("ФИО: %s %s %s Данные документа. Серия:%s Номер:%s",
                    blockedPersonnelData.getLastName(), blockedPersonnelData.getFirstName(),
                    ensureNotNull(blockedPersonnelData.getMiddleName()),
                    ensureNotNull(blockedPersonnelData.getPassportSeries()),
                    ensureNotNull(blockedPersonnelData.getPassportNumber()));
            clientCheck = new ClientCheck(CheckUnitTypeEnum.BLOCKAGE, CheckStateEnum.TRUE, clientShortData, message, updateId, LocalDateTime.now());
        } else {
            clientCheck = new ClientCheck(CheckUnitTypeEnum.BLOCKAGE, CheckStateEnum.FALSE, clientShortData, null, updateId, LocalDateTime.now());
        }
        Long checkId = clientCheckService.save(clientCheck).getId();
        return new CheckResult(true, blockedPersonnelData, checkId);

    }


    /**
     * Сервис проверки клиента по справочнику террористов и экстремистов
     *
     * @param client клиент для проверки
     * @return результат проверки
     * @throws RestClientException исключение взаимодействия с другими сервисами
     */
    public CheckResult checkClientByTerrorist(Client client) throws RestClientException {
        Long cliId = StringUtils.isNumeric(client.getId()) ? Long.parseLong(client.getId()) : null;
        ClientEntity clientEntity = cliId != null ? clientService.get(cliId) : null;
        ClientCheck clientCheck = null;

        PersonnelData personnelData = new PersonnelData();
        personnelData.setFirstName(client.getFirstName());
        personnelData.setLastName(client.getSurName());
        personnelData.setMiddleName(client.getMiddleName());
        personnelData.setBirthDate(client.getBirthDate());
        String clientId = client.getId() != null ? client.getId() : UUID.randomUUID().toString();
        personnelData.setId(clientId);
        List<PersonnelData> dataList = new ArrayList<>();
        dataList.add(personnelData);
        //Выполняем проверку по справочнику террористов
        CheckTerroristRs checkResult = terroristChecker.checkTerrorist(new CheckTerroristRq(dataList), TIMEOUT_IN_SECONDS);
        if (checkResult == null) {
            LOGGER.error("Ответ от сервиса checkTerrorist не получен !");
            return new CheckResult(false, null, null);
        }
        Long updateId = StringUtils.isNotBlank(checkResult.getUpdateId()) && NumberUtils.isNumber(checkResult.getUpdateId()) ? Long.parseLong(checkResult.getUpdateId()) : 0L;
        Optional<CheckTerroristResult> isTerrorist = checkResult.getCitizens().stream().filter(s -> s.getId().equals(clientId) && s.isIsTerrorist()).findFirst();
        ClientShortData clientShortData = (clientEntity != null) ? clientShortDataService.findById(clientEntity.getId()) : null;
        if (isTerrorist.isPresent()) {
            String message = String.format("Клиент ФИО %s %s%s не прошел проверку по справочнику экстремистов/террористов.",
                    client.getSurName(), client.getFirstName(), ensureNotNull(client.getMiddleName()));
            clientCheck = new ClientCheck(CheckUnitTypeEnum.TERRORIST, CheckStateEnum.TRUE, clientShortData, message, updateId, LocalDateTime.now());
        } else {
            clientCheck = new ClientCheck(CheckUnitTypeEnum.TERRORIST, CheckStateEnum.FALSE, clientShortData, null, updateId, LocalDateTime.now());
        }
        Long checkId = clientCheckService.save(clientCheck).getId();
        return new CheckResult(true, null, !isTerrorist.isPresent(), checkId);
    }


    /**
     * Сервис проверки клиента по справочнику недействительных паспортов
     *
     * @param client клиент для проверки
     * @return результат проверки
     * @throws RestClientException исключение взаимодействия с другими сервисами
     */
    public CheckResult checkClientByPassport(Client client) throws RestClientException {
        Optional<Document> identityDoc = client.getDocuments().stream().filter(d -> IdentityDocTypeEnum.PASSPORT_RF.name().equals(d.getDocType())).findFirst();
        if (!identityDoc.isPresent()) {
            return null;
        }
        Document passport = identityDoc.get();
        Long clientId = StringUtils.isNumeric(client.getId()) ? Long.parseLong(client.getId()) : null;
        ClientEntity clientEntity = clientId != null ? clientService.get(clientId) : null;

        //Запустить проверку паспорта
        String id = UUID.randomUUID().toString();
        IdentityDocRq identityDocRq = new IdentityDocRq(id, Integer.parseInt(passport.getDocSeries()), Integer.parseInt(passport.getDocNumber()));
        List<IdentityDocRq> rq = new ArrayList<>();
        rq.add(identityDocRq);
        CheckInvalidIdentityDocRs response = passportChecker.checkInvalidIdentityDoc(new CheckInvalidIdentityDocRq(rq), TIMEOUT_IN_SECONDS);

        if (response == null) {
            return new CheckResult(false, null, null);
        }
        Long updateId = StringUtils.isNotBlank(response.getUpdateId()) && NumberUtils.isNumber(response.getUpdateId()) ? Long.parseLong(response.getUpdateId()) : 0L;
        Optional<IdentityDocCheckResult> docCheckResult = response.getResultList().stream().filter(d -> d.getId().equals(id) && d.isIsInvalidIdentityDoc()).findFirst();
        ClientCheck clientCheck;
        ClientShortData clientShortData = (clientEntity != null) ? clientShortDataService.findById(clientEntity.getId()) : null;
        if (docCheckResult.isPresent()) {

            String msg = String.format("Паспорт %s %s клиента недействующий. Регистрация клиента невозможна", passport.getDocSeries(), passport.getDocNumber());

            passport.setIsValidDocument(false);
            clientCheck = new ClientCheck(CheckUnitTypeEnum.INVALID_IDENTITY_DOC, CheckStateEnum.TRUE, clientShortData, msg, updateId, LocalDateTime.now());
        } else {
            passport.setIsValidDocument(true);
            clientCheck = new ClientCheck(CheckUnitTypeEnum.INVALID_IDENTITY_DOC, CheckStateEnum.FALSE, clientShortData, null, updateId, LocalDateTime.now());
        }
        Long checkId = clientCheckService.save(clientCheck).getId();
        return new CheckResult(true, null, !docCheckResult.isPresent(), checkId);

    }

    private String getClientId() {
        return UUID.randomUUID().toString();
    }

    private String ensureNotNull(String inputString) {
        return StringUtils.isNotBlank(inputString) ? inputString : "";
    }

    private PersonnelData getPersonnelData(String id, Client client, String docSeries, String docNumber) {
        PersonnelData personnelData = new PersonnelData();
        personnelData.setId(id);
        personnelData.setFirstName(client.getFirstName());
        personnelData.setLastName(client.getSurName());
        personnelData.setMiddleName(client.getMiddleName());
        personnelData.setBirthDate(client.getBirthDate());
        personnelData.setPassportSeries(docSeries);
        personnelData.setPassportNumber(docNumber);
        return personnelData;
    }
}
