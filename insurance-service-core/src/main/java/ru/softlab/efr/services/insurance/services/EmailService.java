package ru.softlab.efr.services.insurance.services;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.db.RequestEntity;
import ru.softlab.efr.services.insurance.services.types.InsuranceEmailedDto;
import ru.softlab.efr.services.insurance.services.types.PersonalEmailedDto;
import ru.softlab.efr.services.insurance.services.types.RequestEmailedDto;
import ru.softlab.efr.services.insurance.utils.AppUtils;

import java.util.*;

/**
 * Сервис генерации и отправки электронных писем
 */
@Service
public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class);

    private static final String EMAIL_BODY_TYPE = "text/html";
    private static final String INSURANCE_FORM_EMAIL_SUBJECT = "Документы по договору №%s СоюзМедСервис";


    private NotifyService notifyService;
    private EmailTemplateService emailTemplateService;
    private InsurancePrintFormService printService;

    /**
     * Конструктор класса
     *
     * @param notifyService        сервис уведомлений
     * @param emailTemplateService сервис шаблонов электронных писем
     * @param printService         сервис печати форм
     */

    @Autowired
    public EmailService(NotifyService notifyService,
                        EmailTemplateService emailTemplateService,
                        InsurancePrintFormService printService) {
        this.notifyService = notifyService;
        this.emailTemplateService = emailTemplateService;
        this.printService = printService;
    }

    public void sendAdminRequest(RequestEntity request) {
        PersonalEmailedDto personal = new PersonalEmailedDto();
        personal.setName(request.getClient().getFirstName());
        personal.setSurName(request.getClient().getSurName());
        personal.setThirdName(request.getClient().getMiddleName());
        personal.setBirthDate(request.getClient().getBirthDate());
        RequestEmailedDto requestEmailed = new RequestEmailedDto();
        requestEmailed.setRequestTopic(request.getTopic().getName());
        requestEmailed.setRequestDate(request.getRequestDate());
        if (Objects.nonNull(request.getInsurance())) {
            requestEmailed.setInsuranceNumber(request.getInsurance().getContractNumber());
        } else {
            requestEmailed.setInsuranceNumber("Обращение без указания договора");
        }
        requestEmailed.setPhone(request.getPhone());
        requestEmailed.setEmail(request.getEmail());
        requestEmailed.setText(request.getRequestText());
        String subject = String.format("Обращение из Личного кабинета %s от %s %s %s дата обращения: %s",
                request.getId(),
                request.getClient().getSurName(), request.getClient().getFirstName(), request.getClient().getMiddleName(),
                request.getRequestDate());
        String body = emailTemplateService.getContentForAdminRequest(request.getId(), personal, requestEmailed);
        notifyService.sendEmail(request.getTopic().getEmail(), subject, body, null, EMAIL_BODY_TYPE);
    }

    /**
     * Отправить на электронную почту пользователю данные о договоре страхования (включая ПФ)
     *
     * @param insurance данные договора страхования
     */
    @Transactional(readOnly = true)
    public void sendInsuranceTemplates(Insurance insurance) {
        if (insurance == null) {
            return;
        }

        List<String> contractTemplatesIds = Optional.ofNullable(insurance.getProgramSetting())
                .map(ProgramSetting::getDocumentTemplateList)
                .orElseGet(Collections::emptyList);

        String holderEmail = Optional.ofNullable(insurance.getHolder())
                .map(ClientEntity::getEmail)
                .orElse(StringUtils.EMPTY);

        if (CollectionUtils.isEmpty(contractTemplatesIds) || StringUtils.isBlank(holderEmail)) {
            return;
        }

        Map<String, byte[]> templates = new HashMap<>();

        contractTemplatesIds.stream()
                .map(templateId -> {
                    try {
                        return printService.printContract(insurance, templateId);
                    } catch (Exception e) {
                        LOGGER.error(String.format("Ошибка формирования ПФ с id=%s, причина: %s", templateId, e.getLocalizedMessage()));
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(template -> template.getByteArrayResource() != null)
                .forEach(template -> templates.put(template.getFilename(), template.getByteArrayResource().getByteArray()));

        InsuranceEmailedDto insuranceEmailedDto = new InsuranceEmailedDto();
        insuranceEmailedDto.setNumber(insurance.getContractNumber());

        PersonalEmailedDto personalEmailedDto = new PersonalEmailedDto();
        personalEmailedDto.setFullName(AppUtils.getFullName(insurance.getHolder()));

        notifyService.sendEmail(holderEmail,
                String.format(INSURANCE_FORM_EMAIL_SUBJECT, insurance.getContractNumber()),
                emailTemplateService.getInsurancePrintFormEmailContent(insuranceEmailedDto, personalEmailedDto),
                templates,
                EMAIL_BODY_TYPE);
    }


}
