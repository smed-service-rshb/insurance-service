package ru.softlab.efr.services.insurance.services;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.softlab.efr.common.client.PrintTemplatesClient;
import ru.softlab.efr.common.dict.exchange.model.PrintTemplate;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;
import ru.softlab.ib6.reporting.model.Reportable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Сервис для работы с шаблонами документов и печатных форм
 *
 * @author olshansky
 * @author Kalantaev
 * @since 12.12.2018
 */
@Service
@PropertySource(value = {"classpath:application.properties",
        "classpath:messages.properties"}, encoding = "UTF-8")
public class TemplateService {

    private static final Logger LOGGER = Logger.getLogger(TemplateService.class);
    private static final Long COMMON_DICT_TIMEOUT = 10L;

    @Value("${application.mode}")
    private String applicationMode;

    @Autowired
    private PrintTemplatesClient printTemplatesClient;

    PrintTemplate createTemplate(byte[] content) throws Exception {
        return printTemplatesClient.create(new ByteArrayResource(content), COMMON_DICT_TIMEOUT);
    }

    public Resource getTemplateContent(String templateId) throws Exception {
        return printTemplatesClient.getContent(templateId, COMMON_DICT_TIMEOUT);
    }

    public PrintTemplate getPrintTemplate(String templateId) throws Exception {
        return printTemplatesClient.get(templateId, COMMON_DICT_TIMEOUT);
    }

    public Map<String, byte[]> getAllTemplates(Insurance insurance) {
        List<String> templates = insurance.getProgramSetting().getDocumentTemplateList();
        Map<String, byte[]> templatesByteList = new HashMap<>();
        if (templates != null) {
            for (String template : templates) {
                try {
                    templatesByteList.put(getPrintTemplate(template).getFileName().concat(".pdf"),
                            buildAndMergeTemplates(
                                    Collections.singletonList(getTemplateContent(template)),
                                    ReportableContract.emptyContract(),
                                    new JRPdfExporter()));
                } catch (Exception e) {
                    LOGGER.error("Ошибка при получении шаблона", e);
                }
            }
        }
        return templatesByteList;
    }

    public byte[] buildAndMergeTemplates(List<Resource> templates, Reportable construct, JRAbstractExporter exporter) throws JRException {
        Map<String, Object> paramMap = construct.getFieldValues();
        List<JasperPrint> jasperPrintList = new ArrayList<>();

        templates.forEach(template -> {
            try {
                jasperPrintList.add(buildTemplate(template, paramMap));
            } catch (JRException | IOException e) {
                LOGGER.error("Во время построения шаблона печатной формы произошла ошибка, причина:", e);
            }
        });
        return getBytes(jasperPrintList, exporter);
    }

    private byte[] getBytes(List<JasperPrint> jasperPrintList, JRAbstractExporter exporter) throws JRException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        exporter.exportReport();
        return byteArrayOutputStream.toByteArray();
    }

    private JasperPrint buildTemplate(Resource template, Map<String, Object> params) throws JRException, IOException {
        JasperReport report = JasperCompileManager.compileReport(template.getInputStream());
        return JasperFillManager.fillReport(report, params, new JREmptyDataSource());
    }

    public Boolean isTestInstance() {
        return applicationMode == null || applicationMode.equals("test");
    }

}
