package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.TaxResidenceType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация по выгодоприобретателю по договору страхования
 */
@ApiModel(description = "Информация по выгодоприобретателю по договору страхования")
@Validated
public class InsuranceRecipient   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("surName")
    private String surName = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("middleName")
    private String middleName = null;

    @JsonProperty("birthDate")
    private LocalDate birthDate = null;

    @JsonProperty("birthPlace")
    private String birthPlace = null;

    @JsonProperty("taxResidence")
    private TaxResidenceType taxResidence = null;

    @JsonProperty("relationship")
    private String relationship = null;

    @JsonProperty("share")
    private BigDecimal share = null;

    @JsonProperty("birthCountry")
    private String birthCountry = null;


    /**
     * Создает пустой экземпляр класса
     */
    public InsuranceRecipient() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор
     * @param surName Фамилия страхователя
     * @param firstName Имя страхователя
     * @param middleName Отчество страхователя
     * @param birthDate Дата рождения страхователя
     * @param birthPlace Место рождения
     * @param taxResidence Налоговое резиденство
     * @param relationship Отношение к застрахованному лицу
     * @param share Доля в %
     * @param birthCountry Страна рождения
     */
    public InsuranceRecipient(Long id, String surName, String firstName, String middleName, LocalDate birthDate, String birthPlace, TaxResidenceType taxResidence, String relationship, BigDecimal share, String birthCountry) {
        this.id = id;
        this.surName = surName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.taxResidence = taxResidence;
        this.relationship = relationship;
        this.share = share;
        this.birthCountry = birthCountry;
    }

    /**
     * Идентификатор
    * @return Идентификатор
    **/
    @ApiModelProperty(value = "Идентификатор")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Фамилия страхователя
    * @return Фамилия страхователя
    **/
    @ApiModelProperty(value = "Фамилия страхователя")
    
 @Size(min=1,max=255)

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }


    /**
     * Имя страхователя
    * @return Имя страхователя
    **/
    @ApiModelProperty(value = "Имя страхователя")
    
 @Size(min=1,max=255)

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /**
     * Отчество страхователя
    * @return Отчество страхователя
    **/
    @ApiModelProperty(value = "Отчество страхователя")
    
 @Size(max=255)

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    /**
     * Дата рождения страхователя
    * @return Дата рождения страхователя
    **/
    @ApiModelProperty(value = "Дата рождения страхователя")
    
  @Valid


    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


    /**
     * Место рождения
    * @return Место рождения
    **/
    @ApiModelProperty(value = "Место рождения")
    
 @Size(max=255)

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }


    /**
     * Налоговое резиденство
    * @return Налоговое резиденство
    **/
    @ApiModelProperty(value = "Налоговое резиденство")
    
  @Valid


    public TaxResidenceType getTaxResidence() {
        return taxResidence;
    }

    public void setTaxResidence(TaxResidenceType taxResidence) {
        this.taxResidence = taxResidence;
    }


    /**
     * Отношение к застрахованному лицу
    * @return Отношение к застрахованному лицу
    **/
    @ApiModelProperty(value = "Отношение к застрахованному лицу")
    
 @Size(max=50)

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }


    /**
     * Доля в %
    * @return Доля в %
    **/
    @ApiModelProperty(value = "Доля в %")
    
  @Valid


    public BigDecimal getShare() {
        return share;
    }

    public void setShare(BigDecimal share) {
        this.share = share;
    }


    /**
     * Страна рождения
    * @return Страна рождения
    **/
    @ApiModelProperty(value = "Страна рождения")
    
 @Size(max=100)

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InsuranceRecipient insuranceRecipient = (InsuranceRecipient) o;
        return Objects.equals(this.id, insuranceRecipient.id) &&
            Objects.equals(this.surName, insuranceRecipient.surName) &&
            Objects.equals(this.firstName, insuranceRecipient.firstName) &&
            Objects.equals(this.middleName, insuranceRecipient.middleName) &&
            Objects.equals(this.birthDate, insuranceRecipient.birthDate) &&
            Objects.equals(this.birthPlace, insuranceRecipient.birthPlace) &&
            Objects.equals(this.taxResidence, insuranceRecipient.taxResidence) &&
            Objects.equals(this.relationship, insuranceRecipient.relationship) &&
            Objects.equals(this.share, insuranceRecipient.share) &&
            Objects.equals(this.birthCountry, insuranceRecipient.birthCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surName, firstName, middleName, birthDate, birthPlace, taxResidence, relationship, share, birthCountry);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class InsuranceRecipient {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    surName: ").append(toIndentedString(surName)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
        sb.append("    birthPlace: ").append(toIndentedString(birthPlace)).append("\n");
        sb.append("    taxResidence: ").append(toIndentedString(taxResidence)).append("\n");
        sb.append("    relationship: ").append(toIndentedString(relationship)).append("\n");
        sb.append("    share: ").append(toIndentedString(share)).append("\n");
        sb.append("    birthCountry: ").append(toIndentedString(birthCountry)).append("\n");
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

