package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.InsuranceStatus;
import ru.softlab.efr.services.insurance.model.db.InsuranceStatusHistory;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.repositories.StatusHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы c историей статусов документов
 *
 * @author krivenko
 * @since 26.10.2018
 */
@Service
public class StatusHistoryService {

    private StatusHistoryRepository repository;

    @Autowired
    private StatusService statusService;

    @Autowired
    public StatusHistoryService(StatusHistoryRepository repository) {
        this.repository = repository;
    }

    public InsuranceStatusHistory getById(Long id) {
        return repository.findOne(id);
    }

    @Transactional(readOnly = true)
    public InsuranceStatusHistory getByInsurance(Insurance insurance){
        return repository.findTopByInsurance(insurance);
    }

    @Transactional(readOnly = true)
    public InsuranceStatusHistory getLastButOneAssigned(Insurance insurance){
        List<InsuranceStatusHistory> insuranceStatuses = repository.findFirst2ByInsuranceOrderByIdDesc(insurance);
        if (insuranceStatuses.size() == 2) {
            return insuranceStatuses.get(1);
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public InsuranceStatusHistory getLastAssigned(Insurance insurance){
        return repository.findFirst2ByInsuranceOrderByIdDesc(insurance).get(0);
    }

    public boolean isCrmImported(Insurance insurance) {
        InsuranceStatus crmStatus = statusService.getByCode(InsuranceStatusCode.CRM_IMPORTED);
        return repository.countAllByInsuranceAndStatusEquals(insurance, crmStatus) >= 1;
    }

    /**
     * Создание записи в справочнике истории изменений статусов
     * @return сохраненная запись справочника
     */
    @Transactional
    public InsuranceStatusHistory save(InsuranceStatusHistory statusHistory) {
        return repository.save(statusHistory);
    }


    /**
     * Создание записи в справочнике истории изменений статусов
     * @return сохраненная запись справочника
     */
    @Transactional
    public InsuranceStatusHistory save(Insurance insurance, InsuranceStatusCode newStatus, Long employeeId, String employeeName, Long subdivisionId, String subdivisionName, String description) {
        InsuranceStatus status = statusService.getByCode(newStatus);
        InsuranceStatusHistory history = new InsuranceStatusHistory();
        history.setInsurance(insurance);
        history.setChangeDate(LocalDateTime.now());
        history.setEmployeeId(employeeId);
        history.setEmployeeName(employeeName);
        history.setSubdivisionId(subdivisionId);
        history.setSubdivisionName(subdivisionName);
        history.setStatus(status);
        history.setDescription(description);
        return repository.save(history);
    }

}
