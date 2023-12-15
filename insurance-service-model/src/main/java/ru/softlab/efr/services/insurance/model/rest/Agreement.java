package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.AgreementType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Согласие на обработку персональных данных клиента
 */
@ApiModel(description = "Согласие на обработку персональных данных клиента")
@Validated
public class Agreement   {
    @JsonProperty("type")
    private AgreementType type = null;

    @JsonProperty("scanId")
    private Long scanId = null;

    @JsonProperty("scanFileName")
    private String scanFileName = null;

    @JsonProperty("recallScanId")
    private Long recallScanId = null;

    @JsonProperty("recallScanFileName")
    private String recallScanFileName = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("isRecall")
    private Boolean isRecall = null;


    /**
     * Создает пустой экземпляр класса
     */
    public Agreement() {}

    /**
     * Создает экземпляр класса
     * @param type Тип согласия
     * @param scanId Id сканa согласия
     * @param scanFileName Имя файла согласия
     * @param recallScanId Id скана отозванного согласия
     * @param recallScanFileName Имя файла отозванного согласия
     * @param startDate Дата подписания
     * @param endDate Дата окончания действия согласия
     * @param isRecall Признак отозвано ли согласие
     */
    public Agreement(AgreementType type, Long scanId, String scanFileName, Long recallScanId, String recallScanFileName, LocalDate startDate, LocalDate endDate, Boolean isRecall) {
        this.type = type;
        this.scanId = scanId;
        this.scanFileName = scanFileName;
        this.recallScanId = recallScanId;
        this.recallScanFileName = recallScanFileName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRecall = isRecall;
    }

    /**
     * Тип согласия
    * @return Тип согласия
    **/
    @ApiModelProperty(value = "Тип согласия")
    
  @Valid


    public AgreementType getType() {
        return type;
    }

    public void setType(AgreementType type) {
        this.type = type;
    }


    /**
     * Id сканa согласия
    * @return Id сканa согласия
    **/
    @ApiModelProperty(value = "Id сканa согласия")
    


    public Long getScanId() {
        return scanId;
    }

    public void setScanId(Long scanId) {
        this.scanId = scanId;
    }


    /**
     * Имя файла согласия
    * @return Имя файла согласия
    **/
    @ApiModelProperty(value = "Имя файла согласия")
    


    public String getScanFileName() {
        return scanFileName;
    }

    public void setScanFileName(String scanFileName) {
        this.scanFileName = scanFileName;
    }


    /**
     * Id скана отозванного согласия
    * @return Id скана отозванного согласия
    **/
    @ApiModelProperty(value = "Id скана отозванного согласия")
    


    public Long getRecallScanId() {
        return recallScanId;
    }

    public void setRecallScanId(Long recallScanId) {
        this.recallScanId = recallScanId;
    }


    /**
     * Имя файла отозванного согласия
    * @return Имя файла отозванного согласия
    **/
    @ApiModelProperty(value = "Имя файла отозванного согласия")
    


    public String getRecallScanFileName() {
        return recallScanFileName;
    }

    public void setRecallScanFileName(String recallScanFileName) {
        this.recallScanFileName = recallScanFileName;
    }


    /**
     * Дата подписания
    * @return Дата подписания
    **/
    @ApiModelProperty(value = "Дата подписания")
    
  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Дата окончания действия согласия
    * @return Дата окончания действия согласия
    **/
    @ApiModelProperty(value = "Дата окончания действия согласия")
    
  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    /**
     * Признак отозвано ли согласие
    * @return Признак отозвано ли согласие
    **/
    @ApiModelProperty(value = "Признак отозвано ли согласие")
    


    public Boolean isIsRecall() {
        return isRecall;
    }

    public void setIsRecall(Boolean isRecall) {
        this.isRecall = isRecall;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Agreement agreement = (Agreement) o;
        return Objects.equals(this.type, agreement.type) &&
            Objects.equals(this.scanId, agreement.scanId) &&
            Objects.equals(this.scanFileName, agreement.scanFileName) &&
            Objects.equals(this.recallScanId, agreement.recallScanId) &&
            Objects.equals(this.recallScanFileName, agreement.recallScanFileName) &&
            Objects.equals(this.startDate, agreement.startDate) &&
            Objects.equals(this.endDate, agreement.endDate) &&
            Objects.equals(this.isRecall, agreement.isRecall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, scanId, scanFileName, recallScanId, recallScanFileName, startDate, endDate, isRecall);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Agreement {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    scanId: ").append(toIndentedString(scanId)).append("\n");
        sb.append("    scanFileName: ").append(toIndentedString(scanFileName)).append("\n");
        sb.append("    recallScanId: ").append(toIndentedString(recallScanId)).append("\n");
        sb.append("    recallScanFileName: ").append(toIndentedString(recallScanFileName)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    isRecall: ").append(toIndentedString(isRecall)).append("\n");
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

