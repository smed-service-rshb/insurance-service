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
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SetStatusInsuranceModel
 */
@Validated
public class SetStatusInsuranceModel   {
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

    @JsonProperty("kind")
    private ProgramKind kind = null;

    @JsonProperty("newStatus")
    private InsuranceStatusType newStatus = null;

    @JsonProperty("comment")
    private String comment = null;


    /**
     * Создает пустой экземпляр класса
     */
    public SetStatusInsuranceModel() {}

    /**
     * Создает экземпляр класса
     * @param startDate Дата начала действия договора страхования
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
     * @param id Идентификатор учётной записи сотрудника
     * @param kind Вид программы страхования
     * @param newStatus Актуальный статус договора
     * @param comment Комментарий при переводе в новый статус
     */
    public SetStatusInsuranceModel(LocalDate startDate, Long programSettingId, Long holderId, Boolean newHolder, Client holderData, Long insuredId, Boolean newInsured, Client insuredData, Boolean holderEqualsInsured, Integer duration, CalendarUnit calendarUnit, Long currencyId, String currencyCode, BigDecimal premium, BigDecimal premiumWithoutDiscount, BigDecimal discount, BigDecimal rurPremium, BigDecimal amount, BigDecimal rurAmount, PaymentPeriodicity periodicity, Integer growth, Integer weight, Integer upperPressure, Integer lowerPressure, BigDecimal guaranteeLevel, BigDecimal participationRate, Boolean recipientEqualsHolder, List<RiskInfo> riskInfoList, List<AddRiskInfo> addRiskInfoList, List<InsuranceRecipient> recipientList, Long strategyId, FindProgramType type, Integer paymentTerm, String uuid, LocalDate conclusionDate, Boolean fullSetDocument, String commentForNotFullSetDocument, Boolean individualRate, BigDecimal exchangeRate, Long employeeId, Long id, ProgramKind kind, InsuranceStatusType newStatus, String comment) {
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
        this.kind = kind;
        this.newStatus = newStatus;
        this.comment = comment;
    }

    /**
     * Дата начала действия договора страхования
    * @return Дата начала действия договора страхования
    **/
    @ApiModelProperty(value = "Дата начала действия договора страхования")
    
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


    public SetStatusInsuranceModel addRiskInfoListItem(RiskInfo riskInfoListItem) {
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


    public SetStatusInsuranceModel addAddRiskInfoListItem(AddRiskInfo addRiskInfoListItem) {
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


    public SetStatusInsuranceModel addRecipientListItem(InsuranceRecipient recipientListItem) {
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
     * Идентификатор учётной записи сотрудника
    * @return Идентификатор учётной записи сотрудника
    **/
    @ApiModelProperty(value = "Идентификатор учётной записи сотрудника")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
     * Актуальный статус договора
    * @return Актуальный статус договора
    **/
    @ApiModelProperty(value = "Актуальный статус договора")
    
  @Valid


    public InsuranceStatusType getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(InsuranceStatusType newStatus) {
        this.newStatus = newStatus;
    }


    /**
     * Комментарий при переводе в новый статус
    * @return Комментарий при переводе в новый статус
    **/
    @ApiModelProperty(value = "Комментарий при переводе в новый статус")
    


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SetStatusInsuranceModel setStatusInsuranceModel = (SetStatusInsuranceModel) o;
        return Objects.equals(this.startDate, setStatusInsuranceModel.startDate) &&
            Objects.equals(this.programSettingId, setStatusInsuranceModel.programSettingId) &&
            Objects.equals(this.holderId, setStatusInsuranceModel.holderId) &&
            Objects.equals(this.newHolder, setStatusInsuranceModel.newHolder) &&
            Objects.equals(this.holderData, setStatusInsuranceModel.holderData) &&
            Objects.equals(this.insuredId, setStatusInsuranceModel.insuredId) &&
            Objects.equals(this.newInsured, setStatusInsuranceModel.newInsured) &&
            Objects.equals(this.insuredData, setStatusInsuranceModel.insuredData) &&
            Objects.equals(this.holderEqualsInsured, setStatusInsuranceModel.holderEqualsInsured) &&
            Objects.equals(this.duration, setStatusInsuranceModel.duration) &&
            Objects.equals(this.calendarUnit, setStatusInsuranceModel.calendarUnit) &&
            Objects.equals(this.currencyId, setStatusInsuranceModel.currencyId) &&
            Objects.equals(this.currencyCode, setStatusInsuranceModel.currencyCode) &&
            Objects.equals(this.premium, setStatusInsuranceModel.premium) &&
            Objects.equals(this.premiumWithoutDiscount, setStatusInsuranceModel.premiumWithoutDiscount) &&
            Objects.equals(this.discount, setStatusInsuranceModel.discount) &&
            Objects.equals(this.rurPremium, setStatusInsuranceModel.rurPremium) &&
            Objects.equals(this.amount, setStatusInsuranceModel.amount) &&
            Objects.equals(this.rurAmount, setStatusInsuranceModel.rurAmount) &&
            Objects.equals(this.periodicity, setStatusInsuranceModel.periodicity) &&
            Objects.equals(this.growth, setStatusInsuranceModel.growth) &&
            Objects.equals(this.weight, setStatusInsuranceModel.weight) &&
            Objects.equals(this.upperPressure, setStatusInsuranceModel.upperPressure) &&
            Objects.equals(this.lowerPressure, setStatusInsuranceModel.lowerPressure) &&
            Objects.equals(this.guaranteeLevel, setStatusInsuranceModel.guaranteeLevel) &&
            Objects.equals(this.participationRate, setStatusInsuranceModel.participationRate) &&
            Objects.equals(this.recipientEqualsHolder, setStatusInsuranceModel.recipientEqualsHolder) &&
            Objects.equals(this.riskInfoList, setStatusInsuranceModel.riskInfoList) &&
            Objects.equals(this.addRiskInfoList, setStatusInsuranceModel.addRiskInfoList) &&
            Objects.equals(this.recipientList, setStatusInsuranceModel.recipientList) &&
            Objects.equals(this.strategyId, setStatusInsuranceModel.strategyId) &&
            Objects.equals(this.type, setStatusInsuranceModel.type) &&
            Objects.equals(this.paymentTerm, setStatusInsuranceModel.paymentTerm) &&
            Objects.equals(this.uuid, setStatusInsuranceModel.uuid) &&
            Objects.equals(this.conclusionDate, setStatusInsuranceModel.conclusionDate) &&
            Objects.equals(this.fullSetDocument, setStatusInsuranceModel.fullSetDocument) &&
            Objects.equals(this.commentForNotFullSetDocument, setStatusInsuranceModel.commentForNotFullSetDocument) &&
            Objects.equals(this.individualRate, setStatusInsuranceModel.individualRate) &&
            Objects.equals(this.exchangeRate, setStatusInsuranceModel.exchangeRate) &&
            Objects.equals(this.employeeId, setStatusInsuranceModel.employeeId) &&
            Objects.equals(this.id, setStatusInsuranceModel.id) &&
            Objects.equals(this.kind, setStatusInsuranceModel.kind) &&
            Objects.equals(this.newStatus, setStatusInsuranceModel.newStatus) &&
            Objects.equals(this.comment, setStatusInsuranceModel.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, programSettingId, holderId, newHolder, holderData, insuredId, newInsured, insuredData, holderEqualsInsured, duration, calendarUnit, currencyId, currencyCode, premium, premiumWithoutDiscount, discount, rurPremium, amount, rurAmount, periodicity, growth, weight, upperPressure, lowerPressure, guaranteeLevel, participationRate, recipientEqualsHolder, riskInfoList, addRiskInfoList, recipientList, strategyId, type, paymentTerm, uuid, conclusionDate, fullSetDocument, commentForNotFullSetDocument, individualRate, exchangeRate, employeeId, id, kind, newStatus, comment);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SetStatusInsuranceModel {\n");
        
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
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    newStatus: ").append(toIndentedString(newStatus)).append("\n");
        sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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

