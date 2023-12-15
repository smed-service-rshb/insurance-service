package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.DbImage;

public interface ImageRepository extends JpaRepository<DbImage, Long> {

    public DbImage findByIdAndDeleted(Long id, boolean deleted);

    @Query("from DbImage where id = :id and deleted = :deleted")
    public DbImage findImageById(@Param("id") Long id, @Param("deleted") boolean deleted);

    /**
     * Обновить контент изображения
     *
     * @param identifier Идентификатор изображения
     * @param content    масив байтов
     */
    @Modifying
    @Query(value = "update {h-schema}images set image =:content where id=:id", nativeQuery = true)
    void setContent(@Param("id") Long identifier, @Param("content") byte[] content);

    /**
     * Получить масив байтов изображения
     *
     * @param id идентификатор изображения
     * @return масив байтов изображения
     */
    @Query(value = "SELECT image from {h-schema}images where id=:id", nativeQuery = true)
    byte[] getContent(@Param("id") Long id);


}
