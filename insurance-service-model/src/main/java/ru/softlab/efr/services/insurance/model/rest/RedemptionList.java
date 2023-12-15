package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.RedemptionSum;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные по выкупным суммам для договора
 */
@ApiModel(description = "Данные по выкупным суммам для договора")
@Validated
public class RedemptionList   {
    @JsonProperty("redemptionData")
    @Valid
    private List<RedemptionSum> redemptionData = null;


    /**
     * Создает пустой экземпляр класса
     */
    public RedemptionList() {}

    /**
     * Создает экземпляр класса
     * @param redemptionData 
     */
    public RedemptionList(List<RedemptionSum> redemptionData) {
        this.redemptionData = redemptionData;
    }

    public RedemptionList addRedemptionDataItem(RedemptionSum redemptionDataItem) {
        if (this.redemptionData == null) {
            this.redemptionData = new ArrayList<RedemptionSum>();
        }
        this.redemptionData.add(redemptionDataItem);
        return this;
    }

    /**
    * Get redemptionData
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<RedemptionSum> getRedemptionData() {
        return redemptionData;
    }

    public void setRedemptionData(List<RedemptionSum> redemptionData) {
        this.redemptionData = redemptionData;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedemptionList redemptionList = (RedemptionList) o;
        return Objects.equals(this.redemptionData, redemptionList.redemptionData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(redemptionData);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RedemptionList {\n");
        
        sb.append("    redemptionData: ").append(toIndentedString(redemptionData)).append("\n");
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

