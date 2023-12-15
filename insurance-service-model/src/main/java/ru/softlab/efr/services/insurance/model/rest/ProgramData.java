package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.ProgramKind;
import ru.softlab.efr.services.insurance.model.rest.RelatedOfficeFilterType;
import ru.softlab.efr.services.insurance.model.rest.Segment;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Описание программы страхования
 */
@ApiModel(description = "Описание программы страхования")
@Validated
public class ProgramData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("nameForPrint")
    private String nameForPrint = null;

    @JsonProperty("kind")
    private ProgramKind kind = null;

    @JsonProperty("number")
    private String number = null;

    @JsonProperty("policyCode")
    private String policyCode = null;

    @JsonProperty("option")
    private String option = null;

    @JsonProperty("coolingPeriod")
    private Integer coolingPeriod = null;

    @JsonProperty("waitingPeriod")
    private Integer waitingPeriod = null;

    @JsonProperty("relatedOfficeFilterType")
    private RelatedOfficeFilterType relatedOfficeFilterType = null;

    @JsonProperty("relatedOffices")
    @Valid
    private List<String> relatedOffices = null;

    @JsonProperty("relatedGroupFilterType")
    private RelatedOfficeFilterType relatedGroupFilterType = null;

    @JsonProperty("relatedGroups")
    @Valid
    private List<String> relatedGroups = null;

    @JsonProperty("segment")
    private Segment segment = null;

    @JsonProperty("activeFlag")
    private Boolean activeFlag = null;

    @JsonProperty("comulation")
    private Long comulation = null;

    @JsonProperty("programCharCode")
    private String programCharCode = null;

    @JsonProperty("programCode")
    private String programCode = null;

    @JsonProperty("programTariff")
    private String programTariff = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ProgramData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор программы страхования
     * @param name Наименование программы страхования
     * @param nameForPrint Наименование программы страхования для печатной формы
     * @param kind Вид программы страхования
     * @param number Номер программы страхования
     * @param policyCode Кодировка полиса
     * @param option Вариант программы страхования
     * @param coolingPeriod Период охлаждения (в календарных днях)
     * @param waitingPeriod Период ожидания (в календарных днях)
     * @param relatedOfficeFilterType Тип привязки программы страхования к элементам организационной структуры организации.
     * @param relatedOffices Список названий элементов организационной структуры организации, которые используются для построения фильтра, определяющего, имеет пользователь доступ к данной программе страхования или нет. Данный список заполняется в случаях, когда в relatedOfficeFilterType указаны следующие значения: INCLUDE или EXCLUDE. 
     * @param relatedGroupFilterType Тип привязки программы страхования к группам пользователей.
     * @param relatedGroups Список названий групп пользователей, которые используются для построения фильтра, определяющего, имеет пользователь доступ к данной программе страхования или нет. Данный список заполняется в случаях, когда в relatedGroupFilterType указаны следующие значения: INCLUDE или EXCLUDE. 
     * @param segment Сегмент
     * @param activeFlag Признак действующей программы страхования
     * @param comulation Признак кумуляции
     * @param programCharCode Буквенный код договора программы страхования
     * @param programCode Кодировка программы (Латинская буква)
     * @param programTariff Кодировка тарифа (Латинская буква)
     */
    public ProgramData(Long id, String name, String nameForPrint, ProgramKind kind, String number, String policyCode, String option, Integer coolingPeriod, Integer waitingPeriod, RelatedOfficeFilterType relatedOfficeFilterType, List<String> relatedOffices, RelatedOfficeFilterType relatedGroupFilterType, List<String> relatedGroups, Segment segment, Boolean activeFlag, Long comulation, String programCharCode, String programCode, String programTariff) {
        this.id = id;
        this.name = name;
        this.nameForPrint = nameForPrint;
        this.kind = kind;
        this.number = number;
        this.policyCode = policyCode;
        this.option = option;
        this.coolingPeriod = coolingPeriod;
        this.waitingPeriod = waitingPeriod;
        this.relatedOfficeFilterType = relatedOfficeFilterType;
        this.relatedOffices = relatedOffices;
        this.relatedGroupFilterType = relatedGroupFilterType;
        this.relatedGroups = relatedGroups;
        this.segment = segment;
        this.activeFlag = activeFlag;
        this.comulation = comulation;
        this.programCharCode = programCharCode;
        this.programCode = programCode;
        this.programTariff = programTariff;
    }

    /**
     * Идентификатор программы страхования
    * @return Идентификатор программы страхования
    **/
    @ApiModelProperty(value = "Идентификатор программы страхования")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Наименование программы страхования
    * @return Наименование программы страхования
    **/
    @ApiModelProperty(required = true, value = "Наименование программы страхования")
      @NotNull



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Наименование программы страхования для печатной формы
    * @return Наименование программы страхования для печатной формы
    **/
    @ApiModelProperty(value = "Наименование программы страхования для печатной формы")
    


    public String getNameForPrint() {
        return nameForPrint;
    }

    public void setNameForPrint(String nameForPrint) {
        this.nameForPrint = nameForPrint;
    }


    /**
     * Вид программы страхования
    * @return Вид программы страхования
    **/
    @ApiModelProperty(required = true, value = "Вид программы страхования")
      @NotNull

  @Valid


    public ProgramKind getKind() {
        return kind;
    }

    public void setKind(ProgramKind kind) {
        this.kind = kind;
    }


    /**
     * Номер программы страхования
    * @return Номер программы страхования
    **/
    @ApiModelProperty(value = "Номер программы страхования")
    


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    /**
     * Кодировка полиса
    * @return Кодировка полиса
    **/
    @ApiModelProperty(value = "Кодировка полиса")
    


    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
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
     * Период охлаждения (в календарных днях)
    * @return Период охлаждения (в календарных днях)
    **/
    @ApiModelProperty(value = "Период охлаждения (в календарных днях)")
    


    public Integer getCoolingPeriod() {
        return coolingPeriod;
    }

    public void setCoolingPeriod(Integer coolingPeriod) {
        this.coolingPeriod = coolingPeriod;
    }


    /**
     * Период ожидания (в календарных днях)
    * @return Период ожидания (в календарных днях)
    **/
    @ApiModelProperty(value = "Период ожидания (в календарных днях)")
    


    public Integer getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(Integer waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }


    /**
     * Тип привязки программы страхования к элементам организационной структуры организации.
    * @return Тип привязки программы страхования к элементам организационной структуры организации.
    **/
    @ApiModelProperty(value = "Тип привязки программы страхования к элементам организационной структуры организации.")
    
  @Valid


    public RelatedOfficeFilterType getRelatedOfficeFilterType() {
        return relatedOfficeFilterType;
    }

    public void setRelatedOfficeFilterType(RelatedOfficeFilterType relatedOfficeFilterType) {
        this.relatedOfficeFilterType = relatedOfficeFilterType;
    }


    public ProgramData addRelatedOfficesItem(String relatedOfficesItem) {
        if (this.relatedOffices == null) {
            this.relatedOffices = new ArrayList<String>();
        }
        this.relatedOffices.add(relatedOfficesItem);
        return this;
    }

    /**
     * Список названий элементов организационной структуры организации, которые используются для построения фильтра, определяющего, имеет пользователь доступ к данной программе страхования или нет. Данный список заполняется в случаях, когда в relatedOfficeFilterType указаны следующие значения: INCLUDE или EXCLUDE. 
    * @return Список названий элементов организационной структуры организации, которые используются для построения фильтра, определяющего, имеет пользователь доступ к данной программе страхования или нет. Данный список заполняется в случаях, когда в relatedOfficeFilterType указаны следующие значения: INCLUDE или EXCLUDE. 
    **/
    @ApiModelProperty(value = "Список названий элементов организационной структуры организации, которые используются для построения фильтра, определяющего, имеет пользователь доступ к данной программе страхования или нет. Данный список заполняется в случаях, когда в relatedOfficeFilterType указаны следующие значения: INCLUDE или EXCLUDE. ")
    


    public List<String> getRelatedOffices() {
        return relatedOffices;
    }

    public void setRelatedOffices(List<String> relatedOffices) {
        this.relatedOffices = relatedOffices;
    }


    /**
     * Тип привязки программы страхования к группам пользователей.
    * @return Тип привязки программы страхования к группам пользователей.
    **/
    @ApiModelProperty(value = "Тип привязки программы страхования к группам пользователей.")
    
  @Valid


    public RelatedOfficeFilterType getRelatedGroupFilterType() {
        return relatedGroupFilterType;
    }

    public void setRelatedGroupFilterType(RelatedOfficeFilterType relatedGroupFilterType) {
        this.relatedGroupFilterType = relatedGroupFilterType;
    }


    public ProgramData addRelatedGroupsItem(String relatedGroupsItem) {
        if (this.relatedGroups == null) {
            this.relatedGroups = new ArrayList<String>();
        }
        this.relatedGroups.add(relatedGroupsItem);
        return this;
    }

    /**
     * Список названий групп пользователей, которые используются для построения фильтра, определяющего, имеет пользователь доступ к данной программе страхования или нет. Данный список заполняется в случаях, когда в relatedGroupFilterType указаны следующие значения: INCLUDE или EXCLUDE. 
    * @return Список названий групп пользователей, которые используются для построения фильтра, определяющего, имеет пользователь доступ к данной программе страхования или нет. Данный список заполняется в случаях, когда в relatedGroupFilterType указаны следующие значения: INCLUDE или EXCLUDE. 
    **/
    @ApiModelProperty(value = "Список названий групп пользователей, которые используются для построения фильтра, определяющего, имеет пользователь доступ к данной программе страхования или нет. Данный список заполняется в случаях, когда в relatedGroupFilterType указаны следующие значения: INCLUDE или EXCLUDE. ")
    


    public List<String> getRelatedGroups() {
        return relatedGroups;
    }

    public void setRelatedGroups(List<String> relatedGroups) {
        this.relatedGroups = relatedGroups;
    }


    /**
     * Сегмент
    * @return Сегмент
    **/
    @ApiModelProperty(value = "Сегмент")
    
  @Valid


    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }


    /**
     * Признак действующей программы страхования
    * @return Признак действующей программы страхования
    **/
    @ApiModelProperty(required = true, value = "Признак действующей программы страхования")
      @NotNull



    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }


    /**
     * Признак кумуляции
    * @return Признак кумуляции
    **/
    @ApiModelProperty(value = "Признак кумуляции")
    


    public Long getComulation() {
        return comulation;
    }

    public void setComulation(Long comulation) {
        this.comulation = comulation;
    }


    /**
     * Буквенный код договора программы страхования
    * @return Буквенный код договора программы страхования
    **/
    @ApiModelProperty(value = "Буквенный код договора программы страхования")
    
 @Pattern(regexp="(^[А-ЯЁ]{2}|$)")

    public String getProgramCharCode() {
        return programCharCode;
    }

    public void setProgramCharCode(String programCharCode) {
        this.programCharCode = programCharCode;
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
        ProgramData programData = (ProgramData) o;
        return Objects.equals(this.id, programData.id) &&
            Objects.equals(this.name, programData.name) &&
            Objects.equals(this.nameForPrint, programData.nameForPrint) &&
            Objects.equals(this.kind, programData.kind) &&
            Objects.equals(this.number, programData.number) &&
            Objects.equals(this.policyCode, programData.policyCode) &&
            Objects.equals(this.option, programData.option) &&
            Objects.equals(this.coolingPeriod, programData.coolingPeriod) &&
            Objects.equals(this.waitingPeriod, programData.waitingPeriod) &&
            Objects.equals(this.relatedOfficeFilterType, programData.relatedOfficeFilterType) &&
            Objects.equals(this.relatedOffices, programData.relatedOffices) &&
            Objects.equals(this.relatedGroupFilterType, programData.relatedGroupFilterType) &&
            Objects.equals(this.relatedGroups, programData.relatedGroups) &&
            Objects.equals(this.segment, programData.segment) &&
            Objects.equals(this.activeFlag, programData.activeFlag) &&
            Objects.equals(this.comulation, programData.comulation) &&
            Objects.equals(this.programCharCode, programData.programCharCode) &&
            Objects.equals(this.programCode, programData.programCode) &&
            Objects.equals(this.programTariff, programData.programTariff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameForPrint, kind, number, policyCode, option, coolingPeriod, waitingPeriod, relatedOfficeFilterType, relatedOffices, relatedGroupFilterType, relatedGroups, segment, activeFlag, comulation, programCharCode, programCode, programTariff);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ProgramData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    nameForPrint: ").append(toIndentedString(nameForPrint)).append("\n");
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    number: ").append(toIndentedString(number)).append("\n");
        sb.append("    policyCode: ").append(toIndentedString(policyCode)).append("\n");
        sb.append("    option: ").append(toIndentedString(option)).append("\n");
        sb.append("    coolingPeriod: ").append(toIndentedString(coolingPeriod)).append("\n");
        sb.append("    waitingPeriod: ").append(toIndentedString(waitingPeriod)).append("\n");
        sb.append("    relatedOfficeFilterType: ").append(toIndentedString(relatedOfficeFilterType)).append("\n");
        sb.append("    relatedOffices: ").append(toIndentedString(relatedOffices)).append("\n");
        sb.append("    relatedGroupFilterType: ").append(toIndentedString(relatedGroupFilterType)).append("\n");
        sb.append("    relatedGroups: ").append(toIndentedString(relatedGroups)).append("\n");
        sb.append("    segment: ").append(toIndentedString(segment)).append("\n");
        sb.append("    activeFlag: ").append(toIndentedString(activeFlag)).append("\n");
        sb.append("    comulation: ").append(toIndentedString(comulation)).append("\n");
        sb.append("    programCharCode: ").append(toIndentedString(programCharCode)).append("\n");
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

