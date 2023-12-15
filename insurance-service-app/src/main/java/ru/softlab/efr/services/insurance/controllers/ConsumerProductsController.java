package ru.softlab.efr.services.insurance.controllers;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.config.Permissions;
import ru.softlab.efr.services.insurance.converter.InsuranceConverter;
import ru.softlab.efr.services.insurance.converter.ProgramSettingConverter;
import ru.softlab.efr.services.insurance.exception.AcquiringException;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.ReportableRedemption;
import ru.softlab.efr.services.insurance.model.enums.SourceEnum;
import ru.softlab.efr.services.insurance.model.rest.ContractTemplatesRs;
import ru.softlab.efr.services.insurance.model.rest.ListInsuranceModel;
import ru.softlab.efr.services.insurance.model.rest.RedemptionList;
import ru.softlab.efr.services.insurance.model.rest.ViewInsuranceModel;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.service.AcquiringInsuranceService;
import ru.softlab.efr.services.insurance.services.BuyoutService;
import ru.softlab.efr.services.insurance.services.ClientService;
import ru.softlab.efr.services.insurance.services.InsurancePrintFormService;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.services.models.PrintFormServiceResponse;
import ru.softlab.efr.services.insurance.utils.StringUtils;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с клиентским договором.
 *
 * @author Uskov
 * @since 13.12.2018
 */
@RestController
public class ConsumerProductsController implements ConsumerProductsApi {

    private final ClientService clientService;
    private final PrincipalDataSource principalDataSource;
    private final InsuranceService insuranceService;
    private final InsuranceConverter insuranceConverter;
    private final BuyoutService buyoutService;
    private final ProgramSettingConverter programSettingConverter;
    private final InsurancePrintFormService insurancePrintFormService;
    private final AcquiringInsuranceService acquiringInsuranceService;

    /**
     * Конструктор
     *
     * @param insuranceService          сервис договоров страхования
     * @param clientService             сервис работы с клиентами
     * @param principalDataSource       принципал
     * @param buyoutService             сервис расчета выкупных сум
     * @param programSettingConverter   конвертер программ страхования
     * @param insurancePrintFormService сервис работы с печатными формами
     * @param acquiringInsuranceService сервис программ для оформления в ЛК
     */
    @Autowired
    public ConsumerProductsController(InsuranceService insuranceService, ClientService clientService, InsuranceConverter insuranceConverter, PrincipalDataSource principalDataSource, BuyoutService buyoutService, ProgramSettingConverter programSettingConverter, InsurancePrintFormService insurancePrintFormService, AcquiringInsuranceService acquiringInsuranceService) {
        this.insuranceService = insuranceService;
        this.clientService = clientService;
        this.principalDataSource = principalDataSource;
        this.insuranceConverter = insuranceConverter;
        this.buyoutService = buyoutService;
        this.programSettingConverter = programSettingConverter;
        this.insurancePrintFormService = insurancePrintFormService;
        this.acquiringInsuranceService = acquiringInsuranceService;
    }

    @Override
    @HasRight(Right.CLIENT_VIEW_CONTRACT)
    public ResponseEntity<ViewInsuranceModel> getConsumerContractV2(@PathVariable("id") Long id) throws Exception {
        Insurance insurance = getInsurance(id);
        if (insurance == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(insuranceConverter.convertInsuranceToConsumerViewModel(insurance));
    }

    @Override
    @HasPermission(Permissions.CLIENT_VIEW_CONTRACT)
    public ResponseEntity<RedemptionList> getRedemptionList(@PathVariable("contractId") Long contractId) throws Exception {

        Insurance insurance = getInsurance(contractId);
        if (insurance == null) {
            return ResponseEntity.notFound().build();
        }

        List<ReportableRedemption> redemptions = buyoutService.getRedemptionByContract(insurance, null);
        return ResponseEntity.ok(new RedemptionList(redemptions.stream().map(insuranceConverter::convert).collect(Collectors.toList())));
    }

    @Override
    @HasRight(Right.CLIENT_VIEW_CONTRACTS_LIST)
    public ResponseEntity<Page<ListInsuranceModel>> listConsumerContractV2(Pageable pageable) throws Exception {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        Pageable pageableDesc = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                InsuranceRepository.CONTRACT_SORT_BY_ID);
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
            return ResponseEntity.badRequest().build();
        }

        Page<Insurance> insurances = insuranceService.findAllByHoldersPageable(clients, pageableDesc);
        return ResponseEntity.ok(insurances.map(insuranceConverter::convertInsuranceToListModel));
    }

    @Override
    @HasRight(Right.CLIENT_VIEW_CONTRACT)
    public ResponseEntity<ContractTemplatesRs> getContractDocuments(@PathVariable("id") Long id) {
        Insurance insurance = getInsurance(id);
        if (insurance == null) {
            return ResponseEntity.notFound().build();
        }
        List<String> documentTemplateList;
        if (insurance.getSource() != SourceEnum.OFFICE) {
            documentTemplateList = insurance.getAcquiringProgram() != null ? insurance.getAcquiringProgram().getDocumentTemplateList() : new ArrayList<>();
        } else {
            documentTemplateList = insurance.getProgramSetting().getDocumentTemplateList();
        }
        return ResponseEntity.ok(new ContractTemplatesRs(programSettingConverter.getDocumentTemplateById(documentTemplateList)));
    }

    @Override
    @HasRight(Right.CLIENT_VIEW_CONTRACT)
    public ResponseEntity<Resource> printContract(@PathVariable("id") Long id, @PathVariable("templateId") String templateId) throws Exception {
        Insurance insurance = getInsurance(id);
        if (insurance == null) {
            return ResponseEntity.notFound().build();
        }
        PrintFormServiceResponse response = insurancePrintFormService.printContract(insurance, templateId);
        if (Objects.isNull(response)) {
            return ResponseEntity.notFound().build();
        }
        String encodedFileName = URLEncoder.encode(response.getFilename(), StandardCharsets.UTF_8.name()).replaceAll("\\+", " ");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName)
                .body(response.getByteArrayResource());
    }

    @Override
    @HasRight(Right.CLIENT_VIEW_CONTRACT)
    public ResponseEntity<Void> sendContractDocuments(@PathVariable("id") Long id, @Valid @RequestParam(value = "email", required = false) String email) {

        Insurance insurance = getInsurance(id);
        if (insurance == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            if (email != null) {
                insurance.getHolder().setEmail(email);
            }
            acquiringInsuranceService.sendDocumentEmail(insurance);
        } catch (AcquiringException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    private Insurance getInsurance(Long id) {
        PrincipalData principalData = principalDataSource.getPrincipalData();

        Insurance insurance = insuranceService.findById(id);
        if (insurance == null) {
            return null;
        }
        if (!AccessChecker.isInsuranceAccessible(insurance, principalData)) {
            throw new IllegalStateException("Договор не принадлежит данному клиенту.");
        }
        return insurance;
    }

    /**
     * Класс, проверяющий доступ пользователя к договорам страхования.
     */
    static class AccessChecker {

        /**
         * Проверка доступа пользователя к договору страхования.
         *
         * @param insurance     Договор страхования.
         * @param principalData Информация о пользователе.
         * @return true - доступ к договору страхования есть; false - доступа нет.
         */
        static boolean isInsuranceAccessible(Insurance insurance, PrincipalData principalData) {
            if (principalData.getRights().contains(Right.VIEW_PERSONAL_OFFICE)) return true;
            ClientEntity client = insurance.getHolder();
            List<String> phoneNumbers = new ArrayList<>();
            client.getPhones().forEach(phone -> phoneNumbers.add(phone.getNumber()));
            return principalData.getFirstName().equals(client.getFirstName())
                    && principalData.getSecondName().equals(client.getSurName())
                    && StringUtils.equals(principalData.getMiddleName(), client.getMiddleName())
                    && phoneNumbers.contains(principalData.getMobilePhone());
        }
    }
}
