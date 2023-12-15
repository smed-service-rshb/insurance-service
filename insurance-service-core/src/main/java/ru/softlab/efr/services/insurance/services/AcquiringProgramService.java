package ru.softlab.efr.services.insurance.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.insurance.model.db.AcquiringProgram;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.repositories.AcquiringProgramRepository;
import ru.softlab.efr.services.insurance.utils.AppUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис работы с сущнотью программы для покупки в ЛК
 */
@Service
public class AcquiringProgramService {

    private final AcquiringProgramRepository acquiringProgramRepository;
    private final ClientService clientService;

    public AcquiringProgramService(AcquiringProgramRepository acquiringProgramRepository, ClientService clientService) {
        this.acquiringProgramRepository = acquiringProgramRepository;
        this.clientService = clientService;
    }

    /**
     * Получение программы для оформления в ЛК по идентификатору
     *
     * @param id идентификатор
     * @return программа
     */
    @Transactional(readOnly = true)
    public AcquiringProgram findById(Long id) {
        return acquiringProgramRepository.findById(id);
    }

    /**
     * Получение постранично списка программ страхования для оформления в ЛК
     *
     * @param pageable параметры страницы
     * @return страница программ
     */
    @Transactional(readOnly = true)
    public Page<AcquiringProgram> getPageAcquiringProgram(Pageable pageable) {
        return acquiringProgramRepository.findAll(pageable);
    }

    /**
     * Получение списка программ для оформления в ЛК, которые соответствуют настройкам программы страхования.
     *
     * @param programSetting настройка программы страхования
     * @param isAuthorized   признак авторизованной зоны
     * @return список програм для оформления в ЛК
     */
    @Transactional(readOnly = true)
    public List<AcquiringProgram> findByProgram(ProgramSetting programSetting,
                                                Boolean isAuthorized) {
        return acquiringProgramRepository.findByProgram(programSetting, LocalDate.now(), isAuthorized);
    }

    /**
     * Сохранить сущность программы страхования для оформления в ЛК
     *
     * @param program сущность для сохранения
     * @return сохраненная сущность
     */
    @Transactional
    public AcquiringProgram save(AcquiringProgram program) {
        return acquiringProgramRepository.save(program);
    }

    /**
     * Получить список доступных для оформления клиенту програм на текущую дату
     *
     * @param principalData данные текущего пользователя
     * @return список доступных программ
     */
    public List<AcquiringProgram> getAcquiringProgramForClient(PrincipalData principalData) {
        if (principalData == null) {
            return acquiringProgramRepository.findAllAllowedProgram(LocalDate.now());
        } else {
            ClientEntity clientEntity = clientService.get(principalData);
            if (clientEntity != null) {
                Integer clientAge = AppUtils.getAgeByToday(clientEntity.getBirthDate());
                return acquiringProgramRepository.findAllAllowedProgram(clientAge, LocalDate.now());
            }
            return new ArrayList<>();
        }
    }
}
