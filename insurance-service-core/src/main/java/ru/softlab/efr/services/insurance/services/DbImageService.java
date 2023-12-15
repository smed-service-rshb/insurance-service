package ru.softlab.efr.services.insurance.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.DbImage;
import ru.softlab.efr.services.insurance.repositories.ImageRepository;

/**
 * Сервис рабты с изображениями
 */
@Service
public class DbImageService {

    private final ImageRepository imageRepository;

    public DbImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Сохранение изображения в БД
     *
     * @param dbImage сущность для сохранения
     * @return сохраненная сущность
     */
    @Transactional
    public DbImage save(DbImage dbImage, byte [] bytes) {
        DbImage save = imageRepository.save(dbImage);
        imageRepository.setContent(save.getId(), bytes);
        return save;
    }

    /**
     * Получить изображение по идентификатору
     *
     * @param id идентификатор изображения
     * @return сущность изображения
     */
    @Transactional(readOnly = true)
    public DbImage findDbImageById(Long id) {
        DbImage dbImage = imageRepository.findByIdAndDeleted(id, false);
        if (dbImage != null) {
            dbImage.setImage(imageRepository.getContent(id));
        }
        return dbImage;
    }
    /**
     * Получить информацию об изображении по идентификатору
     *
     * @param id идентификатор изображения
     * @return сущность изображения
     */
    @Transactional(readOnly = true)
    public DbImage findImageById(Long id) {
        return imageRepository.findImageById(id, false);
    }

    /**
     * Удалить изображение из бд
     *
     * @param id идентификатор изображения
     */
    @Transactional
    public boolean delete(Long id) {
        DbImage dbImage = imageRepository.findOne(id);
        if (dbImage == null || dbImage.getDeleted()) {
            return false;
        }
        imageRepository.setContent(id, null);
        dbImage.setDeleted(true);
        imageRepository.save(dbImage);
        return true;
    }
}
