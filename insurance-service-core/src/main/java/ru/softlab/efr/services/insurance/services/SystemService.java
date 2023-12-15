package ru.softlab.efr.services.insurance.services;

import org.springframework.util.StringUtils;
import ru.softlab.efr.clients.model.SystemInformationTypes;

import java.util.regex.Pattern;

/**
 * сервис для получения информации из id систем
 *
 * @author krutikov
 * @since 16.03.2018
 */
public class SystemService {
    public static final int OFFICE_LENGTH = 2;
    private static final int SYSTEM_DATA_LENGTH = 2;
    private static final String SYSTEM_SERVICE_ID_FORMAT = "%s:%s";
    private static final String FILIAL_POSTFIX = "00";

    /**
     * распарсить systemId
     * @param systemId id системы
     * @return распарсенная информация из systemId
     */
    public static String[] splitSystemId(String systemId){
        return Pattern.compile(":").split(systemId);
    }

    /**
     * получить systemId из информации о системе и номере офиса
     * @param type информации о систем
     * @param id номер офииса
     * @return systemId
     */
    @Deprecated
    public static String getSystemServiceIdFormat(SystemInformationTypes type, String id){
        return String.format(SYSTEM_SERVICE_ID_FORMAT, type, id);
    }

    /**
     * Проверка, что указанная система - CIF
     * @param systemId идентификатор системы
     * @return true - CIF
     */
    public static boolean isCIF(String systemId) {
        return !StringUtils.isEmpty(systemId) && systemId.equals(SystemInformationTypes.CIF.name());
    }

    /**
     * пролучить id филиала
     * @param officeId id офиса
     * @return id филиала
     */
    public static String getFilialId(String officeId) {
        if (officeId == null) {
            return null;
        }
        return officeId.substring(0, officeId.length() - OFFICE_LENGTH) + FILIAL_POSTFIX;
    }

    /**
     * пролучить id филиала
     * @param systemId id системы
     * @return id филиала
     */
    public static String getFilialIdFromSystemId(String systemId) {
        if (StringUtils.isEmpty(systemId)){
            return null;
        }
        String[] systemData = SystemService.splitSystemId(systemId);
        if (systemData.length != SYSTEM_DATA_LENGTH) {
            return null;
        }
        return systemData[1];
    }

    /**
     * Проверка, что указанная система - BISQUIT
     * @param systemId идентификатор системы
     * @return true - BISQUIT
     */
    public static boolean isBISQUIT(String systemId) {
        return !StringUtils.isEmpty(systemId) && SystemService.splitSystemId(systemId)[0].equals(SystemInformationTypes.BISQUIT.name());
    }

    /**
     * Проверка, что указанная система - PC
     * @param systemId идентификатор системы
     * @return true - PC
     */
    public static boolean isPC(String systemId) {
        return !StringUtils.isEmpty(systemId) && SystemService.splitSystemId(systemId)[0].equals(SystemInformationTypes.PC.name());
    }
}
