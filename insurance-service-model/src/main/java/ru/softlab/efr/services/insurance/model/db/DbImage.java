package ru.softlab.efr.services.insurance.model.db;

import javax.persistence.*;

/**
 * Сущность изображения в БД
 */
@Entity
@Table(name = "images")
public class DbImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id записи

    /**
     * Масив байтов изображения
     */
    @Transient
    private byte[] image;
    /**
     * Имя изображения
     */
    @Column
    private String name;
    /**
     * Признак удаленного
     */
    @Column
    private Boolean deleted;

    public DbImage() {
    }

    public DbImage(byte[] image, String name) {
        this.image = image;
        this.name = name;
        this.deleted = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
