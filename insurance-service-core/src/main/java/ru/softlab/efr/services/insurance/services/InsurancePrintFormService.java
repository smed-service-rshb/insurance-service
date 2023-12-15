package ru.softlab.efr.services.insurance.services;

import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.client.CurrenciesClient;
import ru.softlab.efr.common.dict.exchange.model.Currency;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.services.models.PrintFormServiceResponse;
import ru.softlab.efr.services.insurance.utils.EmployeeFilter;

import java.math.BigDecimal;
import java.util.*;

import static ru.softlab.efr.services.insurance.services.InsuranceAvailabilityService.isProgramAllowable;

/**
 * Сервис для работы с печатными формами.
 *
 * @author olshansky
 * @author Danilov
 * @since 21.03.2019
 */
@Service
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class InsurancePrintFormService {
    private static final Logger LOGGER = Logger.getLogger(InsurancePrintFormService.class);

    private InsuranceRepository insuranceRepository;
    private BuyoutService buyoutService;
    private TemplateService templateService;
    private CurrenciesClient currenciesClient;
    private CalculationService calculationService;
    private PrincipalDataSource principalDataSource;
    private InsuranceService insuranceService;

    @Value("${insurance.error.template.not.found}")
    private String TEMPLATE_NOT_FOUND;
    @Value("${insurance.error.not.found}")
    private String INSURANCE_NOT_FOUND;

    private static final long GET_CURRENCIES_TIMEOUT = 10L;


    @Autowired
    InsurancePrintFormService(InsuranceRepository insuranceRepository,
                              BuyoutService buyoutService,
                              TemplateService templateService,
                              CurrenciesClient currenciesClient,
                              CalculationService calculationService,
                              PrincipalDataSource principalDataSource,
                              InsuranceService insuranceService) {
        this.insuranceRepository = insuranceRepository;
        this.buyoutService = buyoutService;
        this.templateService = templateService;
        this.currenciesClient = currenciesClient;
        this.calculationService = calculationService;
        this.principalDataSource = principalDataSource;
        this.insuranceService = insuranceService;
    }

    @Transactional(readOnly = true)
    public PrintFormServiceResponse printContractByInsuranceIdAndTemplateId(Long id, String templateId,
                                                                            EmployeeFilter filter,
                                                                            Set<Long> offices) throws Exception {
        Resource template;
        String filename;
        String fileType = ".pdf";

        try {
            template = templateService.getTemplateContent(templateId);
            filename = templateService.getPrintTemplate(templateId).getFileName()
                    .replaceAll("\\r\\n", "");
        } catch (RestClientException ex) {
            LOGGER.error(String.format(TEMPLATE_NOT_FOUND, templateId), ex);
            return null;
        }

        ByteArrayResource byteArrayResource;

        if (id != 0) {
            Insurance insurance;
            Insurance insuranceNonAudited = insuranceService.findById(id, filter.canViewAllContract(), filter.isAdmin(), filter.getEmployeeOfficesFilter(),
                    filter.getEmployeeIdFilter(), filter.getEmployeeGroupFilter(), offices);

            if (Objects.nonNull(insuranceNonAudited)) {
                Revision<Integer, Insurance> lastChangeRevision = insuranceRepository.findLastChangeRevision(id);
                if (Objects.nonNull(lastChangeRevision)) {
                    insurance = lastChangeRevision.getEntity();
                } else {
                    // если нет в репозитории ревизий, попробовать найти в обычном репозитории
                    insurance = insuranceNonAudited;
                }
            } else {
                LOGGER.warn(String.format(INSURANCE_NOT_FOUND, id));
                return null;
            }
            insurance.setProgramSetting(insuranceNonAudited.getProgramSetting());
            if (!isProgramAllowable(insurance.getProgramSetting().getProgram(), principalDataSource.getPrincipalData())) {
                return null;
            }

            ClientEntity client = insurance.getHolder();

            String currency = getCurrencyById(insurance.getCurrency());

            byteArrayResource = new ByteArrayResource(templateService.buildAndMergeTemplates(
                    Collections.singletonList(template),
                    ReportableContract.construct(insurance, client,
                            templateService.isTestInstance(),
                            currency,
                            insurance.getAmount(),
                            insurance.getPremium() != null ? insurance.getPremium().multiply(new BigDecimal(
                                    calculationService.periodCount(insurance.getCalendarUnit(),
                                            insurance.getDuration(),
                                            insurance.getPeriodicity()))) : null,
                            buyoutService.getRedemptionByContract(insurance, currency),
                            buyoutService.getPayDateString(insurance)
                    ), new JRPdfExporter()));

            filename = insurance.getContractNumber() != null ?
                    filename.concat(" №").concat(insurance.getContractNumber()) : filename;
            filename += fileType;
        } else {
            byteArrayResource = new ByteArrayResource(templateService.buildAndMergeTemplates(
                    Collections.singletonList(template),
                    ReportableContract.emptyContract(),
                    new JRPdfExporter()));
        }

        PrintFormServiceResponse printFormServiceResponse = new PrintFormServiceResponse();
        printFormServiceResponse.setFilename(filename);
        printFormServiceResponse.setByteArrayResource(byteArrayResource);
        return printFormServiceResponse;
    }

    @Transactional(readOnly = true)
    public PrintFormServiceResponse printContract(Insurance insurance, String templateId) throws Exception {
        Resource template;
        String filename;
        String fileType = ".pdf";

        try {
            template = templateService.getTemplateContent(templateId);
            filename = templateService.getPrintTemplate(templateId).getFileName()
                    .replaceAll("\\r\\n", "");
        } catch (RestClientException ex) {
            LOGGER.error(String.format(TEMPLATE_NOT_FOUND, templateId), ex);
            return null;
        }

        ByteArrayResource byteArrayResource;

        ClientEntity client = insurance.getHolder();

        String currency = getCurrencyById(insurance.getCurrency());

        byteArrayResource = new ByteArrayResource(templateService.buildAndMergeTemplates(
                Collections.singletonList(template),
                ReportableContract.construct(insurance, client,
                        templateService.isTestInstance(),
                        currency,
                        insurance.getAmount(),
                        insurance.getPremium() != null ? insurance.getPremium().multiply(new BigDecimal(
                                calculationService.periodCount(insurance.getCalendarUnit(),
                                        insurance.getDuration(),
                                        insurance.getPeriodicity()))) : null,
                        buyoutService.getRedemptionByContract(insurance, currency),
                        buyoutService.getPayDateString(insurance)
                ), new JRPdfExporter()));

        filename = insurance.getContractNumber() != null ?
                filename.concat(" №").concat(insurance.getContractNumber()) : filename;
        filename += fileType;

        PrintFormServiceResponse printFormServiceResponse = new PrintFormServiceResponse();
        printFormServiceResponse.setFilename(filename);
        printFormServiceResponse.setByteArrayResource(byteArrayResource);
        return printFormServiceResponse;
    }

    private String getCurrencyById(Long currencyId) {
        String result = "Нет информации";
        try {
            Currency currency = currenciesClient.getCurrency(currencyId).get(GET_CURRENCIES_TIMEOUT);
            if (currency != null && currency.getLiteralISO() != null) {
                result = currency.getLiteralISO();
            }
        } catch (RestClientException e) {
            result = "Произошла ошибка при получении валюты";
            LOGGER.error(String.format("Во время запроса валюты c ID='%s' из сервиса common-dict произошла ошибка, причина: %s", currencyId, e), e);
        }
        return result;
    }

}
