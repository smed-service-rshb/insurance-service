package ru.softlab.efr.services.insurance.utils;

import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для подсчёта статистики обращения к системного журналу при выполнении тестов.
 */
public class OperationLogServiceStatistics {

    private List<OperationLogEntry> logs = new ArrayList<>();

    public void reset() {
        logs.clear();
    }

    public void append(OperationLogEntry entry) {
        logs.add(entry);
    }

    public List<OperationLogEntry> getLogs() {
        return logs;
    }
}
