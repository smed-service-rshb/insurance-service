package ru.softlab.efr.services.insurance.model.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import ru.softlab.efr.services.insurance.model.rest.AgreementType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Согласие на обработку персональных данных клиента
 *
 * @author olshansky
 * @since 07.11.2018
 */
public class Agreement {
    private AgreementType type = null;
    private Long scanId = null;
    private String scanFileName = null;
    private Long recallScanId = null;
    private String recallScanFileName = null;
    private LocalDate startDate = null;
    private LocalDate endDate = null;
    private Boolean isRecall = null;

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

    public AgreementType getType() {
        return type;
    }

    public void setType(AgreementType type) {
        this.type = type;
    }

    public Long getScanId() {
        return scanId;
    }

    public void setScanId(Long scanId) {
        this.scanId = scanId;
    }

    public String getScanFileName() {
        return scanFileName;
    }

    public void setScanFileName(String scanFileName) {
        this.scanFileName = scanFileName;
    }

    public Long getRecallScanId() {
        return recallScanId;
    }

    public void setRecallScanId(Long recallScanId) {
        this.recallScanId = recallScanId;
    }

    public String getRecallScanFileName() {
        return recallScanFileName;
    }

    public void setRecallScanFileName(String recallScanFileName) {
        this.recallScanFileName = recallScanFileName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getRecall() {
        return isRecall;
    }

    public void setRecall(Boolean recall) {
        isRecall = recall;
    }

    @Override
    public boolean equals(Object o) {
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
    private String toIndentedString(Object o) {
        if (o == null) {
          return "null";
        }
        return o.toString().replace("\n", "\n    ");
        }
    }

