package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.ContractNumberSequence;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;

/**
 * Репозиторий для работы с сущностью счётчика номера договора страхования.
 *
 * @author olshansky
 * @since 19.12.2018
 */
public interface ContractNumberSequenceRepository extends JpaRepository<ContractNumberSequence, Long> {

    /**
     * Получение текущего состояния счётчика номера договора по типу программы
     */
    @Query("SELECT lastId " +
            "FROM ContractNumberSequence c " +
            "WHERE c.programKind = :programKind")
    Long getSequenceValueByProgramKind(
            @Param("programKind") ProgramKind programKind);

    /**
     * Уточнение текущего состояния счётчика номера договора по типу программы
     */
    @Modifying
    @Query("UPDATE ContractNumberSequence c " +
            "SET c.lastId = c.lastId + 1 " +
            "WHERE c.programKind = :programKind")
    void incrementSequenceValueByProgramKind(
            @Param("programKind") ProgramKind programKind);
}
