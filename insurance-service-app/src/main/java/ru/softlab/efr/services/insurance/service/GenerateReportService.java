package ru.softlab.efr.services.insurance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.AddressForClientEntity;
import ru.softlab.efr.services.insurance.model.db.ClientShortData;
import ru.softlab.efr.services.insurance.model.db.DocumentForClientShortData;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;
import ru.softlab.efr.services.insurance.repositories.ClientCheckDTO;
import ru.softlab.efr.services.insurance.repositories.InsuranceSummary;
import ru.softlab.efr.services.insurance.services.ClientCheckService;
import ru.softlab.efr.services.insurance.utils.ExcelReportExtractor;
import ru.softlab.efr.services.insurance.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

@Service
public class GenerateReportService {

    private static final String NOT_SPECIFIED = "(не указано)";
    private static final String NOT_VALID = "Не дейсвителен";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER_FOR_FILE = DateTimeFormatter.ofPattern("HH-mm");
    private static final String FILE_NAME_PARTIAL_CHECK = "_по выбранным данным";
    private static final String FILE_NAME_FOR_TERRORISTS = "Проверка по справочнику экстремистов";
    private static final String FILE_NAME_FOR_BLOCKAGES = "Проверка по решению межведомственной комиссии";
    private static final String FILE_NAME_FOR_INVALIDS_DOCS = "Недействительные паспорта";
    private static final String TITLE_FOR_BLOCKAGES = "Отчет о проверке клиентов по перечню лиц, в отношении которых действует решение " +
            "межведомственной комиссии о замораживании (блокировании) принадлежащих им денежных средств или иного имущества";
    private static final String TITLE_FOR_INVALIDS_DOCS = "Отчет о проверке клиентов по справочнику недействительных паспортов";
    private static final String TITLE_FOR_TERRORISTS = "Отчет о проверке клиентов по справочнику экстремистов и террористов";
    private static final List<String> HEAD_FOR_TERRORISTS_AND_BLOCKAGES = asList("п/н", "Фамилия", "Имя", "Отчество", "Дата рождения", "Адрес");
    private static final List<String> HEAD_FOR_INVALIDS_DOCS = asList("п/н", "ФИО", "Серия паспорта", "Номер паспорта", "Номер договора",
            "Дата договора", "Статус паспорта");

    private ClientCheckService checkService;

    public static String presentLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return NOT_SPECIFIED;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Autowired
    public void setClientCheckService(ClientCheckService clientCheckService) {
        this.checkService = clientCheckService;
    }

    /**
     * Формирует отчет по справочнику террористов не прошедших проверку
     *
     * @return возвращает байтовый массив файла со всеми данными внутри (кроме названия файла)
     */
    @Transactional(readOnly = true)
    public ByteArrayOutputStream createReportForTerrorists(List<Long> clientIds) {

        List<List<Object>> data = new ArrayList<>();
        Integer clientChecksSize = checkService.countByType(CheckUnitTypeEnum.TERRORIST, clientIds);
        List<ClientCheckDTO> terroristsInfo = checkService.findAllTerroristsAndBlocked(CheckUnitTypeEnum.TERRORIST, clientIds);
        String mainTitle = titleWithInfo(TITLE_FOR_TERRORISTS, CheckUnitTypeEnum.TERRORIST, clientChecksSize, terroristsInfo.size());

        for (int i = 0; i < terroristsInfo.size(); i++) {
            List<Object> row = new ArrayList<>();
            row.add(i + 1);
            row.addAll(convertTerroristAndBlockageToTemplate(terroristsInfo.get(i)));
            data.add(row);
        }

        try (ExcelReportExtractor extractor = new ExcelReportExtractor()) {
            extractor.addReport(mainTitle, HEAD_FOR_TERRORISTS_AND_BLOCKAGES, data);

            return extractor.getByteArrayOutputStreamFromExcel();
        }
    }

    @Transactional(readOnly = true)
    public ByteArrayOutputStream createReportForBlockages(List<Long> clientIds) {
        List<List<Object>> data = new ArrayList<>();
        Integer clientChecksSize = checkService.countByType(CheckUnitTypeEnum.BLOCKAGE, clientIds);
        List<ClientCheckDTO> blockagesInfo = checkService.findAllTerroristsAndBlocked(CheckUnitTypeEnum.BLOCKAGE, clientIds);
        String mainTitle = titleWithInfo(TITLE_FOR_BLOCKAGES, CheckUnitTypeEnum.BLOCKAGE, clientChecksSize, blockagesInfo.size());

        for (int i = 0; i < blockagesInfo.size(); i++) {
            List<Object> row = new ArrayList<>();
            row.add(i + 1);
            row.addAll(convertTerroristAndBlockageToTemplate(blockagesInfo.get(i)));
            data.add(row);
        }

        try (ExcelReportExtractor extractor = new ExcelReportExtractor()) {
            extractor.addReport(mainTitle, HEAD_FOR_TERRORISTS_AND_BLOCKAGES, data);

            return extractor.getByteArrayOutputStreamFromExcel();
        }
    }

    @Transactional(readOnly = true)
    public ByteArrayOutputStream createReportForInvalidDocs(List<Long> clientIds) {
        List<List<Object>> data = new ArrayList<>();
        Integer clientChecksSize = checkService.countByType(CheckUnitTypeEnum.INVALID_IDENTITY_DOC, clientIds);
        List<ClientShortData> invalidDocsInfo = checkService.findAllSuspiciousClients(CheckUnitTypeEnum.INVALID_IDENTITY_DOC, clientIds);
        String mainTitle = titleWithInfo(TITLE_FOR_INVALIDS_DOCS, CheckUnitTypeEnum.INVALID_IDENTITY_DOC, clientChecksSize, invalidDocsInfo.size());
        invalidDocsInfo.forEach(clientEntity -> {
            //берем все контракты относящиеся к пользователю - clientEntity
            List<InsuranceSummary> summaryList = checkService.findAllContracts(clientEntity.getId());
            //формируем отдельную строку в отчете, для каждого найденного контракта
            summaryList.stream().map(summary -> convertInvalidsDocsToTemplate(clientEntity, summary)).forEach(data::add);
            //в случае если контрактов нет, просто выводим данные о пользователе
            if (summaryList.isEmpty()) {
                List<Object> objects = convertInvalidsDocsToTemplate(clientEntity, new InsuranceSummary());
                data.add(objects);
            }
        });

        data.forEach(item -> item.add(0, data.indexOf(item) + 1));

        try (ExcelReportExtractor extractor = new ExcelReportExtractor()) {
            //добавляем в отчет шапку, название заголовков и данные
            extractor.addReport(mainTitle, HEAD_FOR_INVALIDS_DOCS, data);

            return extractor.getByteArrayOutputStreamFromExcel();
        }
    }

    private List<Object> convertInvalidsDocsToTemplate(ClientShortData clientEntity, InsuranceSummary summary) {
        List<Object> data = new ArrayList<>();

        String docSeries = NOT_SPECIFIED;
        String docNumber = NOT_SPECIFIED;
        String contractNumber = summary.getContractNumber() != null ? summary.getContractNumber() : NOT_SPECIFIED;
        String creationDate = presentLocalDateTime(summary.getCreationDate());
        Optional<DocumentForClientShortData> idDocClient = clientEntity.getDocuments().stream().filter(f -> f.isActive() && f.isMain()).findFirst();
        if (idDocClient.isPresent()) {
            docSeries = idDocClient.get().getDocSeries();
            docNumber = idDocClient.get().getDocNumber();
        }
        data.add(StringUtils.getClientFullName(clientEntity.getSurName(), clientEntity.getFirstName(), clientEntity.getMiddleName()));
        data.add(docSeries);
        data.add(docNumber);
        data.add(contractNumber);
        data.add(creationDate);
        //статус паспорта
        data.add(NOT_VALID);
        return data;
    }

    private List<Object> convertTerroristAndBlockageToTemplate(ClientCheckDTO clientEntity) {
        if (clientEntity != null) {
            List<Object> data = new ArrayList<>();
            data.add(clientEntity.getSurname());
            data.add(clientEntity.getFirstName());
            data.add(clientEntity.getMiddleName());
            data.add(ReportableContract.presentLocalDate(clientEntity.getBirthDate()));
            data.add(AddressForClientEntity.toAddressForClientEntity(clientEntity.getAddresses()).getAddress());
            return data;
        }
        return new ArrayList<>();
    }

    /**
     * @param mainTitle наименование документа для шапки файла
     * @param type      тип необходимого пользователя
     * @param sizeMatch
     * @return наименование + информация о проверке
     */
    private String titleWithInfo(String mainTitle, CheckUnitTypeEnum type, int allCheckSize, int sizeMatch) {
        int sizeMatches;
        switch (type) {
            case TERRORIST:
                sizeMatches = sizeMatch;
                break;
            case BLOCKAGE:
                sizeMatches = sizeMatch;
                break;
            case INVALID_IDENTITY_DOC:
                sizeMatches = sizeMatch;
                break;
            default:
                sizeMatches = 0;
        }

        return String.format("%s %n" + "Дата проведенной проверки: %s%n Время проведенной проверки: %s %n" +
                        "Количество проверенных клиентов: %s %n Количество совпадений: %s", mainTitle,
                presentLocalDateTime(LocalDateTime.now()), LocalTime.now().format(TIME_FORMATTER), allCheckSize, sizeMatches);
    }

    public String fileName(CheckUnitTypeEnum type, boolean isPartialCheck) {
        String mainFileName;
        switch (type) {
            case TERRORIST:
                mainFileName = FILE_NAME_FOR_TERRORISTS;
                break;
            case BLOCKAGE:
                mainFileName = FILE_NAME_FOR_BLOCKAGES;
                break;
            case INVALID_IDENTITY_DOC:
                mainFileName = FILE_NAME_FOR_INVALIDS_DOCS;
                break;
            default:
                mainFileName = "Название не задано";
        }
        mainFileName = isPartialCheck ? mainFileName + FILE_NAME_PARTIAL_CHECK : mainFileName;
        mainFileName = String.format("%s %s %s", mainFileName, presentLocalDateTime(LocalDateTime.now()), LocalTime.now().format(TIME_FORMATTER_FOR_FILE));
        return mainFileName;
    }
}
