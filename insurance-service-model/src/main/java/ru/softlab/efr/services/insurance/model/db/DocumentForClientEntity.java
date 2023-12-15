package ru.softlab.efr.services.insurance.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.envers.Audited;
import ru.softlab.efr.services.insurance.model.enums.IdentityDocTypeEnum;
import ru.softlab.efr.services.insurance.model.rest.Document;
import ru.softlab.efr.services.insurance.model.rest.DocumentType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Документ клиента
 * @author basharin
 * @since 05.02.2018
 */
@Entity
@Table(name = "documents_for_client")
@Audited
public class DocumentForClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id записи

    @Column(name = "doc_type")
    @Enumerated(EnumType.STRING)
    private IdentityDocTypeEnum docType; //тип документа

    @Column(name = "doc_series")
    private String docSeries; // серия документа

    @Column(name = "doc_number")
    private String docNumber; // номер документа

    @Column(name = "scan_id")
    private Long scanId; // ссылка скан документа

    @Column(name = "issued_by")
    private String issuedBy; // кем выдан

    @Column(name = "issued_date")
    private LocalDate issuedDate; // дата выдачи

    @Column(name = "is_active")
    private boolean isActive; // чек-бокс «Активен»

    @Column(name = "is_main")
    private boolean isMain; // чек-бокс «Основной»

    @Column(name = "division_code")
    private String divisionCode; // код подразделения

    @Column(name = "issued_end_date")
    private LocalDate issuedEndDate; // дата окончания

    @Column(name = "is_valid_document")
    private boolean isValidDocument; // признак действительного документа

    @Column(name = "stay_start_date")
    private LocalDate stayStartDate; // дата начала пребывания

    @Column(name = "stay_end_date")
    private LocalDate stayEndDate; // дата окончания пребывания

    @Column(name = "is_identity")
    private boolean isIdentity; // является ли документ ДУЛ

    @Column(name = "doc_name")
    private String docName; // наименование документа (для тех у которых наименование вводится вручную)

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id")
    protected ClientEntity client;

    public DocumentForClientEntity() {}

    /**
     * Конструктор
     * @param document документ
     * @param client заявка
     */
    public DocumentForClientEntity(Document document, ClientEntity client) {
        update(document);
        this.client = client;
    }

    /**
     * получить id документа
     * @return id документа
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * получить тип документа
     * @return тип документа
     */
    public IdentityDocTypeEnum getDocType() {
        return docType;
    }

    public void setDocType(IdentityDocTypeEnum docType) {
        this.docType = docType;
    }

    /**
     * получить серия документа
     * @return серия документа
     */
    public String getDocSeries() {
        return docSeries;
    }

    public void setDocSeries(String docSeries) {
        this.docSeries = docSeries;
    }

    /**
     * получить номер документа
     * @return номер документа
     */
    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    /**
     * получить id скана документа
     * @return id скана документа
     */
    public Long getScanId() {
        return scanId;
    }

    public void setScanId(Long scanId) {
        this.scanId = scanId;
    }

    /**
     * получить кем выдан документа
     * @return кем выдан документа
     */
    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    /**
     * получить дата выдачи документа
     * @return дата выдачи документа
     */
    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    /**
     * получить Активен ли документ
     * @return Активен ли документ
     */
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * получить главный ли документа
     * @return главный ли документа
     */
    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    /**
     * получить код подразделения документа
     * @return код подразделения документа
     */
    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    /**
     * получить дата окончания документа
     * @return дата окончания документа
     */
    public LocalDate getIssuedEndDate() {
        return issuedEndDate;
    }

    public void setIssuedEndDate(LocalDate issuedEndDate) {
        this.issuedEndDate = issuedEndDate;
    }

    /**
     * получить признак действительного документа
     * @return признак действительного документа
     */
    public boolean isValidDocument() {
        return isValidDocument;
    }

    public void setValidDocument(boolean validDocument) {
        isValidDocument = validDocument;
    }

    /**
     * получить дата начала пребывания документа
     * @return дата начала пребывания документа
     */
    public LocalDate getStayStartDate() {
        return stayStartDate;
    }

    public void setStayStartDate(LocalDate stayStartDate) {
        this.stayStartDate = stayStartDate;
    }

    /**
     * получить дата окончания пребывания документа
     * @return дата окончания пребывания документа
     */
    public LocalDate getStayEndDate() {
        return stayEndDate;
    }

    public void setStayEndDate(LocalDate stayEndDate) {
        this.stayEndDate = stayEndDate;
    }

    public boolean isIdentity() {
        return isIdentity;
    }

    public void setIdentity(boolean identity) {
        isIdentity = identity;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getDocType(), this.getDocSeries(), this.getDocNumber(), this.getScanId(), this.getIssuedBy(),
                this.getIssuedDate(), this.isActive(), this.isMain(), this.getDivisionCode(), this.getIssuedEndDate(),
                 this.isValidDocument(), this.getStayStartDate(), this.getStayEndDate()
                );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DocumentForClientEntity documentForClientEntity = (DocumentForClientEntity) obj;
        return new EqualsBuilder()
                .append(this.getDocType(), documentForClientEntity.getDocType())
                .append(this.getDocSeries(), documentForClientEntity.getDocSeries())
                .append(this.getDocNumber(), documentForClientEntity.getDocNumber())
                .append(this.getScanId(), documentForClientEntity.getScanId())
                .append(this.getIssuedBy(), documentForClientEntity.getIssuedBy())
                .append(this.getIssuedDate(), documentForClientEntity.getIssuedDate())
                .append(this.isActive(), documentForClientEntity.isActive())
                .append(this.isMain(), documentForClientEntity.isMain())
                .append(this.getDivisionCode(), documentForClientEntity.getDivisionCode())
                .append(this.getIssuedEndDate(), documentForClientEntity.getIssuedEndDate())
                .append(this.isValidDocument(), documentForClientEntity.isValidDocument())
                .append(this.getStayStartDate(), documentForClientEntity.getStayStartDate())
                .append(this.getStayEndDate(), documentForClientEntity.getStayEndDate())
                .append(this.isIdentity(), documentForClientEntity.isIdentity())
                .append(this.getDocName(), documentForClientEntity.getDocName())
                .isEquals();
    }

    /**
     * Обновить информацию о документе
     * @param document документ
     */
    public final void update(Document document) {
        this.docType = document.getDocType() != null ? IdentityDocTypeEnum.valueOf(document.getDocType().name()) : null;
        this.docSeries = document.getDocSeries();
        this.docNumber = document.getDocNumber();
        this.scanId = document.getScanId();
        this.docName = document.getDocName();
        this.issuedBy = document.getIssuedBy();
        this.issuedDate = document.getIssuedDate();
        this.isActive = BooleanUtils.isTrue(document.isIsActive());
        this.isMain = BooleanUtils.isTrue(document.isIsMain());
        this.divisionCode = document.getDivisionCode();
        this.issuedEndDate = document.getIssuedEndDate();
        this.isValidDocument = BooleanUtils.isTrue(document.isIsValidDocument());
        this.stayEndDate = document.getStayEndDate();
        this.stayStartDate = document.getStayStartDate();
    }

    /**
     * Конветрация в объекта класса {@link Document}
     * @param entity документ
     * @return документ
     */
    public static Document toDocument(DocumentForClientEntity entity) {
        Document document = new Document();
        document.setDocType(entity.getDocType() != null ? DocumentType.valueOf(entity.getDocType().name()) : null);
        document.setDocSeries(entity.getDocSeries());
        document.setDocNumber(entity.getDocNumber());
        document.setDocName(entity.getDocName());
        document.setScanId(entity.getScanId());
        document.setIssuedBy(entity.getIssuedBy());
        document.setIssuedDate(entity.getIssuedDate());
        document.setIsActive(BooleanUtils.isTrue(entity.isActive()));
        document.setIsMain(BooleanUtils.isTrue(entity.isMain()));
        document.setDivisionCode(entity.getDivisionCode());
        document.setIssuedEndDate(entity.getIssuedEndDate());
        document.setIsValidDocument(BooleanUtils.isTrue(entity.isValidDocument()));
        document.setStayStartDate(entity.getStayStartDate());
        document.setStayEndDate(entity.getStayEndDate());
        return document;
    }

    public boolean isActualDocument() {
        LocalDate now = LocalDate.now();
        if (!isActive) return false;
        return issuedDate != null && issuedEndDate != null &&
                issuedDate.compareTo(now) <= 0 && issuedEndDate.isAfter(now);
    }

    public static boolean isActualDocument(Document document) {
        LocalDate now = LocalDate.now();
        if (!document.isIsActive()) return false;
        return document.getIssuedDate() != null &&
                document.getIssuedDate().compareTo(now) <= 0 && document.getIssuedDate().isAfter(now);
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }
}
