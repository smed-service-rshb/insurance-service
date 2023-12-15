package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.exceptions.EntityNotFoundException;
import ru.softlab.efr.services.auth.exchange.model.EmploeeDataWithOrgUnits;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.PhoneForClaimEntity;
import ru.softlab.efr.services.insurance.model.db.RequestEntity;
import ru.softlab.efr.services.insurance.model.rest.ClientRequestRq;
import ru.softlab.efr.services.insurance.model.rest.FilterRequestsRq;
import ru.softlab.efr.services.insurance.model.rest.RequestStatus;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.repositories.RequestsRepository;
import ru.softlab.efr.services.insurance.repositories.TopicRequestRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Service
public class RequestService {

    private static final Logger LOGGER = Logger.getLogger(RequestService.class);

    private PrincipalDataSource principalDataSource;
    private RequestsRepository requestsRepository;
    private TopicRequestRepository topicRequestRepository;
    private InsuranceRepository insuranceRepository;
    private ClientService clientService;
    private EmailService emailService;
    private EmployeesClient employeesClient;
    private RequestAttachService requestAttachService;

    @Autowired
    public RequestService(PrincipalDataSource principalDataSource,
                          RequestsRepository requestsRepository,
                          TopicRequestRepository topicRequestRepository,
                          InsuranceRepository insuranceRepository,
                          ClientService clientService,
                          EmailService emailService,
                          EmployeesClient employeesClient,
                          RequestAttachService requestAttachService) {
        this.principalDataSource = principalDataSource;
        this.requestsRepository = requestsRepository;
        this.topicRequestRepository = topicRequestRepository;
        this.insuranceRepository = insuranceRepository;
        this.clientService = clientService;
        this.emailService = emailService;
        this.employeesClient = employeesClient;
        this.requestAttachService = requestAttachService;
    }

    @Transactional(readOnly = true)
    public Page<RequestEntity> getRequestPaginatedList(Pageable pageable, FilterRequestsRq filterData) {
        Pageable pageableSortedByDate = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "requestDate"));
        Date startDate = (filterData.getRequestDateFrom() != null) ? Date.from(filterData.getRequestDateFrom().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) : null;
        Date endDate = (filterData.getRequestDateTo() != null) ? Date.from(filterData.getRequestDateTo().plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) : null;
        String surname = StringUtils.isEmpty(filterData.getClientSurname()) ? null : filterData.getClientSurname().trim().toLowerCase();
        String name = StringUtils.isEmpty(filterData.getClientName()) ? null : filterData.getClientName().trim().toLowerCase();
        return requestsRepository.getRequestEntityList(filterData.getId(), filterData.getTopicId(),
                startDate, endDate, surname, name, filterData.getStatus(), filterData.getClientId(), pageableSortedByDate);
    }

    @Transactional(readOnly = true)
    public Page<RequestEntity> getUserReportsDataPaginated(Pageable pageable) {
        Pageable pageableSortedByDate = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "requestDate"));
        PrincipalData principalData = principalDataSource.getPrincipalData();
        LocalDate clientBirthDate = getClientBirthDate(principalData);
        return requestsRepository.getClientRequest(principalData.getFirstName(),
                principalData.getSecondName(),
                clientBirthDate,
                principalData.getMobilePhone(),
                pageableSortedByDate);
    }

    @Transactional
    public RequestEntity addRequest(ClientRequestRq clientRequestRq) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        ClientEntity client = clientService.get(principalData, getClientBirthDate(principalData));
        if (Objects.isNull(client)) {
            return null;
        }
        RequestEntity request = new RequestEntity();
        request.setClient(client);
        request.setRequestDate(new Timestamp(System.currentTimeMillis()));
        request.setTopic(topicRequestRepository.getById(clientRequestRq.getRequestsTopicId()));
        if (Objects.nonNull(clientRequestRq.getInsuranceId())) {
            Insurance insurance = insuranceRepository.getById(clientRequestRq.getInsuranceId());
            if (Objects.nonNull(insurance)) {
                if (isEqualsClient(insurance.getHolder(), principalData)) {
                    request.setInsurance(insurance);
                    request.setClient(insurance.getHolder());
                    request.setProduct(insurance.getProgramSetting().getProgram());
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        request.setStatus(RequestStatus.NEW);
        request.setPhone(clientRequestRq.getPhone());
        request.setEmail(clientRequestRq.getEmail());
        request.setRequestText(clientRequestRq.getRequestText());
        request.setIsActive(true);
        request = requestsRepository.save(request);

        emailService.sendAdminRequest(request);
        return request;
    }

    @Transactional
    public ResponseEntity<String> closeReport(Long requestId) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        RequestEntity entity;
        if (principalData.getRights().contains(Right.CLIENT_REQUEST_PROCESSING)) {
            entity = requestsRepository.getById(requestId);
        } else {
            entity = requestsRepository.getById(requestId);
            ClientEntity clientEntity = clientService.get(principalData, getClientBirthDate(principalData));
            if (!clientEntity.getId().equals(entity.getClient().getId())) {
                entity = null;
            }
        }
        if (Objects.nonNull(entity)) {
            entity.setIsActive(false);
            requestsRepository.save(entity);
            return ResponseEntity.ok().body(String.format("Закрыто обращение %s", entity.getId()));
        } else {
            return ResponseEntity.ok().body(String.format("Нет доступа к обращению %s", requestId));
        }
    }

    @Transactional(readOnly = true)
    public RequestEntity getRequestById(Long requestId) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        RequestEntity request = null;
        if (principalData.getRights().contains(Right.CLIENT_REQUEST_PROCESSING)) {
            request = requestsRepository.getById(requestId);
        } else {
            LocalDate clientBirthDate = getClientBirthDate(principalData);
            request = requestsRepository.getByIdAndClientData(requestId,
                    principalData.getFirstName(),
                    principalData.getSecondName(),
                    clientBirthDate,
                    principalData.getMobilePhone());
        }
        if (request != null) {
            request.setDocument(requestAttachService.getRequestDocument(requestId));
        }
        return request;
    }

    @Transactional
    public void processing(Long requestId, RequestStatus status, String info, String clientComment) throws EntityNotFoundException {
        RequestEntity request = requestsRepository.getById(requestId);
        if (request == null) {
            throw new EntityNotFoundException();
        }
        request.setStatus(status);
        request.setAdditionalInfo(info);
        request.setComment(clientComment);
        requestsRepository.save(request);
    }


    private LocalDate getClientBirthDate(PrincipalData principalData) {
        EmploeeDataWithOrgUnits client = null;
        try {
            client = employeesClient.getEmployeeByIdWithOutPermission(principalData.getId(), 10L);
        } catch (RestClientException e) {
            LOGGER.warn("При получении данных о клиенте произошла ошибка:", e);
            return null;
        }
        return client.getBirthDate();
    }

    private boolean isEqualsClient(ClientEntity holder, PrincipalData principalData) {
        String phoneNumber = holder.getPhones().stream().filter(PhoneForClaimEntity::isMain).map(PhoneForClaimEntity::getNumber).findFirst().orElse(null);
        EmploeeDataWithOrgUnits client = null;
        try {
            client = employeesClient.getEmployeeByIdWithOutPermission(principalData.getId(), 10L);
        } catch (RestClientException e) {
            LOGGER.warn("При получении данных о клиенте произошла ошибка:", e);
            return false;
        }
        return Objects.equals(holder.getSurName().toUpperCase(), principalData.getSecondName().toUpperCase())
                && Objects.equals(holder.getFirstName().toUpperCase(), principalData.getFirstName().toUpperCase())
                && Objects.equals(holder.getBirthDate(), client.getBirthDate()) &&
                Objects.equals(phoneNumber, principalData.getMobilePhone());
    }
}
