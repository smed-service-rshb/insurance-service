package ru.softlab.efr.services.insurance.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.Extract;
import ru.softlab.efr.services.insurance.model.enums.ExtractStatus;
import ru.softlab.efr.services.insurance.model.enums.ExtractType;
import ru.softlab.efr.services.insurance.repositories.ExtractRepository;
import ru.softlab.efr.services.insurance.utils.EmployeeFilter;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@PropertySource(value = {"classpath:extract.properties"})
@Service
public class ExtractProcessInfoService {

    @Autowired
    private ExtractRepository extractRepository;

    @Transactional
    public ExtractCreateProcessResult createProcessInfo(ExtractType type, String name, EmployeeFilter employeeFilter) {

        String requestDigest = (employeeFilter == null)
                ? DigestUtils.md5Hex(name + type)
                : DigestUtils.md5Hex(name + type + employeeFilter.hashCode());

        Extract extract = extractRepository.findTopByRequestDigestAndStatus(requestDigest, ExtractStatus.CREATING);
        if ((extract != null) && isExtractIsLocked(extract)) {
            // если процесс создания отчёта ещё выполняется, то отдадим ссылку на уже существующий процесс
            return new ExtractCreateProcessResult(extract, true);
        }

        extract = new Extract();
        extract.setRequestDigest(requestDigest);
        extract.setType(type);
        extract.setStatus(ExtractStatus.CREATING);
        extract.setName(name);
        extractRepository.saveAndFlush(extract);
        return new ExtractCreateProcessResult(extract, false);
    }

    /**
     * Выполняется проверка, заблокирована ли запись в таблице extract другим потоком или нет.
     * Если запись заблокирована, то это означает, что выполняется генерация отчёта смежным потоком
     * и мы можем не запускать проверку генерацию отчёта вновь.
     * Если ли же запись не заблокирована, то это означает, что либо генерация отчёта ещё не началась,
     * либо при генерации отчёта произошла какая-то авария (упали с непредвиденной ошибкой, былы выключена JVM
     * и т.д.) и запись в таблице extract "зависла" в статусе CREATING. В этом случае нам надо будет
     * запустить генерацию отчета.
     *
     * @param extract Информация о текущем процессе генерации отчета.
     * @return true - запись заблокирована, false - запись не заблокирована
     */
    private boolean isExtractIsLocked(Extract extract) {
        try {
            // getLockExtract запускается в отдельном потоке (см. аннотацию @Async над методом),
            // чтобы в случае возникновения SQLException не сломалась текущая транзакция
            extractRepository.getLockExtract(extract.getUuid()).get();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof PessimisticLockingFailureException) {
                return true;
            }
        }
        return false;
    }

    /**
     * Получить информацию о формировании отчета, взяв блокировку на запись.
     *
     * @param uuid идентификатор процесса формирования отчета
     * @return информация о процессе
     */
    @Transactional
    public Extract getExtractProcessInfoWithWriteLock(String uuid) {
        return extractRepository.findByUuidWithWriteLock(uuid);
    }

    /**
     * Получить информацию о формировании отчета.
     *
     * @param uuid идентификатор процесса формирования отчета
     * @return информация о процессе
     */
    @Transactional(readOnly = true)
    public Extract getExtractProcessInfo(String uuid) {
        return extractRepository.findByUuid(uuid);
    }

    /**
     * Получить сформированный отчет.
     *
     * @param uuid идентификатор процесса формирования отчета
     * @return масив байтов
     */
    @Transactional
    public Extract getContent(String uuid) {
        Extract extract = extractRepository.findByUuidWithWriteLock(uuid);
        if (extract == null) {
            throw new EntityNotFoundException();
        }
        extract.setContent(extractRepository.getContent(uuid));
        extract.setStatus(ExtractStatus.UPLOADED);
        extractRepository.save(extract);
        return extract;
    }

    /**
     * Сохранить сформированный отчет и обновить статус формирования отчета.
     *
     * @param extract данные о формировании отчета
     * @param bytes   поток байтов для сохранения
     */
    @Transactional
    public void saveContentAndExtract(Extract extract, byte[] bytes) {
        extractRepository.setContent(extract.getUuid(), bytes);
        extractRepository.saveAndFlush(extract);
    }

    public void update(Extract extract) {
        extractRepository.saveAndFlush(extract);
    }

    /**
     * Задача по расписанию, удаляющая записи старше одной недели из таблицы extract.
     */
    @Scheduled(cron = "${delete.extract.schedule.cron}")
    @Transactional
    public void deleteOldExtract() {
        extractRepository.deleteByDate(Date.from(LocalDateTime.now().minusWeeks(1L).atZone(ZoneId.systemDefault()).toInstant()));
    }
}

