package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.SharesData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Ответ на запрос получения списка акций
 */
@ApiModel(description = "Ответ на запрос получения списка акций")
@Validated
public class SharesDataRs   {
    @JsonProperty("shares")
    @Valid
    private List<SharesData> shares = null;


    /**
     * Создает пустой экземпляр класса
     */
    public SharesDataRs() {}

    /**
     * Создает экземпляр класса
     * @param shares 
     */
    public SharesDataRs(List<SharesData> shares) {
        this.shares = shares;
    }

    public SharesDataRs addSharesItem(SharesData sharesItem) {
        if (this.shares == null) {
            this.shares = new ArrayList<SharesData>();
        }
        this.shares.add(sharesItem);
        return this;
    }

    /**
    * Get shares
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public List<SharesData> getShares() {
        return shares;
    }

    public void setShares(List<SharesData> shares) {
        this.shares = shares;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SharesDataRs sharesDataRs = (SharesDataRs) o;
        return Objects.equals(this.shares, sharesDataRs.shares);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shares);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SharesDataRs {\n");
        
        sb.append("    shares: ").append(toIndentedString(shares)).append("\n");
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

