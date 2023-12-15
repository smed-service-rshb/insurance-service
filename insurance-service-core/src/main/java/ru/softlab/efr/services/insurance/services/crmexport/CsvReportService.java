package ru.softlab.efr.services.insurance.services.crmexport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.services.crmexport.models.CrmReportModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CsvReportService {

    private static final Logger LOGGER = Logger.getLogger(CsvReportService.class);

    private CrmReportSettingsService crmReportSettingsService;

    @Autowired
    public CsvReportService(CrmReportSettingsService crmReportSettingsService) {
        this.crmReportSettingsService = crmReportSettingsService;
    }


    public void createReport(List<CrmReportModel> reportModels) {
        StringBuilder writeToFile = new StringBuilder();
        writeToFile
                .append("Уникальный код клиента,")
                .append("Обращение,")
                .append("Имя,")
                .append("Фамилия,")
                .append("Отчество,")
                .append("Дата рождения клиента,")
                .append("Мобильный телефон,")
                .append("Частный E-mail,")
                .append("Источник,")
                .append("Экспорт,")
                .append("Доступен для всех,")
                .append("Адрес регистрации,")
                .append("Регион,")
                .append("Серия и номер паспорта,")
                .append("Дата выдачи паспорта,")
                .append("Кем выдан паспорт,")
                .append("Код подразделения,")
                .append("Вид программы,")
                .append("Сумма оплаты,")
                .append("Дата оплаты,")
                .append("Программа")
                .append("\n");
        reportModels.forEach(reportModel -> writeToFile
                .append(reportModel.getClientCode())                  //"Уникальный код клиента,"
                .append(",")
                .append(reportModel.getClientGenderRequest())         //"Обращение,"
                .append(",")
                .append(reportModel.getClientName())                  //"Имя,"
                .append(",")
                .append(reportModel.getClientSecondname())            //"Фамилия,"
                .append(",")
                .append(reportModel.getClientThirdname())             //"Отчество,"
                .append(",")
                .append(reportModel.getClientBirthDate())             //"Дата рождения клиента,"
                .append(",")
                .append(reportModel.getClientMobilePhone())           //"Мобильный телефон,"
                .append(",")
                .append(reportModel.getClientEmail())                 //"Частный E-mail,"
                .append(",")
                .append(reportModel.getResource())                    //"Источник,"
                .append(",")
                .append(reportModel.getExported())                    //"Экспорт,"
                .append(",")
                .append(reportModel.getAvailability())                //"Доступен для всех,"
                .append(",")
                .append(reportModel.getRegistrationAddress())         //"Адрес регистрации,"
                .append(",")
                .append(reportModel.getRegistrationAddressRegion())   //"Регион,"
                .append(",")
                .append(reportModel.getPassportSeries())              //"Серия и номер паспорта,"
                .append(",")
                .append(reportModel.getPassportCreationDate())        //"Дата выдачи паспорта,"
                .append(",")
                .append(reportModel.getPassportRegistrationChair())   //"Кем выдан паспорт,"
                .append(",")
                .append(reportModel.getPassportDivisionCode())        //"Код подразделения,"
                .append(",")
                .append(reportModel.getGotProgram())                  //"Вид программы,"
                .append(",")
                .append(reportModel.getClientPayedSum())              //"Сумма оплаты,"
                .append(",")
                .append(reportModel.getPayedDate())                   //"Дата оплаты,"
                .append(",")
                .append(reportModel.getProgramKindName())              //"Программа"
                .append("\n"));

        StringBuilder filename = new StringBuilder();
        filename.append(crmReportSettingsService.getCsvPath())
                .append(LocalDateTime.now().toString())
                .append(".csv");
        try {
            File newFile = new File(filename.toString());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                writer.write(writeToFile.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Ошибка сохранения в CSV файл", e);
        }
    }
}
