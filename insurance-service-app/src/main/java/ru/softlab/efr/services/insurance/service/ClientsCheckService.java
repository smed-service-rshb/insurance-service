package ru.softlab.efr.services.insurance.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.softlab.efr.common.client.BlockagesClient;
import ru.softlab.efr.common.client.DictStatusClient;
import ru.softlab.efr.common.client.InvalidIdentityDocsClient;
import ru.softlab.efr.common.client.TerroristsClient;
import ru.softlab.efr.common.dict.exchange.model.*;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.insurance.model.db.ClientCheck;
import ru.softlab.efr.services.insurance.model.db.ClientShortData;
import ru.softlab.efr.services.insurance.model.db.DocumentForClientShortData;
import ru.softlab.efr.services.insurance.model.enums.CheckStateEnum;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;
import ru.softlab.efr.services.insurance.repositories.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.softlab.efr.services.insurance.controllers.Constants.CLIENT_CHECK_BATCH_SIZE;
import static ru.softlab.efr.services.insurance.controllers.Constants.SECURITY_REPORT_STORE_PATH;

/**
 * Сервис массовой проверки клиентов, который инициируется по завершению процесса загрузки справочника
 *
 * @author olshansky
 * @author Andrey Grigorov
 * @since 23.01.2019
 */
@Service
public class ClientsCheckService {

    private static final Logger LOGGER = Logger.getLogger(ClientsCheckService.class);
    private static final int MAX_COUNT_RETRY_CALL = 15;
    private static final long TIMEOUT_IN_SECONDS = 20L;
    private static final int DEFAULT_PAGE_SIZE = 500;
    private static final String DEFAULT_SECURITY_FILE_PATH = System.getProperty("java.io.tmpdir");

    private final BlockagesClient blockagesClientChecker;
    private final InvalidIdentityDocsClient passportChecker;
    private final TerroristsClient terroristChecker;
    private final SettingsService settingsService;
    private final DictStatusClient dictStatusClient;
    private final GenerateReportService generateReportService;
    private final ClientRepository clientRepository;
    private final ClientCheckRepository clientCheckRepository;
    private final ClientShortDataRepository clientShortDataRepository;
    private final DocumentClientShortDataRepository documentClientShortDataRepository;

    @Autowired
    public ClientsCheckService(BlockagesClient blockagesClientChecker, InvalidIdentityDocsClient passportChecker, TerroristsClient terroristChecker, SettingsService settingsService, DictStatusClient dictStatusClient, GenerateReportService generateReportService, ClientRepository clientRepository, ClientCheckRepository clientCheckRepository, ClientShortDataRepository clientShortDataRepository, DocumentClientShortDataRepository documentClientShortDataRepository) {
        this.blockagesClientChecker = blockagesClientChecker;
        this.passportChecker = passportChecker;
        this.terroristChecker = terroristChecker;
        this.settingsService = settingsService;
        this.dictStatusClient = dictStatusClient;
        this.generateReportService = generateReportService;
        this.clientRepository = clientRepository;
        this.clientCheckRepository = clientCheckRepository;
        this.clientShortDataRepository = clientShortDataRepository;
        this.documentClientShortDataRepository = documentClientShortDataRepository;
    }

    @Async("clientCheckThreadPoolTaskExecutor")
    @Transactional
    public void checkTerroristAsync(String updateId, List<Long> clientIds) {
        try {
            checkTerrorist(updateId, clientIds);
        } catch (IOException e) {
            LOGGER.error("Произошла ошибка при выполнении проверки по справочнику террористов", e);
        }
    }

    @Async("clientCheckThreadPoolTaskExecutor")
    @Transactional
    public void checkBlockagesAsync(String updateId, List<Long> clientIds) {
        try {
            checkBlockages(updateId, clientIds);
        } catch (IOException e) {
            LOGGER.error("Произошла ошибка при выполнении проверки по справочнику блокировок", e);
        }
    }

    @Async("clientCheckThreadPoolTaskExecutor")
    @Transactional
    public void checkInvalidIdentityDocAsync(String updateId, List<Long> clientIds) {
        try {
            checkInvalidIdentityDoc(updateId, clientIds);
        } catch (IOException e) {
            LOGGER.error("Произошла ошибка при выполнении проверки по справочнику недействительных паспортов", e);
        }
    }

    private void checkTerrorist(String updateId, List<Long> clientIds) throws IOException {
        final AtomicInteger clientCount = new AtomicInteger(0);
        try {
            int pageSize = getPageSizeOrDefault();
            MultiValueMap<Long, ClientForCheckSummary> clientForCheckSummaryMap = new LinkedMultiValueMap<>();
            final AtomicInteger pageNumber = new AtomicInteger(0);

            try (Stream<ClientForCheckSummary> allClientsForCheck = (clientIds != null) ?
                    clientRepository.findClientsForCheckByIdIn(clientIds) :
                    clientRepository.findAllClientsForCheck()) {
                allClientsForCheck.forEach(clientForCheckSummary -> {
                    if ((clientForCheckSummaryMap.size() >= pageSize) && !clientForCheckSummaryMap.containsKey(clientForCheckSummary.getId())) {
                        // Достигли желаемого размера запроса к common-dict, а также добились того, что все данные по одному
                        // клиенту присутствуют в clientForCheckSummaryMap.
                        checkTerrorist(clientForCheckSummaryMap, pageNumber.addAndGet(1), updateId);
                        clientCount.addAndGet(clientForCheckSummaryMap.size());
                        clientForCheckSummaryMap.clear();
                    }
                    clientForCheckSummaryMap.add(clientForCheckSummary.getId(), clientForCheckSummary);
                });
            }

            if (clientForCheckSummaryMap.size() > 0) {
                checkTerrorist(clientForCheckSummaryMap, pageNumber.addAndGet(1), updateId);
                clientCount.addAndGet(clientForCheckSummaryMap.size());
            }
        } catch (Exception ex) {
            LOGGER.error("Произошла ошибка во время проверки всех клиентов по справочнику террористов", ex);
        }
        LOGGER.info(String.format("Проверка по справочнику террористов завершена, всего проверено клиентов: %s", clientCount.get()));
        changeStatus(DictType.TERRORIST);
        generateReport(CheckUnitTypeEnum.TERRORIST, clientIds);
    }

    private void checkTerrorist(MultiValueMap<Long, ClientForCheckSummary> clientForCheckSummaryMap, int pageNumber, String updateId) {

        CheckTerroristRq request = new CheckTerroristRq(clientForCheckSummaryMap.values()
                .stream()
                .flatMap(Collection::stream)
                .map(clientForCheckSummary -> new PersonnelData(
                        clientForCheckSummary.getId().toString(),
                        clientForCheckSummary.getSurName(),
                        clientForCheckSummary.getFirstName(),
                        clientForCheckSummary.getMiddleName(),
                        clientForCheckSummary.getBirthDate(),
                        clientForCheckSummary.getDocSeries(),
                        clientForCheckSummary.getDocNumber())
                ).collect(Collectors.toList()));

        CheckTerroristRs checkResult = null;
        int currentErrorIterateNumber = 0;
        do {
            try {
                checkResult = terroristChecker.checkTerrorist(request, TIMEOUT_IN_SECONDS);
                currentErrorIterateNumber = 0;
            } catch (Exception ex) {
                currentErrorIterateNumber++;
                LOGGER.error("Произошла ошибка при выполнении запроса проверки клиентов по справочнику террористов", ex);
            }
        } while ((checkResult == null) && (currentErrorIterateNumber < MAX_COUNT_RETRY_CALL));

        if (checkResult == null) {
            LOGGER.error("Ответ от сервиса checkTerrorist не получен!");
        } else {
            LOGGER.info(String.format("Ответ от сервиса checkTerrorist получен! Страница: %s", pageNumber));

            if (CollectionUtils.isNotEmpty(checkResult.getCitizens())) {

                Map<Long, ClientShortData> clients = clientShortDataRepository.findByIds(clientForCheckSummaryMap.keySet())
                        .stream()
                        .collect(Collectors.toMap(ClientShortData::getId, client -> client));

                Map<String, Boolean> results = new HashMap<>();
                for (CheckTerroristResult result : checkResult.getCitizens()) {
                    results.compute(result.getId(), (key, value) -> (value == null)
                            ? result.isIsTerrorist()
                            : result.isIsTerrorist() || value);
                }

                for (Map.Entry<String, Boolean> result : results.entrySet()) {
                    try {
                        boolean isTerrorist = result.getValue();
                        ClientCheck clientCheck = new ClientCheck(
                                CheckUnitTypeEnum.TERRORIST,
                                isTerrorist ? CheckStateEnum.TRUE : CheckStateEnum.FALSE,
                                clients.get(Long.valueOf(result.getKey())),
                                isTerrorist ? "Клиент не прошёл проверку по справочнику экстремистов/террористов." : "Клиент прошёл проверку по справочнику экстремистов/террористов.",
                                Long.valueOf(updateId),
                                LocalDateTime.now());
                        clientCheckRepository.save(clientCheck);
                    } catch (Exception ex) {
                        LOGGER.error("Произошла ошибка при обработке результатов проверки клиента с ID = " + result.getKey()
                                + " по справочнику экстремистов/террористов.", ex);
                    }
                }

                clientCheckRepository.flush();
            } else {
                LOGGER.warn("Ответ от сервиса checkTerrorist получен пустой!");
            }
        }
    }

    private void checkBlockages(String updateId, List<Long> clientIds) throws IOException {
        final AtomicInteger clientCount = new AtomicInteger(0);
        try {
            int pageSize = getPageSizeOrDefault();
            MultiValueMap<Long, ClientForCheckSummary> clientForCheckSummaryMap = new LinkedMultiValueMap<>();
            final AtomicInteger pageNumber = new AtomicInteger(0);

            try (Stream<ClientForCheckSummary> allClientsForCheck = (clientIds != null) ?
                    clientRepository.findClientsForCheckByIdIn(clientIds) :
                    clientRepository.findAllClientsForCheck()) {
                allClientsForCheck.forEach(clientForCheckSummary -> {
                    if ((clientForCheckSummaryMap.size() >= pageSize) && !clientForCheckSummaryMap.containsKey(clientForCheckSummary.getId())) {
                        // Достигли желаемого размера запроса к common-dict, а также добились того, что все данные по одному
                        // клиенту присутствуют в clientForCheckSummaryMap.
                        checkBlockages(clientForCheckSummaryMap, pageNumber.addAndGet(1), updateId);
                        clientCount.addAndGet(clientForCheckSummaryMap.size());
                        clientForCheckSummaryMap.clear();
                    }
                    clientForCheckSummaryMap.add(clientForCheckSummary.getId(), clientForCheckSummary);
                });
            }

            if (clientForCheckSummaryMap.size() > 0) {
                checkBlockages(clientForCheckSummaryMap, pageNumber.addAndGet(1), updateId);
                clientCount.addAndGet(clientForCheckSummaryMap.size());
            }
        } catch (Exception ex) {
            LOGGER.error("Произошла ошибка во время проверки всех клиентов по справочнику блокировок", ex);
        }
        LOGGER.info(String.format("Проверка по справочнику блокировок завершена, всего проверено клиентов: %s", clientCount.get()));
        changeStatus(DictType.BLOCKAGE);
        generateReport(CheckUnitTypeEnum.BLOCKAGE, clientIds);
    }

    private void checkBlockages(MultiValueMap<Long, ClientForCheckSummary> clientForCheckSummaryMap, int pageNumber, String updateId) {

        CheckBlockagesRq request = new CheckBlockagesRq(clientForCheckSummaryMap.values()
                .stream()
                .flatMap(Collection::stream)
                .map(clientForCheckSummary -> new PersonnelData(
                        clientForCheckSummary.getId().toString(),
                        clientForCheckSummary.getSurName(),
                        clientForCheckSummary.getFirstName(),
                        clientForCheckSummary.getMiddleName(),
                        clientForCheckSummary.getBirthDate(),
                        clientForCheckSummary.getDocSeries(),
                        clientForCheckSummary.getDocNumber())
                ).collect(Collectors.toList()));

        CheckBlockagesRs checkResult = null;
        int currentErrorIterateNumber = 0;
        do {
            try {
                checkResult = blockagesClientChecker.checkBlockages(request, TIMEOUT_IN_SECONDS);
                currentErrorIterateNumber = 0;
            } catch (Exception ex) {
                currentErrorIterateNumber++;
                LOGGER.error("Произошла ошибка при выполнении запроса проверки клиентов по справочнику блокировок", ex);
            }
        } while ((checkResult == null) && (currentErrorIterateNumber < MAX_COUNT_RETRY_CALL));

        if (checkResult == null) {
            LOGGER.error("Ответ от сервиса checkBlockages не получен!");
        } else {
            LOGGER.info(String.format("Ответ от сервиса checkBlockages получен! Страница: %s", pageNumber));

            if (CollectionUtils.isNotEmpty(checkResult.getCitizens())) {

                Map<Long, ClientShortData> clients = clientShortDataRepository.findByIds(clientForCheckSummaryMap.keySet())
                        .stream()
                        .collect(Collectors.toMap(ClientShortData::getId, client -> client));

                Map<String, Boolean> results = new HashMap<>();
                for (CheckBlockagesResult result : checkResult.getCitizens()) {
                    results.compute(result.getId(), (key, value) -> (value == null)
                            ? result.isIsBlock()
                            : result.isIsBlock() || value);
                }

                for (Map.Entry<String, Boolean> result : results.entrySet()) {
                    try {
                        boolean isBlock = result.getValue();
                        ClientCheck clientCheck = new ClientCheck(
                                CheckUnitTypeEnum.BLOCKAGE,
                                isBlock ? CheckStateEnum.TRUE : CheckStateEnum.FALSE,
                                clients.get(Long.valueOf(result.getKey())),
                                isBlock ? "Клиент не прошёл проверку по справочнику заморозок/блокировок." : "Клиент прошёл проверку по справочнику заморозок/блокировок.",
                                Long.valueOf(updateId),
                                LocalDateTime.now());
                        clientCheckRepository.save(clientCheck);
                    } catch (Exception ex) {
                        LOGGER.error("Произошла ошибка при обработке результатов проверки клиента с ID = " + result.getKey()
                                + " по справочнику заморозок/блокировок.", ex);
                    }
                }

                clientCheckRepository.flush();
            } else {
                LOGGER.warn("Ответ от сервиса checkBlockages получен пустой!");
            }
        }
    }

    private void checkInvalidIdentityDoc(String updateId, List<Long> clientIds) throws IOException {

        final AtomicInteger clientCount = new AtomicInteger(0);
        try {
            int pageSize = getPageSizeOrDefault();
            MultiValueMap<Long, DocumentForClientShortData> passportsForCheckMap = new LinkedMultiValueMap<>();
            final AtomicInteger pageNumber = new AtomicInteger(0);

            try (Stream<DocumentForClientShortData> passportsForCheck = (clientIds == null) ?
                    documentClientShortDataRepository.findAllRFPasswordForCheck() :
                    documentClientShortDataRepository.findRFPasswordForCheckByClientIds(clientIds)) {
                passportsForCheck.forEach(passportForCheck -> {
                    if ((passportsForCheckMap.size() >= pageSize) && !passportsForCheckMap.containsKey(passportForCheck.getClient().getId())) {
                        // Достигли желаемого размера запроса к common-dict, а также добились того, что все данные по одному
                        // клиенту присутствуют в clientForCheckSummaryMap.
                        checkInvalidIdentityDoc(passportsForCheckMap, pageNumber.addAndGet(1), updateId);
                        clientCount.addAndGet(passportsForCheckMap.size());
                        passportsForCheckMap.clear();
                    }
                    passportsForCheckMap.add(passportForCheck.getClient().getId(), passportForCheck);
                });
            }

            if (passportsForCheckMap.size() > 0) {
                checkInvalidIdentityDoc(passportsForCheckMap, pageNumber.addAndGet(1), updateId);
                clientCount.addAndGet(passportsForCheckMap.size());
            }
        } catch (Exception ex) {
            LOGGER.error("Произошла ошибка во время проверки всех клиентов по справочнику недействительных паспортов", ex);
        }
        LOGGER.info(String.format("Проверка по справочнику недействительных паспортов завершена, всего проверено клиентов: %s", clientCount.get()));
        changeStatus(DictType.INVALID_IDENTITY_DOC);
        generateReport(CheckUnitTypeEnum.INVALID_IDENTITY_DOC, clientIds);
    }

    private void checkInvalidIdentityDoc(MultiValueMap<Long, DocumentForClientShortData> passportForCheckSummaryMap, int pageNumber, String updateId) {
        CheckInvalidIdentityDocRq request = new CheckInvalidIdentityDocRq(passportForCheckSummaryMap.values().stream()
                .flatMap(Collection::stream)
                .filter(clientForCheckSummary -> isInteger(clientForCheckSummary.getDocSeries()) && isInteger(clientForCheckSummary.getDocNumber()))
                .map(clientForCheckSummary -> new IdentityDocRq(
                        clientForCheckSummary.getClient().getId().toString(),
                        Integer.parseInt(clientForCheckSummary.getDocSeries()),
                        Integer.parseInt(clientForCheckSummary.getDocNumber()))
                ).collect(Collectors.toList()));

        CheckInvalidIdentityDocRs checkResult = null;
        int currentErrorIterateNumber = 0;
        do {
            try {
                checkResult = passportChecker.checkInvalidIdentityDoc(request, TIMEOUT_IN_SECONDS);
                currentErrorIterateNumber = 0;
            } catch (Exception ex) {
                currentErrorIterateNumber++;
                LOGGER.error("Произошла ошибка при выполнении запроса проверки клиентов по справочнику недействительных паспортов", ex);
            }
        } while ((checkResult == null) && (currentErrorIterateNumber < MAX_COUNT_RETRY_CALL));

        if (checkResult == null) {
            LOGGER.error("Ответ от сервиса checkInvalidIdentityDoc не получен!");
        } else {
            LOGGER.info(String.format("Ответ от сервиса checkInvalidIdentityDoc получен! Страница: %s", pageNumber));

            if (CollectionUtils.isNotEmpty(checkResult.getResultList())) {

                Map<Long, ClientShortData> clients = passportForCheckSummaryMap.values()
                        .stream()
                        .flatMap(Collection::stream)
                        .map(DocumentForClientShortData::getClient)
                        .collect(Collectors.toMap(ClientShortData::getId, client -> client, (oldValue, newValue) -> oldValue));

                Map<String, Boolean> results = new HashMap<>();
                for (IdentityDocCheckResult result : checkResult.getResultList()) {
                    results.compute(result.getId(), (key, value) -> (value == null)
                            ? result.isIsInvalidIdentityDoc()
                            : result.isIsInvalidIdentityDoc() || value);
                }

                for (Map.Entry<String, Boolean> result : results.entrySet()) {
                    try {
                        boolean isBlock = result.getValue();
                        ClientCheck clientCheck = new ClientCheck(
                                CheckUnitTypeEnum.INVALID_IDENTITY_DOC,
                                isBlock ? CheckStateEnum.TRUE : CheckStateEnum.FALSE,
                                clients.get(Long.valueOf(result.getKey())),
                                isBlock ? "Клиент не прошёл проверку по справочнику недействительных паспортов." : "Клиент прошёл проверку по справочнику недействительных паспортов.",
                                Long.valueOf(updateId),
                                LocalDateTime.now());
                        clientCheckRepository.save(clientCheck);
                    } catch (Exception ex) {
                        LOGGER.error("Произошла ошибка при обработке результатов проверки клиента с ID = " + result.getKey()
                                + " по справочнику недействительных паспортов.", ex);
                    }
                }

                clientCheckRepository.flush();
            } else {
                LOGGER.warn("Ответ от сервиса checkInvalidIdentityDoc получен пустой!");
            }
        }
    }

    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void changeStatus(DictType dictName) {
        DictStatus req = new DictStatus();
        req.setDictName(dictName);
        req.setDictOperation(DictOperation.FINISHED);
        try {
            dictStatusClient.setDictStatus(req, TIMEOUT_IN_SECONDS);
        } catch (RestClientException ex) {
            LOGGER.error(String.format("Ответ от сервиса setDictStatus не получен в указанное время таймаута: %s", TIMEOUT_IN_SECONDS), ex);
        }
    }

    private void generateReport(CheckUnitTypeEnum checkUnitTypeEnum, List<Long> clientIds) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String fileName = "Отчет о проверке пользователей";
        boolean isPartialCheck = clientIds != null;
        if (checkUnitTypeEnum == CheckUnitTypeEnum.TERRORIST) {
            byteArrayOutputStream = generateReportService.createReportForTerrorists(clientIds);
            fileName = generateReportService.fileName(checkUnitTypeEnum, isPartialCheck);
        }
        if (checkUnitTypeEnum == CheckUnitTypeEnum.BLOCKAGE) {
            byteArrayOutputStream = generateReportService.createReportForBlockages(clientIds);
            fileName = generateReportService.fileName(checkUnitTypeEnum, isPartialCheck);
        }
        if (checkUnitTypeEnum == CheckUnitTypeEnum.INVALID_IDENTITY_DOC) {
            byteArrayOutputStream = generateReportService.createReportForInvalidDocs(clientIds);
            fileName = generateReportService.fileName(checkUnitTypeEnum, isPartialCheck);
        }
        FileUtils.writeByteArrayToFile(new File(getReportStoreFolder().concat(fileName.concat(".xlsx"))), byteArrayOutputStream.toByteArray());
    }

    private int getPageSizeOrDefault() {
        return Integer.parseInt(getValueOrDefault(CLIENT_CHECK_BATCH_SIZE, String.valueOf(DEFAULT_PAGE_SIZE)));
    }

    private String getReportStoreFolder() {
        String result = getValueOrDefault(SECURITY_REPORT_STORE_PATH, DEFAULT_SECURITY_FILE_PATH);
        if (result.endsWith("/")) {
            return result;
        }
        return result.concat("/");
    }

    private String getValueOrDefault(String key, String defaultVal) {
        SettingEntity settingEntity = settingsService.get(key);
        if (settingEntity == null || StringUtils.isBlank(settingEntity.getValue())) {
            LOGGER.warn(String.format("Не удалось прочитать значение настройки %s, будет использовано значение по-умолчанию: %s",
                    key, defaultVal));
            return defaultVal;
        }
        return settingEntity.getValue();
    }

    public Long getCurrentDictIdByName(String dictName) {
        try {
            return dictStatusClient.getCurrentDictIdByName(dictName, TIMEOUT_IN_SECONDS).getId();
        } catch (RestClientException ex) {
            LOGGER.error(String.format("Ответ от сервиса getCurrentDictIdByName не получен в указанное время таймаута: %s", TIMEOUT_IN_SECONDS), ex);
            return null;
        }
    }
}
