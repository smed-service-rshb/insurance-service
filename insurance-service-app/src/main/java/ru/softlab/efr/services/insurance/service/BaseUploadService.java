package ru.softlab.efr.services.insurance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.services.FileService;
import ru.softlab.efr.services.insurance.services.InsuranceSettingsService;

import static ru.softlab.efr.services.insurance.controllers.Constants.UPLOAD_CHANGES_RESULT_STORE_PATH;

@Service
public class BaseUploadService {

    static final String IMPORT_ERROR_MESSAGE = "Во время загрузки произошла ошибка. Номер строки " +
            "%s причина: %s";

    @Autowired
    private InsuranceSettingsService insuranceSettingsService;

    @Autowired
    private FileService fileService;

    public String getReportStoreFolder() {
        return insuranceSettingsService.getReportStoreFolder(UPLOAD_CHANGES_RESULT_STORE_PATH);
    }

    boolean saveReceivedFile(byte[] frontFile, String fileName) {
        return fileService.saveReceivedFile(getReportStoreFolder(), frontFile, fileName);
    }
}
