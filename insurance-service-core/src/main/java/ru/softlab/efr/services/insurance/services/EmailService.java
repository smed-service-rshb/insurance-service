package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.RequestEntity;
import ru.softlab.efr.services.insurance.services.types.PersonalEmailedDto;
import ru.softlab.efr.services.insurance.services.types.RequestEmailedDto;

import java.util.Objects;

@Service
public class EmailService {

    private NotifyService notifyService;
    private EmailTemplateService emailTemplateService;

    @Autowired
    public EmailService(NotifyService notifyService,
                        EmailTemplateService emailTemplateService) {
        this.notifyService = notifyService;
        this.emailTemplateService = emailTemplateService;
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
        notifyService.sendEmail(request.getTopic().getEmail(), subject, body, null, "text/html");
    }

}
