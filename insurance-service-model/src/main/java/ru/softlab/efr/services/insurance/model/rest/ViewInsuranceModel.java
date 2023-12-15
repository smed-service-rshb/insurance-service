package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.AddRiskInfo;
import ru.softlab.efr.services.insurance.model.rest.BaseInsuranceModel;
import ru.softlab.efr.services.insurance.model.rest.CalendarUnit;
import ru.softlab.efr.services.insurance.model.rest.Client;
import ru.softlab.efr.services.insurance.model.rest.FindProgramType;
import ru.softlab.efr.services.insurance.model.rest.InsuranceRecipient;
import ru.softlab.efr.services.insurance.model.rest.InsuranceStatusType;
import ru.softlab.efr.services.insurance.model.rest.PaymentPeriodicity;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.RiskInfo;
import ru.softlab.efr.services.insurance.model.rest.StrategyProperty;
import ru.softlab.efr.services.insurance.model.rest.StrategyType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ViewInsuranceModel
 */
@Validated
public class ViewInsuranceModel   {
    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("programSettingId")
    private Long programSettingId = null;

    @JsonProperty("holderId")
    private Long holderId = null;

    @JsonProperty("newHolder")
    private Boolean newHolder = null;

    @JsonProperty("holderData")
    private Client holderData = null;

    @JsonProperty("insuredId")
    private Long insuredId = null;

    @JsonProperty("newInsured")
    private Boolean newInsured = null;

    @JsonProperty("insuredData")
    private Client insuredData = null;

    @JsonProperty("holderEqualsInsured")
    private Boolean holderEqualsInsured = null;

    @JsonProperty("duration")
    private Integer duration = null;

    @JsonProperty("calendarUnit")
    private CalendarUnit calendarUnit = null;

    @JsonProperty("currencyId")
    private Long currencyId = null;

    @JsonProperty("currencyCode")
    private String currencyCode = null;

    @JsonProperty("premium")
    private BigDecimal premium = null;

    @JsonProperty("premiumWithoutDiscount")
    private BigDecimal premiumWithoutDiscount = null;

    @JsonProperty("discount")
    private BigDecimal discount = null;

    @JsonProperty("rurPremium")
    private BigDecimal rurPremium = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("rurAmount")
    private BigDecimal rurAmount = null;

    @JsonProperty("periodicity")
    private PaymentPeriodicity periodicity = null;

    @JsonProperty("growth")
    private Integer growth = null;

    @JsonProperty("weight")
    private Integer weight = null;

    @JsonProperty("upperPressure")
    private Integer upperPressure = null;

    @JsonProperty("lowerPressure")
    private Integer lowerPressure = null;

    @JsonProperty("guaranteeLevel")
    private BigDecimal guaranteeLevel = null;

    @JsonProperty("participationRate")
    private BigDecimal participationRate = null;

    @JsonProperty("recipientEqualsHolder")
    private Boolean recipientEqualsHolder = null;

    @JsonProperty("riskInfoList")
    @Valid
    private List<RiskInfo> riskInfoList = null;

    @JsonProperty("addRiskInfoList")
    @Valid
    private List<AddRiskInfo> addRiskInfoList = null;

    @JsonProperty("recipientList")
    @Valid
    private List<InsuranceRecipient> recipientList = null;

    @JsonProperty("strategyId")
    private Long strategyId = null;

    @JsonProperty("type")
    private FindProgramType type = null;

    @JsonProperty("paymentTerm")
    private Integer paymentTerm = null;

    @JsonProperty("uuid")
    private String uuid = null;

    @JsonProperty("conclusionDate")
    private LocalDate conclusionDate = null;

    @JsonProperty("fullSetDocument")
    private Boolean fullSetDocument = null;

    @JsonProperty("commentForNotFullSetDocument")
    private String commentForNotFullSetDocument = null;

    @JsonProperty("individualRate")
    private Boolean individualRate = null;

    @JsonProperty("exchangeRate")
    private BigDecimal exchangeRate = null;

    @JsonProperty("employeeId")
    private Long employeeId = null;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("option")
    private String option = null;

    @JsonProperty("kind")
    private ProgramKind kind = null;

    @JsonProperty("contractNumber")
    private String contractNumber = null;

    @JsonProperty("status")
    private InsuranceStatusType status = null;

    @JsonProperty("programNumber")
    private String programNumber = null;

    @JsonProperty("programName")
    private String programName = null;

    @JsonProperty("creationDate")
    private LocalDate creationDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("closeDate")
    private LocalDate closeDate = null;

    @JsonProperty("strategyName")
    private String strategyName = null;

    @JsonProperty("strategyProperties")
    @Valid
    private List<StrategyProperty> strategyProperties = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("availableStatuses")
    @Valid
    private List<InsuranceStatusType> availableStatuses = null;

    @JsonProperty("previousStatus")
    private String previousStatus = null;

    @JsonProperty("isCopy")
    private Boolean isCopy = null;

    @JsonProperty("strategyType")
    private StrategyType strategyType = null;

    @JsonProperty("programCode")
    private String programCode = null;

    @JsonProperty("programTariff")
    private String programTariff = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ViewInsuranceModel() {}

    /**
     * Создает экземпляр класса
     * @param startDate Дата начала действия договора
     * @param programSettingId Идентификатор параметров программы страхования
     * @param holderId ID анкеты страхователя
     * @param newHolder Признак создания нового клиента
     * @param holderData Данные страхователя
     * @param insuredId ID анкеты застрахованного
     * @param newInsured Признак создания нового клиента
     * @param insuredData Данные страхователя
     * @param holderEqualsInsured Признак, что Страхователь является Застрахованным
     * @param duration Срок страхования
     * @param calendarUnit Календарная единица срока страхования
     * @param currencyId Идентификатор валюты
     * @param currencyCode ISO код валюты
     * @param premium Страховая премия в валюте договора
     * @param premiumWithoutDiscount Страховая премия в валюте договора без скидки
     * @param discount Процент скидки
     * @param rurPremium Страховая премия в национальной валюте
     * @param amount Страховая сумма в валюте договора
     * @param rurAmount Страховая сумма в национальной валюте
     * @param periodicity Периодичность уплаты взносов
     * @param growth Рост застрахованного. Указывается в сантиметрах.
     * @param weight Вес застрахованного. Указывается в килограммах.
     * @param upperPressure Давление верхнее застрахованного
     * @param lowerPressure Давление нижнее застрахованного
     * @param guaranteeLevel Уровень гарантии (%)
     * @param participationRate Коэффициент участия (%).
     * @param recipientEqualsHolder Признак, что страхователь является Выгодоприобретателем
     * @param riskInfoList Список обязательных рисков
     * @param addRiskInfoList Список дополнительных рисков
     * @param recipientList Список выгодоприобретателей
     * @param strategyId ID выбранной стратегии (ИСЖ)
     * @param type Способ расчёта по договору
     * @param paymentTerm Срок выплаты в годах (для Ренты)
     * @param uuid uuid договора
     * @param conclusionDate Дата оформления договора страхования
     * @param fullSetDocument Признак получения полного комплекта документов
     * @param commentForNotFullSetDocument Комментарий к признаку получения комплекта документов
     * @param individualRate Признак индивидуального курса валюты для договора
     * @param exchangeRate Ккурс валюты для договора
     * @param employeeId Идентификатор учётной записи работника, создавшего договор
     * @param id Идентификатор договора страхования
     * @param option Вариант программы страхования
     * @param kind Вид программы страхования
     * @param contractNumber Номер договора
     * @param status Актуальный статус договора
     * @param programNumber Номер программы страховани
     * @param programName Наименовние программы страхования
     * @param creationDate Дата создания договора
     * @param endDate Дата окончания договора
     * @param closeDate Дата закрытия договора
     * @param strategyName Наименование выбранной стратегии
     * @param strategyProperties 
     * @param code Код договора (заполняется только для сотрудника КЦ)
     * @param availableStatuses 
     * @param previousStatus Предыдущий статус
     * @param isCopy Признак создания договора копированием
     * @param strategyType Вид стратегии ИСЖ
     * @param programCode Кодировка программы (Латинская буква)
     * @param programTariff Кодировка тарифа (Латинская буква)
     */
    public ViewInsuranceModel(LocalDate startDate, Long programSettingId, Long holderId, Boolean newHolder, Client holderData, Long insuredId, Boolean newInsured, Client insuredData, Boolean holderEqualsInsured, Integer duration, CalendarUnit calendarUnit, Long currencyId, String currencyCode, BigDecimal premium, BigDecimal premiumWithoutDiscount, BigDecimal discount, BigDecimal rurPremium, BigDecimal amount, BigDecimal rurAmount, PaymentPeriodicity periodicity, Integer growth, Integer weight, Integer upperPressure, Integer lowerPressure, BigDecimal guaranteeLevel, BigDecimal participationRate, Boolean recipientEqualsHolder, List<RiskInfo> riskInfoList, List<AddRiskInfo> addRiskInfoList, List<InsuranceRecipient> recipientList, Long strategyId, FindProgramType type, Integer paymentTerm, String uuid, LocalDate conclusionDate, Boolean fullSetDocument, String commentForNotFullSetDocument, Boolean individualRate, BigDecimal exchangeRate, Long employeeId, Long id, String option, ProgramKind kind, String contractNumber, InsuranceStatusType status, String programNumber, String programName, LocalDate creationDate, LocalDate endDate, LocalDate closeDate, String strategyName, List<StrategyProperty> strategyProperties, String code, List<InsuranceStatusType> availableStatuses, String previousStatus, Boolean isCopy, StrategyType strategyType, String programCode, String programTariff) {
        this.startDate = startDate;
        this.programSettingId = programSettingId;
        this.holderId = holderId;
        this.newHolder = newHolder;
        this.holderData = holderData;
        this.insuredId = insuredId;
        this.newInsured = newInsured;
        this.insuredData = insuredData;
        this.holderEqualsInsured = holderEqualsInsured;
        this.duration = duration;
        this.calendarUnit = calendarUnit;
        this.currencyId = currencyId;
        this.currencyCode = currencyCode;
        this.premium = premium;
        this.premiumWithoutDiscount = premiumWithoutDiscount;
        this.discount = discount;
        this.rurPremium = rurPremium;
        this.amount = amount;
        this.rurAmount = rurAmount;
        this.periodicity = periodicity;
        this.growth = growth;
        this.weight = weight;
        this.upperPressure = upperPressure;
        this.lowerPressure = lowerPressure;
        this.guaranteeLevel = guaranteeLevel;
        this.participationRate = participationRate;
        this.recipientEqualsHolder = recipientEqualsHolder;
        this.riskInfoList = riskInfoList;
        this.addRiskInfoList = addRiskInfoList;
        this.recipientList = recipientList;
        this.strategyId = strategyId;
        this.type = type;
        this.paymentTerm = paymentTerm;
        this.uuid = uuid;
        this.conclusionDate = conclusionDate;
        this.fullSetDocument = fullSetDocument;
        this.commentForNotFullSetDocument = commentForNotFullSetDocument;
        this.individualRate = individualRate;
        this.exchangeRate = exchangeRate;
        this.employeeId = employeeId;
        this.id = id;
        this.option = option;
        this.kind = kind;
        this.contractNumber = contractNumber;
        this.status = status;
        this.programNumber = programNumber;
        this.programName = programName;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.closeDate = closeDate;
        this.strategyName = strategyName;
        this.strategyProperties = strategyProperties;
        this.code = code;
        this.availableStatuses = availableStatuses;
        this.previousStatus = previousStatus;
        this.isCopy = isCopy;
        this.strategyType = strategyType;
        this.programCode = programCode;
        this.programTariff = programTariff;
    }

    /**
     * Дата начала действия договора
    * @return Дата начала действия договора
    **/
    @ApiModelProperty(value = "Дата начала действия договора")
    
  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Идентификатор параметров программы страхования
    * @return Идентификатор параметров программы страхования
    **/
    @ApiModelProperty(required = true, value = "Идентификатор параметров программы страхования")
      @NotNull



    public Long getProgramSettingId() {
        return programSettingId;
    }

    public void setProgramSettingId(Long programSettingId) {
        this.programSettingId = programSettingId;
    }


    /**
     * ID анкеты страхователя
    * @return ID анкеты страхователя
    **/
    @ApiModelProperty(value = "ID анкеты страхователя")
    


    public Long getHolderId() {
        return holderId;
    }

    public void setHolderId(Long holderId) {
        this.holderId = holderId;
    }


    /**
     * Признак создания нового клиента
    * @return Признак создания нового клиента
    **/
    @ApiModelProperty(value = "Признак создания нового клиента")
    


    public Boolean isNewHolder() {
        return newHolder;
    }

    public void setNewHolder(Boolean newHolder) {
        this.newHolder = newHolder;
    }


    /**
     * Данные страхователя
    * @return Данные страхователя
    **/
    @ApiModelProperty(value = "Данные страхователя")
    
  @Valid


    public Client getHolderData() {
        return holderData;
    }

    public void setHolderData(Client holderData) {
        this.holderData = holderData;
    }


    /**
     * ID анкеты застрахованного
    * @return ID анкеты застрахованного
    **/
    @ApiModelProperty(value = "ID анкеты застрахованного")
    


    public Long getInsuredId() {
        return insuredId;
    }

    public void setInsuredId(Long insuredId) {
        this.insuredId = insuredId;
    }


    /**
     * Признак создания нового клиента
    * @return Признак создания нового клиента
    **/
    @ApiModelProperty(value = "Признак создания нового клиента")
    


    public Boolean isNewInsured() {
        return newInsured;
    }

    public void setNewInsured(Boolean newInsured) {
        this.newInsured = newInsured;
    }


    /**
     * Данные страхователя
    * @return Данные страхователя
    **/
    @ApiModelProperty(value = "Данные страхователя")
    
  @Valid


    public Client getInsuredData() {
        return insuredData;
    }

    public void setInsuredData(Client insuredData) {
        this.insuredData = insuredData;
    }


    /**
     * Признак, что Страхователь является Застрахованным
    * @return Признак, что Страхователь является Застрахованным
    **/
    @ApiModelProperty(required = true, value = "Признак, что Страхователь является Застрахованным")
      @NotNull



    public Boolean isHolderEqualsInsured() {
        return holderEqualsInsured;
    }

    public void setHolderEqualsInsured(Boolean holderEqualsInsured) {
        this.holderEqualsInsured = holderEqualsInsured;
    }


    /**
     * Срок страхования
    * @return Срок страхования
    **/
    @ApiModelProperty(required = true, value = "Срок страхования")
      @NotNull



    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }


    /**
     * Календарная единица срока страхования
    * @return Календарная единица срока страхования
    **/
    @ApiModelProperty(required = true, value = "Календарная единица срока страхования")
      @NotNull

  @Valid


    public CalendarUnit getCalendarUnit() {
        return calendarUnit;
    }

    public void setCalendarUnit(CalendarUnit calendarUnit) {
        this.calendarUnit = calendarUnit;
    }


    /**
     * Идентификатор валюты
    * @return Идентификатор валюты
    **/
    @ApiModelProperty(required = true, value = "Идентификатор валюты")
      @NotNull



    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }


    /**
     * ISO код валюты
    * @return ISO код валюты
    **/
    @ApiModelProperty(value = "ISO код валюты")
    


    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }


    /**
     * Страховая премия в валюте договора
    * @return Страховая премия в валюте договора
    **/
    @ApiModelProperty(value = "Страховая премия в валюте договора")
    
  @Valid


    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }


    /**
     * Страховая премия в валюте договора без скидки
    * @return Страховая премия в валюте договора без скидки
    **/
    @ApiModelProperty(value = "Страховая премия в валюте договора без скидки")
    
  @Valid


    public BigDecimal getPremiumWithoutDiscount() {
        return premiumWithoutDiscount;
    }

    public void setPremiumWithoutDiscount(BigDecimal premiumWithoutDiscount) {
        this.premiumWithoutDiscount = premiumWithoutDiscount;
    }


    /**
     * Процент скидки
    * @return Процент скидки
    **/
    @ApiModelProperty(value = "Процент скидки")
    
  @Valid


    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }


    /**
     * Страховая премия в национальной валюте
    * @return Страховая премия в национальной валюте
    **/
    @ApiModelProperty(value = "Страховая премия в национальной валюте")
    
  @Valid


    public BigDecimal getRurPremium() {
        return rurPremium;
    }

    public void setRurPremium(BigDecimal rurPremium) {
        this.rurPremium = rurPremium;
    }


    /**
     * Страховая сумма в валюте договора
    * @return Страховая сумма в валюте договора
    **/
    @ApiModelProperty(required = true, value = "Страховая сумма в валюте договора")
      @NotNull

  @Valid


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    /**
     * Страховая сумма в национальной валюте
    * @return Страховая сумма в национальной валюте
    **/
    @ApiModelProperty(value = "Страховая сумма в национальной валюте")
    
  @Valid


    public BigDecimal getRurAmount() {
        return rurAmount;
    }

    public void setRurAmount(BigDecimal rurAmount) {
        this.rurAmount = rurAmount;
    }


    /**
     * Периодичность уплаты взносов
    * @return Периодичность уплаты взносов
    **/
    @ApiModelProperty(value = "Периодичность уплаты взносов")
    
  @Valid


    public PaymentPeriodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(PaymentPeriodicity periodicity) {
        this.periodicity = periodicity;
    }


    /**
     * Рост застрахованного. Указывается в сантиметрах.
    * @return Рост застрахованного. Указывается в сантиметрах.
    **/
    @ApiModelProperty(value = "Рост застрахованного. Указывается в сантиметрах.")
    


    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }


    /**
     * Вес застрахованного. Указывается в килограммах.
    * @return Вес застрахованного. Указывается в килограммах.
    **/
    @ApiModelProperty(value = "Вес застрахованного. Указывается в килограммах.")
    


    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }


    /**
     * Давление верхнее застрахованного
    * @return Давление верхнее застрахованного
    **/
    @ApiModelProperty(value = "Давление верхнее застрахованного")
    


    public Integer getUpperPressure() {
        return upperPressure;
    }

    public void setUpperPressure(Integer upperPressure) {
        this.upperPressure = upperPressure;
    }


    /**
     * Давление нижнее застрахованного
    * @return Давление нижнее застрахованного
    **/
    @ApiModelProperty(value = "Давление нижнее застрахованного")
    


    public Integer getLowerPressure() {
        return lowerPressure;
    }

    public void setLowerPressure(Integer lowerPressure) {
        this.lowerPressure = lowerPressure;
    }


    /**
     * Уровень гарантии (%)
    * @return Уровень гарантии (%)
    **/
    @ApiModelProperty(value = "Уровень гарантии (%)")
    
  @Valid


    public BigDecimal getGuaranteeLevel() {
        return guaranteeLevel;
    }

    public void setGuaranteeLevel(BigDecimal guaranteeLevel) {
        this.guaranteeLevel = guaranteeLevel;
    }


    /**
     * Коэффициент участия (%).
    * @return Коэффициент участия (%).
    **/
    @ApiModelProperty(value = "Коэффициент участия (%).")
    
  @Valid


    public BigDecimal getParticipationRate() {
        return participationRate;
    }

    public void setParticipationRate(BigDecimal participationRate) {
        this.participationRate = participationRate;
    }


    /**
     * Признак, что страхователь является Выгодоприобретателем
    * @return Признак, что страхователь является Выгодоприобретателем
    **/
    @ApiModelProperty(value = "Признак, что страхователь является Выгодоприобретателем")
    


    public Boolean isRecipientEqualsHolder() {
        return recipientEqualsHolder;
    }

    public void setRecipientEqualsHolder(Boolean recipientEqualsHolder) {
        this.recipientEqualsHolder = recipientEqualsHolder;
    }


    public ViewInsuranceModel addRiskInfoListItem(RiskInfo riskInfoListItem) {
        if (this.riskInfoList == null) {
            this.riskInfoList = new ArrayList<RiskInfo>();
        }
        this.riskInfoList.add(riskInfoListItem);
        return this;
    }

    /**
     * Список обязательных рисков
    * @return Список обязательных рисков
    **/
    @ApiModelProperty(value = "Список обязательных рисков")
    
  @Valid


    public List<RiskInfo> getRiskInfoList() {
        return riskInfoList;
    }

    public void setRiskInfoList(List<RiskInfo> riskInfoList) {
        this.riskInfoList = riskInfoList;
    }


    public ViewInsuranceModel addAddRiskInfoListItem(AddRiskInfo addRiskInfoListItem) {
        if (this.addRiskInfoList == null) {
            this.addRiskInfoList = new ArrayList<AddRiskInfo>();
        }
        this.addRiskInfoList.add(addRiskInfoListItem);
        return this;
    }

    /**
     * Список дополнительных рисков
    * @return Список дополнительных рисков
    **/
    @ApiModelProperty(value = "Список дополнительных рисков")
    
  @Valid


    public List<AddRiskInfo> getAddRiskInfoList() {
        return addRiskInfoList;
    }

    public void setAddRiskInfoList(List<AddRiskInfo> addRiskInfoList) {
        this.addRiskInfoList = addRiskInfoList;
    }


    public ViewInsuranceModel addRecipientListItem(InsuranceRecipient recipientListItem) {
        if (this.recipientList == null) {
            this.recipientList = new ArrayList<InsuranceRecipient>();
        }
        this.recipientList.add(recipientListItem);
        return this;
    }

    /**
     * Список выгодоприобретателей
    * @return Список выгодоприобретателей
    **/
    @ApiModelProperty(value = "Список выгодоприобретателей")
    
  @Valid


    public List<InsuranceRecipient> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List<InsuranceRecipient> recipientList) {
        this.recipientList = recipientList;
    }


    /**
     * ID выбранной стратегии (ИСЖ)
    * @return ID выбранной стратегии (ИСЖ)
    **/
    @ApiModelProperty(value = "ID выбранной стратегии (ИСЖ)")
    


    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }


    /**
     * Способ расчёта по договору
    * @return Способ расчёта по договору
    **/
    @ApiModelProperty(required = true, value = "Способ расчёта по договору")
      @NotNull

  @Valid


    public FindProgramType getType() {
        return type;
    }

    public void setType(FindProgramType type) {
        this.type = type;
    }


    /**
     * Срок выплаты в годах (для Ренты)
    * @return Срок выплаты в годах (для Ренты)
    **/
    @ApiModelProperty(value = "Срок выплаты в годах (для Ренты)")
    


    public Integer getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(Integer paymentTerm) {
        this.paymentTerm = paymentTerm;
    }


    /**
     * uuid договора
    * @return uuid договора
    **/
    @ApiModelProperty(value = "uuid договора")
    


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    /**
     * Дата оформления договора страхования
    * @return Дата оформления договора страхования
    **/
    @ApiModelProperty(value = "Дата оформления договора страхования")
    
  @Valid


    public LocalDate getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(LocalDate conclusionDate) {
        this.conclusionDate = conclusionDate;
    }


    /**
     * Признак получения полного комплекта документов
    * @return Признак получения полного комплекта документов
    **/
    @ApiModelProperty(value = "Признак получения полного комплекта документов")
    


    public Boolean isFullSetDocument() {
        return fullSetDocument;
    }

    public void setFullSetDocument(Boolean fullSetDocument) {
        this.fullSetDocument = fullSetDocument;
    }


    /**
     * Комментарий к признаку получения комплекта документов
    * @return Комментарий к признаку получения комплекта документов
    **/
    @ApiModelProperty(value = "Комментарий к признаку получения комплекта документов")
    


    public String getCommentForNotFullSetDocument() {
        return commentForNotFullSetDocument;
    }

    public void setCommentForNotFullSetDocument(String commentForNotFullSetDocument) {
        this.commentForNotFullSetDocument = commentForNotFullSetDocument;
    }


    /**
     * Признак индивидуального курса валюты для договора
    * @return Признак индивидуального курса валюты для договора
    **/
    @ApiModelProperty(value = "Признак индивидуального курса валюты для договора")
    


    public Boolean isIndividualRate() {
        return individualRate;
    }

    public void setIndividualRate(Boolean individualRate) {
        this.individualRate = individualRate;
    }


    /**
     * Ккурс валюты для договора
    * @return Ккурс валюты для договора
    **/
    @ApiModelProperty(value = "Ккурс валюты для договора")
    
  @Valid


    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }


    /**
     * Идентификатор учётной записи работника, создавшего договор
    * @return Идентификатор учётной записи работника, создавшего договор
    **/
    @ApiModelProperty(value = "Идентификатор учётной записи работника, создавшего договор")
    


    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }


    /**
     * Идентификатор договора страхования
    * @return Идентификатор договора страхования
    **/
    @ApiModelProperty(value = "Идентификатор договора страхования")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Вариант программы страхования
    * @return Вариант программы страхования
    **/
    @ApiModelProperty(value = "Вариант программы страхования")
    
 @Pattern(regexp="(^[0-9]{2}$)")

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }


    /**
     * Вид программы страхования
    * @return Вид программы страхования
    **/
    @ApiModelProperty(value = "Вид программы страхования")
    
  @Valid


    public ProgramKind getKind() {
        return kind;
    }

    public void setKind(ProgramKind kind) {
        this.kind = kind;
    }


    /**
     * Номер договора
    * @return Номер договора
    **/
    @ApiModelProperty(value = "Номер договора")
    


    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }


    /**
     * Актуальный статус договора
    * @return Актуальный статус договора
    **/
    @ApiModelProperty(value = "Актуальный статус договора")
    
  @Valid


    public InsuranceStatusType getStatus() {
        return status;
    }

    public void setStatus(InsuranceStatusType status) {
        this.status = status;
    }


    /**
     * Номер программы страховани
    * @return Номер программы страховани
    **/
    @ApiModelProperty(value = "Номер программы страховани")
    


    public String getProgramNumber() {
        return programNumber;
    }

    public void setProgramNumber(String programNumber) {
        this.programNumber = programNumber;
    }


    /**
     * Наименовние программы страхования
    * @return Наименовние программы страхования
    **/
    @ApiModelProperty(value = "Наименовние программы страхования")
    


    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }


    /**
     * Дата создания договора
    * @return Дата создания договора
    **/
    @ApiModelProperty(value = "Дата создания договора")
    
  @Valid


    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }


    /**
     * Дата окончания договора
    * @return Дата окончания договора
    **/
    @ApiModelProperty(value = "Дата окончания договора")
    
  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    /**
     * Дата закрытия договора
    * @return Дата закрытия договора
    **/
    @ApiModelProperty(value = "Дата закрытия договора")
    
  @Valid


    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }


    /**
     * Наименование выбранной стратегии
    * @return Наименование выбранной стратегии
    **/
    @ApiModelProperty(value = "Наименование выбранной стратегии")
    


    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }


    public ViewInsuranceModel addStrategyPropertiesItem(StrategyProperty strategyPropertiesItem) {
        if (this.strategyProperties == null) {
            this.strategyProperties = new ArrayList<StrategyProperty>();
        }
        this.strategyProperties.add(strategyPropertiesItem);
        return this;
    }

    /**
    * Get strategyProperties
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<StrategyProperty> getStrategyProperties() {
        return strategyProperties;
    }

    public void setStrategyProperties(List<StrategyProperty> strategyProperties) {
        this.strategyProperties = strategyProperties;
    }


    /**
     * Код договора (заполняется только для сотрудника КЦ)
    * @return Код договора (заполняется только для сотрудника КЦ)
    **/
    @ApiModelProperty(value = "Код договора (заполняется только для сотрудника КЦ)")
    


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public ViewInsuranceModel addAvailableStatusesItem(InsuranceStatusType availableStatusesItem) {
        if (this.availableStatuses == null) {
            this.availableStatuses = new ArrayList<InsuranceStatusType>();
        }
        this.availableStatuses.add(availableStatusesItem);
        return this;
    }

    /**
    * Get availableStatuses
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<InsuranceStatusType> getAvailableStatuses() {
        return availableStatuses;
    }

    public void setAvailableStatuses(List<InsuranceStatusType> availableStatuses) {
        this.availableStatuses = availableStatuses;
    }


    /**
     * Предыдущий статус
    * @return Предыдущий статус
    **/
    @ApiModelProperty(value = "Предыдущий статус")
    


    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }


    /**
     * Признак создания договора копированием
    * @return Признак создания договора копированием
    **/
    @ApiModelProperty(value = "Признак создания договора копированием")
    


    public Boolean isIsCopy() {
        return isCopy;
    }

    public void setIsCopy(Boolean isCopy) {
        this.isCopy = isCopy;
    }


    /**
     * Вид стратегии ИСЖ
    * @return Вид стратегии ИСЖ
    **/
    @ApiModelProperty(value = "Вид стратегии ИСЖ")
    
  @Valid


    public StrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }


    /**
     * Кодировка программы (Латинская буква)
    * @return Кодировка программы (Латинская буква)
    **/
    @ApiModelProperty(value = "Кодировка программы (Латинская буква)")
    
 @Pattern(regexp="(^[A-Z]{1}|$)") @Size(max=1)

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }


    /**
     * Кодировка тарифа (Латинская буква)
    * @return Кодировка тарифа (Латинская буква)
    **/
    @ApiModelProperty(value = "Кодировка тарифа (Латинская буква)")
    
 @Pattern(regexp="(^[A-Z]{1}|$)") @Size(max=1)

    public String getProgramTariff() {
        return programTariff;
    }

    public void setProgramTariff(String programTariff) {
        this.programTariff = programTariff;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ViewInsuranceModel viewInsuranceModel = (ViewInsuranceModel) o;
        return Objects.equals(this.startDate, viewInsuranceModel.startDate) &&
            Objects.equals(this.programSettingId, viewInsuranceModel.programSettingId) &&
            Objects.equals(this.holderId, viewInsuranceModel.holderId) &&
            Objects.equals(this.newHolder, viewInsuranceModel.newHolder) &&
            Objects.equals(this.holderData, viewInsuranceModel.holderData) &&
            Objects.equals(this.insuredId, viewInsuranceModel.insuredId) &&
            Objects.equals(this.newInsured, viewInsuranceModel.newInsured) &&
            Objects.equals(this.insuredData, viewInsuranceModel.insuredData) &&
            Objects.equals(this.holderEqualsInsured, viewInsuranceModel.holderEqualsInsured) &&
            Objects.equals(this.duration, viewInsuranceModel.duration) &&
            Objects.equals(this.calendarUnit, viewInsuranceModel.calendarUnit) &&
            Objects.equals(this.currencyId, viewInsuranceModel.currencyId) &&
            Objects.equals(this.currencyCode, viewInsuranceModel.currencyCode) &&
            Objects.equals(this.premium, viewInsuranceModel.premium) &&
            Objects.equals(this.premiumWithoutDiscount, viewInsuranceModel.premiumWithoutDiscount) &&
            Objects.equals(this.discount, viewInsuranceModel.discount) &&
            Objects.equals(this.rurPremium, viewInsuranceModel.rurPremium) &&
            Objects.equals(this.amount, viewInsuranceModel.amount) &&
            Objects.equals(this.rurAmount, viewInsuranceModel.rurAmount) &&
            Objects.equals(this.periodicity, viewInsuranceModel.periodicity) &&
            Objects.equals(this.growth, viewInsuranceModel.growth) &&
            Objects.equals(this.weight, viewInsuranceModel.weight) &&
            Objects.equals(this.upperPressure, viewInsuranceModel.upperPressure) &&
            Objects.equals(this.lowerPressure, viewInsuranceModel.lowerPressure) &&
            Objects.equals(this.guaranteeLevel, viewInsuranceModel.guaranteeLevel) &&
            Objects.equals(this.participationRate, viewInsuranceModel.participationRate) &&
            Objects.equals(this.recipientEqualsHolder, viewInsuranceModel.recipientEqualsHolder) &&
            Objects.equals(this.riskInfoList, viewInsuranceModel.riskInfoList) &&
            Objects.equals(this.addRiskInfoList, viewInsuranceModel.addRiskInfoList) &&
            Objects.equals(this.recipientList, viewInsuranceModel.recipientList) &&
            Objects.equals(this.strategyId, viewInsuranceModel.strategyId) &&
            Objects.equals(this.type, viewInsuranceModel.type) &&
            Objects.equals(this.paymentTerm, viewInsuranceModel.paymentTerm) &&
            Objects.equals(this.uuid, viewInsuranceModel.uuid) &&
            Objects.equals(this.conclusionDate, viewInsuranceModel.conclusionDate) &&
            Objects.equals(this.fullSetDocument, viewInsuranceModel.fullSetDocument) &&
            Objects.equals(this.commentForNotFullSetDocument, viewInsuranceModel.commentForNotFullSetDocument) &&
            Objects.equals(this.individualRate, viewInsuranceModel.individualRate) &&
            Objects.equals(this.exchangeRate, viewInsuranceModel.exchangeRate) &&
            Objects.equals(this.employeeId, viewInsuranceModel.employeeId) &&
            Objects.equals(this.id, viewInsuranceModel.id) &&
            Objects.equals(this.option, viewInsuranceModel.option) &&
            Objects.equals(this.kind, viewInsuranceModel.kind) &&
            Objects.equals(this.contractNumber, viewInsuranceModel.contractNumber) &&
            Objects.equals(this.status, viewInsuranceModel.status) &&
            Objects.equals(this.programNumber, viewInsuranceModel.programNumber) &&
            Objects.equals(this.programName, viewInsuranceModel.programName) &&
            Objects.equals(this.creationDate, viewInsuranceModel.creationDate) &&
            Objects.equals(this.endDate, viewInsuranceModel.endDate) &&
            Objects.equals(this.closeDate, viewInsuranceModel.closeDate) &&
            Objects.equals(this.strategyName, viewInsuranceModel.strategyName) &&
            Objects.equals(this.strategyProperties, viewInsuranceModel.strategyProperties) &&
            Objects.equals(this.code, viewInsuranceModel.code) &&
            Objects.equals(this.availableStatuses, viewInsuranceModel.availableStatuses) &&
            Objects.equals(this.previousStatus, viewInsuranceModel.previousStatus) &&
            Objects.equals(this.isCopy, viewInsuranceModel.isCopy) &&
            Objects.equals(this.strategyType, viewInsuranceModel.strategyType) &&
            Objects.equals(this.programCode, viewInsuranceModel.programCode) &&
            Objects.equals(this.programTariff, viewInsuranceModel.programTariff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, programSettingId, holderId, newHolder, holderData, insuredId, newInsured, insuredData, holderEqualsInsured, duration, calendarUnit, currencyId, currencyCode, premium, premiumWithoutDiscount, discount, rurPremium, amount, rurAmount, periodicity, growth, weight, upperPressure, lowerPressure, guaranteeLevel, participationRate, recipientEqualsHolder, riskInfoList, addRiskInfoList, recipientList, strategyId, type, paymentTerm, uuid, conclusionDate, fullSetDocument, commentForNotFullSetDocument, individualRate, exchangeRate, employeeId, id, option, kind, contractNumber, status, programNumber, programName, creationDate, endDate, closeDate, strategyName, strategyProperties, code, availableStatuses, previousStatus, isCopy, strategyType, programCode, programTariff);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ViewInsuranceModel {\n");
        
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    programSettingId: ").append(toIndentedString(programSettingId)).append("\n");
        sb.append("    holderId: ").append(toIndentedString(holderId)).append("\n");
        sb.append("    newHolder: ").append(toIndentedString(newHolder)).append("\n");
        sb.append("    holderData: ").append(toIndentedString(holderData)).append("\n");
        sb.append("    insuredId: ").append(toIndentedString(insuredId)).append("\n");
        sb.append("    newInsured: ").append(toIndentedString(newInsured)).append("\n");
        sb.append("    insuredData: ").append(toIndentedString(insuredData)).append("\n");
        sb.append("    holderEqualsInsured: ").append(toIndentedString(holderEqualsInsured)).append("\n");
        sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
        sb.append("    calendarUnit: ").append(toIndentedString(calendarUnit)).append("\n");
        sb.append("    currencyId: ").append(toIndentedString(currencyId)).append("\n");
        sb.append("    currencyCode: ").append(toIndentedString(currencyCode)).append("\n");
        sb.append("    premium: ").append(toIndentedString(premium)).append("\n");
        sb.append("    premiumWithoutDiscount: ").append(toIndentedString(premiumWithoutDiscount)).append("\n");
        sb.append("    discount: ").append(toIndentedString(discount)).append("\n");
        sb.append("    rurPremium: ").append(toIndentedString(rurPremium)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    rurAmount: ").append(toIndentedString(rurAmount)).append("\n");
        sb.append("    periodicity: ").append(toIndentedString(periodicity)).append("\n");
        sb.append("    growth: ").append(toIndentedString(growth)).append("\n");
        sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
        sb.append("    upperPressure: ").append(toIndentedString(upperPressure)).append("\n");
        sb.append("    lowerPressure: ").append(toIndentedString(lowerPressure)).append("\n");
        sb.append("    guaranteeLevel: ").append(toIndentedString(guaranteeLevel)).append("\n");
        sb.append("    participationRate: ").append(toIndentedString(participationRate)).append("\n");
        sb.append("    recipientEqualsHolder: ").append(toIndentedString(recipientEqualsHolder)).append("\n");
        sb.append("    riskInfoList: ").append(toIndentedString(riskInfoList)).append("\n");
        sb.append("    addRiskInfoList: ").append(toIndentedString(addRiskInfoList)).append("\n");
        sb.append("    recipientList: ").append(toIndentedString(recipientList)).append("\n");
        sb.append("    strategyId: ").append(toIndentedString(strategyId)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    paymentTerm: ").append(toIndentedString(paymentTerm)).append("\n");
        sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
        sb.append("    conclusionDate: ").append(toIndentedString(conclusionDate)).append("\n");
        sb.append("    fullSetDocument: ").append(toIndentedString(fullSetDocument)).append("\n");
        sb.append("    commentForNotFullSetDocument: ").append(toIndentedString(commentForNotFullSetDocument)).append("\n");
        sb.append("    individualRate: ").append(toIndentedString(individualRate)).append("\n");
        sb.append("    exchangeRate: ").append(toIndentedString(exchangeRate)).append("\n");
        sb.append("    employeeId: ").append(toIndentedString(employeeId)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    option: ").append(toIndentedString(option)).append("\n");
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    contractNumber: ").append(toIndentedString(contractNumber)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    programNumber: ").append(toIndentedString(programNumber)).append("\n");
        sb.append("    programName: ").append(toIndentedString(programName)).append("\n");
        sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    closeDate: ").append(toIndentedString(closeDate)).append("\n");
        sb.append("    strategyName: ").append(toIndentedString(strategyName)).append("\n");
        sb.append("    strategyProperties: ").append(toIndentedString(strategyProperties)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    availableStatuses: ").append(toIndentedString(availableStatuses)).append("\n");
        sb.append("    previousStatus: ").append(toIndentedString(previousStatus)).append("\n");
        sb.append("    isCopy: ").append(toIndentedString(isCopy)).append("\n");
        sb.append("    strategyType: ").append(toIndentedString(strategyType)).append("\n");
        sb.append("    programCode: ").append(toIndentedString(programCode)).append("\n");
        sb.append("    programTariff: ").append(toIndentedString(programTariff)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
          return "null";
        }
        return o.toString().replace("\n", "\n    ");
        }
    }

