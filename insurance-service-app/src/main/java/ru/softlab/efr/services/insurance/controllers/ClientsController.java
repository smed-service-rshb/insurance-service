package ru.softlab.efr.services.insurance.controllers;

import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.softlab.efr.clients.model.exceptions.ExternalSystemException;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.common.utilities.rest.client.ClientRestResult;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.exchange.model.EmployeeDataForList;
import ru.softlab.efr.services.auth.exchange.model.FilterEmployeesRq;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.config.Permissions;
import ru.softlab.efr.services.insurance.exception.InternalException;
import ru.softlab.efr.services.insurance.exception.ValidationException;
import ru.softlab.efr.services.insurance.model.db.ClientCheck;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.DocumentForClientEntity;
import ru.softlab.efr.services.insurance.model.db.PhoneForClaimEntity;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.GenderTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.IdentityDocTypeEnum;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.reportable.ClientCardReport;
import ru.softlab.efr.services.insurance.service.CacheableDictStatusService;
import ru.softlab.efr.services.insurance.service.ClientsCheckService;
import ru.softlab.efr.services.insurance.services.*;
import ru.softlab.efr.services.insurance.utils.AppUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.insurance.controllers.Constants.*;
import static ru.softlab.efr.services.insurance.model.reportable.ReportableContract.getClientFullName;

/**
 * Контроллер предоставляющий точки доступа для работы с клиентскими данными
 *
 * @author gladishev
 * @since 17.04.2017
 */
@RestController
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class ClientsController implements ClientsDataApi {
    private static final Log LOG = LogFactory.getLog(ClientsController.class);
    private static final String MAX_FOUND_CLIENT_COUNT_SETTING_NOT_FOUND = "Не задана настройка maxFoundClientCount";

    private SettingsService settingsService;
    private ClientService clientService;
    private ClientCheckService checkService;
    private PrincipalDataSource principalDataSource;
    private ClientUnloadService clientUnloadService;
    private ClientsCheckService clientsCheckService;
    private NotifyService notifyService;
    private EmployeesClient employeesClient;
    private TemplateService templateService;
    private CacheableDictStatusService dictStatusService;

    @Value("${client.exist.phone.error}")
    private String existPhoneNumberError;
    @Value("${client.duplicate.notification.subject}")
    private String emailSubject;
    @Value("${client.duplicate.notification.body}")
    private String emailMessage;

    @Autowired
    public void setNotifyService(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    @Autowired
    public void setDictStatusService(CacheableDictStatusService dictStatusService) {
        this.dictStatusService = dictStatusService;
    }

    @Autowired
    public void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Autowired
    public void setClientUnloadService(ClientUnloadService clientUnloadService) {
        this.clientUnloadService = clientUnloadService;
    }

    @Autowired
    public void setClientsCheckService(ClientsCheckService clientsCheckService) {
        this.clientsCheckService = clientsCheckService;
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setPrincipalDataSource(PrincipalDataSource principalDataSource) {
        this.principalDataSource = principalDataSource;
    }

    @Autowired
    public void setEmployeesClient(EmployeesClient employeesClient) {
        this.employeesClient = employeesClient;
    }

    @Autowired
    public void setCheckService(ClientCheckService checkService) {
        this.checkService = checkService;
    }

    /**
     * Поиск клиентов
     *
     * @param surName    Фамилия клиента
     * @param firstName  Имя клиента
     * @param middleName Отчество клиента
     * @param birthDate  Дата рождения
     * @param docType    Тип документа
     * @param docSeries  Серия документа
     * @param docNumber  Номер документа
     * @return список клиентов
     * @throws RestClientException в случае ошибки
     */
    @ResponseBody
    @HasPermission(Permissions.ACCESS_CLIENTS_INFO)
    public ResponseEntity<ListClientsResponse> listClients(
            @RequestParam(value = "surName", required = false) String surName,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "middleName", required = false) String middleName,
            @RequestParam(value = "birthDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate birthDate,
            @RequestParam(value = "docType", required = false) String docType,
            @RequestParam(value = "docSeries", required = false) String docSeries,
            @RequestParam(value = "docNumber", required = false) String docNumber,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "startConclusionDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startConclusionDate,
            @RequestParam(value = "endConclusionDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endConclusionDate,
            @RequestParam(value = "startCheckDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startCheckDate,
            @RequestParam(value = "endCheckDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endCheckDate,
            @RequestParam(value = "type", required = false) String type) throws RestClientException {
        try {
            CheckUnitTypeEnum checkType;
            try {
                checkType = type != null ? CheckUnitTypeEnum.valueOf(type) : null;
            } catch (Exception ex) {
                return ResponseEntity.badRequest().build();
            }
            LocalDateTime startDate = (startCheckDate != null) ? startCheckDate.atStartOfDay() : null;
            LocalDateTime endDate = (endCheckDate != null) ? endCheckDate.plusDays(1).atStartOfDay() : null;
            IdentityDocTypeEnum docTypeValue = StringUtils.isNotEmpty(docType) ? IdentityDocTypeEnum.valueOf(docType) : null;
            List<ClientEntity> clients = clientService.findLikeClient(surName, firstName, middleName, birthDate,
                    phoneNumber, docTypeValue, docSeries, docNumber, email, startDate, endDate, checkType,
                    startConclusionDate, endConclusionDate);
            List<FoundClient> foundClients = new ArrayList<>();
            if (Objects.nonNull(clients)) {
                foundClients = clients.stream().map(c -> c.toFoundClient(docTypeValue, docSeries, docNumber)).collect(Collectors.toList());
            }
            return ResponseEntity.ok(new ListClientsResponse(limitList(foundClients), foundClients.size()));
        } catch (ExternalSystemException | InternalException e) {
            LOG.error("При формировании списка клиентов произошла ошибка", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @HasPermission(Permissions.CREATE_OR_EDIT_CLIENTS)
    public ResponseEntity<Long> saveClientDataWithRequiredFields(@RequestBody ShortClientData shortClientData) throws Exception {
        if (shortClientData.getGender() == null
                || AppUtils.isNullOrWhitespace(shortClientData.getDocSeries())
                || AppUtils.isNullOrWhitespace(shortClientData.getDocNumber())
                || shortClientData.getDocType() == null
                || AppUtils.isNullOrWhitespace(shortClientData.getFirstName())
                || AppUtils.isNullOrWhitespace(shortClientData.getSurName())) {
            return ResponseEntity.badRequest().build();
        }

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setSurName(shortClientData.getSurName());
        clientEntity.setFirstName(shortClientData.getFirstName());
        clientEntity.setMiddleName(shortClientData.getMiddleName());
        clientEntity.setBirthDate(shortClientData.getBirthDate());
        if (Objects.nonNull(shortClientData.getGender())) {
            clientEntity.setGender(GenderTypeEnum.valueOf(shortClientData.getGender().name()));
        }
        clientEntity.setEmail(shortClientData.getEmail());
        if (Objects.nonNull(shortClientData.getPhoneNumber()) && !StringUtils.isEmpty(shortClientData.getPhoneNumber())) {
            PhoneForClaimEntity phone = new PhoneForClaimEntity();
            phone.setMain(true);
            phone.setNumber(shortClientData.getPhoneNumber());
            phone.setPhoneType(PhoneType.MOBILE);
            phone.setClient(clientEntity);
            clientEntity.getPhones().add(phone);
        }
        String docSeries = shortClientData.getDocSeries();
        String docNumber = shortClientData.getDocNumber();
        if (Objects.nonNull(docNumber) && !StringUtils.isEmpty(docNumber) && shortClientData.getDocType() != null) {
            DocumentForClientEntity document = new DocumentForClientEntity();
            document.setDocType(IdentityDocTypeEnum.valueOf(shortClientData.getDocType().name()));
            document.setDocNumber(docNumber);
            document.setDocSeries(docSeries);
            document.setIdentity(true);
            document.setValidDocument(true);
            document.setMain(true);
            document.setActive(true);
            document.setClient(clientEntity);
            clientEntity.getDocuments().add(document);
        }
        clientService.save(clientEntity);
        return ResponseEntity.ok(clientEntity.getId());
    }

    @Override
    public ResponseEntity<Resource> unloadClientWord(@PathVariable("clientId") Integer clientId) throws Exception {

        Resource template;
        String filename;
        ClientEntity clientEntity = clientService.get(Long.valueOf(clientId));
        try {
            template = templateService.getTemplateContent("word-card-for-client");
            filename = getClientFullName(clientEntity.getSurName(), clientEntity.getFirstName(), clientEntity.getMiddleName()).concat(".docx");
        } catch (RestClientException ex) {
            return ResponseEntity.notFound().build();
        }
        ByteArrayResource byteArrayResource = new ByteArrayResource(templateService.buildAndMergeTemplates(
                Collections.singletonList(template),
                new ClientCardReport().construct(
                        checkService.findAllContractsWithAllInfo(Long.valueOf(clientId)),
                        clientEntity,
                        checkService.findAllChecksByClientId(Long.valueOf(clientId)),
                        dictStatusService
                ),
                new JRDocxExporter()));

        String encodedFileName = URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replaceAll("\\+", " ");

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName)
                .body(byteArrayResource);
    }

    @Override
    @HasPermission(Permissions.ACCESS_CLIENTS_INFO)
    public ResponseEntity<byte[]> unloadClientXml(@PathVariable("clientId") String clientId) throws Exception {
        if (!NumberUtils.isNumber(clientId)) {
            return ResponseEntity.badRequest().build();
        }
        if (clientService.isExists(Long.valueOf(clientId))) {
            byte[] xml = clientUnloadService.getClientXmlBytesById(clientId);
            clientUnloadService.checkRequest(xml, ClientsController.class.getResourceAsStream("/client/clientinfo.xsd"));
            String encodedFileName = URLEncoder.encode("client.xml", StandardCharsets.UTF_8.name());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(xml);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    @HasPermission(Permissions.ACCESS_CLIENTS_INFO)
    public ResponseEntity<byte[]> unloadClientsXmlByCriteria(@RequestParam(value = "contractIds", required = false) List<Long> contractIds) throws Exception {
        String encodedFileName = URLEncoder.encode("clients.xml", StandardCharsets.UTF_8.name());
        byte[] xml = clientUnloadService.getClientXmlBytesByContractIds(contractIds);
        clientUnloadService.checkRequest(xml, ClientsController.class.getResourceAsStream("/client/clientinfo.xsd"));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(xml);
    }

    @Override
    public ResponseEntity<Void> updateClientWorkflow(@PathVariable("id") Long id, @NotNull @Valid @RequestParam(value = "workflowAgreements") Boolean workflowAgreements) throws Exception {
        ClientEntity client = clientService.get(id);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            client.setWorkflowAgreements(workflowAgreements);
            clientService.save(client);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOG.error("При обновлении данных по клиенту с идентификатором " + id + " произошла ошибка.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Override
    public ResponseEntity<Void> checkAllClients(@NotNull @Valid @RequestParam(value = "dictName") String dictName, @PathVariable("updateId") String updateId) throws Exception {
        if (checkClient(dictName, updateId, null)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    @HasRight(Right.MANUAL_CHECK_CLIENT)
    public ResponseEntity<Void> manualCheckClient(@RequestBody CheckClientRq checkClientData) {
        checkClientData.getDictTypes().forEach(item -> {
            try {
                checkClient(item.name(),
                        clientsCheckService.getCurrentDictIdByName(item.name()).toString(),
                        checkClientData.isAllClientCheck() ? null : checkClientData.getClientIds());
            } catch (Exception e) {
                LOG.error("При проверке списка клиентов произошла ошибка", e);
            }
        });
        return ResponseEntity.ok().build();
    }

    @HasPermission(Permissions.ACCESS_CLIENTS_INFO)
    public ResponseEntity<Void> putClient(@PathVariable("clientId") Long clientId, @Valid @RequestBody Client updateClientInfo) throws Exception {
        try {
            ClientEntity clientEntity = clientService.get(clientId);
            if (clientEntity == null) {
                LOG.error(String.format("Не найден пользователь по идентификатору %s.", clientId));
                return ResponseEntity.notFound().build();
            }
            clientEntity.update(updateClientInfo);
            clientService.update(clientEntity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOG.error("При обновлении данных по клиенту с идентификатором " + clientId + " произошла ошибка.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean checkClient(String dictName, String updateId, List<Long> clientIds) throws IOException {
        if (StringUtils.isNotBlank(dictName) && StringUtils.isNotBlank(updateId)) {
            if (DictType.TERRORIST.equals(DictType.valueOf(dictName))) {
                clientsCheckService.checkTerroristAsync(updateId, clientIds);
            } else if (DictType.INVALID_IDENTITY_DOC.equals(DictType.valueOf(dictName))) {
                clientsCheckService.checkInvalidIdentityDocAsync(updateId, clientIds);
            } else if (DictType.BLOCKAGE.equals(DictType.valueOf(dictName))) {
                clientsCheckService.checkBlockagesAsync(updateId, clientIds);
            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public ResponseEntity<ListClientsResponse> clientsSearch(@RequestParam(value = "surName", required = false) String surName,
                                                             @RequestParam(value = "firstName", required = false) String firstName,
                                                             @RequestParam(value = "middleName", required = false) String middleName,
                                                             @RequestParam(value = "birthDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate birthDate,
                                                             @RequestParam(value = "phoneNumber", required = false) String phoneNumber) throws Exception {
        try {
            List<ClientEntity> clients = new ArrayList<>(
                    clientService.searchClients(surName, firstName, middleName, birthDate, phoneNumber));

            List<FoundClient> foundClients = new ArrayList<>();
            if (!clients.isEmpty()) {
                foundClients = clients.stream()
                        .map(c -> c.toFoundClient(null, "", "")).collect(Collectors.toList());
            }
            return ResponseEntity.ok(new ListClientsResponse(limitList(foundClients), foundClients.size()));
        } catch (Exception e) {
            LOG.error("При поиске клиента произошла ошибка. surName=" + surName + " firstName=" + firstName
                    + " middleName=" + middleName + " birthDate=" + birthDate + " phoneNumber=" + phoneNumber, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    @HasRight(Right.CREATE_CONTRACT)
    public ResponseEntity<Client> findClient(@Valid @RequestBody FindClientRq findClientRq) throws Exception {
        List<ClientEntity> clientEntities = clientService.findClient(findClientRq.getSurName(), findClientRq.getFirstName(),
                findClientRq.getMiddleName(), findClientRq.getBirthDate(), findClientRq.getPhoneNumber(), findClientRq.getClientId());
        if (clientEntities.isEmpty()) {
            long count = clientService.countClient(null, null, null, null, findClientRq.getPhoneNumber(), findClientRq.getClientId());
            if (count != 0) {
                throw new ValidationException(Collections.singletonList(existPhoneNumberError));
            } else {
                count = clientService.countClient(findClientRq.getSurName(), findClientRq.getFirstName(),
                        findClientRq.getMiddleName(), findClientRq.getBirthDate(), null, findClientRq.getClientId());
                if (count != 0) {
                    SettingEntity settingEntity = settingsService.get(CLIENT_DUPLICATE_NOTIFICATION_EMAIL);
                    if (settingEntity != null && !settingEntity.getValue().isEmpty()) {
                        notifyService.sendEmail(settingEntity.getValue(), emailSubject,
                                String.format(emailMessage, findClientRq.getSurName(), findClientRq.getFirstName(),
                                        findClientRq.getMiddleName(), findClientRq.getBirthDate(), findClientRq.getPhoneNumber()),
                                null, EMAIL_TEXT_CONTENT_TYPE);
                    } else {
                        LOG.warn(String.format("Невозможно отправить уведомление о возможном дубле клиента," +
                                        " в параметре %s, не указан email для уведомлений",
                                CLIENT_DUPLICATE_NOTIFICATION_EMAIL));
                    }
                }
            }
        } else {
            Client client = ClientEntity.toClient(clientEntities.get(0));
            client.setId(clientEntities.get(0).getId().toString());
            return ResponseEntity.ok(client);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Получение данных по клиенту
     *
     * @param clientId Идентификатор клиента в системе
     * @return Данные по клиенту
     */
    @HasPermission(Permissions.ACCESS_CLIENTS_INFO)
    public ResponseEntity<Client> getClient(
            @PathVariable("clientId") String clientId) {
        if (!NumberUtils.isNumber(clientId)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            ClientEntity clientEntity = clientService.get(Long.valueOf(clientId));
            if (clientEntity == null) {
                return ResponseEntity.notFound().build();
            }
            FilterEmployeesRq filterEmployeesRq = new FilterEmployeesRq();
            filterEmployeesRq.setSecondName(clientEntity.getSurName());
            filterEmployeesRq.setFirstName(clientEntity.getFirstName());
            filterEmployeesRq.setBirthDate(clientEntity.getBirthDate());
            filterEmployeesRq.setMobilePhone(clientEntity.getPhones().stream()
                    .filter(PhoneForClaimEntity::isMain)
                    .map(PhoneForClaimEntity::getNumber)
                    .findFirst().orElse(""));
            Client client = ClientEntity.toClient(clientEntity);
            try {
                List<EmployeeDataForList> authClients = employeesClient.getEmployeesWithOutPermissions(filterEmployeesRq, 10L);
                if (!authClients.isEmpty() && authClients.size() == 1) {
                    client.setAuthId(authClients.get(0).getId());
                }
            } catch (Exception ex) {
                LOG.error(String.format("При получении данных в сервисе авторизации по клиенту %s %s %s произошла ошибка.",
                        clientEntity.getSurName(), clientEntity.getFirstName(), client.getMiddleName()), ex);
            }
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            LOG.error("При получении данных по клиенту с идентификатором " + clientId + " произошла ошибка.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * @param clientId Идентификатор клиента в системе
     * @return История изменений данных клиента
     */
    @HasPermission(Permissions.ACCESS_CLIENTS_INFO)
    public ResponseEntity<DataChangeHistoryClient> getClientHistoryChanges(@PathVariable("clientId") Long clientId) {
        try {
            if (clientService.isExists(clientId)) {
                return ResponseEntity.ok(clientService.getHistoryClient(clientId));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("При получении истории изменения данных по клиенту с идентификатором " + clientId + " произошла ошибка.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Получение данных по клиенту
     *
     * @return Данные по клиенту
     */
    //@HasRight(Right.CLIENT_VIEW_PROFILE)
    public ResponseEntity<Client> getConsumer() {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        try {
            List<ClientEntity> clients = clientService.findClient(
                    principalData.getSecondName(),
                    principalData.getFirstName(),
                    principalData.getMiddleName(),
                    null,
                    principalData.getMobilePhone(),
                    null,
                    null,
                    null,
                    null);
            if (CollectionUtils.isEmpty(clients)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(ClientEntity.toClient(clients.get(clients.size() - 1)));
        } catch (Exception e) {
            LOG.error("При получении данных по клиенту произошла ошибка.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @HasPermission(Permissions.ACCESS_CLIENTS_INFO)
    public ResponseEntity<ClientInspectionResults> getInspectionResults(@PathVariable("clientId") String clientId) throws Exception {

        if (!NumberUtils.isNumber(clientId)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            List<ClientCheck> clientChecks = checkService.findAllChecksByClientId(Long.valueOf(clientId));
            if (clientChecks == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(clientService.convertToClientInspection(clientChecks));
        } catch (Exception e) {
            LOG.error("При получении результатов проверок по справочнику произошла ошибка.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private List<FoundClient> limitList(List<FoundClient> foundClients) throws InternalException {
        if (CollectionUtils.isEmpty(foundClients)) {
            return foundClients;
        }

        SettingEntity maxFoundClientCount = settingsService.get(MAX_FOUND_CLIENT_COUNT_SETTING);
        if (Objects.isNull(maxFoundClientCount)) {
            throw new InternalException(MAX_FOUND_CLIENT_COUNT_SETTING_NOT_FOUND);
        }

        try {
            int maxCount = Integer.parseInt(maxFoundClientCount.getValue());
            return foundClients.size() <= maxCount ? foundClients :
                    foundClients.subList(0, maxCount);
        } catch (NumberFormatException e) {
            throw new InternalException(e);
        }
    }
}
