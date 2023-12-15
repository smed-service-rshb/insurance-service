package ru.softlab.efr.services.insurance.model.enums;

public enum ExtractStatus {

    /**
     * Процес формирования отчета запущен
     */
    CREATING,

    /**
     * Отчет сформирован и сохранен
     */
    SAVE,

    /**
     * При формировании отчета произошла ошибка
     */
    ERROR,

    /**
     * Отчет выгружен пользователем
     */
    UPLOADED
}
