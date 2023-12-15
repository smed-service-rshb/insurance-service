package ru.softlab.efr.services.insurance.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Информация договорам для отображения в списке найденных догововоров по клиенту
 */
@ApiModel(description = "Информация договорам для отображения в списке найденных догововоров по клиенту")
public class FoundInsurance {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("contractNumber")
    private String contractNumber = null;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;

    @JsonProperty("closeDate")
    private LocalDate closeDate = null;

    @JsonProperty("programName")
    private String programName = null;

    /**
     * Создает пустой экземпляр класса
     */
    public FoundInsurance() {
    }

    /**
     * Создает экземпляр класса
     *
     * @param id             Идентификатор записи
     * @param contractNumber Номер договора
     * @param startDate      Дата начала действия договора
     * @param endDate        Дата окончания договора
     * @param closeDate      Дата закрытия договора
     * @param programName    Наименование программы
     */
    public FoundInsurance(Long id, String contractNumber, LocalDate startDate, LocalDate endDate, LocalDate closeDate, String programName) {
        this.id = id;
        this.contractNumber = contractNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.closeDate = closeDate;
        this.programName = programName;
    }

    /**
     * Идентификатор записи
     *
     * @return Идентификатор записи
     **/
    @ApiModelProperty(value = "Идентификатор записи")

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Номер договора
     *
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
     * Дата начала действия договора
     *
     * @return Дата начала действия договора
     **/
    @ApiModelProperty(value = "Дата начала действия договора")
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Дата окончания договора
     *
     * @return Дата окончания договора
     **/
    @ApiModelProperty(value = "Дата окончания договора")
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    /**
     * Дата закрытия договора
     *
     * @return Дата закрытия договора
     **/
    @ApiModelProperty(value = "Дата закрытия договора")
    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FoundInsurance listInsuranceModel = (FoundInsurance) o;
        return Objects.equals(this.id, listInsuranceModel.id) &&
                Objects.equals(this.contractNumber, listInsuranceModel.contractNumber) &&
                Objects.equals(this.startDate, listInsuranceModel.startDate) &&
                Objects.equals(this.endDate, listInsuranceModel.endDate) &&
                Objects.equals(this.closeDate, listInsuranceModel.closeDate) &&
                Objects.equals(this.programName, listInsuranceModel.programName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contractNumber, startDate, endDate, closeDate, programName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ListInsuranceModel {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    contractNumber: ").append(toIndentedString(contractNumber)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    closeDate: ").append(toIndentedString(closeDate)).append("\n");
        sb.append("    programName: ").append(toIndentedString(programName)).append("\n");
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

