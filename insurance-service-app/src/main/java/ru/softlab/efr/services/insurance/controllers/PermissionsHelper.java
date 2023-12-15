package ru.softlab.efr.services.insurance.controllers;

import org.springframework.util.CollectionUtils;
import ru.softlab.efr.clients.model.ClientXref;
import ru.softlab.efr.clients.model.FoundClient;
import ru.softlab.efr.clients.model.SystemInformationTypes;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.SecurityContext;
import ru.softlab.efr.services.insurance.services.SystemService;

import java.util.List;

import static ru.softlab.efr.services.insurance.services.SystemService.OFFICE_LENGTH;
import static ru.softlab.efr.services.insurance.services.SystemService.isBISQUIT;
import static ru.softlab.efr.services.insurance.services.SystemService.isPC;


/**
 * Утилитный класс, предоставляющий способ проверки доступности клиента
 * @author gladishev
 * @since 17.05.2018
 */
public class PermissionsHelper {
    private static final String MAIN_OFFICE_PREFIX = "00";

    /**
     * Проверяет наличие доступа
     * @param systemId id системы
     * @param xrefs ссылки на клиента
     * @param securityContext контекст безопасности
     * @return true - доступ есть
     */
    public static boolean isHashPermission(String systemId, List<ClientXref> xrefs, SecurityContext securityContext) {
        boolean accessGO = securityContext.implies(Right.ACCESS_CLIENTS_MAIN_OFFICE);
        boolean accessRF = securityContext.implies(Right.ACCESS_CLIENTS_EXCEPT_MAIN_OFFICE);

        return PermissionsHelper.isHashPermission(systemId, xrefs, accessGO, accessRF);
    }

    /**
     * Проверяет, есть ли доступ к клиенту
     * @param client клиент
     * @param accessGO сотруник ГО
     * @param accessRF сотруник РФ
     * @return true - доступ есть
     */
    static boolean isHashPermission(FoundClient client, boolean accessGO, boolean accessRF) {
        return isHashPermission(client.getSystemId(), client.getXrefs(), accessGO, accessRF);
    }

    /**
     * Проверяет наличие доступа
     * @param systemId id системы
     * @param xrefs ссылки на клиента
     * @param accessGO сотруник ГО
     * @param accessRF сотруник РФ
     * @return true - доступ есть
     */
    public static boolean isHashPermission(String systemId, List<ClientXref> xrefs, boolean accessGO, boolean accessRF) {
        //полный доступ - разрешаем
        if (accessGO && accessRF) {
            return true;
        }

        //клиент из бисквита
        if (isBISQUIT(systemId)) {
            return isHashBISQUITPermission(systemId, accessGO, accessRF);
        }

        //клиент из PC
        if (isPC(systemId)) {
            return true;
        }

        //нет кроссылок - нет доступа
        if (CollectionUtils.isEmpty(xrefs)) {
            return false;
        }

        //сотруднику из ГО доступны клиенты, у которых есть ссылки в АБС ГО
        //сотруднику из РФ клиент доступен, у которых есть ссылки в АБС РФ
        return xrefs.stream().anyMatch(x -> isHashBISQUITPermission(x.getSystemId(), accessGO, accessRF));
    }

    /**
     * Проверка доступности системы сотруднику
     * @param systemId id системы
     * @param isGOAccess сотруник ГО
     * @param isAccessRF сотруник РФ
     * @return true - доступ есть
     */
    public static boolean isHashBISQUITPermission(String systemId, boolean isGOAccess, boolean isAccessRF) {
        String[] split = SystemService.splitSystemId(systemId);
        if (split.length != OFFICE_LENGTH || !split[0].equals(SystemInformationTypes.BISQUIT.name())) {
            return false;
        }

        return split[1].startsWith(MAIN_OFFICE_PREFIX) ? isGOAccess : isAccessRF;
    }
}
