package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.utils.EmailHelper;

import java.util.Map;

/**
 * Сервис отправки уведомлений
 *
 * @author olshansky
 * @since 30.10.2018
 */
@Service
public class NotifyService {

    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(NotifyService.class);

    private final JavaMailSender emailSender;

    @Autowired
    public NotifyService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public boolean sendEmail(String to, String subject, String body, Map<String, byte[]> templates, String bodyContentType) {
        try {
            emailSender.send(EmailHelper.getMimeMessage(emailSender.createMimeMessage().getSession(), to, subject, body, templates, bodyContentType));
            return true;
        } catch (Exception ex) {
            LOGGER.error("Произошла ошибка при отправке email!", ex);
            return false;
        }
    }
}
