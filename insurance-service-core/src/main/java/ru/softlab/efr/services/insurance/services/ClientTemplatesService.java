package ru.softlab.efr.services.insurance.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.ClientTemplate;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.repositories.ClientTemplatesRepository;
import ru.softlab.efr.services.insurance.repositories.ShortClientTemplates;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с справочником шаблонов заявлений и инструкций
 *
 * @author kalantaev
 * @since 12.04.2019
 */
@Service
public class ClientTemplatesService {

    private final ClientTemplatesRepository repositiry;

    public ClientTemplatesService(ClientTemplatesRepository repositiry) {
        this.repositiry = repositiry;
    }

    /**
     * Получение страницы с краткой информацией записей справочника шаблонов заявлений и инструкций
     *
     * @param pageable параметры страницы
     * @return страница с данными
     */
    public Page<ShortClientTemplates> findAll(Pageable pageable, ProgramKind programKind, Long programId, Boolean isTemplate) {
        Pageable pageableSortedById = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "sortPriority"));
        return repositiry.findAllTemplates(programKind, programId, isTemplate, pageableSortedById);
    }

    /**
     * Сохранение сущности справочника шаблонов заявлений и инструкций
     *
     * @param entity сущность для сохранения
     * @return сохраненная сущность
     */
    @Transactional
    public ClientTemplate save(ClientTemplate entity) {
        return repositiry.save(entity);
    }

    /**
     * Получить запись справочника шаблонов заявлений и инструкций по id
     *
     * @param id идентификатор записи справочника
     * @return запись справочника
     */
    @Transactional
    public ClientTemplate findById(Long id) {
        return repositiry.findOne(id);
    }

    /**
     * Получить отсортированный список шаблонов и инструкций применимых к договору страхования
     *
     * @param insuranceId идентификатор договора страхования
     * @return отсортированный список шаблонов и инструкций
     */
    public List<ClientTemplate> findClientList(Long insuranceId) {
        List<ClientTemplate> templates = repositiry.findByContractId(insuranceId);
        List<ClientTemplate> result = new LinkedList<>();
        result.addAll(templates.stream()
                .filter(item -> item.getKind() == null)
                .sorted(Comparator.comparing(ClientTemplate::getName))
                .collect(Collectors.toList()));
        result.addAll(templates.stream()
                .filter(item -> item.getKind() != null && item.getProgram() == null)
                .sorted(Comparator.comparing(ClientTemplate::getName))
                .collect(Collectors.toList()));
        result.addAll(templates.stream()
                .filter(item -> item.getKind() != null && item.getProgram() != null)
                .sorted(Comparator.comparing(ClientTemplate::getName))
                .collect(Collectors.toList()));
        return result;
    }
}
