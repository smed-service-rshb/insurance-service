package ru.softlab.efr.services.insurance.controllers;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.exchange.model.EmploeeDataWithOrgUnits;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.insurance.converter.AcquiringInsuranceConverter;
import ru.softlab.efr.services.insurance.converter.AcquiringProgramConverter;
import ru.softlab.efr.services.insurance.converter.InsuranceConverter;
import ru.softlab.efr.services.insurance.exception.AcquiringException;
import ru.softlab.efr.services.insurance.model.db.AcquiringProgram;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.AcquiringStatus;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.pojo.CheckAcquiringResult;
import ru.softlab.efr.services.insurance.service.AcquiringInsuranceService;
import ru.softlab.efr.services.insurance.services.*;

import javax.transaction.NotSupportedException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class AcquiringContractController implements AcquiringContractApi {

    private static final Logger LOGGER = Logger.getLogger(AcquiringContractController.class);

    private final AcquiringProgramService acquiringProgramService;
    private final PrincipalDataSource principalDataSource;
    private final AcquiringProgramConverter acquiringProgramConverter;
    private final AcquiringInsuranceService acquiringInsuranceService;
    private final AcquiringInsuranceConverter converter;
    private final InsuranceConverter insuranceConverter;
    private final InsuranceCheckService insuranceCheckService;
    private final AcquiringInfoService acquiringInfoService;
    private final OrderService orderService;
    private final ClientService clientService;
    private final PaymentGateService paymentGateService;
    private final InsuranceService insuranceService;
    private final EmployeesClient employeesClient;
    private final ProgramSettingService programSettingService;

    @Value("${acquiring.error}")
    private String acquiringError;
    @Value("${exist.ksp.error}")
    private String existKspError;
    @Value("${find.by.code.error}")
    private String findByCodeError;
    @Value("${insurance.converter.static.date.notvalid}")
    private String STATIC_DATE_LESS_CURRENT_DATE;

    @Autowired
    public AcquiringContractController(AcquiringProgramService acquiringProgramService, PrincipalDataSource principalDataSource, AcquiringProgramConverter acquiringProgramConverter, AcquiringInsuranceService acquiringInsuranceService, AcquiringInsuranceConverter converter, InsuranceConverter insuranceConverter, InsuranceCheckService insuranceCheckService, AcquiringInfoService acquiringInfoService, OrderService orderService, ClientService clientService, PaymentGateService paymentGateService, InsuranceService insuranceService, EmployeesClient employeesClient, ProgramSettingService programSettingService) {
        this.acquiringProgramService = acquiringProgramService;
        this.principalDataSource = principalDataSource;
        this.acquiringProgramConverter = acquiringProgramConverter;
        this.acquiringInsuranceService = acquiringInsuranceService;
        this.converter = converter;
        this.insuranceConverter = insuranceConverter;
        this.insuranceCheckService = insuranceCheckService;
        this.acquiringInfoService = acquiringInfoService;
        this.orderService = orderService;
        this.clientService = clientService;
        this.paymentGateService = paymentGateService;
        this.insuranceService = insuranceService;
        this.employeesClient = employeesClient;
        this.programSettingService = programSettingService;
    }

    @Override
    public ResponseEntity<AcquiringInsuranceRs> acquiringFindByCode(@Valid @RequestBody AcquiringFindByCodeRq body) throws Exception {
        Insurance insurance = insuranceService.findByCode(body.getCode());
        if (insurance == null || insurance.getStatus().getCode() != InsuranceStatusCode.PROJECT) {
            throw new AcquiringException(findByCodeError);
        }
        PrincipalData principalData = principalDataSource.getPrincipalData();
        if (!isInsuranceAvailable(insurance.getHolder(), principalData, body)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        AcquiringInfo info = acquiringInfoService.saveAsNew(converter.convertInfo(insurance, principalData == null));
        return ResponseEntity.ok(converter.convert(info));
    }

    @Override
    public ResponseEntity<AcquiringInsuranceRs> acquiringInfo(@PathVariable("uuid") String uuid) throws Exception {
        AcquiringInfo info = acquiringInfoService.findByUuid(uuid);
        if ((info == null || (!isProgramAvailable(info.getAcquiringProgram()) && !(info.getInsurance() != null && info.getInsurance().getCode() != null)))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(converter.convert(info));
    }

    @Override
    public ResponseEntity<AcquiringPaymentInfoRs> acquiringPaymentInfo(@PathVariable("orderId") Long orderId) throws Exception {
        AcquiringInfo info;
        OrderEntity order = orderService.getById(orderId);
        if (order != null && order.getOrderCode() == null && StringUtils.isNotBlank(order.getExtId())) {
            order = paymentGateService.getOrderStatus(order);
        } else if (order == null || order.getContract() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        info = acquiringInsuranceService.updateInsurance(order);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        AcquiringPaymentInfoRs response = new AcquiringPaymentInfoRs();
        BeanUtils.copyProperties(converter.convert(info), response);
        response.setOrderCode(order.getOrderCode());
        response.setErrorCode(order.getErrorCode());
        response.setErrorMessage(order.getErrorMessage());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> checkExistKsp(@PathVariable("clientId") Long clientId) throws Exception {
        List<Insurance> kspContract = insuranceService.findAllByProgramKindAndClient(ProgramKind.KSP, clientId);
        if (!kspContract.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AvailableProgramsRs> getAvailableProgram() {
        List<AcquiringProgram> acquiringProgram = acquiringProgramService.getAcquiringProgramForClient(principalDataSource.getPrincipalData());
        return ResponseEntity.ok(new AvailableProgramsRs(acquiringProgram.stream()
                .sorted(Comparator.comparing(AcquiringProgram::getPriority).thenComparing(AcquiringProgram::getTitle))
                .map(acquiringProgramConverter::convertToClient)
                .collect(Collectors.toList())));
    }

    @Override
    public ResponseEntity<AcquiringInsuranceRs> registrationAcquiring(@Valid @RequestBody AcquiringInsuranceRq rq) throws Exception {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        AcquiringProgram acquiringProgram = acquiringProgramService.findById(rq.getProgramId());
        if (!isProgramAvailable(acquiringProgram)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (principalData == null) {
            CheckAcquiringResult checkAcquiringResult = acquiringInsuranceService.checkAvailabilityAcquiring(
                    rq.getSurName(), rq.getFirstName(), rq.getMiddleName(), rq.getBirthDate(), rq.getPhoneNumber());
            if (checkAcquiringResult.isCanAcquire()) {
                Long clientId = checkAcquiringResult.getClientEntity().isPresent() ? checkAcquiringResult.getClientEntity().get().getId() : null;
                AcquiringInfo info = acquiringInfoService.saveAsNew(converter.convertInfo(acquiringProgram, rq, clientId));
                AcquiringInsuranceRs response = converter.convert(info);
                response.setNeedShowExist(checkAcquiringResult.getDescription() != null);
                return ResponseEntity.ok(response);
            } else {
                throw new NotSupportedException(checkAcquiringResult.getDescription());
            }
        } else {
            AcquiringInfo info = acquiringInfoService.saveAsNew(converter.convertInfo(acquiringProgram, clientService.get(principalData)));
            return ResponseEntity.ok(converter.convert(info));
        }
    }

    @Override
    public ResponseEntity<AcquiringInsuranceRs> issueInsurance(@Valid @RequestBody AcquiringInsuranceRq request) throws Exception {
        AcquiringInfo info = acquiringInfoService.findByUuid(request.getUuid());
        if (info == null || info.getStatus() != AcquiringStatus.REGISTRATION ||
                (!isProgramAvailable(info.getAcquiringProgram()) && !(info.getInsurance() != null && info.getInsurance().getCode() != null))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Insurance insurance = info.getInsurance();
        BaseInsuranceModel model;
        if (insurance == null) {
            insurance = new Insurance();
            model = converter.convertToBaseInsuranceModel(request, info);
            if (request.getId() != null) {
                //если задан id клиента, то необходимо обновить только его email
                ClientEntity clientEntity = clientService.get(request.getId());
                clientEntity.setEmail(request.getEmail());
                model.setHolderData(ClientEntity.toClient(clientEntity));
            }
            ProgramSetting programSetting = programSettingService.findById(model.getProgramSettingId());
            if (programSetting != null && programSetting.getStaticDate() != null
                    && programSetting.getStaticDate().compareTo(new Date()) < 0) {
                acquiringInfoService.saveAsError(request.getUuid(), "Ошибка валидации");
                throw new AcquiringException(STATIC_DATE_LESS_CURRENT_DATE);
            }
            insuranceConverter.updateInsuranceFromBaseModel(model, insurance, null, null, null, null);
        } else {
            model = insuranceConverter.convertInsuranceToBaseModel(insurance);
        }
        final Long insuranceId = insurance.getId();
        if (model.getHolderId() != null) {
            List<Insurance> kspContract = insuranceService.findAllByProgramKindAndClient(ProgramKind.KSP, model.getHolderId());
            kspContract = kspContract.stream().filter(item -> !item.getId().equals(insuranceId)).collect(Collectors.toList());
            if (!kspContract.isEmpty()) {
                throw new AcquiringException(existKspError);
            }
        }
        List<CheckModel> errors = insuranceCheckService.getContractErrors(model, insurance, InsuranceStatusCode.DRAFT.name());
        if (!CollectionUtils.isEmpty(errors)) {
            acquiringInfoService.saveAsError(request.getUuid(), "Ошибка валидации");
            throw new AcquiringException(acquiringError);
        }
        try {
            info = acquiringInsuranceService.issueInsurance(model, request.getUuid(), insuranceId, request.isIsMobile(),
                    info.getAcquiringProgram() != null ? info.getAcquiringProgram().getId() : null);
        } catch (AcquiringException ex) {
            LOGGER.error(ex.getMessage(), ex);
            String message = ex.getMessage().length() > 255 ? ex.getMessage().substring(0, 254) : ex.getMessage();
            acquiringInfoService.saveAsError(info.getUuid(), message);
            throw new AcquiringException(acquiringError);
        }
        return ResponseEntity.ok(converter.convert(info));
    }

    private boolean isProgramAvailable(AcquiringProgram acquiringProgram) {
        boolean isAuthorized = principalDataSource.getPrincipalData() != null;
        return !(acquiringProgram == null
                || (isAuthorized && !acquiringProgram.getAuthorizedZoneEnable())
                || (!isAuthorized && !acquiringProgram.getNotAuthorizedZoneEnable())
                || LocalDate.now().isBefore(acquiringProgram.getStartDate())
                || !LocalDate.now().isBefore(acquiringProgram.getEndDate()));
    }

    private boolean isInsuranceAvailable(ClientEntity holder, PrincipalData principalData, AcquiringFindByCodeRq rq) throws RestClientException {
        String phoneNumber = holder.getPhones().stream().filter(PhoneForClaimEntity::isMain).map(PhoneForClaimEntity::getNumber).findFirst().orElse(null);
        if (principalData == null) {
            return Objects.equals(holder.getSurName().toUpperCase(), rq.getSurName().toUpperCase());
        } else {
            EmploeeDataWithOrgUnits client = employeesClient.getEmployeeByIdWithOutPermission(principalData.getId(), 10L);
            return Objects.equals(holder.getSurName().toUpperCase(), principalData.getSecondName().toUpperCase())
                    && Objects.equals(holder.getFirstName().toUpperCase(), principalData.getFirstName().toUpperCase())
                    && Objects.equals(holder.getBirthDate(), client.getBirthDate()) &&
                    Objects.equals(phoneNumber, principalData.getMobilePhone());
        }
    }
}