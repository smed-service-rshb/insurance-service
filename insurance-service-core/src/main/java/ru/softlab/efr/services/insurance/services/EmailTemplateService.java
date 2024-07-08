package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.softlab.efr.services.insurance.services.constants.EmailMessageParameter;
import ru.softlab.efr.services.insurance.services.constants.EmailTemplateParameter;
import ru.softlab.efr.services.insurance.services.types.InsuranceEmailedDto;
import ru.softlab.efr.services.insurance.services.types.PersonalEmailedDto;
import ru.softlab.efr.services.insurance.services.types.RequestEmailedDto;

import java.util.Locale;

@Service
public class EmailTemplateService {

    private TemplateEngine templateEngine;

    @Autowired
    public EmailTemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String getContentForAdminRequest(Long id, PersonalEmailedDto personal, RequestEmailedDto request) {
        Context context = new Context(Locale.getDefault());
        context.setVariable(EmailMessageParameter.REQUEST_ID, id);
        context.setVariable(EmailMessageParameter.PERSONAL, personal);
        context.setVariable(EmailMessageParameter.REQUEST, request);
        return templateEngine.process(EmailTemplateParameter.ADMIN_REQUEST_TEMPLATE, context);
    }
    /**
     * Подготовить тело электронного письма для отправки
     *
     * @param insuranceData данные договора
     * @return тело эл. письма
     */
    public String getInsurancePrintFormEmailContent(InsuranceEmailedDto insuranceData, PersonalEmailedDto personal) {
        Context context = new Context(Locale.getDefault());
        context.setVariable(EmailMessageParameter.INSURANCE, insuranceData);
        context.setVariable(EmailMessageParameter.PERSONAL, personal);
        return templateEngine.process(EmailTemplateParameter.ADMIN_REQUEST_TEMPLATE, context);
    }
}
