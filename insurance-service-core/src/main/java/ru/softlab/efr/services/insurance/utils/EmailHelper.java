package ru.softlab.efr.services.insurance.utils;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * Утилитарный класс предоставляющий методы работы с email
 *
 * @author olshansky
 * @since 27.03.2019
 */
public class EmailHelper {

    /**
     * Формирование структуры email-сообщения с вложениями
     *
     * @param to                Адрес получателя
     * @param subject           Тема письма
     * @param body              Тело письма
     * @param templates         Список шаблонов документов
     * @param bodyContentType   Тип контента, передаваемый в header
     * @return Сообщение с вложениями
     */
    public static MimeMessage getMimeMessage(Session session, String to, String subject, String body, Map<String, byte []> templates, String bodyContentType) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(body, String.format("%s; charset=UTF-8", bodyContentType));
        multipart.addBodyPart(messageBodyPart);
        if (Objects.nonNull(templates)) {
            for (Map.Entry<String, byte[]> templateInfo : templates.entrySet()) {
                String filename = templateInfo.getKey();
                byte[] content = templateInfo.getValue();
                multipart.addBodyPart(getAttachmentBodyPart(content, filename));
            }
        }
        message.setContent(multipart);
        message.saveChanges();
        return message;
    }

    private static BodyPart getAttachmentBodyPart(byte[] byteArray, String fileName) throws MessagingException, UnsupportedEncodingException {
        String mimeType = "application/pdf";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        MimeBodyPart result = new MimeBodyPart();
        ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(byteArray, mimeType);
        byteArrayDataSource.setName(fileName);
        result.setDataHandler(new DataHandler(byteArrayDataSource));
        result.setFileName(byteArrayDataSource.getName());
        result.addHeader("Content-Type", mimeType.concat("; name*=UTF-8''").concat(encodedFileName));
        result.addHeader("Content-Disposition", "attachment; filename*=UTF-8''".concat(encodedFileName));
        return result;
    }
}