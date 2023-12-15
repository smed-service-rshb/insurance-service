package ru.softlab.efr.services.insurance.model.db;

import javax.persistence.*;

/**
 * Маппинг для получение сокращённой инфомрации по ДУЛам клиента.
 * Создан для оптимизации запросов к БД.
 * <p>
 * Не добавляйте лишние поля без нужды, это увеличивает количество данных, которые передаются по сети
 * между СУБД и приложением.
 *
 * @author Andrey Grigorov
 */
@Entity
@Table(name = "documents_for_client")
public class DocumentForClientShortData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id записи

    @Column(name = "doc_type")
    private String docType; //тип документа

    @Column(name = "doc_series")
    private String docSeries; // серия документа

    @Column(name = "doc_number")
    private String docNumber; // номер документа

    @Column(name = "is_active")
    private boolean isActive; // чек-бокс «Активен»

    @Column(name = "is_main")
    private boolean isMain; // чек-бокс «Основной»

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientShortData client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocSeries() {
        return docSeries;
    }

    public void setDocSeries(String docSeries) {
        this.docSeries = docSeries;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public ClientShortData getClient() {
        return client;
    }

    public void setClient(ClientShortData client) {
        this.client = client;
    }
}
