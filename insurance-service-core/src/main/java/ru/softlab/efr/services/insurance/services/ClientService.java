package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.exchange.model.EmployeeDataForList;
import ru.softlab.efr.services.auth.exchange.model.FilterEmployeesRq;
import ru.softlab.efr.services.auth.exchange.model.GetEmployeeRs;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.IdentityDocTypeEnum;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.repositories.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static ru.softlab.efr.services.insurance.repositories.ClientSearchSpecification.clientEntitySpecification;


/**
 * Сервис для работы с данными клиентов
 *
 * @author krivenko
 * @since 28.09.2018
 */
@Service
public class ClientService {

    private static final Logger LOGGER = Logger.getLogger(ClientService.class);
    private static final String NOT_DATA = "(нет данных)";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeCachedClientService employeeCachedClientService;

    @Autowired
    private AddressesForClientEntityRepository addressesRepository;

    @Autowired
    private DocumentForClientDataRepository documentRepository;

    @Autowired
    private PhoneForClientDataRepository phoneRepository;

    @Autowired
    private EmployeesClient employeesClient;

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /**
     * Получение клиента  по id
     *
     * @param id идентификатор клиента
     * @return клиент
     */
    @Transactional(readOnly = true)
    public ClientEntity get(Long id) {
        return clientRepository.findOne(id);
    }

    /**
     * Получение клиента по PrincipalData
     *
     * @return клиент
     */
    @Transactional(readOnly = true)
    public ClientEntity get(PrincipalData principalData) {
        List<ClientEntity> entitiesList = clientRepository.findTopByOrderByRegistrationDateDesc(principalData.getFirstName(),
                principalData.getSecondName(), principalData.getMiddleName(), principalData.getMobilePhone());
        if (entitiesList.isEmpty()) {
            return null;
        }
        return entitiesList.get(0);
    }

    /**
     * Получение клиента по PrincipalData и дате рождения клиента
     *
     * @return клиент
     */
    @Transactional(readOnly = true)
    public ClientEntity get(PrincipalData principalData, LocalDate birthDate) {
        List<ClientEntity> entitiesList = clientRepository.findTopByOrderByRegistrationDateDesc(principalData.getFirstName(),
                principalData.getSecondName(), birthDate, principalData.getMiddleName(), principalData.getMobilePhone());
        if (entitiesList.isEmpty()) {
            return null;
        }
        return entitiesList.get(0);
    }

    /**
     * Проверка существования клиента по id
     *
     * @param id идентификатор клиента
     * @return клиент
     */
    @Transactional(readOnly = true)
    public Boolean isExists(Long id) {
        return clientRepository.countClientEntitiesById(id) > 0;
    }

    /**
     * Сохранение данных клиента
     *
     * @param client сущность клиента
     * @return сущность клиента
     */
    @Transactional
    public ClientEntity save(ClientEntity client) {
        client.setCacheSaveTime(LocalDateTime.now());
        return clientRepository.save(client);
    }

    /**
     * Удаление клиента
     *
     * @param id идентификатор клиента
     */
    @Transactional
    public void delete(Long id) {
        clientRepository.delete(id);
    }

    /**
     * Найти клиента
     *
     * @param surName     фамилия
     * @param firstName   имя
     * @param middleName  отчество
     * @param birthDate   дата рождения
     * @param mobilePhone номер телефона
     * @param docType     тип документа см @{@link ru.softlab.efr.clients.model.IdentityDocType}
     * @param docNumber   номер удаостоверяющего документа
     * @param docSeries   серия
     * @return клиентские данные
     */
    @Transactional(readOnly = true)
    public List<ClientEntity> findClient(final String surName, final String firstName, final String middleName,
                                         final LocalDate birthDate, String mobilePhone, IdentityDocTypeEnum docType, String docSeries, String docNumber, String email) {
        return clientRepository.findAll(clientEntitySpecification(surName, firstName, middleName, birthDate, mobilePhone,
                docType, docSeries, docNumber, email, null, null, null, null, null, false, null),
                new Sort(Sort.Direction.DESC, "id"));
    }

    /**
     * Найти клиента
     *
     * @param surName     фамилия
     * @param firstName   имя
     * @param middleName  отчество
     * @param birthDate   дата рождения
     * @param mobilePhone номер телефона
     * @return клиентские данные
     */
    @Transactional(readOnly = true)
    public List<ClientEntity> findClient(final String surName, final String firstName, final String middleName,
                                         final LocalDate birthDate, String mobilePhone, Long excludeClientId) {
        return clientRepository.findAll(clientEntitySpecification(surName, firstName, middleName, birthDate, mobilePhone,
                null, null, null, null, null, null, null, null, null, false, excludeClientId),
                new Sort(Sort.Direction.DESC, "id"));
    }

    /**
     * Найти количество клиентов
     *
     * @param surName     фамилия
     * @param firstName   имя
     * @param middleName  отчество
     * @param birthDate   дата рождения
     * @param mobilePhone номер телефона
     * @param excludeClientId Идентификатор клиента, которого не нужно учитывать
     * @return клиентские данные
     */
    @Transactional(readOnly = true)
    public long countClient(final String surName, final String firstName, final String middleName,
                            final LocalDate birthDate, String mobilePhone, Long excludeClientId) {
        return clientRepository.count(clientEntitySpecification(surName, firstName, middleName, birthDate, mobilePhone,
                null, null, null, null, null, null, null, null, null, false, excludeClientId));
    }

    /**
     * Найти клиента.
     *
     * @param surName        Фамилия
     * @param firstName      Имя
     * @param middleName     Отчество
     * @param birthDate      Дата рождения
     * @param mobilePhone    Номер телефона
     * @param docType        Тип документа см @{@link ru.softlab.efr.clients.model.IdentityDocType}
     * @param docSeries      Серия удостоверяющего документа
     * @param docNumber      Номер удостоверяющего документа
     * @param email          Адрес электронной почты клиента
     * @param checkStartDate Дата начала периода, в который выполнялась проверка клиента, тип которой указан в параметре {@code type}
     * @param checkEndDate   Дата окончания периода, в который выполнялась проверка клиента, тип которой указан в параметре {@code type}
     * @param checkType      Тип проверки, которая выполнялась над клиентом
     * @return клиентские данные
     */
    @Transactional(readOnly = true)
    public List<ClientEntity> findLikeClient(final String surName, final String firstName, final String middleName,
                                             final LocalDate birthDate, String mobilePhone, IdentityDocTypeEnum docType, String docSeries,
                                             String docNumber, String email, LocalDateTime checkStartDate, LocalDateTime checkEndDate,
                                             CheckUnitTypeEnum checkType, LocalDate startConclusionDate, LocalDate endConclusionDateTime) {
        return clientRepository.findAll(clientEntitySpecification(surName, firstName, middleName, birthDate, mobilePhone,
                docType, docSeries, docNumber, email, checkStartDate, checkEndDate, startConclusionDate, endConclusionDateTime, checkType, true, null));
    }

    @Transactional(readOnly = true)
    public List<ClientEntity> searchClients(final String surName, final String firstName, final String middleName,
                                            final LocalDate birthDate, String mobilePhone) {
        List<ClientEntity> clients = new ArrayList<>();
        /*Пара 1: ФИО + дата рождения*/
        clients.addAll(findClient(surName, firstName, middleName, birthDate, null, null, null, null, null));
        /*Пара 2: ФИО + мобильный номер телефона*/
        clients.addAll(findClient(surName, firstName, middleName, null, mobilePhone, null, null, null, null));
        /*Пара 3: Дата рождения + мобильный номер телефона*/
        clients.addAll(findClient(null, null, null, birthDate, mobilePhone, null, null, null, null));
        return clients.stream().filter(distinctByKey(ClientEntity::getId)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FoundClient> findSimilarClients(Client client) {
        String mobilePhone = ClientEntity.getMobilePhone(client);
        List<ClientEntity> found = searchClients(client.getSurName(), client.getFirstName(), client.getMiddleName(), client.getBirthDate(), mobilePhone);
        return found.stream().map(c -> c.toFoundClient(null, "", "")).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientEntity> findAllClientByContractIdIn(List<Long> contractIds) {
        return clientRepository.findAllClientByContractIdIn(contractIds);
    }

    public ClientInspectionResults convertToClientInspection(List<ClientCheck> clientChecks) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<ClientCheck> terrorists = new ArrayList<>();
        List<ClientCheck> blockages = new ArrayList<>();
        List<ClientCheck> invalidsDocs = new ArrayList<>();
        List<ClientCheck> objects = new ArrayList<>();
        clientChecks.forEach(clientCheck -> {
            if (clientCheck.getCheckType() == CheckUnitTypeEnum.TERRORIST) {
                terrorists.add(clientCheck);
            }
            if (clientCheck.getCheckType() == CheckUnitTypeEnum.BLOCKAGE) {
                blockages.add(clientCheck);
            }
            if (clientCheck.getCheckType() == CheckUnitTypeEnum.INVALID_IDENTITY_DOC) {
                invalidsDocs.add(clientCheck);
            }
        });
        if (!terrorists.isEmpty()) {
            objects.add(findLastDate(terrorists));
        }
        if (!blockages.isEmpty()) {
            objects.add(findLastDate(blockages));
        }
        if (!invalidsDocs.isEmpty()) {
            objects.add(findLastDate(invalidsDocs));
        }
        return new ClientInspectionResults(objects.stream()
                .map(item -> new InspectionResult(item.getCreationDate().format(formatter),
                        item.getCheckType().name(), item.getCheckResult().name())).collect(Collectors.toList()));

    }

    private ClientCheck findLastDate(List<ClientCheck> list) {
        return Collections.max(list, comparing(ClientCheck::getCreationDate));
    }

    public ClientEntity update(ClientEntity clientEntity) {
        ClientEntity entity = clientRepository.findOne(clientEntity.getId());
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return clientRepository.save(clientEntity);
    }

    /**
     * Получить список истории изменения данных клиента (основные данные, адреса, документы, телефоны)
     *
     * @param clientId id клиента
     */
    public DataChangeHistoryClient getHistoryClient(Long clientId) {
        DataChangeHistoryClient historyClient = new DataChangeHistoryClient();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        Revisions<Integer, ClientEntity> listClient = clientRepository.findRevisions(clientId);
        List<HistoryDataRevision> revisionDocList = documentRepository.findAllByClientId(clientId, HistoryDataRevision.class);
        List<HistoryDataRevision> revisionPhoneList = phoneRepository.findAllByClientId(clientId, HistoryDataRevision.class);
        List<HistoryDataRevision> revisionAddressesList = addressesRepository.findAllByClientId(clientId, HistoryDataRevision.class);

        //основные данные
        if (!listClient.getContent().isEmpty()) {
            List<ClientDataHistory> list = listClient.getContent().stream()
                    .peek(revision -> {
                        revision.getEntity().setDocuments(null); //client_aud возвращает 'java.lang.IllegalStateException' exception.
                        revision.getEntity().setAddresses(null);
                        revision.getEntity().setPhones(null);
                    })
                    .map(revision -> new ClientDataHistory(
                            ClientEntity.toClient(revision.getEntity()),
                            getModifiedDate(revision),
                            getUserFullName(revision)))
                    .sorted(Comparator.comparing((ClientDataHistory rev) -> LocalDateTime.parse(rev.getLastModifiedDate(), fmt))
                            .reversed())
                    .collect(Collectors.toList());
            historyClient.setClientData(list);
        }
        if (!revisionDocList.isEmpty()) {
            List<DocumentsDataHistory> documentList = new ArrayList<>();
            Map<DocumentType, List<DocumentsDataHistory>> documentsMap = revisionDocList.stream()
                    .map(item -> documentRepository.findRevision(item.getId(), item.getRev()))
                    .map(revision -> new DocumentsDataHistory(
                            DocumentForClientEntity.toDocument(revision.getEntity()),
                            getModifiedDate(revision),
                            getUserFullName(revision)))
                    .sorted(comparing((DocumentsDataHistory rev) -> LocalDateTime.parse(rev.getLastModifiedDate(), fmt))
                            .reversed())
                    .collect(Collectors.groupingBy((DocumentsDataHistory d) -> d.getDocument().getDocType()));

            documentsMap.values().forEach(documentList::addAll);
            historyClient.setClientDocuments(documentList);
        }
        if (!revisionPhoneList.isEmpty()) {
            historyClient.setClientPhones(revisionPhoneList.stream()
                    .map(item -> phoneRepository.findRevision(item.getId(), item.getRev()))
                    .map(revision -> new PhonesDataHistory(
                            PhoneForClaimEntity.toPhone(revision.getEntity()),
                            getModifiedDate(revision),
                            getUserFullName(revision)))
                    .sorted(comparing((PhonesDataHistory rev) -> LocalDateTime.parse(rev.getLastModifiedDate(), fmt))
                            .reversed())
                    .collect(Collectors.toList()));
        }
        if (!revisionAddressesList.isEmpty()) {
            historyClient.setClientAddresses(revisionAddressesList.stream()
                    .map(item -> addressesRepository.findRevision(item.getId(), item.getRev()))
                    .map(revision -> new AddressesDataHistory(
                            AddressForClientEntity.toAddress(revision.getEntity()),
                            getModifiedDate(revision),
                            getUserFullName(revision)))
                    .sorted(comparing((AddressesDataHistory rev) -> LocalDateTime.parse(rev.getLastModifiedDate(), fmt))
                            .reversed())
                    .collect(Collectors.toList()));
        }
        return historyClient;
    }

    private String getUserFullName(Revision revision) {
        String userFullName = NOT_DATA;
        Long userId = ((AuditEnversInfo) revision.getMetadata().getDelegate()).getUserId();
        try {
            GetEmployeeRs user = employeeCachedClientService.getById(userId);
            userFullName = ReportableContract.getClientFullName(user.getSecondName(), user.getFirstName(), user.getMiddleName());
        } catch (RestClientException | NullPointerException e) {
            LOGGER.error(String.format("Во время получения данных пользователя c ID='%s' из сервиса auth-service произошла ошибка, причина: %s", userId, e), e);
        }
        return userFullName;
    }

    private String getModifiedDate(Revision revision) {
        return revision.getRevisionDate().toLocalDateTime().toString("dd-MM-yyyy HH:mm:ss");
    }

    /**
     * Получить идентификатор пользователя в сервисе авторизации в случае необходимости обновления данного клиента
     *
     * @param oldClient данные клиента сохраненного в бд
     * @param newClient данные клиента которые сохраняются
     * @return идентификатор клиента из сервиса авторизации
     */
    public Long getAuthClientIdToUpdate(ClientEntity oldClient, Client newClient) {
        if (oldClient == null || oldClient.getId() == null) return null;
        String oldPhone = oldClient.getPhones().stream().filter(PhoneForClaimEntity::isMain).map(PhoneForClaimEntity::getNumber).findFirst().orElse("");
        String newPhone = newClient.getPhones().stream().filter(Phone::isMain).map(Phone::getNumber).findFirst().orElse("");
        String oldEmail = oldClient.getEmail() != null ? oldClient.getEmail() : "";
        String newEmail = newClient.getEmail() != null ? newClient.getEmail() : "";
        if (!(oldClient.getSurName().equals(newClient.getSurName()) &&
                oldClient.getFirstName().equals(newClient.getFirstName()) &&
                oldClient.getBirthDate().equals(newClient.getBirthDate()) &&
                oldPhone.equals(newPhone)) && oldEmail.equals(newEmail)) {
            try {
                FilterEmployeesRq filterEmployeesRq = new FilterEmployeesRq();
                filterEmployeesRq.setSecondName(oldClient.getSurName());
                filterEmployeesRq.setFirstName(oldClient.getFirstName());
                filterEmployeesRq.setBirthDate(oldClient.getBirthDate());
                filterEmployeesRq.setMobilePhone(oldPhone);
                List<EmployeeDataForList> listEmployee = employeesClient.getEmployeesWithOutPermissions(filterEmployeesRq, 10);
                if (listEmployee.isEmpty()) {
                    return null;
                } else {
                    return listEmployee.get(0).getId();
                }
            } catch (RestClientException e) {
                LOGGER.warn(String.format("Произошла ошибка при получении данных о клиенте из сервиса авторизации: %s, %s, %s, %s",
                        oldClient.getSurName(), oldClient.getFirstName(), oldClient.getBirthDate(), oldPhone), e);
            }
        }
        return null;
    }
}
