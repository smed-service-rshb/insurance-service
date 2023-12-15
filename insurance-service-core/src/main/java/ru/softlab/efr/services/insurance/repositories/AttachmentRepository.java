package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.Attachment;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Репозиторий для работы с вложениями, сканами документов
 *
 * @author olshansky
 * @since 25.10.2018
 */
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    Attachment getById(String id);

    @Transactional(readOnly = true)
    Integer countById(String id);

    @Transactional(readOnly = true)
    Attachment findOneById(String id);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<Attachment> findAllByDeleted(Pageable pageable, Boolean deleted);

    @Transactional(readOnly = true)
    @Query("select a from Attachment a join a.documentType d join a.contract c where d.id =:documentType and c.id = :contractId")
    Attachment findByDocumentTypeAndContractId(@Param("documentType") Long documentType, @Param("contractId") Long contractId);

    @Transactional(readOnly = true)
    @Query("select count(a) from Attachment a join a.documentType d join a.contract c where d.id =:documentType " +
            "and (c.id = :contractId or :contractId is null)")
    Integer countByDocTypeAndContractId(@Param("documentType") Long documentType, @Param("contractId") Long contractId);

    @Transactional(readOnly = true)
    @Query("select count(a) from Attachment a join a.documentType d where d.id =:documentType " +
            "and (a.contractUuid = :contractUuid or :contractUuid is null)")
    Integer countByDocTypeAndContractUuid(@Param("documentType") Long documentType, @Param("contractUuid") String contractUuid);

    Integer countAllByDeletedAndContractId(Boolean deleted, Long contractId);

    Integer countAllByDeletedAndContractUuid(Boolean deleted, String contractUuid);

    /**
     * Получить контент вложения
     *
     * @param id Идентификатор вложения
     * @return контент
     */
    @Query(value = "SELECT content from {h-schema}attachments where id=:id", nativeQuery = true)
    byte[] getContent(@Param("id") String id);

    /**
     * Обновить контент вложения
     *
     * @param identifier Идентификатор вложения
     * @param content    контент
     */
    @Modifying
    @Query(value = "update {h-schema}attachments set content =:content where id=:id", nativeQuery = true)
    void setContent(@Param("id") String identifier, @Param("content") byte[] content);

    /**
     *  Получить последний добавленный контент вложения из необходимого контракта
     * @param kind Тип документа
     * @param contract Идентификатор контракта
     * @param deleted
     * @return контент
     */
    @Query(value = "SELECT content from {h-schema}attachments where kind=:kind and contract=:contract and deleted=:deleted order by createdate DESC limit 1", nativeQuery = true)
    byte[] getOneFormCertification(@Param("kind") String kind, @Param("contract") Long contract, @Param("deleted") Boolean deleted);

    @Query(value = "SELECT * from {h-schema}attachments where kind=:kind and contract=:contract and deleted=:deleted order by createdate DESC limit 1", nativeQuery = true)
    Attachment findByKindAndContracts(@Param("kind") String kind, @Param("contract") Long contract, @Param("deleted") Boolean deleted);

    @Query(value = "SELECT * from {h-schema}attachments where kind=:kind and contract_uuid=:contract and deleted=:deleted order by createdate DESC limit 1", nativeQuery = true)
    Attachment findByKindAndContracts(@Param("kind") String kind, @Param("contract") String contractUuid, @Param("deleted") Boolean deleted);

    @Query(value = "SELECT * from {h-schema}attachments where deleted=:deleted and contract=:contract and kind=:kind and documenttype is not null", nativeQuery = true)
    List<Attachment> findAllByDeletedAndContractIdAndKind(@Param("deleted") Boolean deleted, @Param("contract") Long contract, @Param("kind") String kind);

    @Query(value = "SELECT * from {h-schema}attachments where deleted=:deleted and contract_uuid=:contractUuid and kind=:kind and documenttype is not null", nativeQuery = true)
    List<Attachment> findAllByDeletedAndContractUuidAndKind(@Param("deleted") Boolean deleted, @Param("contractUuid") String contractUuid, @Param("kind") String kind);

    List<Attachment> findAllByDeletedAndContractUuidAndContractIsNull(Boolean deleted, String contractUuid);
}