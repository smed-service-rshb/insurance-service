package ru.softlab.efr.services.insurance.repositories;

/**
 * Интерфейс проекции для таблиц хранящих историю изменений (при использовании native query)
 */
public interface HistoryDataRevision {
    Long getId();
    Integer getRev();
}
