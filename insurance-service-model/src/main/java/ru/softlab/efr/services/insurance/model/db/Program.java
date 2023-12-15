package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.enums.RelatedEmployeeGroupFilterType;
import ru.softlab.efr.services.insurance.model.enums.RelatedOfficeFilterType;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

/**
 * Сущность для хранения данных вида программ страхования.
 *
 * @author Kalantaev
 * @since 19.09.2018
 */
@Entity
@Table(name = "program_v2")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Program implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Вид программы страхования.
     * Возможные типы: ИСЖ, НСЖ, КСП.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private ProgramKind type;

    /**
     * Номер программы страхования. Цифровое поле длиной максимум 3 символа.
     * Обязательно для заполнения.
     */
    @Column
    private String number;

    /**
     * Кодировка полиса. 3 символа. Заполняется по шаблону:
     * первый символ кириллица, два следующих – цифра.
     * Обязательно для заполнения.
     */
    @Column
    private String policyCode;

    /**
     * Вариант программы страхования. Формат 99, где 9 – цифра от нуля до девяти. По умолчанию не установлен.
     * Необязательно для заполнения.
     */
    @Column
    private String variant;

    /**
     * Наименование программы страхования. Текстовое поле длиной 50 символов.
     * Обязательно для заполнения.
     */
    @Column
    private String name;

    /**
     * Наименование программы страхования для печатной формы. Текстовое поле длиной 50 символов.
     */
    @Column(name = "name_for_print")
    private String nameForPrint;

    /**
     * Период охлаждения (дни). Период задается в днях.
     * Обязательно для заполнения.
     */
    @Column
    private Integer coolingPeriod;


    /**
     * Период ожидания (дни). Период задается в календарных днях.
     * Обязательно для заполнения для КСП, а для остальных видов программ страхования не задается.
     */
    @Column
    private Integer waitingPeriod;

    /**
     * Сегмент. Значение по умолчанию не выбрано. Выбирается из справочника сегментов (п.2.6.1)
     * Обязательно для заполнения.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "segment", nullable = true)
    @Fetch(FetchMode.JOIN)
    private Segment segment;

    /**
     * Тип привязки программы страхования к элементам организационной структуры организации.
     * Используется для построения фильтра, определяющего, имеет пользователь доступ к данной программе страхования
     * или нет.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private RelatedOfficeFilterType relatedOfficeFilterType;

    /**
     * Тип привязки программы страхования к группе пользователей.
     * Используется для построения фильтра, определяющего, имеет пользователь доступ к данной программе страхования
     * или нет.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private RelatedEmployeeGroupFilterType relatedEmployeeGroupFilterType;

    /**
     * Список названий элементов организационной структуры организации, которые используются для построения фильтра,
     * определяющего, имеет пользователь доступ к данной программе страхования или нет.
     * Данный список заполняется в случаях, когда в {@link Program#relatedOfficeFilterType} указаны следующие значения:
     * {@link RelatedOfficeFilterType#INCLUDE} или {@link RelatedOfficeFilterType#EXCLUDE}.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "program")
    @Fetch(FetchMode.SUBSELECT)
    private List<RelatedOffice> relatedOffices;


    /**
     * Список названий групп пользователей, которые используются для построения фильтра,
     * определяющего, имеет пользователь доступ к данной программе страхования или нет.
     * Данный список заполняется в случаях, когда в {@link Program#relatedEmployeeGroupFilterType} указаны следующие значения:
     * {@link RelatedEmployeeGroupFilterType#INCLUDE} или {@link RelatedEmployeeGroupFilterType#EXCLUDE}.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "program")
    @Fetch(FetchMode.SUBSELECT)
    private List<RelatedEmployeeGroup> relatedEmployeeGroups;

    /**
     * Признак действующей программы страхования. По умолчанию признак установлен.
     * Обязательно для заполнения.
     */
    @Column
    private Boolean isActive;

    /**
     * Признак комуляции
     */
    @Column
    private Long comulation;

    /**
     * буквенный код программы
     */
    @Column(name = "insurance_char_code")
    private String programCharCode;

    /**
     * код программы
     */
    @Column(name = "insurance_program_code")
    private String programCode;


    /**
     * Тариф программы страхования
     */
    @Column(name = "program_tariff")
    private String programTariff;

    /**
     * Признак удалённой программы страхования
     */
    @Column
    private Boolean deleted;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public ProgramKind getType() {
        return type;
    }

    public void setType(ProgramKind type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameForPrint() {
        return nameForPrint;
    }

    public void setNameForPrint(String nameForPrint) {
        this.nameForPrint = nameForPrint;
    }

    public Integer getCoolingPeriod() {
        return coolingPeriod;
    }

    public void setCoolingPeriod(Integer coolingPeriod) {
        this.coolingPeriod = coolingPeriod;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }

    public Integer getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(Integer waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public RelatedOfficeFilterType getRelatedOfficeFilterType() {
        return relatedOfficeFilterType;
    }

    public void setRelatedOfficeFilterType(RelatedOfficeFilterType relatedOfficeFilterType) {
        this.relatedOfficeFilterType = relatedOfficeFilterType;
    }

    public List<RelatedOffice> getRelatedOffices() {
        return relatedOffices;
    }

    public void setRelatedOffices(List<RelatedOffice> relatedOffices) {
        this.relatedOffices = relatedOffices;
    }

    public RelatedEmployeeGroupFilterType getRelatedEmployeeGroupFilterType() {
        return relatedEmployeeGroupFilterType;
    }

    public void setRelatedEmployeeGroupFilterType(RelatedEmployeeGroupFilterType relatedEmployeeGroupFilterType) {
        this.relatedEmployeeGroupFilterType = relatedEmployeeGroupFilterType;
    }

    public List<RelatedEmployeeGroup> getRelatedEmployeeGroups() {
        return relatedEmployeeGroups;
    }

    public void setRelatedEmployeeGroups(List<RelatedEmployeeGroup> relatedEmployeeGroups) {
        this.relatedEmployeeGroups = relatedEmployeeGroups;
    }

    public Long getComulation() {
        return comulation;
    }

    public void setComulation(Long comulation) {
        this.comulation = comulation;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getProgramCharCode() {
        return programCharCode;
    }

    public void setProgramCharCode(String programCharCode) {
        this.programCharCode = programCharCode;
    }

    public String getProgramTariff() {
        return programTariff;
    }

    public void setProgramTariff(String programTariff) {
        this.programTariff = programTariff;
    }
}
