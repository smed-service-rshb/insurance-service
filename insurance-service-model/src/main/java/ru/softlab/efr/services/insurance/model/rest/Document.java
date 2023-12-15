package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.DocumentType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Документ
 */
@ApiModel(description = "Документ")
@Validated
public class Document   {
    @JsonProperty("docType")
    private DocumentType docType = null;

    @JsonProperty("docSeries")
    private String docSeries = null;

    @JsonProperty("docNumber")
    private String docNumber = null;

    @JsonProperty("scanId")
    private Long scanId = null;

    @JsonProperty("docName")
    private String docName = null;

    @JsonProperty("fileName")
    private String fileName = null;

    @JsonProperty("issuedBy")
    private String issuedBy = null;

    @JsonProperty("issuedDate")
    private LocalDate issuedDate = null;

    @JsonProperty("isActive")
    private Boolean isActive = null;

    @JsonProperty("isMain")
    private Boolean isMain = null;

    @JsonProperty("divisionCode")
    private String divisionCode = null;

    @JsonProperty("issuedEndDate")
    private LocalDate issuedEndDate = null;

    @JsonProperty("isValidDocument")
    private Boolean isValidDocument = null;

    @JsonProperty("stayStartDate")
    private LocalDate stayStartDate = null;

    @JsonProperty("stayEndDate")
    private LocalDate stayEndDate = null;


    /**
     * Создает пустой экземпляр класса
     */
    public Document() {}

    /**
     * Создает экземпляр класса
     * @param docType Тип документа
     * @param docSeries Серия документа
     * @param docNumber Номер документа
     * @param scanId Id скана документа
     * @param docName Название документа
     * @param fileName Имя файла
     * @param issuedBy Кем выдан
     * @param issuedDate Дата выдачи
     * @param isActive Чек-бокс «Активен»
     * @param isMain Чек-бокс «Основной»
     * @param divisionCode Код подразделения
     * @param issuedEndDate Дата окончания
     * @param isValidDocument Признак действительного документа
     * @param stayStartDate Дата начала пребывания
     * @param stayEndDate Дата окончания пребывания
     */
    public Document(DocumentType docType, String docSeries, String docNumber, Long scanId, String docName, String fileName, String issuedBy, LocalDate issuedDate, Boolean isActive, Boolean isMain, String divisionCode, LocalDate issuedEndDate, Boolean isValidDocument, LocalDate stayStartDate, LocalDate stayEndDate) {
        this.docType = docType;
        this.docSeries = docSeries;
        this.docNumber = docNumber;
        this.scanId = scanId;
        this.docName = docName;
        this.fileName = fileName;
        this.issuedBy = issuedBy;
        this.issuedDate = issuedDate;
        this.isActive = isActive;
        this.isMain = isMain;
        this.divisionCode = divisionCode;
        this.issuedEndDate = issuedEndDate;
        this.isValidDocument = isValidDocument;
        this.stayStartDate = stayStartDate;
        this.stayEndDate = stayEndDate;
    }

    /**
     * Тип документа
    * @return Тип документа
    **/
    @ApiModelProperty(value = "Тип документа")
    
  @Valid


    public DocumentType getDocType() {
        return docType;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
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
     * Id скана документа
    * @return Id скана документа
    **/
    @ApiModelProperty(value = "Id скана документа")
    


    public Long getScanId() {
        return scanId;
    }

    public void setScanId(Long scanId) {
        this.scanId = scanId;
    }


    /**
     * Название документа
    * @return Название документа
    **/
    @ApiModelProperty(value = "Название документа")
    


    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }


    /**
     * Имя файла
    * @return Имя файла
    **/
    @ApiModelProperty(value = "Имя файла")
    


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    /**
     * Кем выдан
    * @return Кем выдан
    **/
    @ApiModelProperty(value = "Кем выдан")
    
 @Size(max=100)

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }


    /**
     * Дата выдачи
    * @return Дата выдачи
    **/
    @ApiModelProperty(value = "Дата выдачи")
    
  @Valid


    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }


    /**
     * Чек-бокс «Активен»
    * @return Чек-бокс «Активен»
    **/
    @ApiModelProperty(value = "Чек-бокс «Активен»")
    


    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


    /**
     * Чек-бокс «Основной»
    * @return Чек-бокс «Основной»
    **/
    @ApiModelProperty(value = "Чек-бокс «Основной»")
    


    public Boolean isIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }


    /**
     * Код подразделения
    * @return Код подразделения
    **/
    @ApiModelProperty(value = "Код подразделения")
    


    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }


    /**
     * Дата окончания
    * @return Дата окончания
    **/
    @ApiModelProperty(value = "Дата окончания")
    
  @Valid


    public LocalDate getIssuedEndDate() {
        return issuedEndDate;
    }

    public void setIssuedEndDate(LocalDate issuedEndDate) {
        this.issuedEndDate = issuedEndDate;
    }


    /**
     * Признак действительного документа
    * @return Признак действительного документа
    **/
    @ApiModelProperty(value = "Признак действительного документа")
    


    public Boolean isIsValidDocument() {
        return isValidDocument;
    }

    public void setIsValidDocument(Boolean isValidDocument) {
        this.isValidDocument = isValidDocument;
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
        Document document = (Document) o;
        return Objects.equals(this.docType, document.docType) &&
            Objects.equals(this.docSeries, document.docSeries) &&
            Objects.equals(this.docNumber, document.docNumber) &&
            Objects.equals(this.scanId, document.scanId) &&
            Objects.equals(this.docName, document.docName) &&
            Objects.equals(this.fileName, document.fileName) &&
            Objects.equals(this.issuedBy, document.issuedBy) &&
            Objects.equals(this.issuedDate, document.issuedDate) &&
            Objects.equals(this.isActive, document.isActive) &&
            Objects.equals(this.isMain, document.isMain) &&
            Objects.equals(this.divisionCode, document.divisionCode) &&
            Objects.equals(this.issuedEndDate, document.issuedEndDate) &&
            Objects.equals(this.isValidDocument, document.isValidDocument) &&
            Objects.equals(this.stayStartDate, document.stayStartDate) &&
            Objects.equals(this.stayEndDate, document.stayEndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docType, docSeries, docNumber, scanId, docName, fileName, issuedBy, issuedDate, isActive, isMain, divisionCode, issuedEndDate, isValidDocument, stayStartDate, stayEndDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Document {\n");
        
        sb.append("    docType: ").append(toIndentedString(docType)).append("\n");
        sb.append("    docSeries: ").append(toIndentedString(docSeries)).append("\n");
        sb.append("    docNumber: ").append(toIndentedString(docNumber)).append("\n");
        sb.append("    scanId: ").append(toIndentedString(scanId)).append("\n");
        sb.append("    docName: ").append(toIndentedString(docName)).append("\n");
        sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
        sb.append("    issuedBy: ").append(toIndentedString(issuedBy)).append("\n");
        sb.append("    issuedDate: ").append(toIndentedString(issuedDate)).append("\n");
        sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
        sb.append("    isMain: ").append(toIndentedString(isMain)).append("\n");
        sb.append("    divisionCode: ").append(toIndentedString(divisionCode)).append("\n");
        sb.append("    issuedEndDate: ").append(toIndentedString(issuedEndDate)).append("\n");
        sb.append("    isValidDocument: ").append(toIndentedString(isValidDocument)).append("\n");
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

