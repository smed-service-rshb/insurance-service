package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * данные докумнента, подтв. право пребывания (если гражданство - иное)
 */
@ApiModel(description = "данные докумнента, подтв. право пребывания (если гражданство - иное)")
@Validated
public class ForeignerDoc   {
    @JsonProperty("docName")
    private String docName = null;

    @JsonProperty("docSeries")
    private String docSeries = null;

    @JsonProperty("docNumber")
    private String docNumber = null;

    @JsonProperty("stayStartDate")
    private LocalDate stayStartDate = null;

    @JsonProperty("stayEndDate")
    private LocalDate stayEndDate = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ForeignerDoc() {}

    /**
     * Создает экземпляр класса
     * @param docName Наименование документа
     * @param docSeries Серия документа
     * @param docNumber Номер документа
     * @param stayStartDate Дата начала пребывания
     * @param stayEndDate Дата окончания пребывания
     */
    public ForeignerDoc(String docName, String docSeries, String docNumber, LocalDate stayStartDate, LocalDate stayEndDate) {
        this.docName = docName;
        this.docSeries = docSeries;
        this.docNumber = docNumber;
        this.stayStartDate = stayStartDate;
        this.stayEndDate = stayEndDate;
    }

    /**
     * Наименование документа
    * @return Наименование документа
    **/
    @ApiModelProperty(value = "Наименование документа")
    
 @Size(max=100)

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }


    /**
     * Серия документа
    * @return Серия документа
    **/
    @ApiModelProperty(value = "Серия документа")
    
 @Size(max=10)

    public String getDocSeries() {
        return docSeries;
    }

    public void setDocSeries(String docSeries) {
        this.docSeries = docSeries;
    }


    /**
     * Номер документа
    * @return Номер документа
    **/
    @ApiModelProperty(value = "Номер документа")
    
 @Size(max=20)

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }


    /**
     * Дата начала пребывания
    * @return Дата начала пребывания
    **/
    @ApiModelProperty(value = "Дата начала пребывания")
    
  @Valid


    public LocalDate getStayStartDate() {
        return stayStartDate;
    }

    public void setStayStartDate(LocalDate stayStartDate) {
        this.stayStartDate = stayStartDate;
    }


    /**
     * Дата окончания пребывания
    * @return Дата окончания пребывания
    **/
    @ApiModelProperty(value = "Дата окончания пребывания")
    
  @Valid


    public LocalDate getStayEndDate() {
        return stayEndDate;
    }

    public void setStayEndDate(LocalDate stayEndDate) {
        this.stayEndDate = stayEndDate;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ForeignerDoc foreignerDoc = (ForeignerDoc) o;
        return Objects.equals(this.docName, foreignerDoc.docName) &&
            Objects.equals(this.docSeries, foreignerDoc.docSeries) &&
            Objects.equals(this.docNumber, foreignerDoc.docNumber) &&
            Objects.equals(this.stayStartDate, foreignerDoc.stayStartDate) &&
            Objects.equals(this.stayEndDate, foreignerDoc.stayEndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docName, docSeries, docNumber, stayStartDate, stayEndDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ForeignerDoc {\n");
        
        sb.append("    docName: ").append(toIndentedString(docName)).append("\n");
        sb.append("    docSeries: ").append(toIndentedString(docSeries)).append("\n");
        sb.append("    docNumber: ").append(toIndentedString(docNumber)).append("\n");
        sb.append("    stayStartDate: ").append(toIndentedString(stayStartDate)).append("\n");
        sb.append("    stayEndDate: ").append(toIndentedString(stayEndDate)).append("\n");
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

