package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Описание доли выгодоприобретателя по риску
 */
@ApiModel(description = "Описание доли выгодоприобретателя по риску")
@Validated
public class RecipientShare   {
    @JsonProperty("riskId")
    private Long riskId = null;

    @JsonProperty("share")
    private BigDecimal share = null;


    /**
     * Создает пустой экземпляр класса
     */
    public RecipientShare() {}

    /**
     * Создает экземпляр класса
     * @param riskId Идентификатор риска
     * @param share Размер доли в %
     */
    public RecipientShare(Long riskId, BigDecimal share) {
        this.riskId = riskId;
        this.share = share;
    }

    /**
     * Идентификатор риска
    * @return Идентификатор риска
    **/
    @ApiModelProperty(value = "Идентификатор риска")
    


    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }


    /**
     * Размер доли в %
    * @return Размер доли в %
    **/
    @ApiModelProperty(value = "Размер доли в %")
    
  @Valid


    public BigDecimal getShare() {
        return share;
    }

    public void setShare(BigDecimal share) {
        this.share = share;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipientShare recipientShare = (RecipientShare) o;
        return Objects.equals(this.riskId, recipientShare.riskId) &&
            Objects.equals(this.share, recipientShare.share);
    }

    @Override
    public int hashCode() {
        return Objects.hash(riskId, share);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RecipientShare {\n");
        
        sb.append("    riskId: ").append(toIndentedString(riskId)).append("\n");
        sb.append("    share: ").append(toIndentedString(share)).append("\n");
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

