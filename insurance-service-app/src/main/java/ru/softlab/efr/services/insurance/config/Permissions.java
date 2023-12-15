package ru.softlab.efr.services.insurance.config;

/**
 * Разрешения.
 *
 * @author Andrey Grigorov
 */
public class Permissions {

    /**
     * Разрешение на просмотр информации о договоре.
     */
    public static final String VIEW_CONTRACT = "view-contract";

    /**
     * Разрешение на просмотр формирование отчёта по продажам.
     */
    public static final String VIEW_CONTRACT_REPORT = "view-contract-report";

    /**
     * Разрешение на просмотр справочников
     */
    public static final String VIEW_DICT = "view-dict";

    /**
     * Доступ на поиск и просмотр карточки клиента
     */
    public static final String ACCESS_CLIENTS_INFO = "access-clients-info";

    /**
     * Доступ на создание или редактирование данных о клиенте
     */
    public static final String CREATE_OR_EDIT_CLIENTS = "create-or-edit-clients";

    /**
     * Разрешение на создание логоворов.
     */
    public static final String CREATE_CONTRACT = "create-contract";
    /**
     * Просмотр вложений справочника шаблонов и инструкций.
     */
    public static final String VIEW_TEMPLATE_CONTENT = "view-template-content";
    /**
     * Просмотр договоров клиента в ЛК.
     */
    public static final String CLIENT_VIEW_CONTRACT = "client-view-contract";
    /**
     * Просмотр шаблонов и инструкций в ЛК.
     */
    public static final String VIEW_CLIENT_TEMPLATE = "view-client-template";
    /**
     * Разрешение на добавление и редактирование вложений к обращениям клиентов
     */
    public static final String UPDATE_CLIENT_REQUEST_ATTACHMENT = "update-client-request-attachment";
}