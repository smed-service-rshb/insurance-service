package ru.softlab.efr.services.insurance.model.db;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import ru.softlab.efr.services.insurance.model.enums.CalendarUnitEnum;
import ru.softlab.efr.services.insurance.model.enums.PeriodicityEnum;
import ru.softlab.efr.services.insurance.model.enums.SourceEnum;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность для хранения данных договора страхования.
 *
 * @author Krivenko
 * @since 16.10.2018
 */
@Entity
@Table(name = "insurance")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * uuid договора.
     */
    @Column(name = "uuid")
    private String uuid;

    /**
     * Дата и время создания договора.
     */
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    /**
     * Номер договора.
     */
    @Column(name = "contract_number")
    private String contractNumber;

    /**
     * Номер договора.
     */
    @Column(name = "initial_contract_number")
    private String initialContractNumber;

    /**
     * Идентификатор учётной записи работника, создавшего договор.
     */
    @Column(name = "employee_id")
    private Long employeeId;

    /**
     * ФИО работника, создавшего договор.
     */
    @Column(name = "employee_name")
    private String employeeName;

    /**
     * Id работника КЦ, создавшего договор.
     */
    @Column(name = "call_center_employee_id")
    private Long callCenterEmployeeId;

    /**
     * ФИО работника КЦ, создавшего договор.
     */
    @Column(name = "call_center_employee_name")
    private String callCenterEmployeeName;

    /**
     * Табельный номер работника КЦ, создавшего договор.
     */
    @Column(name = "call_center_employee_number")
    private String callCenterEmployeeNumber;


    /**
     * Наименование регионального филиала банка, работника КЦ, создавшего договор.
     */
    @Column(name = "call_center_branch_name")
    private String callCenterBranchName;

    /**
     * Наименование ВСП, работника КЦ, создавшего договор.
     */
    @Column(name = "call_center_subdivision_name")
    private String callCenterSubdivisionName;

    /**
     * Идентификатор регионального филиала банка, в котором оформлен договор.
     */
    @Column(name = "branch_id")
    private Long branchId;

    /**
     * Наименование регионального филиала банка, в котором оформлен договор.
     */
    @Column(name = "branch_name")
    private String branchName;

    /**
     * Идентификатор ВСП в котором был оформлен договор
     */
    @Column(name = "subdivision_id", nullable = false)
    private long subdivisionId;

    /**
     * Наименование ВСП.
     * В настоящий момент значение данного поля будет использоваться для организации связи
     * со значениями в справочнике подразделений из сервиса авторизации.
     * Сейчас название ВСП представляет собой код из четырёх цифр, которые изменятся часто не должны.
     * Пока будем исходить из того, что, если будет какое-то переименование ВСП в БД сервиса авторизации,
     * то изменения в БД сервиса страхования будем осуществлять специально подготовленными миграционными SQL-скриптами.
     */
    @Column(name = "subdivision_name")
    private String subdivisionName;

    /**
     * Параметры программы страхования
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "program_setting_id", nullable = false)
    @NotAudited
    private ProgramSetting programSetting;

    /**
     * Параметры программы страхования
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acquiring_program_id")
    @NotAudited
    private AcquiringProgram acquiringProgram;

    /**
     * Признак логического удаления договора.
     */
    @Column(name = "deleted")
    private Boolean deleted;

    /**
     * ID анкеты страхователя
     */
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "holder_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private ClientEntity holder;

    /**
     * Номер версии анкеты страхователя
     */
    @Column(name = "holder_version")
    private Integer holderVersion;

    /**
     * ID анкеты застрахованного
     */
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "insured_id", nullable = true)
    @Fetch(FetchMode.JOIN)
    private ClientEntity insured;

    /**
     * Номер версии анкеты застрахованного
     */
    @Column(name = "insured_version")
    private Integer insuredVersion;

    /**
     * •	Признак, что Страхователь является Застрахованным
     */
    @Column(name = "holder_equals_insured")
    private Boolean holderEqualsInsured = Boolean.TRUE;

    /**
     * Срок страхования
     */
    @Column(name = "duration", nullable = false)
    private Integer duration;

    /**
     * Дата перехода документа в статус "Оформлен"
     */
    @Column
    private LocalDate conclusionDate;

    /**
     * Дата начала действия договора страхования
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * Фиксированная дата начала действия договора страхования
     * Дата начала действия не может быть менее фиксированной
     * Введена для КСП для случая когда найдены другие действующие договора
     */
    @Column(name = "fixed_start_date")
    private LocalDate fixedStartDate;

    /**
     * Дата закрытия договора
     */
    @Column(name = "close_date")
    private LocalDate closeDate;

    /**
     * ID валюты договора
     */
    @Column(name = "currency")
    private Long currency;

    /**
     * Курс валюты договора к рублю РФ на дату оформления договора
     */
    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    /**
     * Страховая премия в валюте договора
     */
    @Column(name = "premium")
    private BigDecimal premium;

    /**
     * Премия без скидки
     */
    @Column(name = "premium_without_discount")
    private BigDecimal premiumWithoutDiscount;

    /**
     * Процент скидки
     */
    @Column(name = "discount")
    private BigDecimal discount;

    /**
     * Страховая премия в национальной валюте
     */
    @Column(name = "rur_premium")
    private BigDecimal rurPremium;

    /**
     * Страховая сумма в валюте договора
     */
    @Column(name = "amount")
    private BigDecimal amount;


    /**
     * Страховая сумма в национальной валюте
     */
    @Column(name = "rur_amount")
    private BigDecimal rurAmount;

    /**
     * Периодичность уплаты взносов, выбор одного значения:
     * единовременно, ежемесячно, ежеквартально, «раз в полгода», ежегодно
     * По умолчанию единовременно
     */
    @Column(name = "periodicity")
    @Enumerated(value = EnumType.STRING)
    private PeriodicityEnum periodicity = PeriodicityEnum.YEARLY;

    /**
     * Рост застрахованного. Указывается в сантиметрах.
     */
    @Column(name = "growth")
    private Integer growth;

    /**
     * Вес застрахованного. Указывается в килограммах.
     */
    @Column(name = "weight")
    private Integer weight;

    /**
     * Давление верхнее застрахованного
     */
    @Column(name = "upper_pressure")
    private Integer upperPressure;

    /**
     * Давление нижнее застрахованного
     */
    @Column(name = "lower_pressure")
    private Integer lowerPressure;

    /**
     * Уровень гарантии (%)
     */
    @Column(name = "guarantee_level")
    private BigDecimal guaranteeLevel;

    /**
     * •	Коэффициент участия (%).
     */
    @Column(name = "participation_rate")
    private BigDecimal participationRate;

    /**
     * Признак, что страхователь является Выгодоприобретателем
     */
    @Column(name = "recipient_equals_holder")
    private Boolean recipientEqualsHolder = true;

    /**
     * Информация по обязательным рискам
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "insurance", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<InsuranceRiskEntity> riskInfoList = new ArrayList<>();

    /**
     * Информация по дополнительным рискам
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "insurance", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotAudited
    private List<InsuranceAddRiskEntity> addRiskInfoList = new ArrayList<>();


    /**
     * Информация по коэффициентам выкупных сумм
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "insurance", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<InsuranceBuyoutEntity> buyoutList = new ArrayList<>();

    /**
     * Выбранная стратегия (ИСЖ)
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "strategy_id")
    private Strategy strategy;

    /**
     * Информация выгодоприобретателям
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "insurance", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<InsuranceRecipientEntity> recipientList = new ArrayList<>();


    /**
     * Статус договор страхования
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private InsuranceStatus status;

    /**
     * Источник продажи продукта
     */
    @Column(name = "source")
    @Enumerated(value = EnumType.STRING)
    private SourceEnum source = SourceEnum.OFFICE;

    /**
     * Календарная единица срока страхования, выбор одного значения
     * лет, месяцев, дней
     * по умолчанию лет
     */
    @Column(name = "calendar_unit", nullable = false)
    @Enumerated(EnumType.STRING)
    private CalendarUnitEnum calendarUnit;

    /**
     * Флаг который указывает на тип расчета указанный при подборе договора
     * true - по сумме
     * false - по премии
     */
    @Column(name = "is_calc_by_sum")
    private Boolean isCalcBySum = Boolean.TRUE;

    /**
     * Срок выплаты в годах, число
     * Максимум 4 символа.
     * Указывается только для ренты
     */
    @Column
    private Integer paymentTerm;

    /**
     * Код идентификации, при оформлении в колл-центре
     */
    @Column(name = "code")
    private String code;


    /**
     * Дата окончания действия договора страхования
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Связка копии на исходный договор
     */
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_insurance")
    private Insurance parent;

    /**
     * Признак получения полного пакета документа клиентов
     */
    @Column(name = "full_set_document")
    private Boolean fullSetDocument;

    @Column(name = "comment_for_not_full_documents")
    private String commentForNotFullSetDocument;

    @Column(name = "individual_rate")
    private Boolean individualRate;

    @Column(name = "individual_rate_date")
    private LocalDate individualRateDate;

    @Column(name = "set_rate_empoloyee_id")
    private Long setRateEmployeeId;


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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getSubdivisionName() {
        return subdivisionName;
    }

    public void setSubdivisionName(String subdivisionName) {
        this.subdivisionName = subdivisionName;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public long getSubdivisionId() {
        return subdivisionId;
    }

    public void setSubdivisionId(long subdivisionId) {
        this.subdivisionId = subdivisionId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public ProgramSetting getProgramSetting() {
        return programSetting;
    }

    public void setProgramSetting(ProgramSetting programSetting) {
        this.programSetting = programSetting;
    }

    public ClientEntity getHolder() {
        return holder;
    }

    public void setHolder(ClientEntity holder) {
        this.holder = holder;
    }

    public ClientEntity getInsured() {
        return insured;
    }

    public void setInsured(ClientEntity insured) {
        this.insured = insured;
    }

    public Integer getHolderVersion() {
        return holderVersion;
    }

    public void setHolderVersion(Integer holderVersion) {
        this.holderVersion = holderVersion;
    }


    public Integer getInsuredVersion() {
        return insuredVersion;
    }

    public void setInsuredVersion(Integer insuredVersion) {
        this.insuredVersion = insuredVersion;
    }

    public Boolean getHolderEqualsInsured() {
        return holderEqualsInsured;
    }

    public void setHolderEqualsInsured(Boolean holderEqualsInsured) {
        this.holderEqualsInsured = holderEqualsInsured;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public BigDecimal getPremiumWithoutDiscount() {
        return premiumWithoutDiscount;
    }

    public void setPremiumWithoutDiscount(BigDecimal premiumWithoutDiscount) {
        this.premiumWithoutDiscount = premiumWithoutDiscount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getRurPremium() {
        return rurPremium;
    }

    public void setRurPremium(BigDecimal rurPremium) {
        this.rurPremium = rurPremium;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRurAmount() {
        return rurAmount;
    }

    public void setRurAmount(BigDecimal rur_amount) {
        this.rurAmount = rur_amount;
    }

    public PeriodicityEnum getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(PeriodicityEnum periodicity) {
        this.periodicity = periodicity;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getUpperPressure() {
        return upperPressure;
    }

    public void setUpperPressure(Integer upperPressure) {
        this.upperPressure = upperPressure;
    }

    public Integer getLowerPressure() {
        return lowerPressure;
    }

    public void setLowerPressure(Integer lowerPressure) {
        this.lowerPressure = lowerPressure;
    }

    public BigDecimal getGuaranteeLevel() {
        return guaranteeLevel;
    }

    public void setGuaranteeLevel(BigDecimal guaranteeLevel) {
        this.guaranteeLevel = guaranteeLevel;
    }

    public BigDecimal getParticipationRate() {
        return participationRate;
    }

    public void setParticipationRate(BigDecimal participationRate) {
        this.participationRate = participationRate;
    }

    public Boolean getRecipientEqualsHolder() {
        return recipientEqualsHolder;
    }

    public void setRecipientEqualsHolder(Boolean recipientEqualsHolder) {
        this.recipientEqualsHolder = recipientEqualsHolder;
    }

    public List<InsuranceRiskEntity> getRiskInfoList() {
        return riskInfoList;
    }

    public void setRiskInfoList(List<InsuranceRiskEntity> riskInfoList) {
        this.riskInfoList = riskInfoList;
    }

    public List<InsuranceAddRiskEntity> getAddRiskInfoList() {
        return addRiskInfoList;
    }

    public void setAddRiskInfoList(List<InsuranceAddRiskEntity> addRiskInfoList) {
        this.addRiskInfoList = addRiskInfoList;
    }

    public List<InsuranceBuyoutEntity> getBuyoutList() {
        return buyoutList;
    }

    public void setBuyoutList(List<InsuranceBuyoutEntity> buyoutList) {
        this.buyoutList = buyoutList;
    }

    public List<InsuranceRecipientEntity> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List<InsuranceRecipientEntity> recipientList) {
        this.recipientList = recipientList;
    }

    public InsuranceStatus getStatus() {
        return status;
    }

    public void setStatus(InsuranceStatus status) {
        this.status = status;
    }

    public SourceEnum getSource() {
        return source;
    }

    public void setSource(SourceEnum source) {
        this.source = source;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public CalendarUnitEnum getCalendarUnit() {
        return calendarUnit;
    }

    public void setCalendarUnit(CalendarUnitEnum calendarUnit) {
        this.calendarUnit = calendarUnit;
    }

    public Boolean getCalcBySum() {
        return isCalcBySum;
    }

    public void setCalcBySum(Boolean calcBySum) {
        isCalcBySum = calcBySum;
    }

    public Integer getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(Integer paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public LocalDate getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(LocalDate conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCallCenterEmployeeName() {
        return callCenterEmployeeName;
    }

    public void setCallCenterEmployeeName(String callCenterEmployeeName) {
        this.callCenterEmployeeName = callCenterEmployeeName;
    }

    public String getCallCenterEmployeeNumber() {
        return callCenterEmployeeNumber;
    }

    public void setCallCenterEmployeeNumber(String callCenterEmployeeNumber) {
        this.callCenterEmployeeNumber = callCenterEmployeeNumber;
    }

    public Long getCallCenterEmployeeId() {
        return callCenterEmployeeId;
    }

    public void setCallCenterEmployeeId(Long callCenterEmployeeId) {
        this.callCenterEmployeeId = callCenterEmployeeId;
    }

    public LocalDate getEndDate() {
        if (endDate != null) {
            return endDate;
        } else {
            return calculateEndDate();
        }
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getFixedStartDate() {
        return fixedStartDate;
    }

    public void setFixedStartDate(LocalDate fixedStartDate) {
        this.fixedStartDate = fixedStartDate;
    }


    public String getCallCenterBranchName() {
        return callCenterBranchName;
    }

    public void setCallCenterBranchName(String callCenterBranchName) {
        this.callCenterBranchName = callCenterBranchName;
    }

    public String getCallCenterSubdivisionName() {
        return callCenterSubdivisionName;
    }

    public void setCallCenterSubdivisionName(String callCenterSubdivisionName) {
        this.callCenterSubdivisionName = callCenterSubdivisionName;
    }

    public Boolean getFullSetDocument() {
        return fullSetDocument;
    }

    public void setFullSetDocument(Boolean fullSetDocument) {
        this.fullSetDocument = fullSetDocument;
    }

    public Insurance getParent() {
        return parent;
    }

    public void setParent(Insurance parent) {
        this.parent = parent;
    }

    public String getInitialContractNumber() {
        return initialContractNumber;
    }

    public void setInitialContractNumber(String initialContractNumber) {
        this.initialContractNumber = initialContractNumber;
    }

    public String getCommentForNotFullSetDocument() {
        return commentForNotFullSetDocument;
    }

    public void setCommentForNotFullSetDocument(String commentForNotFullSetDocument) {
        this.commentForNotFullSetDocument = commentForNotFullSetDocument;
    }

    public Boolean getIndividualRate() {
        return individualRate;
    }

    public void setIndividualRate(Boolean individualRate) {
        this.individualRate = individualRate;
    }

    public LocalDate getIndividualRateDate() {
        return individualRateDate;
    }

    public void setIndividualRateDate(LocalDate individualRateDate) {
        this.individualRateDate = individualRateDate;
    }

    public Long getSetRateEmployeeId() {
        return setRateEmployeeId;
    }

    public void setSetRateEmployeeId(Long setRateEmployeeId) {
        this.setRateEmployeeId = setRateEmployeeId;
    }

    public AcquiringProgram getAcquiringProgram() {
        return acquiringProgram;
    }

    public void setAcquiringProgram(AcquiringProgram acquiringProgram) {
        this.acquiringProgram = acquiringProgram;
    }

    @Transient
    public LocalDate calculateEndDate() {
        Integer paymentTerm = this.getPaymentTerm();
        LocalDate startDate = this.getStartDate();
        if (startDate == null)
            return null;
        if (paymentTerm != null) {
            startDate = startDate.plusYears(paymentTerm);
        }
        startDate = startDate.plusDays(-1);
        Integer duration = this.getDuration();
        if (duration == null)
            return startDate;
        switch (this.getCalendarUnit()) {
            case DAY:
                return startDate.plusDays(duration);
            case MONTH:
                return startDate.plusMonths(duration);
            case YEAR:
                return startDate.plusYears(duration);
            default:
                return null;
        }
    }

    @Transient
    public ClientEntity getClient() {
        if (this.insured != null) {
            return insured;
        } else {
            return holder;
        }
    }

}
