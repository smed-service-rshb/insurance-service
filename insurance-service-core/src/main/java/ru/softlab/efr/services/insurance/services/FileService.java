package ru.softlab.efr.services.insurance.services;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с файлами
 *
 * @author olshansky
 * @since 29.07.2019
 */
@Service
public class FileService {

    protected static final Logger LOGGER = Logger.getLogger(FileService.class);
    private static final String CONTRACT_FILE_FORMATS = "contractFileFormats";
    private static final String DEFAULT_CONTRACT_FILE_FORMATS = "JPG, PDF, DOC, DOCX, JPEG, PNG, TIFF, TIF, BMP, TXT";

    private static final String STATEMENTS_FILE_FORMATS = "statementFileFormats";
    private static final String DEFAULT_STATEMENTS_FILE_FORMATS = "JPG, JPEG, PNG, PDF, DOC, DOCX, XLS, XLSX";

    private static final String PICTURE_FILE_FORMATS = "pictureFileFormats";
    private static final String DEFAULT_PICTURE_FILE_FORMATS = "JPG, JPEG, PNG";

    private static final String TEMPLATES_FILE_FORMATS = "templateFileFormats";
    private static final String DEFAULT_TEMPLATES_FILE_FORMATS = "PDF, JPEG, JPG";

    @Autowired
    private InsuranceSettingsService insuranceSettingsService;

    public boolean saveReceivedFile(String path, byte[] frontFile, String fileName) {
        try {
            FileUtils.writeByteArrayToFile(new File(path.concat(fileName)), frontFile);
        } catch (IOException ex) {
            LOGGER.error("Произошла ошибка во время сохранения файла, причина: ", ex);
            return false;
        }
        return true;
    }

    boolean isExistStoredFile(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    public String getExtensionByName(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    public String getExtensionByMimeType(byte[] bytes, String fileName) {
        try {
            TikaConfig config = TikaConfig.getDefaultConfig();
            MediaType mediaType = config.getDetector().detect(new ByteArrayInputStream(bytes), new Metadata());
            MimeType mimeType = config.getMimeRepository().forName(mediaType.toString());
            return mimeType.getExtension().replaceAll("\\W", StringUtils.EMPTY);
        } catch (Exception ex) {
            LOGGER.error("Произошла ошибка во время получения расширения файла при чтении контента, причина: ", ex);
            String notExistFormatFile = "NOT_EXISTS_FILE_FORMAT";
            if (StringUtils.isNotBlank(fileName)) {
                String formatFile = getExtensionByName(fileName);
                if (StringUtils.isNotBlank(formatFile)) {
                    return formatFile;
                }
            }
            return notExistFormatFile;
        }
    }

    public boolean isAllowedContractFileFormat(String formatFile, byte[] bytes) {
        return isAllowedFileFormat(getExtensionByMimeType(bytes, formatFile), CONTRACT_FILE_FORMATS, DEFAULT_CONTRACT_FILE_FORMATS) &&
                isAllowedFileFormat(formatFile, CONTRACT_FILE_FORMATS, DEFAULT_CONTRACT_FILE_FORMATS);
    }

    public boolean isAllowedStatementsFileFormat(String formatFile, byte[] bytes) {
        return isAllowedFileFormat(getExtensionByMimeType(bytes, formatFile), CONTRACT_FILE_FORMATS, DEFAULT_CONTRACT_FILE_FORMATS) &&
                isAllowedFileFormat(formatFile, STATEMENTS_FILE_FORMATS, DEFAULT_STATEMENTS_FILE_FORMATS);
    }

    public boolean isAllowedPictureFileFormat(String formatFile, byte[] bytes) {
        return isAllowedFileFormat(getExtensionByMimeType(bytes, formatFile), CONTRACT_FILE_FORMATS, DEFAULT_CONTRACT_FILE_FORMATS) &&
                isAllowedFileFormat(formatFile, PICTURE_FILE_FORMATS, DEFAULT_PICTURE_FILE_FORMATS);
    }

    public boolean isAllowedTemplatesFileFormat(String formatFile, byte[] bytes) {
        return isAllowedFileFormat(getExtensionByMimeType(bytes, formatFile), CONTRACT_FILE_FORMATS, DEFAULT_CONTRACT_FILE_FORMATS) &&
                isAllowedFileFormat(formatFile, TEMPLATES_FILE_FORMATS, DEFAULT_TEMPLATES_FILE_FORMATS);
    }

    private boolean isAllowedFileFormat(String formatFile, String formatSettingName, String defaultFormats) {
        if (StringUtils.isBlank(formatFile)) {
            return true;
        }
        String fileFormats = insuranceSettingsService.getValueOrDefault(formatSettingName, defaultFormats).toUpperCase();
        List<String> needCheckFileNames = Arrays.stream(
                fileFormats.replaceAll("\\s", StringUtils.EMPTY).split("[,;]"))
                .filter(StringUtils::isNotBlank).collect(Collectors.toList());

        return needCheckFileNames.contains(formatFile.toUpperCase());
    }

}
