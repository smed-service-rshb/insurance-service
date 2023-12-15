package ru.softlab.efr.services.insurance.services;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.db.Insurance;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class InsuranceServiceTest {

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private TemplateService templateService;

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testNotificationSuccess() throws Exception{
        Insurance insurance = insuranceService.findById(2L);
        insurance.getProgramSetting().getProgram().setName("Тестовая программа");
        insurance.getProgramSetting().setDocumentTemplateList(Arrays.asList("RshbInsuranceRent01", "RshbInsuranceRent02"));
        ServerSetup serverSetup = new ServerSetup(3025,"localhost", "smtp");
        serverSetup.setServerStartupTimeout(10000);
        GreenMail greenMail = new GreenMail(serverSetup);
        try {
            greenMail.start();
            insuranceService.notifyClient(insurance, "1111", "1001", "Московский", templateService.getAllTemplates(insurance));
            String code = insurance.getCode();
            assertNotNull(GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));

            MimeMessage message = greenMail.getReceivedMessages()[0];
            MimeMultipart body = (MimeMultipart) message.getContent();
            assertTrue(body.getContentType().startsWith("multipart/mixed"));
//            assertEquals(3, body.getCount());
//
//            BodyPart text = body.getBodyPart(0);
//
//            System.out.println(text.getContent());
//            assertTrue(((String) text.getContent()).contains(code));
//
//            BodyPart attachment = body.getBodyPart(1);
//
//            InputStream attachmentStream = (InputStream) attachment.getContent();
//            byte[] bytes = IOUtils.toByteArray(attachmentStream);
//            assertNotNull(bytes);
//            assertTrue(bytes.length > 130000);
        } finally {
            greenMail.stop();
        }
    }
}
