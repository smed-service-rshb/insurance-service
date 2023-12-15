package ru.softlab.efr.services.insurance.model.db;

import javax.persistence.*;

/**
 * Информация об аватаре или скане согласия/документа
 * @author bazanova
 * @since 20.07.2018
 */
@Entity
@Table(name = "client_resources")
public class ClientResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "system_id")
    private String systemId;

    @Column(name = "resource_key")
    private String resourceKey;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "file_name")
    private String fileName;

    public ClientResourceEntity() {}

    /**
     * Конструктор
     * @param clientId - Id клиента
     * @param systemId - Id системы
     * @param resourceKey - Ключ ресурса
     * @param resourceId - Id аватара или скана
     */
    public ClientResourceEntity(String clientId, String systemId, String resourceKey, String resourceId) {
        this.clientId = clientId;
        this.systemId = systemId;
        this.resourceKey = resourceKey;
        this.resourceId = resourceId;
    }

    /**
     * Конструктор
     * @param clientId - Id клиента
     * @param systemId - Id системы
     * @param resourceKey - Ключ ресурса
     * @param resourceId - Id аватара или скана
     * @param fileName - Имя файла скана документа/согласия
     */
    public ClientResourceEntity(String clientId, String systemId, String resourceKey, String resourceId, String fileName) {
        this(clientId, systemId, resourceKey, resourceId);
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
